package application;

import entity.*;

public class CollisionChecker {
	
	private GamePanel gp;
	
	/** CONSTRUCTOR **/
	public CollisionChecker(GamePanel gp) {		
		this.gp = gp;
	}

	// TILE COLLISION
	public void checkTile(Entity entity) {
						
		// COLLISION BOX (left side, right side, top, bottom)
		int entityLeftWorldX = entity.worldX + entity.hitbox.x;
		int entityRightWorldX = entity.worldX + entity.hitbox.x + entity.hitbox.width;
		int entityTopWorldY = entity.worldY + entity.hitbox.y;
		int entityBottomWorldY = entity.worldY + entity.hitbox.y + entity.hitbox.height;
		
		int entityLeftCol = entityLeftWorldX / gp.tileSize;
		int entityRightCol = entityRightWorldX / gp.tileSize;
		int entityTopRow = entityTopWorldY / gp.tileSize;
		int entityBottomRow = entityBottomWorldY / gp.tileSize;
		
		// PREVENT COLLISION DETECTION OUT OF BOUNDS
		if (entityTopRow <= 0) return;		
		if (entityBottomRow >= gp.maxWorldRow - 1) return;		
		if (entityLeftCol <= 0) return;		
		if (entityRightCol >= gp.maxWorldCol - 1) return;
		
		// detect the two tiles player is interacting with
		int tileNum1 = 0, tileNum2 = 0;
		
		// KNOCKBACK DIRECTION
		String direction = entity.direction;
		
		// find tile player will interact with, factoring in speed
		switch (direction) {
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;	
				
				// tiles at top-left and top-right
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
				break;
			case "upleft":
				
				// tiles at top-left and left-top
				entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
								
				entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];	
				
				break;
			case "upright":
				
				// tiles at top-right and right-top
				entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
				
				entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];	
				
				break;
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
				
				// tiles at bottom-left and bottom-right
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
				
				break;
			case "downleft":
				
				// tiles at bottom-left and left-bottom
				entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
				
				entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
				
				break;
			case "downright":
				
				// tiles at bottom-right and right-bottom
				entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
				
				entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
				
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
				
				// tiles at left-top and left-bottom
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
				
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
				
				// tiles at right-top and right-bottom
				tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
				
