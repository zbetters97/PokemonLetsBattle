package ai;

public class Node {
	
	public int col;
	public int row;
	Node parent; // CORROSPONDING NODE
	
	int gCost; // DISTANCE BETWEEN START AND CURRENT
	int hCost; // DISTANCE BETWEEN CURRENT AND GOAL
	int fCost; // SUM OF H AND G
	
	boolean solid; // COLLISION
	boolean open; // EVALUATION
	boolean checked; // ALREADY EVALUATED
	
	public Node(int col, int row) {
		this.col = col;
		this.row = row;		
	}
}