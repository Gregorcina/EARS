package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.Problem;

/**
* Problem function!
* <p>
* 
* @author Matej Crepinsek
* @version 1
* 
*          <h3>License</h3>
* 
*          Copyright (c) 2011 by Matej Crepinsek. <br>
*          All rights reserved. <br>
* 
*          <p>
*          Redistribution and use in source and binary forms, with or without
*          modification, are permitted provided that the following conditions
*          are met:
*          <ul>
*          <li>Redistributions of source code must retain the above copyright
*          notice, this list of conditions and the following disclaimer.
*          <li>Redistributions in binary form must reproduce the above
*          copyright notice, this list of conditions and the following
*          disclaimer in the documentation and/or other materials provided with
*          the distribution.
*          <li>Neither the name of the copyright owners, their employers, nor
*          the names of its contributors may be used to endorse or promote
*          products derived from this software without specific prior written
*          permission.
*          </ul>
*          <p>
*          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
*          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
*          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
*          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
*          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
*          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
*          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
*          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*          POSSIBILITY OF SUCH DAMAGE.
* 
*/
public class ProblemRastrigin  extends Problem{

	public ProblemRastrigin(int d) {
		super(d,0);
		
		upperLimit = new ArrayList<Double>(d);
		lowerLimit = new ArrayList<Double>(d);
		Collections.fill(lowerLimit, -5.12);
		Collections.fill(upperLimit, 10.24);
		
		name = "Rastrigin";
	}
	
	public ProblemRastrigin(int d, double j) {
		super(d,0);
		
		upperLimit = new ArrayList<Double>(d);
		lowerLimit = new ArrayList<Double>(d);
		Collections.fill(lowerLimit, j*-1.0);
		Collections.fill(upperLimit, j*2.0);
		
		name = "Rastrigin";
	}

	public double eval(double x[]) {
		double v=0;
		for (int i=0; i<(numberOfDimensions); i++) {
			//10�n+sum(x(i)^2-10�cos(2�pi�x(i)))
			v+=(x[i]*x[i]-10*Math.cos(2*Math.PI*x[i]));
			//v+=Math.pow(1-ds[i],2)+100*Math.pow(ds[i+1]-ds[i]*ds[i],2);
		}
		return 10*numberOfDimensions+v;
	}
	
	@Override
	public double eval(List<Double> ds) {
		double v=0;
		for (int i=0; i<(numberOfDimensions); i++) {
			//10�n+sum(x(i)^2-10�cos(2�pi�x(i)))
			v+=(ds.get(i)*ds.get(i)-10*Math.cos(2*Math.PI*ds.get(i)));
			//v+=Math.pow(1-ds[i],2)+100*Math.pow(ds[i+1]-ds[i]*ds[i],2);
		}
		return 10*numberOfDimensions+v;
	}
	
	public double getOptimumEval() {
		return 0;
	}
	@Override
	public boolean isFirstBetter(List<Double> x, double eval_x, List<Double> y,
			double eval_y) {
		return eval_x<eval_y;
	}
}