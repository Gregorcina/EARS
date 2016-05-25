package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public abstract class QualityIndicator<Type> {

	/** Returns the calculated quality indicator
	   *  @param population
	   *  @param referenceSet
	   *  @param numberOfObjectives The number of objectives.
	   */
	public abstract double evaluate(ParetoSolution<Type> population);
	
	protected String name;
	
	/**
	 * The epsilon value of the indicator.
	 */
	protected double eps;
	
	/**
	 * The problem.
	 */
	protected final MOProblemBase problem;
	

	protected ParetoSolution<Type> referencePopulation;
	
	/**
	 * Stores the number of objectives.
	 */
	protected int numberOfObjectives;
	
	/**
	 *  Stores the Reference set.
	 */
	protected double[][] referenceSet;
	
	/**
	 * Stores the maximum values of the reference set.
	 */
	protected double[] maximumValue;
	/**
	 * Stores the minimum values of the reference set.
	 */
	protected double[] minimumValue;
	
	/**
	 * Stores the reference point for the problem.
	 */
	double[] referencePoint;
	
	/**
	 * Stores the normalized Reference set.
	 */
	double[][] normalizedReference;
	
	public QualityIndicator(MOProblemBase<Type> problem, ParetoSolution<Type> population)
	{
		this.problem = problem;
		if(problem != null)
		{
			this.numberOfObjectives = problem.getNumberOfObjectives();
		}
		minimumValue = new double[problem.getNumberOfObjectives()];
		maximumValue = new double[problem.getNumberOfObjectives()];
		referencePoint = getReferencePoint(problem.getFileName());
		referencePopulation = population;
		this.referenceSet = population.writeObjectivesToMatrix(); 
		normalizedReference = normalize(population);
	}
	/**
	 * Quality Indicator constructor for indicators without reference sets
	 * @param moProblemBase
	 */
	public QualityIndicator(MOProblemBase moProblemBase)
	{
		this.problem = moProblemBase;
		if(moProblemBase != null)
		{
			this.numberOfObjectives = moProblemBase.getNumberOfObjectives();
		}
		minimumValue = new double[moProblemBase.getNumberOfObjectives()];
		maximumValue = new double[moProblemBase.getNumberOfObjectives()];
	}
	
	public double getEpsilon()
	{
		return eps;
	}
	
	private double[][] normalize(ParetoSolution<Type> population) {
		
		if (population.solutions.size() < 2) {
			throw new IllegalArgumentException("requires at least two solutions");
		}
		
		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			minimumValue[i] = Double.POSITIVE_INFINITY;
			maximumValue[i] = Double.NEGATIVE_INFINITY;
		}

		for (int i = 0; i < referenceSet.length; i++) {
			MOSolutionBase<Type> solution = population.get(i);
			
			if (solution.violatesConstraints()) {
				continue;
			}
			
			for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
				minimumValue[j] = Math.min(minimumValue[j], solution.getObjective(j));
				maximumValue[j] = Math.max(maximumValue[j], solution.getObjective(j));
			}
		}
		
		checkRanges();
		
		if(referencePoint != null)
			maximumValue = referencePoint;
		
		double[][] normalizedReference;

		normalizedReference = MetricsUtil.getNormalizedFront(referenceSet, maximumValue, minimumValue);
		
		return normalizedReference;
	}
	
	/**
	 * Checks if any objective has a range that is smaller than machine
	 * precision.
	 * 
	 * @throws IllegalArgumentException if any objective has a range that is
	 *         smaller than machine precision
	 */
	private void checkRanges() {
		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			if (Math.abs(minimumValue[i] - maximumValue[i]) < 1e-10) {
				throw new IllegalArgumentException(
						"objective with empty range");
			}
		}
	}

	protected static <T> ParetoSolution<T> getReferenceSet(String fileName) {
		
		ParetoSolution<T> referenceSet = new ParetoSolution<T>(0);

		if(fileName != null && !fileName.isEmpty())
		{
			referenceSet = MetricsUtil.<T>readNonDominatedSolutionSet("pf_data/"+ fileName +".dat");
		}
		else
		{
			System.out.println("The file name containg the Paret front is not valid.");
		}

		return referenceSet;
	}
	
	protected static double[] getReferencePoint(String problemName)
	{
		double[] referencePoint;

		referencePoint = MetricsUtil.readReferencePoint("pf_data/ReferencePoint.dat", problemName);

		return referencePoint;
	}

	public enum IndicatorType {
		Unary,
		Binary,
		Arbitrary
	}
	
	public enum IndicatorName {
	    CovergeOfTwoSets, Epsilon, EpsilonBin, ErrorRatio, GD, Hypervolume, IGD, IGDPlus, MPFE, MaximumSpread, NR, ONVG, ONVGR, R1, R2, R3, RNI, Spacing, Spread, GeneralizedSpread, NativeHV
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public MOProblemBase getProblem() {
		return problem;
	}
	
	/**
	 * The method returns an enum which tells the number of approximations the quality indicator requires to operate.
	 * Unary - one approximation
	 * Binary - two approximation
	 * Arbitrary - arbitrary number of approximation
	 * @return the enum for the indicator type.
	 */
	abstract public IndicatorType getIndicatorType();
	
	/**
	 * The method must return true if smaller values are better else return false.
	 * @return true if smaller values are better else return false.
	 */
	abstract public boolean isMin();
	
	/**
	 * The method returns true if reference set is required else returns false.
	 * 
	 * @return true if reference set is required else returns false.
	 */
	abstract public boolean requiresReferenceSet();

	/**
	 * Compares two approximations.
	 * @param front1 Object representing the first front.
	 * @param front2 Object representing the second front.
	 * @param epsilon the draw limit
	 * @return -1, or 0, or 1 if front1 is better than front2, both are 
     * equal, or front2 is better than front1, respectively.
	 */
	public abstract int compare(ParetoSolution<Type> front1, ParetoSolution<Type> front2, Double epsilon);
	
}