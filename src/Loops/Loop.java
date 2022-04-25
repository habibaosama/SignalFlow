package Loops;

import Calculations.Edge;
import Calculations.GraphFlow;
import Calculations.Vertex;

import java.util.Vector;

public class Loop {
    private int start;
    private int end;
    private Vector<Vector<Vertex>> loops;
    private GraphFlow graph;
    private boolean[] visited;

    private void dfs(int id, Vector<Vertex> path, int end) {
        int newId;
        if (id == end) {
            path.add(path.get(0));
            loops.add((Vector<Vertex>) path.clone());
            path.remove(path.size() - 1);
            path.remove(path.size() - 1);

            return;
        }
        for (Edge i : graph.getGraphlist()[id]) {
            newId = i.getDestination().getId();
            if (!visited[newId]) {
                visited[newId] = true;
                path.add(i.getDestination());
                dfs(newId, path, end);
            }
        }
        path.remove(path.size() - 1);
    }

    public Loop(Vertex start, Vertex end, GraphFlow graph) {
        visited = new boolean[graph.getGraphlist().length];
        loops = new Vector<>();
        this.graph = graph;
        Vector<Vertex> path = new Vector<Vertex>();
        path.add(start);
        visited[start.getId()] = true;
        dfs(start.getId(), path, end.getId());
    }

    public Vector<Vector<Vertex>> getLoops() {
        return loops;
    }


}
