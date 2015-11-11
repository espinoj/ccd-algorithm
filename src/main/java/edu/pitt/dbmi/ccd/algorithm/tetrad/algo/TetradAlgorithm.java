package edu.pitt.dbmi.ccd.algorithm.tetrad.algo;

import edu.cmu.tetrad.data.CovarianceMatrixOnTheFly;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.search.FastGes;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.AlgorithmException;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.FgsParams;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
import java.io.PrintStream;

/**
 *
 * Feb 16, 2015 9:19:37 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradAlgorithm implements Algorithm {

    private Graph graph;

    private PrintStream executionOutput;

    public TetradAlgorithm() {
        this.executionOutput = null;
    }

    @Override
    public void run(Class algorithm, Class testOfIndependence, Dataset dataset, Parameters parameters) throws AlgorithmException {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm class is required.");
        }
        if (dataset == null || !(dataset instanceof TetradDataSet)) {
            throw new IllegalArgumentException("TetradDataSet is required.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters are required.");
        }

        DataSet dataSet = (DataSet) dataset.getDataSet();
        if (algorithm == FastGes.class) {
            // get parameters
            Double pd = (Double) parameters.getParameter(FgsParams.PENALTY_DISCOUNT);
            double penaltyDiscount = (pd == null) ? 4.0 : pd;
            Integer d = (Integer) parameters.getParameter(FgsParams.DEPTH);
            int depth = (d == null) ? 3 : d;
            Boolean f = (Boolean) parameters.getParameter(FgsParams.FAITHFUL);
            boolean faithful = (f == null) ? false : f;
            Boolean v = (Boolean) parameters.getParameter(FgsParams.VERBOSE);
            boolean verbose = (v == null) ? false : v;

            FastGes ges;
            if (dataset.isContinuous()) {
                ges = new FastGes(new CovarianceMatrixOnTheFly(dataSet));
                ges.setPenaltyDiscount(penaltyDiscount);
            } else {
                ges = new FastGes(dataSet);
            }
            ges.setDepth(depth);
            ges.setNumPatternsToStore(0);  // always set to zero
            ges.setFaithfulnessAssumed(faithful);
            ges.setVerbose(verbose);
            if (executionOutput != null) {
                ges.setOut(executionOutput);
            }

            graph = ges.search();
            if (executionOutput != null) {
                executionOutput.println();
            }
        } else {
            throw new IllegalArgumentException(String.format("Unknow algorithm class %s.", algorithm.getName()));
        }
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void setExecutionOutput(PrintStream executionOutput) {
        this.executionOutput = executionOutput;
    }

}
