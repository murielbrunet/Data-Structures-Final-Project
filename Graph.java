import java.awt.Color;
import java.awt.Point;
import java.lang.Object;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Graph class with all the methods necessary to access, create 
 * and delete edges and nodes.
 * 
 * @author Muriel Brunet
 * @version April 17th, 2014
 */
public class Graph<V,E> { //generic type for data(V) and edges (E)
	protected LinkedList<Graph<V,E>.Node> nodes; 
	protected LinkedList<Graph<V,E>.Edge> edges;

	/**
	 * Constructor for Graph class.
	 */
	public Graph(){
		edges = new LinkedList<Graph<V,E>.Edge>();
		nodes = new LinkedList<Graph<V,E>.Node>();
	}

	/**
	 * Accessor for nodes.
	 *
	 * @param i of type int, index for node.
	 * @return node at index i
	 */
	public Node getNode(int i){
		return nodes.get(i);
	}

	/**
	 * Accessor for edges.
	 * 
	 * @param i of type int, index for edge
	 * @return edge at index i
	 */
	public Edge getEdge(int i){
		return edges.get(i);
	}

	/**
	 * Returns nodes.
	 * @return LinkedList of nodes
	 */
	public LinkedList<Graph<V, E>.Node> getNodes(){
		return nodes;
	}

	/**
	 * Returns edges.
	 * @return LinkedList of edges
	 */
	public LinkedList<Graph<V,E>.Edge> getEdges(){
		return edges;
	}

	/**
	 * Accessor for specific edge.
	 * 
	 * @param head of type Node
	 * @param tail of type Node
	 * @return edge with given head and tail
	 */
	public Edge getEdgeRef(Node head, Node tail){
		for (Edge edge : edges) {
			if(edge.getHead().equals(head) && edge.getTail().equals(tail)){
				return edge;
			}  
			else if(edge.getHead().equals(tail) && edge.getTail().equals(head)){
				return edge;
			} else { 
				continue;
			}
		}
		return null;
	}

	/**
	 * Accessor for number of nodes.
	 * 
	 * @return total number of nodes
	 */
	public int numNodes(){
		return nodes.size();
	}

	/**
	 * Accessor for number of edges.
	 *  
	 * @return total number of edges
	 */
	public int numEdges(){
		return edges.size();
	}

	/**
	 * Adds a node
	 * @param data of type V
	 * @return newly created node
	 */
	public Node addNode(V data){
		Node newNode = new Node(data);
		nodes.add(newNode);
		return newNode;
	}

	/**
	 * Adds a node
	 * @param data of type V
	 * @param x coordinate
	 * @param y coordinate
	 * @return newly created node
	 */
	public Node addNode(V data, int X, int Y){
		Node newNode = new Node(data, X, Y);
		nodes.add(newNode);
		return newNode;
	}

	/**
	 * Adds an edge
	 * @param data
	 * @param head
	 * @param tail
	 * @return the newly created edge
	 */
	public Edge addEdge(E data, Node head, Node tail){
		Edge newEdge = new Edge(data, head, tail);
		head.getNodeEdges().add(newEdge);
		tail.getNodeEdges().add(newEdge);
		edges.add(newEdge);
		return newEdge;
	}

	/**
	 * Removes a node
	 * 
	 * @param node Node to remove
	 */
	public void removeNode(Node node){
		while(node.getNodeEdges().size() > 0){ // removing all of its edges from the master list
			Edge tempEdge = node.getNodeEdges().get(0);
			removeEdge(tempEdge);
		}
		nodes.remove(node); //remove actual node
	}

	/**
	 * Removes an edge
	 * 
	 * @param edge Edge to remove
	 */
	public void removeEdge(Edge edge){
		edges.remove(edge); //remove from master list in Graph class
		Node head = edge.getHead();
		Node tail = edge.getTail();
		head.getNodeEdges().remove(edge); // remove from the head node's list of edges
		tail.getNodeEdges().remove(edge);
	}

	/**
	 * Removes an edge taking edge's
	 * head and tail.
	 * 
	 * @param head
	 * @param tail
	 */
	public void removeEdge(Node head, Node tail){
		Edge specifiedEdge = getEdgeRef(head, tail); //get specified edge
		edges.remove(specifiedEdge); //remove from master list in Graph class
		head.getNodeEdges().remove(specifiedEdge);  // remove from head node's list of edges
		tail.getNodeEdges().remove(specifiedEdge);
	}

