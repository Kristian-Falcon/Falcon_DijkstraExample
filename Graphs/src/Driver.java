//Author: Kristian Falcon
//Date: December 7th, 2018
//Purpose: To recreate Dijkstra's algorithm in a weighted, directed graph class then draw the graph and highlight the path in a simple gui
//NOTE: I had some trouble translating what I have done to work with the simple GUI class provided. Everything works, but there is a bit of mess trying to access other data types
//ALSO NOTE: for point 19, I changed the x value from 380 to 480 to match the image on the R drive
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Driver extends Application{
	public static ArrayList<Vertex> vertices = new ArrayList<Vertex>(); //ArrayList used to store the vertex objects
	
	Scene scene = null;
	int WINDOW_WIDTH = 600;
	int WINDOW_HEIGHT = 600;
	static int vNum; 					//Number of vertices
	static int eNum;					//Number of edges
	static Point LeftPoint[];			//An array of the "left" points of the edges
	static Point RightPoint[];			//An array of the "right" points of the edges
	public static Point[] PathOfPoints; //An array of points which consist of the path necessary 
	
	public static <T> void main(String[] args) {
		WDGraph<String> graph = new WDGraph();	
		String left;	//A string representing the "left" point of an edge
		String right;	//A string representing the "right" point of an edge
		String source;	//A string representing the source point
		String end;		//A string representing the end point
		File file = new File("graph.dat");
		
		try {
			Scanner sc = new Scanner(file);
			vNum = sc.nextInt();				//Established the number of vertices in the graph
			for(int i = 0; i < vNum; i++) {		//Adds certicies to the ArrayList and the WDGraph
				vertices.add(new Vertex(sc.nextInt(), sc.nextInt(), sc.nextLine().trim()));
				graph.addVertex(vertices.get(i).getName());
			}
			eNum = sc.nextInt();				//Establishes the number of edges in the graph
			LeftPoint = new Point[eNum];		//Declares the LeftPoint and RightPoint arrays; these are mostly used for the GUI below, the path is found without them
			RightPoint = new Point[eNum];
			for(int i = 0; i< eNum; i++) {		//This loop ultimately adds the edges to the WDGraph
				left = sc.next().trim();		//Declares left, which is used below in the same loop
				for(int j = 0; j < vertices.size(); j++) //This and the following for-loops find the vertex within the Array List that matches the string found in the .dat file and add it to the two arrays of points
					if(vertices.get(j).getName().equals(left)) 
						LeftPoint[i] = new Point(vertices.get(j).getX(), vertices.get(j).getY());
				right = sc.next().trim();		//Declares right, which is used below in the same loop
				for(int j = 0; j < vertices.size(); j++) 
					if(vertices.get(j).getName().equals(right)) 
						RightPoint[i] = new Point(vertices.get(j).getX(), vertices.get(j).getY());
				graph.addEdge(left, right, Math.round(distance(left,right)));
			}
			source = sc.next();
			end = sc.next();
			graph.shortestPath (source, end);
			
			System.out.println("\n" + graph.toString());  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		launch(args);
	}
	
	public static double distance(String a, String b) {
		double d = 0;
		Vertex A = null;
		Vertex B = null;
		for (int i = 0; i < vertices.size(); i++) {
			if (a.equals(vertices.get(i).getName()))
				A = vertices.get(i);
			if (b.equals(vertices.get(i).getName()))
				B = vertices.get(i);
		}

		d = Math.sqrt(Math.pow((B.getX() - A.getX()), 2) + Math.pow((B.getY() - A.getY()), 2));

		return d;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Graph");
		Group root = new Group();
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		drawImage(gc);

		root.getChildren().add(canvas);
		stage.setScene(new Scene(root));
		stage.show();
	}

	private void drawImage(GraphicsContext gc) {
		ArrayList<Point> Points = new ArrayList();

		// Takes the code that was here before but instead adds the points from the vertices array List
		for (int i = 0; i < vNum; i++)
			Points.add(new Point(vertices.get(i).getX(), vertices.get(i).getY()));

		drawPoints(gc, Points);
		drawLines(gc, Points);
	}

	private void drawPoints(GraphicsContext gc, List<Point> list) {
		for (Point p : list)
			drawPoint(gc, p);
	}

	private void drawPoint(GraphicsContext gc, Point p) {
		int width = 20; // point diameter
		gc.setFill(Color.GREEN);
		gc.fillOval(p.getX() - width / 2, p.getY() - width / 2, width, width);

	}

	private void drawLines(GraphicsContext gc, List<Point> list) {
		Point p1, p2;
		
		for (int i = 0; i < eNum; i++) { //Draws the lines throughout the entire graph
			p1 = RightPoint[i];
			p2 = LeftPoint[i];
			drawLine(gc, p1, p2, Color.BLUE);
		}
		for (int i = 0; i < PathOfPoints.length-1; i++) { //Redraws the lines that make up the path in red
			p1 = PathOfPoints[i];							
			p2 = PathOfPoints[i+1];
			drawLine(gc, p1, p2, Color.RED);
		}

	}

	private void drawLine(GraphicsContext gc, Point p1, Point p2, Color color) { //Added color to the input of this function
		gc.setStroke(color);
		gc.setLineWidth(5);
		gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
	}
}