package Loops;

import Calculations.Edge;
import Calculations.GraphFlow;
import Calculations.Vertex;

import java.util.Vector;

public class Loop {
    private Vertex start;
    private Vertex end;
    private Vector<Vector<Vertex>> loops;
    private GraphFlow graph;
    private boolean[] visited;

    private void dfs(int id, Vector<Vertex> path, int end,boolean first) {
        int newId;
        if (!first && id == start.getId()) {
            visited[path.get(path.size() - 1).getId()] = false;
            path.remove(path.size() - 1);
            return;
        };
        if (id == end) {
            path.add(path.get(0));
            loops.add((Vector<Vertex>) path.clone());
            visited[path.get(path.size() - 1).getId()] = false;
            visited[path.get(path.size() - 2).getId()] = false;
            path.remove(path.size() - 1);
            path.remove(path.size() - 1);
            return;
        }
        for (Edge i : graph.getGraphlist()[id]) {
            newId = i.getDestination().getId();
            if (!visited[newId]) {
                visited[newId] = true;
                path.add(i.getDestination());
                dfs(newId, path, end,false);
            }
        }
        visited[path.get(path.size() - 1).getId()] = false;
        path.remove(path.size() - 1);
    }

    public Loop(Vertex start, Vertex end, GraphFlow graph) {
        this.start = start;
        visited = new boolean[graph.getGraphlist().length];
        loops = new Vector<>();
        this.graph = graph;
        Vector<Vertex> path = new Vector<Vertex>();
        path.add(start);
        visited[start.getId()] = true;
        dfs(start.getId(), path, end.getId(),true);
    }

    public Vector<Vector<Vertex>> getLoops() {
        return loops;
    }


}
