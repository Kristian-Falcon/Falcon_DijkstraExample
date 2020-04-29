//Class of the vertext object. This is pretty straight forward
public class Vertex {
	private int x;
	private int y;
	private String name;
	
	public Vertex(int x, int y, String name) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Vertex v) {
		if(v.equals(getName()) && v.getX() == x && v.getY() == y) 
			return true;
		return false;
	}
}
