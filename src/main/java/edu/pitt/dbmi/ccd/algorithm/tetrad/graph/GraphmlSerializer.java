package edu.pitt.dbmi.ccd.algorithm.tetrad.graph;

import edu.cmu.tetrad.graph.Edge;
import edu.cmu.tetrad.graph.Edges;
import edu.cmu.tetrad.graph.Endpoint;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.graph.Triple;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.graphdrawing.graphml.xmlns.EdgeType;
import org.graphdrawing.graphml.xmlns.GraphEdgedefaultType;
import org.graphdrawing.graphml.xmlns.GraphType;
import org.graphdrawing.graphml.xmlns.GraphmlType;
import org.graphdrawing.graphml.xmlns.NodeType;
import org.graphdrawing.graphml.xmlns.ObjectFactory;

/**
 * Author : Jeremy Espino MD Created 5/26/15 11:02 PM
 */
public class GraphmlSerializer {

    private static final Logger LOGGER = Logger.getLogger(GraphmlSerializer.class.getSimpleName());

    public static String serialize(Graph graph, String graphId) {

        List<Node> nodes = graph.getNodes();
        Set<Edge> edgesSet = graph.getEdges();
        Set<Triple> ambiguousTriples = graph.getAmbiguousTriples();
        Set<Triple> underLineTriples = graph.getUnderLines();
        Set<Triple> dottedUnderLineTriples = graph.getDottedUnderlines();

        try {
            GraphmlType graphmlType = new GraphmlType();

            GraphType graphType = new GraphType();
            graphType.setId(graphId);

            graphType.setEdgedefault(GraphEdgedefaultType.DIRECTED);

            List<Object> nodesOrEdges = graphType.getDataOrNodeOrEdge();
            for (int i = 0; i < nodes.size(); i++) {
                // buf.append("\n" + (i + 1) + ". " + nodes.get(i));
                NodeType node = new NodeType();
                node.setId(nodes.get(i).getName());
                nodesOrEdges.add(node);
            }

            List<Edge> edges = new ArrayList<Edge>(edgesSet);
            Edges.sortEdges(edges);
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                EdgeType edgeType = new EdgeType();
                if (edge.getEndpoint1() == Endpoint.TAIL && edge.getEndpoint2() == Endpoint.ARROW) {
                    edgeType.setSource(edge.getNode1().getName());
                    edgeType.setTarget(edge.getNode2().getName());
                    edgeType.setDirected(true);
                } else if (edge.getEndpoint1() == Endpoint.ARROW && edge.getEndpoint2() == Endpoint.TAIL) {
                    edgeType.setSource(edge.getNode2().getName());
                    edgeType.setTarget(edge.getNode1().getName());
                    edgeType.setDirected(true);
                } else if (edge.getEndpoint1() == Endpoint.TAIL && edge.getEndpoint2() == Endpoint.TAIL) {
                    edgeType.setSource(edge.getNode2().getName());
                    edgeType.setTarget(edge.getNode1().getName());
                    edgeType.setDirected(false);
                } else {
                    // cannot handle all edges yet
                    LOGGER.log(Level.WARNING, "Encountered edge we currently don't handle when serializing graphml:"
                            + edge.toString());
                    edgeType = null;
                }
                if (edgeType != null) {
                    nodesOrEdges.add(edgeType);
                }
            }

            graphmlType.getGraphOrData().add(graphType);

            // TODO: handle ambiguousTriples
            // TODO: handle underLineTriples
            // TODO: handle dottedUnderLineTriples
            // Context is the name of package
            String context = "org.graphdrawing.graphml.xmlns";
            // Initialise JAXB Context
            JAXBContext jc = JAXBContext.newInstance(context);

            // Always use factory methods to initialise XML classes
            ObjectFactory factory = new ObjectFactory();
            JAXBElement<GraphmlType> root = factory.createGraphml(graphmlType);

            // Now Create JAXB XML Marshallar
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write the XML File
            java.io.StringWriter sw = new StringWriter();

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(root, sw);

            return sw.toString();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();

        }

        return null;
    }
}
