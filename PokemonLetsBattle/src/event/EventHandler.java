package event;
import java.util.List;

import application.GamePanel;

public class EventHandler {
	
	private GamePanel gp;
	private EventRect eventRect[][][];
	
	public int previousEventX;
	public int previousEventY;
	public boolean canTouchEvent = true;
	public int tempMap, tempCol, tempRow;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		
		// EVENT RECTANGLE ON EVERY WORLD MAP TILE
		eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		int map = 0;
		int col = 0;
		int row = 0;
		while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			// DRAW HIT BOX ON EACH EVENT
			eventRect[map][col][row] = new EventRect();			
			eventRect[map][col][row].x = 23;
			eventRect[map][col][row].y = 23;
			eventRect[map][col][row].width = 2;
			eventRect[map][col][row].height = 2;
			eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
			eventRect[map][col][row].eventRectDefaultWidth = eventRect[map][col][row].width;
			eventRect[map][col][row].eventRectDefaultHeight = eventRect[map][col][row].height;
			
			col++;
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
				
				if (row == gp.maxWorldRow) {
					row = 0;
					map++;
				}
			}
		}
	}
	
	public void checkEvent() {
		
		// CHECK IF PLAYER IS >1 TILE AWAY FROM PREVIOUS EVENT
		int xDistance = Math.abs(gp.player.worldX - previousEventX); 
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance); // FIND GREATER OF THE TWO
		
		if (distance > gp.tileSize) 
			canTouchEvent = true;
		
		// IF EVENT CAN HAPPEN AT X/Y FACING DIRECTION
		if (canTouchEvent) {
			
			// TELEPORT SPOTS
			if (hit(0, 30, 25, true)) teleport(0, 12, 12, gp.shop, "up");
			else if (hit(0, 35, 21, true)) teleport(0, 12, 12, gp.shop, "up"); 
			else if (hit(0, 25, 18, true)) teleport(0, 12, 12, gp.shop, "up"); 
		}
	}
	private boolean hit(int map, int col, int row, boolean fullTile) {
		
		boolean hit = false;
		
		if (map == gp.currentMap) {

			if (fullTile) {
				eventRect[map][col][row].x = 16;
				eventRect[map][col][row].y = 16;
				eventRect[map][col][row].width = 16;
				eventRect[map][col][row].height = 16;
			}
			
			if (getHit(map, col, row, null))
				hit = true;
		}		
		
		return hit;		
	}
		
	private boolean getHit(int map, int col, int row, List<String> reqDirection) {
		
		boolean hit = false;
		
		// EVENT hitbox
		eventRect[map][col][row].x += col * gp.tileSize;
		eventRect[map][col][row].y += row * gp.tileSize;
		
		// PLAYER hitbox
		gp.player.hitbox.x += gp.player.worldX;		
		gp.player.hitbox.y += gp.player.worldY;
		
		// PLAYER INTERACTS WITH EVENT AND EVENT CAN HAPPEN
		if (gp.player.hitbox.intersects(eventRect[map][col][row]) && 
				!eventRect[map][col][row].eventDone) {
			
			if (reqDirection != null) {
				for (String dir : reqDirection) {
					if (gp.player.direction.equals(dir)) {
						hit = true;
						
						previousEventX = gp.player.worldX;
						previousEventY = gp.player.worldY;
						
						break;
					}
				}
			}
			else {				
				hit = true;
					
				previousEventX = gp.player.worldX;
				previousEventY = gp.player.worldY;
			}			
		}
		
		// RESET PLAYER hitbox
		gp.player.hitbox.x = gp.player.hitboxDefaultX;
		gp.player.hitbox.y = gp.player.hitboxDefaultY;
		
		// RESET EVENT hitbox
		eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
		eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;	
		eventRect[map][col][row].width = eventRect[map][col][row].eventRectDefaultWidth;
		eventRect[map][col][row].height = eventRect[map][col][row].eventRectDefaultHeight;
		
		return hit;
	}
		
	private void teleport(int map, int col, int row, int area, String direction) {		
		
		tempMap = map;
		tempCol = col;
		tempRow = row;
		
		canTouchEvent = false;
	}	
	
	private void playStairsUpSE() {
		
	}
	private void playStairsDownSE() {
		
	}
}