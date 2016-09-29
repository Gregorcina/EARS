package org.um.feri.ears.mains;

import java.awt.Dimension;
import java.util.ArrayList;

import org.um.feri.ears.algorithms.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.pso.PSO;
import org.um.feri.ears.algorithms.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.mine.graphing.GraphEARSStatic;
import org.um.feri.ears.mine.graphing.GraphSet;
import org.um.feri.ears.mine.graphing.PlotColorScheme;
import org.um.feri.ears.mine.graphing.PlotType;
import org.um.feri.ears.mine.graphing.data.*;
import org.um.feri.ears.mine.graphing.recording.*;
import org.um.feri.ears.problems.unconstrained.ProblemAckley;
import org.um.feri.ears.problems.unconstrained.ProblemEasom;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.run.RunMainBestAlgSettings;
import org.um.feri.ears.util.Util;

import com.um.feri.brest.de.DEAlgorithm;

//import net.sourceforge.jswarm_pso.SwarmAlgorithm;
//import com.erciyes.karaboga.bee.BeeColonyAlgorithm;
//import com.um.feri.brest.de.DEAlgorithm;

/**
 * @author Administrator
 *
 */
public class Main_SO {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        RunMainBestAlgSettings rbs = new RunMainBestAlgSettings(true,false, new RatingRPUOed2());
        
//        rbs.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06));  
        //rbs.addAlgorithm(new RandomWalkAMAlgorithm(),new Rating(1500, 350, 0.06));   
        rbs.addAlgorithm(new PSO(),new Rating(1500, 350, 0.06)); 
        //rbs.addAlgorithm(new DEAlgorithm(3,20),new Rating(1500, 350, 0.06)); 	// Ne dela, zastarela koda?
//        rbs.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));
        
        rbs.run(50);
        System.out.println(rbs);
        
        // ----------------------------------------------------------------
        
        
        //*
        //GraphDataSet datas = GraphDataManager.GetDataFor(null, ProblemAckley.class);
        //GraphDataSet datas = GraphDataManager.GetDataFor(PSO.class, ProblemEasom.class);
        GraphDataSet datas = GraphDataManager.GetDataFor(null, null);
        
        //DEBUG//
        /*
        RecordedData[][] test = datas.getSubsets();
        System.err.println("test.length="+test.length);
        System.err.println("test[0].length="+test[0].length);
        System.err.println("test[0][0].iteration="+test[0][0].iteration+"  test[0][last].iteration="+test[0][test[0].length-1].iteration);
        */
        
        //GraphSet graphs = new GraphSet(datas, 40);
        GraphSet graphs = new GraphSet(datas);
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        //graphs.setPlotColorScheme(PlotColorScheme.Colored);
        //graphs.setPlotColorScheme(PlotColorScheme.Grayscale);
        
        graphs.Plot(PlotType.AverageOfIterations);
        graphs.Plot(PlotType.StandardDeviationOfIterations);
        //graphs.Plot(PlotType.BestOfIterations);
        //graphs.Plot(PlotType.WorstOfIterations);
        //graphs.setTitle(0, "FERI FTW");
        graphs.Flush();
        //*/
        
        
        //graphs.SaveToPlotFiles();
        //graphs.SaveStatisticsToFiles();
        
        
        // alternative: graphs.Add(graphs.getCombinedGraphsByProblem());
        GraphSet combinedGraphs = graphs.getCombinedGraphsByProblem();
        combinedGraphs.Flush();
        
    }

}