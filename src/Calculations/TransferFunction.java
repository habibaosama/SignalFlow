package Calculations;
import Loops.Loop;
import Loops.LoopDetection;

import java.util.ArrayList;

public class TransferFunction {
    private GraphFlow graph;
    private int sumOfLoops(ArrayList<Integer> gains){

        int sum=0;

        for(int i=0; i<gains.size(); i++){
            sum+=gains.get(i);
        }

        return sum;
    }

    public int sumOfNonTouchingLoops(ArrayList<ArrayList<Integer>> gainsNon,int n){

        int sum=0;
        for(int i=0; i<gainsNon.get(n).size(); i++){
            sum+=gainsNon.get(n).get(i);
        }

        return sum;
    }


    public int deltaTotal(ArrayList<Integer> gains, ArrayList<ArrayList<Integer>> gainsNon) {
        int sumIndividualLoops = sumOfLoops(gains);
        LoopDetection obj = new LoopDetection(graph);
        int result = 1-sumIndividualLoops;

        for (int i = 0; i < obj.getNonTouchingLoops().size()-1; i++) {
            int sum = sumOfNonTouchingLoops(gainsNon,i);
            result += Math.pow(-1, i) * sum;
        }

        return result;
    }

    public double TransFunction(int deltaTotal, int[] deltaI){
        double result=0;

        for (int i=0; i<graph.forwardPaths.size(); i++){
            result+=graph.gain[i]*deltaI[i];
        }
        result=result/deltaTotal;
        return result;
    }
}
