package Calculations;

import java.util.LinkedList;

public class GraphFlow {
    LinkedList<Edge>[] Graphlist;

    public GraphFlow(int vertices) {
        Graphlist = new LinkedList[vertices];
        addVertex();
    }
    public void addVertex()
    {
        for(int i = 0; i< Graphlist.length; i++)
        {
            Graphlist[i] = new LinkedList<Edge>();
        }
    }
    public void addEgde(Vertex source, Vertex destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        Graphlist[source.getId()].add(edge); //for directed graph
    }

    /* public void debugGraph() {
         for (int i = 0; i < Graphlist.length; i++) {
             LinkedList<Edge> list = Graphlist[i];
             for (int j = 0; j < list.size(); j++) {
                 System.out.println("vertex-" + list.get(j).source.getName() + " index " + list.get(j).source.getId() + "Boolean : " + list.get(j).source.isOutput()+ " is connected to " +
                         list.get(j).destination.getName() + " index " + list.get(j).destination.getId()+ " Boolean: " + list.get(j).destination.isOutput()+ " with weight " + list.get(j).weight);
             }
         }
     }*/



}
