
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;



/**
 * Creates an interactive application
 * that allows the user to create a graph with
 * buttons and clicks and alter it.
 * 
 * @author Muriel Brunet
 * @version April 24th, 2014
 */
public class GraphGUI {

	GraphViewer graphView;
	JLabel jLabel;
	InputMode mode = InputMode.ADD_NODE;
	Graph<Integer, Integer> graph = new Graph<Integer, Integer>();
	Graph<Integer, Integer>.Node nodeUnderMouse;
	Point pointUnderMouse;
	Graph<Integer, Integer>.Node addStart;
	Graph<Integer, Integer>.Node removeStart;
	int numNodes;
	JTextField dataInput;

	/**
	 * Sets up the window.
	 */
	public void setupWindows() {

		// Make sure we have nice window decorations.                                                               
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.                                                                            
		JFrame frame = new JFrame("Graph Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add components                                                                                           
		createComponents(frame.getContentPane());

		// Display the window.                                                                                      
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Draws and formats buttons, graph view, etc. within 
	 * a given pane. 
	 * @param pane
	 */
	public void createComponents(Container pane){
		graphView = new GraphViewer(graph);

		pane.setLayout(new FlowLayout(FlowLayout.CENTER));
		pane.add(graphView);
		graphView.addMouseListener(new MouseListener());

		JPanel panel = new JPanel();
		pane.add(panel);
		panel.setLayout(new GridLayout(2, 1));

		jLabel = new JLabel("Create a new graph! Click anywhere to create a new node.");
		jLabel.setHorizontalAlignment(jLabel.CENTER);
		panel.add(jLabel);

		JPanel panelButtons = new JPanel();
		pane.add(panelButtons);
		panelButtons.setLayout(new GridLayout(5, 4));

		dataInput = new JTextField("Enter desired value here.", 20); 
		dataInput.setHorizontalAlignment(JTextField.RIGHT);
		panelButtons.add(dataInput);	

		JButton setDataButton = new JButton("Set Data");
		panelButtons.add(setDataButton);
		setDataButton.addActionListener(new SetDataListener());

		JButton addMoveNodeButton = new JButton("Add Node");
		panelButtons.add(addMoveNodeButton);
		addMoveNodeButton.addActionListener(new AddNodeListener());

		JButton addEdgeButton = new JButton("Add Edge");
		panelButtons.add(addEdgeButton);
		addEdgeButton.addActionListener(new AddEdgeListener());

		JButton removeEdgeButton = new JButton("Remove Edge");
		panelButtons.add(removeEdgeButton);
		removeEdgeButton.addActionListener(new RemoveEdgeListener());

		JButton removeNodeButton = new JButton("Remove Node");
		panelButtons.add(removeNodeButton);
		removeNodeButton.addActionListener(new RemoveNodeListener());

		JButton bftButton = new JButton("Breadth First Traversal");
		panelButtons.add(bftButton);
		bftButton.addActionListener(new BFTListener());

		JButton dftButton = new JButton("Depth First Traversal");
		panelButtons.add(dftButton);
		dftButton.addActionListener(new DFTListener());

		JButton resetTraversalButton = new JButton("Reset Traversal");
		panelButtons.add(resetTraversalButton);
		resetTraversalButton.addActionListener(new ResetTraversalListener());

		JButton distancesButton = new JButton("Find Distances to Nodes");
		panelButtons.add(distancesButton);
		distancesButton.addActionListener(new DistancesListener());

		panel.add(panelButtons);
	}


	/** 
	 * Returns node found within the drawing radius of the given location, 
	 * or null if none
	 *
	 *  @param x  the x coordinate of the location
	 *  @param y  the y coordinate of the location
	 *  @return  node
	 */
	public Graph<Integer, Integer>.Node findNearbyNode(int x, int y) {
		for(Graph<Integer, Integer>.Node n : graph.nodes){
			Point point = n.getPoint();
			double distance = point.distance((double) x, (double) y);
			if (distance <= graphView.radius) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Resets all the nodes and edges to their
	 * original colors.
	 */
	public void originalColors(){
		for(Graph<Integer, Integer>.Node n : graph.nodes){
			n.setColor(Color.orange);
		}
		for(Graph<Integer, Integer>.Edge e : graph.edges){
			e.setColor(Color.red);
		}
	}

	/** Constants for recording the input mode */
	enum InputMode {
		ADD_NODE, MOVE_NODE, RMV_NODE, ADD_EDGE, RMV_EDGE, BFT, DFT, DISTANCES, SET_DATA
	}


	/**
	 * Adds a node wherever the user clicks
	 * on the screen via MouseClick Event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class AddNodeListener implements ActionListener {
		/** Event handler for Add Node Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Click anywhere to create a new node.");
			mode = InputMode.ADD_NODE;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Removes node clicked by user via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class RemoveNodeListener implements ActionListener {
		/** Event handler for Remove Node Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Click a node to remove.");
			mode = InputMode.RMV_NODE;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Creates new edge between two selected 
	 * nodes via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class AddEdgeListener implements ActionListener {
		/** Event handler for Add Edge Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Click two nodes to create new edge.");
			mode = InputMode.ADD_EDGE;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Removes edge between the two nodes 
	 * selected by the user via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class RemoveEdgeListener implements ActionListener {
		/** Event handler for Remove Edge Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Select two nodes to remove their edge.");
			mode = InputMode.RMV_EDGE;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Calls Breadth First Traversal on selected node.
	 * Prints out unreachable nodes and highlights visited
	 * nodes and edges via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class BFTListener implements ActionListener {
		/** Event handler for Breadth First Search Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Select start node.");
			mode = InputMode.BFT;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Calls Depth First Traversal on whatever
	 * node is selected. Prints out unreachable nodes
	 * and highlights visited nodes and edges via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class DFTListener implements ActionListener {
		/** Event handler for Depth First Traversal Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Select start node.");
			mode = InputMode.DFT;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Allows user to set data by selecting node
	 * and entering data into a JTextField via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class SetDataListener implements ActionListener {
		/** Event handler for Set Data Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Enter number and select a node.");
			mode = InputMode.SET_DATA;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Finds the shortest distances to each node
	 * from selected starting point via MouseClick event.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class DistancesListener implements ActionListener {
		/** Event handler for Distances Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Click on starting node.");
			mode = InputMode.DISTANCES;
			originalColors();
			graphView.repaint();
		}
	}

	/**
	 * Restores all original colors.
	 * 
	 * @author Muriel Brunet
	 * @version April 24th, 2014
	 */
	class ResetTraversalListener implements ActionListener {
		/** Event handler for Reset Traversal Button */
		public void actionPerformed(ActionEvent e) {
			jLabel.setText("Original colors restored.");
			mode = InputMode.ADD_NODE;
			originalColors();
			graphView.repaint();
		}
	}


	/** Mouse listener for GraphViewer element */
	class MouseListener extends MouseAdapter implements MouseMotionListener {

		/** Responds to click event depending on mode */
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 1){
				int x = e.getX();
				int y = e.getY();
				nodeUnderMouse = findNearbyNode(x, y);
				switch (mode) {
				case ADD_NODE:
					if(nodeUnderMouse == null){ //check to make sure there isn't already a node there
						numNodes = numNodes + 1;
						graph.addNode(numNodes, x, y);
						dataInput.setText("Enter desired value here.");
					}
					break;

				case RMV_NODE:
					Graph<Integer, Integer>.Node node = findNearbyNode(x, y);
					if(node != null){
						graph.removeNode(node);
					}
					break;

				case SET_DATA:
					if(nodeUnderMouse != null){
						String newData = dataInput.getText();
						if(!newData.equalsIgnoreCase("Enter desired value here.")){
							int data = Integer.parseInt(newData);
							nodeUnderMouse.setData(data);
							dataInput.setText("Enter desired value here.");
						}
					}
					break;

				case BFT:
					Graph<Integer, Integer>.Node startBFT = findNearbyNode(x, y);
					if(startBFT != null){
						HashSet<Graph<Integer, Integer>.Edge> pathBFT = graph.BFT(startBFT);
						String text = "Unreachable nodes: ";
						LinkedList<Graph<Integer, Integer>.Node> unvisitedNodes = new LinkedList<Graph<Integer, Integer>.Node>();
						for(Graph<Integer, Integer>.Node n : graph.nodes){
							unvisitedNodes.add(n);
						}
						for(Graph<Integer, Integer>.Edge edgeBFT : pathBFT){
							edgeBFT.setColor(Color.green);
							Graph<Integer, Integer>.Node head = edgeBFT.getHead();
							head.setColor(Color.green);
							Graph<Integer, Integer>.Node tail = edgeBFT.getTail();
							tail.setColor(Color.green);

							unvisitedNodes.remove(head);
							unvisitedNodes.remove(tail);
						}
						for(Graph<Integer, Integer>.Node n : unvisitedNodes){
							text = text + n.getData() + ", ";
						}
						jLabel.setText(text);
					}
					break;

				case DFT:
					Graph<Integer, Integer>.Node startDFT = findNearbyNode(x, y);
					if(startDFT != null){
						HashSet<Graph<Integer, Integer>.Edge> pathDFT = graph.DFT(startDFT);
						String textDFT = "Unreachable nodes: ";
						LinkedList<Graph<Integer, Integer>.Node> unvisitedNodes = new LinkedList<Graph<Integer, Integer>.Node>();
						for(Graph<Integer, Integer>.Node n : graph.nodes){
							unvisitedNodes.add(n);
						}
						for(Graph<Integer, Integer>.Edge edgeDFT : pathDFT){
							edgeDFT.setColor(Color.cyan);
							Graph<Integer, Integer>.Node head = edgeDFT.getHead();
							head.setColor(Color.cyan);
							Graph<Integer, Integer>.Node tail = edgeDFT.getTail();
							tail.setColor(Color.cyan);

							unvisitedNodes.remove(head);
							unvisitedNodes.remove(tail);
						}
						for(Graph<Integer, Integer>.Node n : unvisitedNodes){
							textDFT = textDFT + n.getData() + ", ";
						}
						jLabel.setText(textDFT);
					}
					break;

				case DISTANCES:
					Graph<Integer, Integer>.Node startDistances = findNearbyNode(x, y);
					if(startDistances != null){
						String textDistances = "Distances to Nodes... ";
						for(Graph<Integer, Integer>.Node n : graph.nodes){	
							textDistances = textDistances + n.getData() + ": " + n.getCost() + ", ";
						}
						jLabel.setText(textDistances);
					}


					break;
				}	
			}	
			graphView.repaint();
		}

		/** Records point under mousedown event in anticipation of possible drag */
		public void mousePressed(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			switch (mode) {
			case ADD_NODE:
				/**	Point point = findNearbyPoint(x,y);
				if (point != null) {
					pointUnderMouse = point;
				}
				 */	
				Graph<Integer, Integer>.Node node = findNearbyNode(x, y);
				if(node != null){
					nodeUnderMouse = node;
				}

				break;

			case ADD_EDGE:
				int numEdges = graph.numEdges();
				int newNumEdges = numEdges;
				Graph<Integer, Integer>.Node addEnd = findNearbyNode(x, y);
				if(addStart != null && addEnd != null){
					Point startPoint = addStart.getPoint();
					int endPointX = addEnd.getX();
					int endPointY = addEnd.getY();
					double distance = startPoint.distance((double) endPointX, (double) endPointY);
					graph.addEdge((int) distance, addStart, addEnd);
					addStart = null;
					addEnd = null;
					newNumEdges = numEdges + 1;
				}
				if(numEdges == newNumEdges){
					addStart = findNearbyNode(x, y);
				}
				break;

			case RMV_EDGE:
				int totalEdges = graph.numEdges();
				int newTotalEdges = totalEdges;
				Graph<Integer, Integer>.Node removeEnd = findNearbyNode(x, y);
				if(removeStart != null && removeEnd != null){
					graph.removeEdge(removeStart, removeEnd);
					removeStart = null;
					removeEnd = null;
					newTotalEdges = totalEdges - 1;
				}
				if(totalEdges == newTotalEdges){
					removeStart = findNearbyNode(x, y);
				}
				break;
			}
		}

		/** Records point under mousedown event in anticipation of possible drag */
		public void mouseDragged(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			switch (mode) {
			case ADD_NODE:
				if(nodeUnderMouse != null){
					nodeUnderMouse.setX(x);
					nodeUnderMouse.setY(y);
					graphView.repaint();
				}
				/**	if (pointUnderMouse!=null) {
					pointUnderMouse.setLocation(x, y);
					graphView.repaint();
				}
				 */
				break;
			}
		}

		/** Responds to mouse drag event */
		public void mouseReleased(MouseEvent e){
			switch (mode) {
			case ADD_NODE:
				//	pointUnderMouse = null;
				nodeUnderMouse = null;
				break;
			}
		}

	}

	public static void main(String[] args) throws Throwable {
		// set up windows with viewers:
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new GraphGUI().setupWindows();
				}
			});
		} catch (InterruptedException e) {
			System.out.println("Couldn't create GUI.");
			System.exit(-1);
		} catch (InvocationTargetException e) {
			throw(e.getTargetException());
		}

	}
}