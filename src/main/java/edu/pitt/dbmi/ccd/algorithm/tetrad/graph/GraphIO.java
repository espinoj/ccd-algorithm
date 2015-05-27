package edu.pitt.dbmi.ccd.algorithm.tetrad.graph;

import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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

	public enum GraphOutputType {
		TETRAD, XML, GRAPHML
	}

	public static void write(final Graph graph, final GraphOutputType graphOutputType, final PrintStream stream, final String graphId)
			throws IOException {

		switch (graphOutputType) {
		case XML:
			stream.println(GraphUtils.graphToXml(graph));
			return;
		case TETRAD:
			stream.println(graph.toString().trim());
			return;
		case GRAPHML:
            stream.println(GraphmlSerializer.serialize(graph, graphId));
			return;

		default:
			// print some warning
			stream.println(graph.toString().trim());
			return;
		}

	}

	public static void write(final Graph graph, final GraphOutputType graphOutputType, final File file) throws IOException {
        Path path = file.toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            switch (graphOutputType) {
                case XML:
                    writer.write(GraphUtils.graphToXml(graph));
                    return;
                case TETRAD:
                    writer.write(graph.toString());
                    return;
                case GRAPHML:
                    return;
                default:
                    // print some warning
                    writer.write(graph.toString());
                    return;
            }
        }
    }



	public static Graph read(final File file, final boolean xml) throws IOException {
		return xml ? GraphUtils.loadGraph(file) : GraphUtils.loadGraphTxt(file);
	}

}
