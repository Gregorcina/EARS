package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem10;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem2;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem3;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem4;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem5;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem6;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem7;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem8;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem9;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class BIOMABenchmark extends MORatingBenchmark<Double, DoubleMOTask, DoubleMOProblem>{
    public static final String name="BIOMA Benchmark";
    protected int evaluationsOnDimension;
    protected int dimension=3;
    private double draw_limit=0.0000001;
    private boolean random;
    
	@Override
	public boolean resultEqual(ParetoSolution<Double> a, ParetoSolution<Double> b, QualityIndicator<Double> qi) {
		if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if(qi.getIndicatorType() == IndicatorType.Unary)
        	return a.isEqual(b,draw_limit); //TODO Quality indicator get eps 
        else if(qi.getIndicatorType() == IndicatorType.Binary)
        {
			if(qi.compare(a, b, draw_limit) == 0)
			{
				return true;
			}
        }
        return false;
	}
    
    public BIOMABenchmark(List<IndicatorName> indicators, double draw_limit, boolean random) {
        super(indicators);
        this.random = random;
        this.draw_limit = draw_limit;
        evaluationsOnDimension=300000;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"2");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);

    }
    
    
    @Override
    protected void setWinLoseFromResultList(ResultArena arena, DoubleMOTask t) {

    	if(random)
    	{
    		MOAlgorithmEvalResult first;
			MOAlgorithmEvalResult second;
			QualityIndicator<Double> qi;
			IndicatorName indicatorName;
    		for (int i=0; i<results.size(); i++) {
    			first = results.get(i);
    			for (int j=i+1; j<results.size(); j++) {
    				second = results.get(j);
    				indicatorName = indicators.get(Util.nextInt(indicators.size()));
    				qi = IndicatorFactory.createIndicator(indicatorName, t.getProblem());
    				
    				try {
    					if(qi.getIndicatorType() == IndicatorType.Unary)
    					{
    						first.getBest().evaluate(qi);
    						second.getBest().evaluate(qi);
    					}
					} catch (Exception e) {
						e.printStackTrace();
					}
    				if (resultEqual(first.getBest(), second.getBest(), qi)) { 
						arena.addGameResult(Game.DRAW, first.getAl().getAlgorithmInfo().getVersionAcronym(), second.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemShortName(), indicatorName.toString());
					} 
    				else 
    				{
    					if (t.isFirstBetter(first.getBest(),second.getBest(), qi))
    					{
    						arena.addGameResult(Game.WIN, first.getAl().getAlgorithmInfo().getVersionAcronym(), second.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemShortName(), indicatorName.toString());
    					}
    					else
    					{
    						arena.addGameResult(Game.WIN, second.getAl().getAlgorithmInfo().getVersionAcronym(), first.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemShortName(), indicatorName.toString());
    					}
    				}
        		}
    		}
    		
    	}
    	else
    		super.setWinLoseFromResultList(arena, t);
    	
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(EnumStopCriteria sc, int eval, double epsilon, DoubleMOProblem p) {
        listOfProblems.add(new DoubleMOTask(sc, eval, epsilon, p));
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	ArrayList<DoubleMOProblem> problems = new ArrayList<DoubleMOProblem>();
    	
    	
    	problems.add(new UnconstrainedProblem1());
    	problems.add(new UnconstrainedProblem2());
    	problems.add(new UnconstrainedProblem3());
    	problems.add(new UnconstrainedProblem4());
    	problems.add(new UnconstrainedProblem5());
    	problems.add(new UnconstrainedProblem6());
    	problems.add(new UnconstrainedProblem7());
    	problems.add(new UnconstrainedProblem8());
    	problems.add(new UnconstrainedProblem9());
    	problems.add(new UnconstrainedProblem10());

    	
    	for (DoubleMOProblem moProblem : problems) {
    		registerTask(stopCriteria, evaluationsOnDimension, 0.001, moProblem);
		}
    }
        
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getAcronym()
     */
    @Override
    public String getAcronym() {
        return "BIOMA";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
}