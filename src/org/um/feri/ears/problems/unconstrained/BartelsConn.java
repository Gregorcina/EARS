package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/72-bartels-conn-s-function
http://infinity77.net/global_optimization/test_functions_nd_B.html#test-functions-n-d-test-functions-b
 */

public class BartelsConn extends Problem {

    public BartelsConn() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "BartelsConn";
    }

    @Override
    public double eval(double[] x) {
        double fitness = Math.abs(Math.pow(x[0], 2) + Math.pow(x[1], 2) + x[0] * x[1]) + Math.abs(Math.sin(x[0])) + Math.abs(Math.cos(x[1]));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 1.0;
    }
}