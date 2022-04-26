package Loops;

import Calculations.Edge;
import Calculations.GraphFlow;
import Calculations.TransferFunction;
import Calculations.Vertex;

import java.util.LinkedList;
import java.util.Vector;

public class LoopDetection {
    private GraphFlow graph;
    private boolean[] visited;
    private int[] stage;
    private Vector<Vector<Vertex>> loops;
    private Vector<Vector<Edge>> loopsEdges;
    private Vector<Vector<Vector<Vertex>>> nonTouchingLoops;
    private Vector<Vector<Vector<Edge>>> nonTouchingEdges;
    public Vector<Vector<Double>> nonTouchingGains;
    public Vector<Double> loopsGain;
    public Vector<Double> deltaI;
    public String[][] loopTouch = new String[1000][1000];
    private String deltaIString;

    public LoopDetection(GraphFlow graph) {
        this.graph = graph;
        this.nonTouchingLoops = new Vector<>();
        this.nonTouchingEdges = new Vector<>();
        this.nonTouchingGains = new Vector<>();
        this.loopsGain = new Vector<>();
        this.deltaI = new Vector<>();
        this.stage = new int[graph.getGraphlist().length];
        this.deltaIString = "";
    }

    private Double getGain(Vector<Edge> loop) {
        double res = 1;
        for (Edge k : loop) {
            res *= k.getWeight();
        }
        return res;
    }

    private void generateLoopGains() {
        for (Vector<Edge> i : loopsEdges) {
            Double res = getGain(i);
            loopsGain.add(res);
        }
    }

    private boolean compareLoopAndPath(Vector<Vertex> loop, LinkedList<Edge> path) {
        for (Vertex i : loop) {
            for (Edge j : path) {
                if (j.getDestination() == i || j.getSource() == i) return false;
            }
        }
        return true;
    }

    private void generateDeltaI() {
        computeDeltaIWithNonTouching();
        computeDeltaIWithLoops();
        fillDeltaI();
    }

    public String getDeltaIString() {
        return deltaIString;
    }

    private void computeDeltaIWithNonTouching() {
        LinkedList<LinkedList<Edge>> forwardPaths = graph.forwardPaths;
        for (int i = 0; i < forwardPaths.size(); i++) {
            double sum = 0;
            for (int j = 0; j < nonTouchingLoops.size(); j++) {
                for (int k = 0; k < nonTouchingLoops.get(j).size(); k++) {
                    if (compareLoopAndPath(nonTouchingLoops.get(j).get(k), forwardPaths.get(i))) {
                        sum += Math.pow(-1, j) * nonTouchingGains.get(j).get(k);
                    }
                }
            }
            deltaI.add(1 + sum);
        }
    }

    private void computeDeltaIWithLoops() {
        LinkedList<LinkedList<Edge>> forwardPaths = graph.forwardPaths;
        for (int i = 0; i < forwardPaths.size(); i++) {
            double sum = 0;
            for (int j = 0; j < loops.size(); j++) {
                if (compareLoopAndPath(loops.get(j), forwardPaths.get(i))) {
                    sum += loopsGain.get(j);
                }
            }
            double temp = deltaI.get(i);
            temp -= sum;
            deltaI.set(i, temp);

        }
    }


    private void generateNonTouchingGains() {
        Vector<Double> results = new Vector<>();
        for (Vector<Vector<Edge>> i : nonTouchingEdges) {
            for (Vector<Edge> j : i) {
                Double res = getGain(j);
                results.add(res);
            }
            nonTouchingGains.add(results);
        }
    }

    private Vector<Edge> getEdges(Vector<Vertex> path) {
        Vector<Edge> res = new Vector<>();
        for (int i = 0; i < path.size() - 1; i++) {
            for (Edge j : graph.getGraphlist()[path.get(i).getId()]) {
                if (j.getDestination() == path.get(i + 1)) {
                    res.add(j);
                    break;
                }
            }
        }
        return res;
    }

    private void dfs(Vertex source, int stage) {
        int newId;
        this.stage[source.getId()] = stage + 1;

        for (Edge i : graph.getGraphlist()[source.getId()]) {
            newId = i.getDestination().getId();
            if (!visited[newId]) {
                visited[newId] = true;
                dfs(i.getDestination(), stage + 1);
            } else if (this.stage[i.getDestination().getId()] < this.stage[source.getId()]) {
                Loop loop = new Loop(i.getDestination(), source, graph);
                loops.addAll(loop.getLoops());
            }
        }
    }


