package edu.pitt.dbmi.ccd.algorithm.tetrad.graph;

import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;
import edu.cmu.tetrad.search.GesGes;
import edu.cmu.tetrad.search.PcStable;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.GesParams;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.PcStableParams;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class that handles reading and writing graph.
 *
 * Feb 13, 2015 12:28:29 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class GraphIO {

    public static void write(final Graph graph, Parameters parameters, Class algorithm, final File file) throws IOException {
        Path path = file.toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write("Graph Parameters:\n");
            if (algorithm == PcStable.class) {
                Integer depth = (Integer) parameters.getParameter(PcStableParams.DEPTH);
                writer.write(String.format("Depth: %d\n", depth));

                Double alpha = (Double) parameters.getParameter(PcStableParams.ALPHA);
                writer.write(String.format("Alpha: %f\n", alpha));
            } else if (algorithm == GesGes.class) {
                Double penaltyDiscount = (Double) parameters.getParameter(GesParams.PENALTY_DISCOUNT);
                writer.write(String.format("Penalty Discount: %f\n", penaltyDiscount));

                Integer numPatternsToStore = (Integer) parameters.getParameter(GesParams.NUM_PATTERN_STORE);
                writer.write(String.format("Number of Pattern Stored: %d\n", numPatternsToStore));

                Boolean faithful = (Boolean) parameters.getParameter(GesParams.FAITHFUL);
                writer.write(String.format("Faithful: %s\n", faithful));
            }
            writer.write("\n");
            writer.write(graph.toString());
        }
    }

    public static void write(final Graph graph, final boolean xml, final File file) throws IOException {
        Path path = file.toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            if (xml) {
                writer.write(GraphUtils.graphToXml(graph));
            } else {
                writer.write(graph.toString());
            }
        }
    }

    public static Graph read(final File file, final boolean xml) throws IOException {
        return xml ? GraphUtils.loadGraph(file) : GraphUtils.loadGraphTxt(file);
    }

}
