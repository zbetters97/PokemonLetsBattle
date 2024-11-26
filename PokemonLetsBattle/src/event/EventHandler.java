package event;
import java.util.Arrays;
import java.util.List;

import application.GamePanel;
import entity.Entity;

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
			
			// EVENT SPOTS
			if (hit(0, 30, 25, Arrays.asList("up"))) teleport(1, 22, 32, gp.shop, "up", false, false); // POKECENTER ENTRANCE
			else if (hit(1, 21, 32, "right", 2, Arrays.asList("down"))) teleport(0, 30, 25, gp.town, "down", true, true); // POKECETER EXIT
			else if (hit(1, 22, 28, Arrays.asList("up"))) speak(gp.npc[1][0]); // NURSE JOY TALK
			else if (hit(2, 18, 27, Arrays.asList("left"))) speak(gp.npc[2][0]); // STORE CLERK TALK 
			else if (hit(0, 35, 21, Arrays.asList("up"))) teleport(2, 19, 31, gp.shop, "up", false, false); // POKEMART ENTRANCE
			else if (hit(2, 18, 31, "right", 2, Arrays.asList("down"))) teleport(0, 35, 21, gp.town, "down", true, true); // POKEMART EXIT
		}
	}
	private boolean hit(int map, int col, int row, List<String> reqDirection) {
		
		boolean hit = false;
		
		if (map == gp.currentMap) {

			eventRect[map][col][row].x = 16;
			eventRect[map][col][row].y = 16;
			eventRect[map][col][row].width = 16;
			eventRect[map][col][row].height = 16;			
			
			if (getHit(map, col, row, reqDirection))
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
	
	private boolean hit(int map, int col, int row, String spanDirection, int tiles, List<String> reqDirection) {
		
		boolean hit = false;
		
		if (map == gp.currentMap) {
									
			eventRect[map][col][row].x = 0;
			eventRect[map][col][row].y = 0;
			eventRect[map][col][row].width = 48;
			eventRect[map][col][row].height = 48;	
			
			// EXTEND HEIGHT/WIDTH X NUMBER OF TILES
			switch (spanDirection) {
				case "down":
					eventRect[map][col][row].height = 48 * tiles;										
					break;
				case "right":
					eventRect[map][col][row].width = 48 * tiles;							
					break;
				case "downright":
					eventRect[map][col][row].height = 48 * tiles;	
					eventRect[map][col][row].width = 48 * tiles;					
					break;
			}		
						
			if (getHit(map, col, row, reqDirection))
				hit = true;
		}		
		
		return hit;		
	}
		
	private void teleport(int map, int col, int row, int area, String direction, boolean moving, boolean playExitSE) {		
				
//		canTouchEvent = false;
		
		if (playExitSE) playExitSE(); 
		
		tempMap = map;
		tempCol = col;
		tempRow = row;
		
		gp.nextArea = area;		
		gp.ui.tDirection = direction;
		gp.ui.tMoving = moving;
				
		gp.gameState = gp.transitionState;
	}	
	
	private void speak(Entity npc) {		
		if (gp.keyH.aPressed) {
			npc.speak();
		}
	}
	
	private void playExitSE() { 
		gp.playSE(gp.world_SE, "exit");
	}
}