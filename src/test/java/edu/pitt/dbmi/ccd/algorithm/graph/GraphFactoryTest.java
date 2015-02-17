package edu.pitt.dbmi.ccd.algorithm.graph;

import edu.cmu.tetrad.graph.Graph;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author kvb2
 */
public class GraphFactoryTest {

    public GraphFactoryTest() {
    }

    /**
     * Test of createRandomDAG method, of class GraphFactory.
     */
    @Test
    @Ignore
    public void testCreateRandomDAG() {
        System.out.println("createRandomDAG");
        int numofVars = 3;
        double edgesPerNode = 1.0;
        Graph result = GraphFactory.createRandomDAG(numofVars, edgesPerNode);
        System.out.println(result);
    }

}
