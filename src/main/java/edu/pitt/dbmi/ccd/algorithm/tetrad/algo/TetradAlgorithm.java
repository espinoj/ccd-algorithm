package edu.pitt.dbmi.ccd.algorithm.tetrad.algo;

import edu.cmu.tetrad.data.CovarianceMatrix5;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.search.GesGes;
import edu.cmu.tetrad.search.IndependenceTest;
import edu.cmu.tetrad.search.PcStable;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.AlgorithmException;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.GesParams;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.PcStableParams;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.util.TetradIndependenceTestFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * Feb 16, 2015 9:19:37 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradAlgorithm implements Algorithm {

    private Graph graph;

    private String executionOutput;

    public TetradAlgorithm() {
    }

    @Override
    public void run(Class algorithm, Class testOfIndependence, Dataset dataset, Parameters parameters) throws AlgorithmException {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm class is required.");
        }
        if (dataset == null || !(dataset instanceof TetradDataSet)) {
            throw new IllegalArgumentException("TetradDataSet is required.");
        }

        DataSet dataSet = (DataSet) dataset.getDataSet();
        if (algorithm == PcStable.class) {
            if (testOfIndependence == null) {
                throw new IllegalArgumentException("Independence test class is required.");
            }

            IndependenceTest independenceTest = TetradIndependenceTestFactory.buildIndependenceTest(testOfIndependence, dataSet, parameters);

            // get parameters
            Integer d = (Integer) parameters.getParameter(PcStableParams.DEPTH);
            int depth = (d == null) ? 3 : d;

            Boolean b = (Boolean) parameters.getParameter(PcStableParams.VERBOSE);
            boolean verbose = (b == null) ? false : b;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(bos);

            PcStable pcStable = new PcStable(independenceTest);
            pcStable.setVerbose(verbose);
            pcStable.setDepth(depth);
            pcStable.setOut(out);

            graph = pcStable.search();
            executionOutput = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } else if (algorithm == GesGes.class) {
            Double pd = (Double) parameters.getParameter(GesParams.PENALTY_DISCOUNT);
            double penaltyDiscount = (pd == null) ? 2.0 : pd;

            Integer np = (Integer) parameters.getParameter(GesParams.NUM_PATTERN_STORE);
            int numPatternsToStore = (np == null) ? 0 : np;

            Boolean f = (Boolean) parameters.getParameter(GesParams.FAITHFUL);
            boolean faithful = (f == null) ? false : f;

            Boolean v = (Boolean) parameters.getParameter(GesParams.VERBOSE);
            boolean verbose = (v == null) ? false : v;

            GesGes ges;
            if (dataSet.isContinuous()) {
                ges = new GesGes(new CovarianceMatrix5(dataSet));
                ges.setPenaltyDiscount(penaltyDiscount);
            } else {
                ges = new GesGes(dataSet);
            }
            ges.setNumPatternsToStore(numPatternsToStore);
            ges.setFaithfulnessAssumed(faithful);
            ges.setVerbose(verbose);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(bos);
            ges.setOut(out);

            graph = ges.search();
            executionOutput = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException(String.format("Unknow algorithm class %s.", algorithm.getName()));
        }
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    public String getExecutionOutput() {
        return executionOutput;
    }

}