				break;
			default: 
				entity.collisionOn = false; 
				return;
		}		
		// WATER
		if (gp.tileM.tile[tileNum1].water || gp.tileM.tile[tileNum2].water) {			
			entity.collisionOn = true;				
		}
		
		// NORMAL COLLISION
		else if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {	
			entity.collisionOn = true;			
		}
		
		// GRASS
		else if (gp.tileM.grassTiles.contains(tileNum1) || gp.tileM.grassTiles.contains(tileNum2)) {			
			entity.inGrass = true;		
		}
		else {
			entity.inGrass = false;
		}
	}
			
	// ENTITY COLLISION
	public int checkEntity(Entity entity, Entity[][] target) {
		
		int index = -1;
		
		String direction = entity.direction;
		
		for (int i  = 0; i < target[1].length; i++) {
			
			if (target[gp.currentMap][i] != null) {			
												
				entity.hitbox.x = entity.worldX + entity.hitbox.x;
				entity.hitbox.y = entity.worldY + entity.hitbox.y;
								
				target[gp.currentMap][i].hitbox.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].hitbox.x;
				target[gp.currentMap][i].hitbox.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].hitbox.y;
			
				switch (direction) {
					case "up":					
						entity.hitbox.y -= entity.speed;
						break;					
					case "upleft":
						entity.hitbox.y -= entity.speed;
						entity.hitbox.x -= entity.speed;
						break;
					case "upright":
						entity.hitbox.y -= entity.speed;
						entity.hitbox.x += entity.speed;
						break;
					case "down":					
						entity.hitbox.y += entity.speed;
						break;
					case "downleft":					
						entity.hitbox.y += entity.speed;
						entity.hitbox.x -= entity.speed;
						break;
					case "downright":					
						entity.hitbox.y += entity.speed;
						entity.hitbox.x += entity.speed;
						break;
					case "left":					
						entity.hitbox.x -= entity.speed;
						break;
					case "right":					
						entity.hitbox.x += entity.speed;
						break;	
					default: 
						entity.collision = true; 
						return index;
				}
				
				if (entity.hitbox.intersects(target[gp.currentMap][i].hitbox)) {	
																				
					if (target[gp.currentMap][i] != entity) {
						index = i;	
						
						if (target[gp.currentMap][i].collision) {
							entity.collisionOn = true;								
						}											
					}
				}
				
				// reset entity solid area
				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				
				// reset object solid area
				target[gp.currentMap][i].hitbox.x = target[gp.currentMap][i].hitboxDefaultX;
				target[gp.currentMap][i].hitbox.y = target[gp.currentMap][i].hitboxDefaultY;
			}
		}		
		return index;
	}
	
	// NPC COLLISION
	public int checkNPC() {
		
		int index = -1;
		int speed = 30;
		
		String direction = gp.player.direction;
			
		for (int i  = 0; i < gp.npc[1].length; i++) {
			
			if (gp.npc[gp.currentMap][i] != null) {			
				
				// get gp.player's solid area position
				gp.player.hitbox.x = gp.player.worldX + gp.player.hitbox.x;
				gp.player.hitbox.y = gp.player.worldY + gp.player.hitbox.y;
				
				// get object's solid area position
				gp.npc[gp.currentMap][i].hitbox.x = gp.npc[gp.currentMap][i].worldX + gp.npc[gp.currentMap][i].hitbox.x;
				gp.npc[gp.currentMap][i].hitbox.y = gp.npc[gp.currentMap][i].worldY + gp.npc[gp.currentMap][i].hitbox.y;
				
				// find where gp.player will be after moving in a direction
				// ask if gp.npc and gp.player intersect 
				switch (direction) {
					case "up":					
						gp.player.hitbox.y -= speed;
						break;					
					case "upleft":
						gp.player.hitbox.y -= speed;
						gp.player.hitbox.x -= speed;
						break;
					case "upright":
						gp.player.hitbox.y -= speed;
						gp.player.hitbox.x += speed;
						break;
					case "down":					
						gp.player.hitbox.y += speed;
						break;
					case "downleft":					
						gp.player.hitbox.y += speed;
						gp.player.hitbox.x -= speed;
						break;
					case "downright":					
						gp.player.hitbox.y += speed;
						gp.player.hitbox.x += speed;
						break;
					case "left":					
						gp.player.hitbox.x -= speed;
						break;
					case "right":					
						gp.player.hitbox.x += speed;
						break;	
					default: 
						return index;
				}
				
				if (gp.player.hitbox.intersects(gp.npc[gp.currentMap][i].hitbox)) {	
					
					if (gp.npc[gp.currentMap][i] != gp.player) {		
						index = i;	
					}
				}
				
				// reset gp.player solid area
				gp.player.hitbox.x = gp.player.hitboxDefaultX;
				gp.player.hitbox.y = gp.player.hitboxDefaultY;
				
				// reset object solid area
				gp.npc[gp.currentMap][i].hitbox.x = gp.npc[gp.currentMap][i].hitboxDefaultX;
				gp.npc[gp.currentMap][i].hitbox.y = gp.npc[gp.currentMap][i].hitboxDefaultY;
			}
		}		
		
		return index;
	}

	// CONTACT PLAYER COLLISION
	public boolean checkPlayer(Entity entity) {
				
		boolean contactPlayer = false;
		
		String direction = entity.direction;
				
		entity.hitbox.x = entity.worldX + entity.hitbox.x;
		entity.hitbox.y = entity.worldY + entity.hitbox.y;
				
		gp.player.hitbox.x = gp.player.worldX + gp.player.hitbox.x;
		gp.player.hitbox.y = gp.player.worldY + gp.player.hitbox.y;
			
		switch (direction) {
			case "up": entity.hitbox.y -= entity.speed; break;		
			case "down": entity.hitbox.y += entity.speed; break;
			case "left": entity.hitbox.x -= entity.speed; break;
			case "right": entity.hitbox.x += entity.speed; break;	
			default: entity.collision = true; return false;
		}
		
		if (entity.hitbox.intersects(gp.player.hitbox)) {																			
			entity.collisionOn = true;
			contactPlayer = true;
		}
		
		// reset entity solid area
		entity.hitbox.x = entity.hitboxDefaultX;
		entity.hitbox.y = entity.hitboxDefaultY;
		
		// reset object solid area
		gp.player.hitbox.x = gp.player.hitboxDefaultX;
		gp.player.hitbox.y = gp.player.hitboxDefaultY;
		
		return contactPlayer;
	}
}