package Calculations;

import java.util.LinkedList;

public class GraphFlow {
    LinkedList<Edge>[] Graphlist;
    private LinkedList<LinkedList<Edge>> forwardPaths = new LinkedList<LinkedList<Edge>>();
    private LinkedList<Edge> forwardPath = new LinkedList<Edge>();
    private boolean[] visited=new boolean[1000];
    private long[] gain=new long[1000];//contains the gain of every path "bltarteeb" :)

    public GraphFlow(int vertices) {
        Graphlist = new LinkedList[vertices];
        addVertex();
    }

    public void addVertex(){
        for(int i = 0; i< Graphlist.length; i++)
        {
            Graphlist[i] = new LinkedList<Edge>();
        }
    }
    public void addEgde(Vertex source, Vertex destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        Graphlist[source.getId()].add(edge); //for directed graph
    }

    /*public void debugGraph() {

         for (int i = 0; i < Graphlist.length; i++) {
             LinkedList<Edge> list = Graphlist[i];
             for (int j = 0; j < list.size(); j++) {
                 System.out.println("vertex-" + list.get(j).source.getName() + " index " + list.get(j).source.getId() + "Boolean : " + list.get(j).source.isOutput()+ " is connected to " +
                         list.get(j).destination.getName() + " index " + list.get(j).destination.getId()+ " Boolean: " + list.get(j).destination.isOutput()+ " with weight " + list.get(j).weight);
             }
         }
     }*/





    private  void findForwardPaths(Edge edge) {

        if( visited[edge.destination.getId()] ) {//not a forward path: self loop,or already visited node in the same path!!
            return;
        }

        forwardPath.add(edge);
        visited[edge.destination.getId()] = true;

        if( edge.destination.isOutput()) {//if output vertex, then add the edge to the path and return.
            forwardPaths.add((LinkedList<Edge>) forwardPath.clone());
            forwardPath.removeLast();//3shan law fih path tany.
            visited[edge.destination.getId()] = false;//3shan mmkn azor l output node dy tany bs mn path mo5tlf.
            return;
        }

        LinkedList<Edge> next = Graphlist[edge.destination.getId()];//continue to the next edge

        for( int i=0; i<next.size(); i++ ) {
            this.findForwardPaths(next.get(i));
        }

        forwardPath.removeLast();//3shan law fih path tany mn a5er node 7asalaha visting.
        visited[edge.destination.id] = false;//3shan mmkn azor l node dy tany bs mn path mo5tlf.

    }




    public LinkedList<LinkedList<Edge>> findForwardPaths(){

        LinkedList<Edge> v1 = Graphlist[0];//Always start from the input node.

        visited[0] = true;//input node can't be revisited!!

        for( int i = 0 ; i < v1.size() ; i++ ) {
            findForwardPaths(v1.get(i));
            forwardPath.clear();
        }

        return forwardPaths;
    }


    public void printPaths() {
        for( int i = 0 ; i < forwardPaths.size() ; i++ ) {
            LinkedList<Edge> forPaths= forwardPaths.get(i);
            int gain = 1;
            System.out.print("M"+(i+1)+" = "+forPaths.get(0).source.getName() + " ");
            for( int j = 0 ; j < forPaths.size(); j++ ) {
                System.out.print("--> ");
                gain*= forPaths.get(j).weight;
                System.out.print(forPaths.get(j).destination.getName() + " ");
            }
            this.gain[i]=gain;
            System.out.print("= "+gain);
            System.out.println();
        }
        //System.out.println(this.gain[0]+"  "+this.gain[1]+"   "+this.gain[2]);
    }







}
