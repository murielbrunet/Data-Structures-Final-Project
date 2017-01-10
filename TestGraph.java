import java.util.HashSet;

/**
 * 
 * @author Muriel Brunet
 * @version April 22nd, 2014
 */
public class TestGraph {

	public static void main(String[] args){
		// you have to specify the types for the Graph class
		Graph<String, Integer> newGraph = new Graph<String, Integer>();
		
		newGraph.addNode("A"); // 0
		newGraph.addNode("B"); // 1
		newGraph.addNode("C"); // 2
		newGraph.addNode("D"); // 3
		newGraph.addNode("E"); // 4
		newGraph.addNode("F"); // 5
		newGraph.addNode("G"); // 6
/**     newGraph.addNode("H"); // 7
		newGraph.addNode("I"); // 8
		newGraph.addNode("J"); // 9
		newGraph.addNode("K"); // 10
		newGraph.addNode("L"); // 11
		newGraph.addNode("M"); // 12
		newGraph.addNode("N"); // 13
		newGraph.addNode("O"); // 14
		newGraph.addNode("P"); // 15
*/
		newGraph.addEdge(16, newGraph.getNode(0), newGraph.getNode(1)); // 0
		
		newGraph.addEdge(23, newGraph.getNode(3), newGraph.getNode(6)); // 1
		
		newGraph.addEdge(105, newGraph.getNode(5), newGraph.getNode(0)); // 2		

		newGraph.addEdge(76, newGraph.getNode(0), newGraph.getNode(6)); // 3
		
		newGraph.addEdge(96, newGraph.getNode(5), newGraph.getNode(1)); // 4
				
		System.out.println("---------");
		System.out.println("| GRAPH |");
		System.out.println("---------");
		System.out.println("---------------");

		System.out.println("Number of Nodes: " + newGraph.numNodes());
		System.out.println("Number of Edges: " + newGraph.numEdges());
		newGraph.printLists();
		newGraph.printNodesEdges();
		
		System.out.println("---------------");

		
		HashSet<Graph<String, Integer>.Edge> path = newGraph.BFT(newGraph.getNode(0));

		for(Graph<String, Integer>.Edge edge : path){
			System.out.println(edge.getData());
		}
		
		System.out.println("---------------");

		
		HashSet<Graph<String, Integer>.Edge> pathDFT = newGraph.DFT(newGraph.getNode(0));

		for(Graph<String, Integer>.Edge edge : pathDFT){
			System.out.println(edge.getData());
		}
		
		//newGraph.removeEdge(newGraph.edges.get(1)); // removing edge(1)
		//newGraph.removeEdge(newGraph.nodes.get(5), newGraph.nodes.get(0)); //removing edge(2)
/**		newGraph.removeNode(newGraph.nodes.get(0)); 
		
		System.out.println("----------");
		System.out.println("| EDITED |");
		System.out.println("| GRAPH  |");
		System.out.println("----------");
		System.out.println("---------------");

		System.out.println("Number of Nodes: " + newGraph.numNodes());
		System.out.println("Number of Edges: " + newGraph.numEdges());
		newGraph.printLists();
		newGraph.printNodesEdges();
*/		
		
	}
}
