import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JComponent;


/**
 * Presents various graphical views of information
 * stored in Graph class.
 * 
 * @author Muriel Brunet
 * @version April 24th, 2014
 */
public class GraphViewer extends JComponent{
	/** gives the dimensions of the canvas */
	public static final int CANVAS_SIZE = 400;

	Graph<Integer, Integer> graph;
	int radius = 20;
	
	/** Constructor for GraphViewer.
	 * 
	 * @param inputGraph of type Graph<Integer, Integer>
	 */
	public GraphViewer(Graph<Integer, Integer> inputGraph){
		graph = inputGraph;
	}

	/**
	 * Paint component for Graph Viewer. Repaints all nodes and edges.
	 *  @param g Graphics object
	 */
	public void paintComponent(Graphics g){
		LinkedList<Graph<Integer, Integer>.Node> nodes = graph.nodes;
		for(Graph<Integer, Integer>.Node node : nodes){
			int x = node.getX() - radius/2;
			int y = node.getY() - radius/2;
			g.setColor(node.getColor());
			g.fillOval(x, y, radius, radius);
			int data = node.getData();
			g.setColor(Color.black);
			String dataString = String.valueOf(data); //draws value of nodes
			g.drawString(dataString, x, y);
		}
		for(Graph<Integer, Integer>.Edge e : graph.edges){
			Graph<Integer, Integer>.Node head = e.getHead();
			Graph<Integer, Integer>.Node tail = e.getTail();
			int headX = head.getX();
			int headY = head.getY();
			int tailX = tail.getX();
			int tailY = tail.getY();
			g.setColor(e.getColor());
			g.drawLine(headX, headY, tailX, tailY);
			g.setColor(Color.black);
			int data = e.getData();
			String dataString = String.valueOf(data); //draws weight of edges
			g.drawString(dataString, ((headX + tailX)/2), ((headY + tailY)/2));
		}
	}
	
	/**
	 * Gets the minimum dimension. The component will look bad 
	 * if it's sized smaller than this.
	 * 
	 * @returns The minimum dimension
	 */
	public Dimension getMinimumSize(){
		return new Dimension(CANVAS_SIZE, CANVAS_SIZE);
	}

	/**
	 * Gets the preferred dimension. The component will look best
	 * if it's sized with these dimensions. 
	 *
	 * @returns The preferred dimension
	 */
	public Dimension getPreferredSize(){
		return new Dimension(CANVAS_SIZE, CANVAS_SIZE);
	}
}
