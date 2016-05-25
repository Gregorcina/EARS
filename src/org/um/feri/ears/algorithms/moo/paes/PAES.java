//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.paes;

import java.util.Comparator;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.pesa2.AdaptiveGridArchive;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.DominanceComparator;

public class PAES<T extends MOTask, Type> extends MOAlgorithm<T, Type> {

	AdaptiveGridArchive<Type> archive;
	int archiveSize = 100;
	int bisections = 5;
	int num_var;
	int num_obj;
	
	MutationOperator<Type, MOTask> mut;

	public PAES(MutationOperator mutation, int populationSize) {
		
		this.archiveSize = populationSize;
		this.mut = mutation;
		
		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"PAES",
				"\\bibitem{knowles1999}\nJ.~Knowles,D.W.~Corne\n\\newblock The Pareto Archived Evolution Strategy: A New Baseline Algorithm for Pareto Multiobjective Optimisation.\n\\newblock \\emph{Proceedings of the Congress of Evolutionary Computation}, 98--105, 1999.\n",
				"PAES", "Pareto Archived Evolution Strategy");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
	}

	@Override
	public ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException {
		task = taskProblem;
		num_var = task.getDimensions();
		num_obj = task.getNumberOfObjectives();
		
		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");

		if(display_data)
		{
			archive.displayAllUnaryQulaityIndicators(task.getProblem());
			archive.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName(), task.getProblem());
		}
		if(save_data)
		{
			archive.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName());
			archive.printFeasibleFUN("FUN_PEAS");
			archive.printVariablesToFile("VAR");
			archive.printObjectivesToCSVFile("FUN");
		}
		return archive;
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

	private void init() {
		archive = new AdaptiveGridArchive<Type>(archiveSize, bisections, num_obj);
	}

	public void start() throws StopCriteriaException {
		
		Comparator<MOSolutionBase<Type>> dominance;

		PolynomialMutation plm = new PolynomialMutation(1.0 / num_var, 20.0);
		dominance = new DominanceComparator();

		if (task.isStopCriteria())
			return;
		MOSolutionBase<Type> solution = new MOSolutionBase<Type>(task.getRandomMOIndividual());
		// problem.evaluateConstraints(solution);

		archive.add(new MOSolutionBase<Type>(solution));

		do {
			// Create the mutate one
			MOSolutionBase<Type> mutatedIndividual = new MOSolutionBase<Type>(solution);
			mut.execute(mutatedIndividual, task);

			if (task.isStopCriteria())
				break;
			task.eval(mutatedIndividual);
			// problem.evaluateConstraints(mutatedIndividual);

			// Check dominance
			int flag = dominance.compare(solution, mutatedIndividual);

			if (flag == 1) { // If mutate solution dominate
				solution = new MOSolutionBase<Type>(mutatedIndividual);
				archive.add(mutatedIndividual);
			} else if (flag == 0) { // If none dominate the other
				if (archive.add(mutatedIndividual)) {
					solution = test(solution, mutatedIndividual, archive);
				}
			}
			/*
			 * if ((evaluations % 100) == 0) {
			 * archive.printObjectivesToFile("FUN"+evaluations) ;
			 * archive.printVariablesToFile("VAR"+evaluations) ;
			 * archive.printObjectivesOfValidSolutionsToFile("FUNV"+evaluations)
			 * ; }
			 */
		} while (!task.isStopCriteria());

	}

	public MOSolutionBase<Type> test(MOSolutionBase<Type> solution,
			MOSolutionBase<Type> mutatedSolution, AdaptiveGridArchive<Type> archive) {

		int originalLocation = archive.getGrid().location(solution);
		int mutatedLocation = archive.getGrid().location(mutatedSolution);

		if (originalLocation == -1) {
			return new MOSolutionBase<Type>(mutatedSolution);
		}

		if (mutatedLocation == -1) {
			return new MOSolutionBase<Type>(solution);
		}

		if (archive.getGrid().getLocationDensity(mutatedLocation) < archive
				.getGrid().getLocationDensity(originalLocation)) {
			return new MOSolutionBase<Type>(mutatedSolution);
		}

		return new MOSolutionBase<Type>(solution);
	}

}