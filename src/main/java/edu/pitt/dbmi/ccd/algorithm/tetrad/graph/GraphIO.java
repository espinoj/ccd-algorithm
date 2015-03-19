package edu.pitt.dbmi.ccd.algorithm.tetrad.graph;

import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;
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