	/**
	 * Returns nodes that are end points of a list of edges
	 * @param edges
	 * @return HashSet of Nodes
	 */
	public HashSet<Node> endpoints(HashSet<Edge> edges){
		HashSet<Node> tails = new HashSet<Node>();
		for(Edge e : edges){
			tails.add(e.getTail());
		}
		return tails;
	}

	/**
	 * Breadth-first traversal of graph
	 * @param start, the Node at which to start the traversal
	 * @return HashSet<Edge> path through the graph
	 */
	public HashSet<Edge> BFT(Node start){
		HashSet<Edge> path = new HashSet<Edge>();
		ArrayDeque<Node> unvisited = new ArrayDeque<Node>();
		unvisited.add(start); 
		LinkedList<Node> visited = new LinkedList<Node>();
		visited.add(start);

		while(!unvisited.isEmpty()){
			Node node = unvisited.remove();
			visited.add(node);
			for(Node n : node.getNeighbors()){
				if(!visited.contains(n)){ 
					unvisited.add(n); 
					visited.add(n);
					path.add(node.edgeTo(n));
				}
			}
		}
		return path;
	}


	/**
	 * Depth-first traversal of graph -- public interface
	 * @param start
	 * @return HashSet of Edges traversed
	 */
	public HashSet<Edge> DFT(Node start){
		LinkedList<Node> visited = new LinkedList<Node>();
		HashSet<Edge> path = new HashSet<Edge>();
		return helperDFT(start, visited, path);
	}

	/**
	 * Helper method for DFT... goes through all the nodes.
	 * @param node
	 * @return
	 */
	public HashSet<Edge> helperDFT(Node start, LinkedList<Node> visited, HashSet<Edge> path){
		if(!visited.contains(start)){
			visited.add(start);
			for(Node n : start.getNeighbors()){
				if(!visited.contains(n)){
					path.add(start.edgeTo(n));
					helperDFT(n, visited, path); // recursion!
				}
			}
		}		
		return path;
	}

	/**
	 * Dijkstra's shortest-path algorithm to compute distances to nodes
	 * @param start, node at which to start path
	 * @return 
	 */

	public void distances(Node start){
		for(Node node : nodes){
			node.setCost(Double.POSITIVE_INFINITY);
			start.setCost(0);
			LinkedList<Node> unvisited = nodes;
			while(!unvisited.isEmpty()){
				double weight = Double.POSITIVE_INFINITY;
				for(Edge e : node.getNodeEdges()){
					double tempWeight = e.getWeight();
					if(tempWeight < weight){
						weight = tempWeight;
					}
				}
				node.setCost(weight); //set the cost of the node to the lowest weight of a neighboring edge
				unvisited.remove(node); 
				for(Node unvisitedNode : unvisited){
					Edge tempEdge = new Edge(node, unvisitedNode);
					if(edges.contains(tempEdge)){ //&& unvisited.contains(unvisitedNode)){
						if(unvisitedNode.getCost() > (node.getCost() + tempEdge.getWeight())){
							unvisitedNode.setCost(node.getCost() + tempEdge.getWeight());
							unvisitedNode.setPredecessor(node);
						}
					}
				}
			}
		}
	}

	
	/**Print graph list of edges and lists of nodes.*/
	public void printLists(){
		System.out.println();

		System.out.print("Nodes: ");
		if(nodes.isEmpty()){
			System.out.print("none");
		}
		for(int i = 0; i < nodes.size(); i++){	
			System.out.print(nodes.get(i).data + ", ");
		}

		System.out.println();
		System.out.println();

		System.out.print("Edges: ");
		if(edges.isEmpty()){
			System.out.print("none");
		}
		for(int i = 0; i < edges.size(); i++){	
			Edge tempEdge = edges.get(i);
			System.out.print(tempEdge.weight + " (" + tempEdge.head.getData() + ", " + tempEdge.tail.getData() + ")" + "; ");
		}

		System.out.println();
		System.out.println();
		System.out.println("---------------");
	}

	/**Prints a representation of the graph*/
	public void printNodesEdges(){
		for(int i = 0; i < nodes.size(); i++){	
			System.out.println("Node: " + nodes.get(i).data);
			Node tempNode = nodes.get(i);
			System.out.println("Edges: ");
			if(tempNode.getNodeEdges().size() == 0){
				System.out.println("      none");
			}
			for(int j = 0; j < tempNode.getNodeEdges().size(); j++){	
				Edge tempEdge = tempNode.getNodeEdges().get(j);
				System.out.println("      " + tempEdge.weight + " (" + tempEdge.head.getData() + ", " + tempEdge.tail.getData() + ")");
			}
			System.out.println("---------------");
		}	
	}

