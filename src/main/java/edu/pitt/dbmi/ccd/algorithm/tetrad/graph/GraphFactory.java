package edu.pitt.dbmi.ccd.algorithm.tetrad.graph;

import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;
import edu.cmu.tetrad.graph.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class that generates simulated graphs.
 *
 * Feb 13, 2015 11:59:15 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class GraphFactory {

    private GraphFactory() {
    }

    public static Graph createRandomDAG(int numofVars, double edgesPerNode) {
        List<Node> vars = new ArrayList<>();
        for (int i = 0; i < numofVars; i++) {
            vars.add(new ContinuousVariable("X" + i));
        }

        return GraphUtils.randomDagQuick2(vars, 0, (int) (numofVars * edgesPerNode));
    }

}
