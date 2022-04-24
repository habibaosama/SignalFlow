package Calculations;

import java.util.UUID;

public class Edge {
   Vertex source;
    Vertex destination;
    int weight;
    Long edgeId;

    public Edge( Vertex source, Vertex destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        //edgeId= generateUniqueId();
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }
    /*public Long getEdgeId() {
        return edgeId;
    }*/

    ///Method generate id/////////////////
  /*  public static Long generateUniqueId() {
        long val = -1;
        do {
            val = UUID.randomUUID().getMostSignificantBits();
        } while (val < 0);
        return val/1000;
    }*/
}