	/**
	 * Checks to see that all nodes have the correct list
	 * of edges that connect them. 
	 * 
	 * @return boolean
	 */
	public boolean isConsistent(){
		for(Node node : nodes){
			for(Edge tempEdge : edges){
				// if the opposite node's edges list contains the edge
				if(!tempEdge.oppositeTo(node).getNodeEdges().contains(tempEdge)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the edges are connecting existing nodes.
	 * 
	 * @return boolean, false if edges don't connect 
	 */
	public boolean edgesAllConnect(){
		// check if the edges are connecting actual nodes
		for(Edge edge : edges){
			if(edge.head == null || edge.tail == null){
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns false if there are duplicate edges.
	 * 
	 * @return boolean
	 */
	public boolean noDuplicateEdges(){
		// check if we don't have duplicate edges	
		for(int i = 0; i < edges.size(); i++){
			Edge originalEdge = edges.get(i);
			// make sure that it doesn't compare to itself by making j start after i
			for(int j = i + 1; j < edges.size(); j++){
				Edge tempEdge = edges.get(j);
				if(originalEdge.equals(tempEdge)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Subclass of Graph class to create edges
	 * that connect nodes. Edges can also hold data.
	 * 
	 * @author Muriel Brunet
	 * @version April 22nd, 2014
	 */
	public class Edge{
		private Node head;
		private Node tail;
		private E data;
		private double weight;
		private Color c = Color.red;

		/**
		 * Constructor for Edge class
		 * @param inputData
		 * @param inputHead
		 * @param inputTail
		 */
		public Edge(E inputData, Node inputHead, Node inputTail){
			head = inputHead;
			tail = inputTail;
			data = inputData;
		}

		/**
		 * Constructor for Edge class
		 * @param inputHead
		 * @param inputTail
		 */
		public Edge(Node inputHead, Node inputTail){
			head = inputHead;
			tail = inputTail;
		}

		/**
		 * Returns color of edge.
		 * @return color
		 */
		public Color getColor(){
			return c;
		}

		/**
		 * Sets color of edge.
		 * @param c
		 */
		public void setColor(Color c){
			this.c = c;
		}
		/**
		 * Set data.
		 * @param data
		 */
		public void setData(E data){
			this.data = data;
		}

		/**
		 * Accessor for data.
		 * @return data
		 */
		public E getData(){
			return data;
		}

		/**
		 * Accessor for weight
		 * @return weight of edge
		 */
		public double getWeight(){
			return weight;
		}

		/**
		 * Manipulator for data
		 * @param data
		 */
		public void setWeight(double inputWeight){
			weight = inputWeight;
		}

		/**
		 * Accessor for head
		 * @return head from edge
		 */
		public Node getHead(){
			return head;
		}

		/**
		 * Accessor for tail
		 * @return tail from edge
		 */
		public Node getTail(){
			return tail;
		}

		/**
		 * Accessor for opposite node
		 * @param node 
		 * @return opposite nodes to given node
		 */
		public Node oppositeTo(Node node){
			if(node.equals(head)){
				return tail;
			} else {
				return head;
			}
		}

		/**
		 * Two edges are equal if they connect 
		 * the same end points regardless of the data they carry.
		 * 
		 * @return boolean
		 */
		public boolean equals(Object o){ 
			if(o instanceof Graph.Edge){
				@SuppressWarnings("unchecked")
				Edge edge = (Edge) o; 
				if(tail.equals(edge.tail) && head.equals(edge.head)){
					return true;
				} 
				if(tail.equals(edge.head) && head.equals(edge.tail)){
					return true;
				}
				return false;
			} else {
				return false;
			}
		}

		/**
		 * Creates hashCode for edge. 
		 *  @returns int, hashcode of edge
		 */
		public int hashCode(){
			return head.hashCode() + tail.hashCode();
		}
	}

	/**
	 * Subclass of Graph that creates nodes.
	 * 
	 * @author Muriel Brunet
	 * @version April 22nd, 2014
	 */
	public class Node{

		private LinkedList<Edge> nodeEdges;
		private V data;
		private int X;
		private int Y;
		private Point point;
		private Color c = Color.orange;
		private double cost;
		private Node predecessor;

		/**
		 * Constructor for class Node
		 * @param inputData, data to assign node
		 */
		public Node(V inputData){
			data = inputData;
			nodeEdges = new LinkedList<Edge>();

		}

		/**
		 * Constructor for class Node
		 * @param inputData, data to assign node
		 * @param x coordinate
		 * @param y coordinate
		 */
		public Node(V inputData, int x, int y){
			data = inputData;
			this.X = x;
			this.Y = y;
			point = new Point(x, y);
			nodeEdges = new LinkedList<Edge>();
		}

		/**
		 * Returns predecessor node for Dijkstra's algorithm
		 * @returns predecessor Node
		 */
		public Node getPredecessor(){
			return predecessor;
		}
		
		/**
		 * Sets predecessor node for Dijkstra's algorithm
		 * @param predecessor
		 */
		public void setPredecessor(Node predecessor){
			this.predecessor = predecessor;
		}
		
		/**
		 * Accessor for cost.
		 * @return cost
		 */
		public double getCost(){
			return cost;
		}

		/**
		 * Set cost.
		 * @param cost of type double
		 */
		public void setCost(double cost){
			this.cost = cost;
		}

		/**
		 * Returns color of node.
		 * @return color
		 */
		public Color getColor(){
			return c;
		}

		/**
		 * Sets color of node.
		 * @param c of type Color
		 */
		public void setColor(Color c){
			this.c = c;
		}

		/**
		 * Set point coordinate for node.
		 * @param x
		 * @param y
		 */
		public void setPoint(int x, int y){
			point = new Point(x,y); 
			System.out.println("x: "+x+" y: "+y);
			this.X = x;
			this.Y = y;
		}

		/**
		 * Get point coordinate for node.
		 * @return point
		 */
		public Point getPoint(){
			return point;
		}

		/**
		 * Set X coordinate.
		 * @param x coordinate
		 */
		public void setX(int x){
			X = x;
		}

		/**
		 * Get X coordinate.
		 * @return X coordinate value
		 */
		public int getX(){
			return X;
		}

		/**
		 * Set Y coordinate.
		 * @param y coordinate
		 */
		public void setY(int y){
			Y = y;
		}

		/**
		 * Get Y coordinate
		 * @return y coordinate
		 */
		public int getY(){
			return Y;
		}


		/**
		 * Accessor for data
		 * @param data for Node
		 */
		public V getData(){
			return data;
		}


		/**
		 * Manipulator for data
		 * @param data, new data for the node
		 */
		public void setData(V inputData){
			data = inputData;
		}

		/**
		 * Accessor for node's list of edges
		 * @param node
		 * @return LinkedList of edges
		 */
		public LinkedList<Edge> getNodeEdges(){
			return nodeEdges;
		}

		/**
		 * Returns a list of neighbors
		 * @return LinkedList<Node> list of neighbors
		 */
		public LinkedList<Node> getNeighbors(){
			LinkedList<Node> neighbors = new LinkedList<Node>();
			for(int i = 0; i < nodeEdges.size(); i++){
				Edge edge = nodeEdges.get(i);
				if(equals(edge.getHead())){ 
					neighbors.add(edge.getTail());
				}
				else if (equals(edge.getTail())){
					neighbors.add(edge.getHead());
				}
			}
			return neighbors;
		}

		/**
		 * Returns true if there is an edge to the node in question
		 * @param nodes
		 * @return boolean
		 */
		public boolean isNeighbor(Node node){
			LinkedList<Node> neighbors = getNeighbors();
			return neighbors.contains(node);
		}

		/**
		 * Returns the edge to a specified node, or null if there is none
		 * @param node
		 * @return LinkedList of edges
		 */
		public Edge edgeTo(Node neighbor){
			Edge edge = new Edge(this, neighbor);
			if(edges.contains(edge)){
				return getEdgeRef(this, neighbor);
			} else {
				return null;
			}
		}


		/**
		 * Adds an edge to the edge list
		 * @param Edge
		 */
		protected void addEdgeRef(Edge edge){
			nodeEdges.add(edge);
		}

		/**
		 * Removes an edge from the edge list
		 * @param Edge
		 */
		protected void removeEdgeRef(Edge edge){
			nodeEdges.remove(edge);
		}
	}
}