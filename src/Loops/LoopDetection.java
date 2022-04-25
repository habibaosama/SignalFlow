package Loops;

import Calculations.Edge;
import Calculations.GraphFlow;
import Calculations.Vertex;

import java.util.LinkedList;
import java.util.Vector;

public class LoopDetection {
    private GraphFlow graph;
    private boolean[] visited;
    private Vector<Vector<Vertex>> loops;
    private Vector<Vector<Edge>> loopsEdges;
    private Vector<Vector<Vector<Vertex>>> nonTouchingLoops;
    private Vector<Vector<Vector<Edge>>> nonTouchingEdges;
    private Vector<Vector<Double>> nonTouchingGains;
    private Vector<Double> loopsGain;
    private Vector<Double> deltaI;


    public LoopDetection(GraphFlow graph) {
        this.graph = graph;
        this.nonTouchingLoops = new Vector<>();
        this.nonTouchingEdges = new Vector<>();
        this.nonTouchingGains = new Vector<>();
        this.loopsGain = new Vector<>();
        this.deltaI = new Vector<>();
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

    public void generateDeltaI() {
        LinkedList<LinkedList<Edge>> forwardPaths = graph.forwardPaths;
        for (int i = 0; i < forwardPaths.size(); i++) {
            double sum = 0;
            for (int j = 0; j < nonTouchingLoops.size(); i++) {
                for (int k = 0; k < nonTouchingLoops.get(i).size(); k++) {
                    if (compareLoopAndPath(nonTouchingLoops.get(j).get(k), forwardPaths.get(i))) {
                        sum += Math.pow(-1, j + 1) * nonTouchingGains.get(k).get(k);
                    }
                }
            }
            deltaI.add(1-sum);
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

    private void dfs(Vertex source) {
        int newId;
        for (Edge i : graph.getGraphlist()[source.getId()]) {
            newId = i.getDestination().getId();
            if (!visited[newId]) {
                visited[newId] = true;
                dfs(i.getDestination());
            } else {
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
                dfs(graph.getVertices()[i]);
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
                    if (loops.get(k).contains(element)) {
                        touching = true;
                        break;
                    }
                }
                if (!touching) {
                    currRes.add(new Vector<>());
                    currEdge.add(new Vector<>());
                    currRes.get(currRes.size() - 1).addAll(loops.get(i));
                    currRes.get(currRes.size() - 1).addAll(loops.get(k));
                    currEdge.get(currEdge.size() - 1).addAll(getEdges(loops.get(i)));
                    currEdge.get(currEdge.size() - 1).addAll(getEdges(loops.get(k)));
                }
            }
        }
        if (currRes.size() == 0) return;
        nonTouchingLoops.add(currRes);
        nonTouchingEdges.add(currEdge);

        while (true) {
            currRes = new Vector<>();
            currEdge = new Vector<>();
            Vector<Vector<Vertex>> prevNonTouchingLoops = nonTouchingLoops.get(nonTouchingLoops.size() - 1);
            for (int i = 0; i < loops.size() - 1; i++) {
                for (int k = 0; k < prevNonTouchingLoops.size(); k++) {
                    boolean touching = false;
                    for (int j = 0; j < loops.get(i).size(); j++) {
                        element = loops.get(i).get(j);
                        if (prevNonTouchingLoops.get(k).contains(element)) {
                            touching = true;
                            break;
                        }
                    }
                    if (!touching) {
                        currRes.add(new Vector<>());
                        currEdge.add(new Vector<>());
                        currRes.get(currRes.size() - 1).addAll(loops.get(i));
                        currRes.get(currRes.size() - 1).addAll(prevNonTouchingLoops.get(k));
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

    public Vector<Vector<Vector<Edge>>> getNonTouchingLoops() {
        return nonTouchingEdges;
    }


    private void generateLoopsEdges() {
        loopsEdges = new Vector<>();
        for (Vector<Vertex> i : loops) {
            loopsEdges.add(getEdges(i));
        }
    }

}