    public Vector<Vector<Edge>> getLoops() {

        visited = new boolean[graph.getGraphlist().length];
        loops = new Vector<>();

        for (int i = 0; i < graph.getGraphlist().length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                dfs(graph.getVertices()[i], 0);
            }
        }

        generateLoopsEdges();
        generateNonTouchingLoops();
        generateLoopGains();
        generateNonTouchingGains();
        generateDeltaI();
        return this.loopsEdges;
    }

    private void generateNonTouchingLoops() {
        Vertex element;
        Vector<Vector<Vertex>> currRes = new Vector<>();
        Vector<Vector<Edge>> currEdge = new Vector<>();

        for (int i = 0; i < loops.size() - 1; i++) {
            for (int k = i + 1; k < loops.size(); k++) {
                boolean touching = false;
                for (int j = 0; j < loops.get(i).size(); j++) {
                    element = loops.get(i).get(j);
                    for (int l = 0; l < loops.get(k).size(); l++) {
                        if (loops.get(k).get(l).getName().equals(element.getName())) {
                            touching = true;
                            break;
                        }
                    }

                }
                if (!touching) {
                    currRes.add(new Vector<>());
                    currEdge.add(new Vector<>());
                    currRes.get(currRes.size() - 1).addAll(loops.get(i));
                    loopTouch[0][currEdge.size() - 1] = "L" + String.valueOf(i + 1);
                    currRes.get(currRes.size() - 1).addAll(loops.get(k));
                    loopTouch[0][currEdge.size() - 1] += "L" + String.valueOf(k + 1);
                    currEdge.get(currEdge.size() - 1).addAll(getEdges(loops.get(i)));
                    currEdge.get(currEdge.size() - 1).addAll(getEdges(loops.get(k)));
                }
            }
        }
        if (currRes.size() == 0) return;
        nonTouchingLoops.add(currRes);
        nonTouchingEdges.add(currEdge);
        int f = 1;
        while (true) {
            currRes = new Vector<>();
            currEdge = new Vector<>();
            Vector<Vector<Vertex>> prevNonTouchingLoops = nonTouchingLoops.get(nonTouchingLoops.size() - 1);
            for (int i = 0; i < loops.size() - 1; i++) {
                for (int k = 0; k < prevNonTouchingLoops.size(); k++) {
                    boolean touching = false;
                    for (int j = 0; j < loops.get(i).size(); j++) {
                        element = loops.get(i).get(j);
                        for (int l = 0; l < prevNonTouchingLoops.get(k).size(); l++) {
                            if (prevNonTouchingLoops.get(k).get(l).getName().equals(element.getName())) {
                                touching = true;
                                break;
                            }
                        }
                    }
                    if (!touching) {
                        currRes.add(new Vector<>());
                        currEdge.add(new Vector<>());
                        currRes.get(currRes.size() - 1).addAll(loops.get(i));
                        loopTouch[f][currEdge.size() - 1] = "L" + String.valueOf(i + 1);
                        currRes.get(currRes.size() - 1).addAll(prevNonTouchingLoops.get(k));
                        loopTouch[f][currEdge.size() - 1] += loopTouch[f - 1][k];
                        currEdge.get(currEdge.size() - 1).addAll(getEdges(prevNonTouchingLoops.get(k)));
                        currEdge.get(currEdge.size() - 1).addAll(getEdges(loops.get(i)));

                    }
                }
            }
            if (currRes.size() == 0) break;
            nonTouchingLoops.add(currRes);
            nonTouchingEdges.add(currEdge);
        }
    }

    public Vector<Vector<Vector<Vertex>>> getNonTouchingLoops() {
        return nonTouchingLoops;
    }

    public Vector<Vector<Vector<Edge>>> getNonTouchingEdges() {
        return nonTouchingEdges;
    }

    private void generateLoopsEdges() {
        loopsEdges = new Vector<>();
        for (Vector<Vertex> i : loops) {
            loopsEdges.add(getEdges(i));
        }
    }

    private void fillDeltaI() {
        char delta = '\u0394';
        TransferFunction tf = new TransferFunction();
        double deltaTotal = tf.deltaTotal(loopsGain, nonTouchingGains);
        deltaIString += delta + " = " + deltaTotal + "\n";
        int counter = 1;
        for (Double i : deltaI) {
            deltaIString += delta + "" + counter + " = " + i + "\n";
            counter++;
        }
    }

}
