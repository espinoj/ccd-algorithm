package edu.pitt.dbmi.ccd.algorithm.tetrad.algo;

import edu.pitt.dbmi.ccd.algorithm.tetrad.util.TetradIndependenceTestFactory;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.search.GesGes;
import edu.cmu.tetrad.search.IndependenceTest;
import edu.cmu.tetrad.search.PcStable;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.AlgorithmException;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.PcStableParams;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
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

        } else {
            throw new IllegalArgumentException(String.format("Unknow algorithm class %s.", algorithm.getName()));
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public String getExecutionOutput() {
        return executionOutput;
    }

}
