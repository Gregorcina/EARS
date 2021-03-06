package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class RandomAlgorithmTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Task sphere=new Task(EnumStopCriteria.EVALUATIONS,1000, 0, 0, 0.001,new Sphere(4));
		Algorithm test = new RandomWalkAlgorithm();
		DoubleSolution best;
        try {
            best = test.execute(sphere);
            System.out.println("Best is:"+best);
        } catch (StopCriteriaException e) {
            e.printStackTrace();
        }
	}

}
