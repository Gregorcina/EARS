package org.um.feri.ears.problems.real_world.cec2011;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.cec2010.base.EllipticShifted;
import org.um.feri.ears.util.Util;

/**
 * Problem function!
 * 
 * @author Matej Črepinšek
 * @version 1
 * 
 **/	
public class CEC2011_Problem1 extends Problem {
	/*
	 * fun_num=1   Parameter Estimation for Frequency-Modulated (FM) Sound Waves,initialization range=[0,6.35], bound=[-6.4,6.35] , length of x=6. 
	 * 
	 */
	public CEC2011_Problem1() {
		super(6,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -6.4));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 6.35));
				
		//Arrays.fill(interval, 12.75);
		//Arrays.fill(intervalL, -6.4);//6.4 + 6.35
		name = "RWP_1";
		description = "RWP_1 Parameter Estimation for Frequency-Modulated (FM) Sound Waves";
	}
	
	public double eval(double x[]) {
	      double theta=2.*Math.PI/100;
	      double f=0;
	      double y_t, y_0_t;
	        for (int t=0; t<=100; t++){
	            y_t=x[0]*Math.sin(x[1]*t*theta+x[2]*Math.sin(x[3]*t*theta+x[4]*Math.sin(x[5]*t*theta)));
	            y_0_t=1*Math.sin(5*t*theta-1.5*Math.sin(4.8*t*theta+2*Math.sin(4.9*t*theta)));
	            f=f+(y_t-y_0_t)*(y_t-y_0_t);
	        }
		return f;
	}

	public double getGlobalOptimum() {
		return 0; //OK
	}
	
	
	@Override
	public double[] getRandomVariables() {
		//initialization range=[0,6.35]
		double[] var=new double[numberOfDimensions];
		for (int j = 0; j < numberOfDimensions; j++) {
			var[j] = Util.nextDouble(0, 6.35);
		}
		return var;
	}

	
	
	@Override
	public DoubleSolution getRandomSolution() {
		//initialization range=[0,6.35]
		List<Double> var=new ArrayList<Double>();
		for (int j = 0; j < numberOfDimensions; j++) {
			var.add(Util.nextDouble(0, 6.35));
		}
		DoubleSolution sol = new DoubleSolution(var, eval(var), calc_constrains(var), upperLimit, lowerLimit);
		return sol;
	}
}