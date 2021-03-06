package org.um.feri.ears.algorithms.so.mfo;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;

public class MFO extends Algorithm{
	
	DoubleSolution bestFlame;
	
	int pop_size;
	double flameNum;
	
	Task task;
	
	ArrayList<DoubleSolution> population;
	
	public MFO()
	{
		this(20);
	}
	
	public MFO(int pop_size)
	{
		super();
		this.pop_size = pop_size;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("MFO",
				"@article{mirjalili2015moth,"
				  +"title={Moth-flame optimization algorithm: A novel nature-inspired heuristic paradigm},"
				  +"author={Mirjalili, Seyedali},"
				  +"journal={Knowledge-Based Systems},"
				  +"volume={89},"
				  +"pages={228--249},"
				  +"year={2015},"
				  +"publisher={Elsevier}}",
				"MFO", "Moth Flame Optimization");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}
	
	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<DoubleSolution>();
	
		for (int i = 0; i < pop_size; i++) {
			population.add(task.getRandomSolution());
			if (task.isStopCriteria())
				break;
		}
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		
		initPopulation();
		
		population.sort(new TaskComparator(task));
		
		bestFlame = new DoubleSolution(population.get(0));
		
		int maxIt = 10000;
		if(task.getStopCriteria() == EnumStopCriteria.ITERATIONS)
		{
			maxIt = task.getMaxIteratirons();
		}
		
		if(task.getStopCriteria() == EnumStopCriteria.EVALUATIONS)
		{
			maxIt = task.getMaxEvaluations() / pop_size;
		}
		
		while (!task.isStopCriteria()) {
			flameNum = Math.round(pop_size - task.getNumberOfIterations() * ((pop_size-1) / maxIt));

			task.incrementNumberOfIterations();
		}
	
		return bestFlame;
	}

	
	@Override
	public void resetToDefaultsBeforeNewRun() {
	}
}
