package Calculations;
import Loops.Loop;
import Loops.LoopDetection;

import java.util.ArrayList;
import java.util.Vector;

public class TransferFunction {
    private GraphFlow graph;
    private Double sumOfLoops(Vector<Double> gains){

        Double sum=0.0;

        for(int i=0; i<gains.size(); i++){
            sum+=gains.get(i);
        }

        return sum;
    }

    public Double sumOfNonTouchingLoops(Vector<Vector<Double>> gainsNon,int n){

        Double sum=0.0;
        for(int i=0; i<gainsNon.get(n).size(); i++){
            sum+=gainsNon.get(n).get(i);
        }

        return sum;
    }


    public Double deltaTotal(Vector<Double> gains, Vector<Vector<Double>> gainsNon) {
        Double sumIndividualLoops = sumOfLoops(gains);

        Double result = 1-sumIndividualLoops;

        for (int i = 0; i < gainsNon.size(); i++) {
            Double sum = sumOfNonTouchingLoops(gainsNon,i);
            result += Math.pow(-1, i) * sum;
        }

        return result;
    }

    public double TransFunction(double deltaTotal, Vector<Double> deltaI, long[] gain){
        double result=0;

        for (int i=0; i< deltaI.size(); i++){
            result+=gain[i]*deltaI.get(i);
        }
        result=result/deltaTotal;
        System.out.println("The Transfer Function = "+result);
        return result;
    }
}
