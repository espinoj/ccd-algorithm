package edu.pitt.dbmi.ccd.algorithm.tetrad.graph;

import edu.cmu.tetrad.graph.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author : Jeremy Espino MD
 * Created  5/26/15 11:02 PM
 */
public class GraphmlSerializer {


    public static String serialize(Graph graph) {

        List<Node> nodes = graph.getNodes();
        Set<Edge> edgesSet = graph.getEdges();
        Set<Triple> ambiguousTriples = graph.getAmbiguousTriples();
        Set<Triple> underLineTriples = graph.getUnderLines();
        Set<Triple> dottedUnderLineTriples = graph.getDottedUnderlines();


            StringBuilder buf = new StringBuilder();

            buf.append("\nGraph Nodes:\n");

            for (int i = 0; i < nodes.size(); i++) {
    //            buf.append("\n" + (i + 1) + ". " + nodes.get(i));
                buf.append(nodes.get(i) + " ");
                if ((i + 1) % 30 == 0) buf.append("\n");
            }

            buf.append("\n\nGraph Edges: ");

            List<Edge> edges = new ArrayList<Edge>(edgesSet);
            Edges.sortEdges(edges);

            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                buf.append("\n").append(i + 1).append(". ").append(edge);
            }

            buf.append("\n");
            buf.append("\n");

    //        Set<Triple> ambiguousTriples = getAmbiguousTriples();

            if (!ambiguousTriples.isEmpty()) {
                buf.append("Ambiguous triples (i.e. list of triples for which there is ambiguous data" +
                        "\nabout whether they are colliders or not): \n");

                for (Triple triple : ambiguousTriples) {
                    buf.append(triple).append("\n");
                }
            }

            if (!underLineTriples.isEmpty()) {
                buf.append("Underline triples: \n");

                for (Triple triple : underLineTriples) {
                    buf.append(triple).append("\n");
                }
            }

            if (!dottedUnderLineTriples.isEmpty()) {
                buf.append("Dotted underline triples: \n");

                for (Triple triple : dottedUnderLineTriples) {
                    buf.append(triple).append("\n");
                }
            }

            return buf.toString();
        }

}
