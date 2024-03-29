package Calculations;

import java.util.LinkedList;
import java.util.Vector;

public class GraphFlow {
    private LinkedList<Edge>[] Graphlist;
    private Vertex[] vertices;
    public LinkedList<LinkedList<Edge>> forwardPaths = new LinkedList<>();
    private LinkedList<Edge> forwardPath = new LinkedList<Edge>();
    private String forwardPathsOutput;
    private boolean[] visited = new boolean[1000];
    public long[] gain = new long[1000];//contains the gain of every path "bltarteeb" :)

    public GraphFlow(int vertices) {
        Graphlist = new LinkedList[vertices];
        this.vertices = new Vertex[vertices];
        forwardPathsOutput = "";
        addVertex();
    }

    public void addVertex() {
        for (int i = 0; i < Graphlist.length; i++) {
            Graphlist[i] = new LinkedList<Edge>();
        }
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void addEgde(Vertex source, Vertex destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        vertices[source.getId()] = source;
        Graphlist[source.getId()].add(edge); //for directed graph
    }

    private void findForwardPaths(Edge edge) {

        if (visited[edge.getDestination().getId()]) {//not a forward path: self loop,or already visited node in the same path!!
            return;
        }

        forwardPath.add(edge);
        visited[edge.getDestination().getId()] = true;

        if (edge.getDestination().isOutput()) {//if output vertex, then add the edge to the path and return.
            forwardPaths.add((LinkedList<Edge>) forwardPath.clone());
            forwardPath.removeLast();//3shan law fih path tany.
            visited[edge.getDestination().getId()] = false;//3shan mmkn azor l output node dy tany bs mn path mo5tlf.
            return;
        }

        LinkedList<Edge> next = Graphlist[edge.getDestination().getId()];//continue to the next edge

        for (int i = 0; i < next.size(); i++) {
            this.findForwardPaths(next.get(i));
        }

        forwardPath.removeLast();//3shan law fih path tany mn a5er node 7asalaha visting.
        visited[edge.getDestination().id] = false;//3shan mmkn azor l node dy tany bs mn path mo5tlf.

    }
/////////////////////////////ForwardPaths/////////////////////////////////////////////

    public LinkedList<LinkedList<Edge>> findForwardPaths() {

        LinkedList<Edge> v1 = Graphlist[0];//Always start from the input node.

        visited[0] = true;//input node can't be revisited!!
        // System.out.print("hhhhhhh");
        for (int i = 0; i < v1.size(); i++) {
            findForwardPaths(v1.get(i));
            //  System.out.print(v1.get(i).getDestination().getName()+"kkkkkk");
            forwardPath.clear();
        }

        return forwardPaths;
    }

    public String getForwardPathsOutput() {
        printPaths();
        return forwardPathsOutput;
    }

    public void printPaths() {
        for (int i = 0; i < forwardPaths.size(); i++) {
            LinkedList<Edge> forPaths = forwardPaths.get(i);
            int gain = 1;
            forwardPathsOutput += "M" + (i + 1) + " = " + forPaths.get(0).getSource().getName() + " ";
            for (int j = 0; j < forPaths.size(); j++) {
                forwardPathsOutput += "--> ";
                gain *= forPaths.get(j).getWeight();
                forwardPathsOutput += forPaths.get(j).getDestination().getName() + " ";
            }
            this.gain[i] = gain;
            forwardPathsOutput += "= " + gain + "\n";
        }

    }


    public LinkedList<Edge>[] getGraphlist() {
        return Graphlist;
    }
}
