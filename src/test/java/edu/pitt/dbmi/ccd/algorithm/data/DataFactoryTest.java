package edu.pitt.dbmi.ccd.algorithm.data;

import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.DataSetFactory;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author kvb2
 */
public class DataFactoryTest {

    public DataFactoryTest() {
    }

    /**
     * Test of semSimulateDataAcyclic method, of class DataFactory.
     */
    @Test
    @Ignore
    public void testSemSimulateDataAcyclic() {
        System.out.println("semSimulateDataAcyclic");
        int numofVars = 3;
        double edgesPerNode = 1.0;
        int numOfCases = 5;

        Graph graph = GraphFactory.createRandomDAG(numofVars, edgesPerNode);
        DataSet data = DataSetFactory.buildSemSimulateDataAcyclic(graph, numOfCases);

        System.out.println();
        System.out.println(graph);
        System.out.println("\n\n\n");
        System.out.println(data);
    }

}
