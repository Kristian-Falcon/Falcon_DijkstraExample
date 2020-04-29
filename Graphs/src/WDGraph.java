
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class WDGraph<T> implements GraphADT<T> {
	private int CAPACITY = 2;
	private final double INFINITY = Double.POSITIVE_INFINITY;
	private int numVertices;
	private int numEdges;
	private double[][] adjMatrix;
	private T[] vertices;
	
	public WDGraph() {
		numVertices = 0;
		numEdges = 0;
		adjMatrix = new double[CAPACITY][CAPACITY];
		vertices = (T[]) new Object[CAPACITY];
		
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				adjMatrix[i][j] = INFINITY;
			}
		}
	}
		
	public String toString(){
		int GAP = 5;
		if(numVertices == 0)
			return "Graph is empty";
		String result = "";
		
		result += String.format("%7s", "");
		for (int i = 0; i < numVertices; i++)
			result += String.format("%7s", vertices[i]);
		result += "\n";
		
		for (int i = 0; i < numVertices; i++) {
			result += String.format("%7s", vertices[i]);
			
			for (int j = 0; j < numVertices; j++) {
				if(adjMatrix[i][j] == INFINITY)
					//result += String.format("%7s", "inf");
					result += String.format("%7c", '\u221e');
				else
				    result += String.format("%7.2f", adjMatrix[i][j]);
			}
			result += "\n";
		}
		return result;
	}
	
	@Override
	public int numVertices() {
		return numVertices;
	}

	@Override
	public int numEdges() {
		return numEdges;
	}

	@Override
	public void addVertex(T vertex) {
		if(!isValidVertex(vertex)) {
			if(numVertices == CAPACITY)
				expand();
			vertices[numVertices] = vertex;
			numVertices++;
		}
		//need to throw exception or assertion if vertex already exists
	}
	
	protected int vertexIndex(T vertex){
		for(int i = 0; i < numVertices; i++)
			if(vertices[i].equals(vertex))
				return i;
		return -1;
	}
	
	protected boolean isValidVertex(T vertex){
		for(int i = 0; i < numVertices; i++)
			if(vertices[i].equals(vertex))
				return true;
		return false;
	}

	@Override
	public void addEdge(T vertex1, T vertex2, double weight) {
		if(isValidVertex(vertex1)  && isValidVertex(vertex2) && vertex1 != vertex2 && weight >= 0){
			if(!this.existEdge(vertex1, vertex2))
				numEdges++;
			adjMatrix[vertexIndex(vertex1)][vertexIndex(vertex2)] = weight;
		}
	}

	@Override
	public void removeVertex(T vertex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEdge(T vertex1, T vertex2) {
		if(existEdge(vertex1, vertex2)){
			adjMatrix[vertexIndex(vertex1)][vertexIndex(vertex2)] = INFINITY;
			numEdges--;
		}
	}

	@Override
	public boolean isEmpty() {
		return numVertices == 0;
	}

	@Override
	public boolean existEdge(T vertex1, T vertex2) {
		return isValidVertex(vertex1) && 
				isValidVertex(vertex2) && 
				(adjMatrix[vertexIndex(vertex1)][vertexIndex(vertex2)] != INFINITY);
	}

	@Override
	public int numComponents() {
		return 0;
	}

	@Override
	public boolean isConnected() {
		return false;
	}
	
	
	private void expand() {
		int newCapacity = CAPACITY * 2;
		double[][] newAdjMatrix = new double[newCapacity][newCapacity];
		T[] newVertices = (T[]) new Object[newCapacity];
		
		for(int i = 0; i < numVertices; i++) 
			newVertices[i] = vertices[i];	
		
		
		for (int i = 0; i < newAdjMatrix.length; i++) 
			for(int j = 0; j < newAdjMatrix.length; j++)
				newAdjMatrix[i][j] = INFINITY;
			
		for (int i = 0; i < numVertices; i++) 
			for(int j = 0; j < numVertices; j++)
				newAdjMatrix[i][j] = adjMatrix[i][j];
		
		adjMatrix = newAdjMatrix;
		vertices = newVertices;
		CAPACITY = newCapacity;	
	}
	
	public List<T> neighbors(T vertex) {
		if(!isValidVertex(vertex))
			return null;
		int row = vertexIndex(vertex);
		ArrayList<T> list = new ArrayList();
		for(int i = 0; i < numVertices; i++)
			if(adjMatrix[row][i] != INFINITY)
				list.add(vertices[i]);
		return list;
	}

	public T nextNeighbor(T vertex, T currNeighbor) {
		if(!isValidVertex(vertex) || !isValidVertex(currNeighbor))
			return null;
		int row = vertexIndex(vertex);
		int col = vertexIndex(currNeighbor);
		for(int i = col + 1; i < numVertices; i++)
			if(adjMatrix[row+1][i] != INFINITY)
				return vertices[i];
		return null;
	}
	
	
	//Added the following methods
	public boolean isNeighbor(T vertex, T neighbor) {
		if(!isValidVertex(vertex) || !isValidVertex(neighbor))
			return false;
		int row = vertexIndex(vertex);
		int col = vertexIndex(neighbor);
		if(adjMatrix[row][col] != INFINITY)
			return true;
		else if(adjMatrix[col][row] != INFINITY)
			return true;
		return false;	
	}
	
	public boolean allVisited(boolean[] array) {
		for (boolean b : array)
			if (!b)
				return false;
		return true;
	}
	
	public int[] singleSourceShortestPath(T sourceVertex) {
		int[] Distance = new int[numVertices];
		int[] Previous = new int[numVertices];
		boolean[] Visited = new boolean[numVertices];
		boolean done = false;

		//sets the source vertex (in this case, 15) to have a distance of 0 and it's previous to be -1
		//all other vertices have a distance of the integer's max value
		for(int j = 0; j < numVertices; j++) {
			if(vertices[j].equals(sourceVertex)) {
				Distance[j] = 0;
				Previous[j] = -1;
			} else {
				Distance[j] = Integer.MAX_VALUE;
			}
			Visited[j] = false;
		}
		
		
		int i = 0;
		while(!done) { 																//While not done, it runs through each vertex to find...
			if(Distance[i] != Integer.MAX_VALUE && Visited[i] == false) {			//the first first vertex to have value < the max value. - that is represented by i.
				for(int j = 0; j < numVertices; j++) {								//It then searches all vertex to find...
					if((isNeighbor(vertices[i], vertices[j]))){						//its neighbors. 
						if(Distance[i] + adjMatrix[i][j] < Distance[j]) {			//Of its neighbors (represented by j), if its distance plus that j's distance is less than the j's distance...
							Previous[j] = vertexIndex(vertices[i]);					//the the previous is set to the index of it...
							Distance[j] = (int) (Distance[i] + adjMatrix[i][j]);	//and the Distance of the neighbor is set to its distance plus the neighbor's.
						} else if (Distance[i] + adjMatrix[j][i] < Distance[j]) {
							Previous[j] = vertexIndex(vertices[i]);
							Distance[j] = (int) (Distance[i] + adjMatrix[j][i]);
						}
					}
				}
				Visited[i] = true;													//This point is now considered visited.
			} 
			
			if(allVisited(Visited))													//If all points are visited...
				done = true;														//the loop ends.
			
			if(!done) {																//Otherwise, ...
				i++;																//it moves on to the next point of the array...
				if(i >= numVertices) 												//and if i is about to exceed the array limit...
					i = 0;															//it gets sent back to the beginning
			}
		}
		

		return Previous;
	}
	
	public T[] shortestPath (T sourceVertex, T endVertex) {
		T[] Path;
		int[] Previous = singleSourceShortestPath(sourceVertex);
		int [] temp = new int[numVertices];
		
		int j = -1; 
		for(int i = 0; i < numVertices; i++)
			if(vertices[i].equals(endVertex))
				j = i;
		
		//Gets the path but backwards
		temp[0] = j;
		int k = 1;
		while(Previous[j] != -1) {
			temp[k] = Previous[j];
			k++;
			j = Previous[j];
		}
		
		Path = (T[]) new Object[k] ;
		int l = k-1;
		for(int i = 0; i < k; i++) { //Flips the order of the path so it starts with the source point and ends with the end point
			Path[i] = vertices[temp[l]];
			l--;
		}
		
		//Prints the path
		System.out.print("\nShortest path: ");
		for(int i = 0; i < Path.length; i++)
			System.out.print(Path[i] + " ");
		System.out.println();
		
		//Adds the points of the referenced vertices to the PathOfPoints array in the Driver class by referencing the vertices array list 
		Driver.PathOfPoints = new Point[Path.length];
		for(int i = 0; i < Path.length; i++) 
			for(int g = 0; g < Driver.vertices.size(); g++) 
				if(Driver.vertices.get(g).getName().equals(Path[i]))
					Driver.PathOfPoints[i] = new Point(Driver.vertices.get(g).getX(), Driver.vertices.get(g).getY());
		
		//Returns the array of T objects
		return Path;
	}
}
 