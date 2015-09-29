package edu.pitt.dbmi.ccd.algorithm;

import edu.cmu.tetrad.graph.Graph;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import java.io.PrintStream;
import java.util.List;

/**
 *
 * Feb 16, 2015 9:19:04 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public interface Algorithm {

    public void run(Class algorithm, Class testOfIndependence, Dataset dataset, Parameters parameters) throws AlgorithmException;

    public void run(Class algorithm, Class testOfIndependence, List<Dataset> datasets, Parameters parameters) throws AlgorithmException;

    public Graph getGraph();

    public void setExecutionOutput(PrintStream executionOutput);

}
