package edu.pitt.dbmi.ccd.algorithm.tetrad.data;

import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.sem.LargeSemSimulator;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphFactory;

/**
 * Factory class that generates simulated datasets.
 *
 * Feb 13, 2015 3:21:43 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class DataFactory {

    public static DataSet semSimulateDataAcyclic(Graph graph, int numOfCases) {
        LargeSemSimulator simulator = new LargeSemSimulator(graph);
        simulator.setOut(System.out);

        return simulator.simulateDataAcyclic(numOfCases);
    }

    public static DataSet semSimulateDataAcyclic(int numofVars, double edgesPerNode, int numOfCases) {
        return semSimulateDataAcyclic(GraphFactory.createRandomDAG(numofVars, edgesPerNode), numOfCases);
    }

}
