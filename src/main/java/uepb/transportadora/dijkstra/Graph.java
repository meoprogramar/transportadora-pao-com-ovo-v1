package uepb.transportadora.dijkstra;


import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Node> nodes = new ArrayList<Node>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public List<Node> getNodes() {
        return nodes;
    }

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
}