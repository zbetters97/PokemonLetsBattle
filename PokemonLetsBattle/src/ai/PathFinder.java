package ai;

import java.util.ArrayList;

import application.GamePanel;

public class PathFinder {

	private GamePanel gp;
	Node[][]node;
	
	ArrayList<Node> openList = new ArrayList<>();
	public ArrayList<Node> pathList = new ArrayList<>();
	
	Node startNode, goalNode, currentNode;
	
	boolean goalReached = false;
	int steps = 0;
	
	public PathFinder(GamePanel gp) {
		this.gp = gp;
		instantiateNodes();
	}
	
	public void instantiateNodes() {		
		
		node = new Node[gp.maxWorldCol][gp.maxWorldRow];
		
		// CREATE NODE ON EVERY MAP TILE
		int col = 0;
		int row = 0;		
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			node[col][row] = new Node(col, row);			
			col++;
			
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
			}				
		}
	}
	
	public void resetNodes() {
				
		// RESET ALL NODES
		int col = 0;
		int row = 0;		
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			node[col][row].open = false;
			node[col][row].checked = false;
			node[col][row].solid = false;
			
			col++;			
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
			}				
		} 
		
		// RESET OTHER SETTINGS
		openList.clear();
		pathList.clear();
		goalReached = false;
		steps = 0;
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
		
		resetNodes();
		
		// SET START AND GOAL NODE
		startNode = node[startCol][startRow];
		goalNode = node[goalCol][goalRow];	
		
		currentNode = startNode;
		openList.add(currentNode);
		
		// LOOP THROUGH ALL TILES
		int col = 0;
		int row = 0;
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			// SET SOLID NODE IF HAS COLLISION
			
			// CHECK TILES
			int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
			if (gp.tileM.tile[tileNum].collision || gp.tileM.tile[tileNum].water) {
				node[col][row].solid = true;
			}
			
			// CHECK iTILES
			for (int i = 0; i < gp.iTile[1].length; i++) {
				
				if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].collision) {
					int iCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
					int iRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
					node[iCol][iRow].solid = true;
				}
			}
			
			// CHECK OBJECTS
			for (int i = 0; i < gp.obj[1].length; i++) {
				
				if (gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].collision) {
					int iCol = gp.obj[gp.currentMap][i].worldX / gp.tileSize;
					int iRow = gp.obj[gp.currentMap][i].worldY / gp.tileSize;
					node[iCol][iRow].solid = true;
				}
			}
			// CHECK INTERACTIVE OBJECTS
			for (int i = 0; i < gp.obj_i[1].length; i++) {
				
				if (gp.obj_i[gp.currentMap][i] != null && gp.obj_i[gp.currentMap][i].collision) {
					int iCol = gp.obj_i[gp.currentMap][i].worldX / gp.tileSize;
					int iRow = gp.obj_i[gp.currentMap][i].worldY / gp.tileSize;
					node[iCol][iRow].solid = true;
				}
			}
			
			// CHECK STATIONARY NPCs
			for (int i = 0; i < gp.npc[1].length; i++) {
				
				if (gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].speed == 0) {
					int iCol = gp.npc[gp.currentMap][i].worldX / gp.tileSize;
					int iRow = gp.npc[gp.currentMap][i].worldY / gp.tileSize;
					node[iCol][iRow].solid = true;
				}
			}
			
			// SET COST ON EACH NODE
			setCost(node[col][row]);
			
			col++;			
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
			}				
		} 
	}	
	
	public void setCost(Node node) {
		
		// SET G COST
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;
		
		// SET H COST
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;
		
		// SET F COST
		node.fCost = node.gCost + node.hCost;
	}
	
	public boolean search() {
		
		// LOOP UNTIL FINISHED OR TOO MANY STEPS
		while (!goalReached && steps < 500) {
			
			int col = currentNode.col;
			int row = currentNode.row;
			
			// CURRENT NODE CHECKED AND NOT OPEN
			currentNode.checked = true;
			openList.remove(currentNode);
			
			// OPEN SURROUNDING NODES (WITHIN WORLD BOUNDARY)
			// UP 
			if (row - 1 >= 0) 
				openNode(node[col][row - 1]);
			// DOWN
			if (row + 1 < gp.maxWorldRow) 
				openNode(node[col][row + 1]);	
			// LEFT
			if (col - 1 >= 0) 
				openNode(node[col - 1][row]);					
			// RIGHT
			if (col + 1 < gp.maxWorldCol) 
				openNode(node[col + 1][row]);		
			
			// RESET BEST NODE (FIRST IS ALWAYS < 999)
			int bestNodeIndex = 0;
			int bestNodefCost = 999;
			
			// LOOP THROUGH EACH POTENTIAL NODE
			for (int i = 0; i < openList.size(); i++) {
				
				// IF F COST IS BETTER
				if (openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				}				
				// IF F COST IS EQUAL, CHECK G COST
				else if (openList.get(i).fCost == bestNodefCost) {
					if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) 
						bestNodeIndex = i;					
				}
			}
			
			// IF NO NODE AVAILABLE, END SEARCH
			if (openList.size() == 0)
				break;			
			
			// FIND NEXT BEST NODE
			currentNode = openList.get(bestNodeIndex);
			
			// GOAL REACHED
			if (currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			
			// STEP TAKEN			
			steps++;
		}
		
		return goalReached;
	}
	
	// NODES BEING EVALUATED
	public void openNode(Node node) {		
		
		// NOT OPEN, CHECKED, OR SOLID
		if (!node.open && !node.checked && !node.solid) {
			node.open = true;
			node.parent = currentNode;
			openList.add(node);
		}
	}
	
	public void trackThePath() {

		// BACKTRACK FROM GOAL UNTIL START IS REACHED
		Node current = goalNode;		
		while (current != startNode) {
			
			// ADD TO FIRST SLOT
			pathList.add(0, current);
			
			// GET PREVIOUS NODE
			current = current.parent;
		}
	}
}