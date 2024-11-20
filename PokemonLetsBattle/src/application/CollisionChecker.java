package application;

import entity.*;
import entity.object.OBJ_Grass;

public class CollisionChecker {
	
	private GamePanel gp;
	
	/** CONSTRUCTOR **/
	public CollisionChecker(GamePanel gp) {		
		this.gp = gp;
	}

	// TILE COLLISION
	public void checkTile(Entity entity) {
		
		int x;
		int y;
		
		// detect the tile player is interacting with
		int tile = 0;
		
		// find tile entity will interact with
		switch (entity.direction) {
			case "up":
				
				// tile above entity
				x = entity.worldX / gp.tileSize;
				y = (entity.worldY - gp.tileSize) / gp.tileSize;
				tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
				
				break;
			case "down":
				
				// tile below entity
				x = entity.worldX / gp.tileSize;
				y = (entity.worldY + gp.tileSize) / gp.tileSize;
				tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
				
				break;
			case "left":
				
				// tile left of entity
				x = (entity.worldX - gp.tileSize)/ gp.tileSize;
				y = entity.worldY / gp.tileSize;
				tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
				
				break;
			case "right":

				// tile right of entity
				x = (entity.worldX + gp.tileSize)/ gp.tileSize;
				y = entity.worldY / gp.tileSize;
				tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
								
				break;
			default: 
				entity.collisionOn = false; 
				return;
		}		
		
		// WATER
		if (gp.tileM.tile[tile].water) {	
			if (entity.action != Entity.Action.SURFING) {
				entity.collisionOn = true;				
			}				
		}		
		
		// NORMAL COLLISION
		else if (gp.tileM.tile[tile].collision) {	
			entity.collisionOn = true;			
		}
		
		// GRASS
		else if (!gp.tileM.tile[tile].grass) {		
			entity.inGrass = false;
		}
	}
	
	// GRASS COLLISION
	public void checkGrass(Entity entity) {
		
		// X,Y OF CURRENT TILE
		int x = entity.worldX / gp.tileSize;
		int y = entity.worldY / gp.tileSize;
		
		// detect the tile player is interacting with
		int tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
		
		// GRASS
		if (gp.tileM.tile[tile].grass) {			
			entity.inGrass = true;		
			gp.particleList.add(new OBJ_Grass(gp, x, y));
		}
		else {
			entity.inGrass = false;
		}
	}
	
	public boolean checkWater(Entity entity) {
		
		boolean foundWater = false;
		
		// X,Y OF CURRENT TILE
		int x = entity.worldX / gp.tileSize;
		int y = entity.worldY / gp.tileSize;
		
		switch (entity.direction) {
			case "up": y--; break;
			case "down": y++; break;
			case "left": x--; break;
			case "right": x++; break;
		}
		
		// detect the tile player is interacting with
		int tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
		
		if (gp.tileM.tile[tile].water) {			
			foundWater = true;
		}
		
		return foundWater;
	}	
	public boolean checkGround(Entity entity) {
		
		boolean foundGround = false;
		
		// X,Y OF CURRENT TILE
		int x = entity.worldX / gp.tileSize;
		int y = entity.worldY / gp.tileSize;
		
		// detect the tile player is interacting with
		int tile = gp.tileM.mapTileNum[gp.currentMap][x][y];
		
		if (!gp.tileM.tile[tile].water && !gp.tileM.tile[tile].collision) {			
			foundGround = true;
		}
		
		return foundGround;
	}
			
	// ENTITY COLLISION
	public int checkEntity(Entity entity, Entity[][] target) {
		
		int index = -1;
		
		for (int i  = 0; i < target[1].length; i++) {
			
			if (target[gp.currentMap][i] != null) {			
												
				entity.hitbox.x = entity.worldX;
				entity.hitbox.y = entity.worldY;
								
				target[gp.currentMap][i].hitbox.x = target[gp.currentMap][i].worldX;
				target[gp.currentMap][i].hitbox.y = target[gp.currentMap][i].worldY;
			
				switch (entity.direction) {
					case "up": entity.hitbox.y -= gp.tileSize;	break;
					case "down": entity.hitbox.y += gp.tileSize; break;
					case "left": entity.hitbox.x -= gp.tileSize; break;
					case "right": entity.hitbox.x += gp.tileSize; break;
					default: entity.collision = true; return index;
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
			
		for (int i  = 0; i < gp.npc[1].length; i++) {
			
			if (gp.npc[gp.currentMap][i] != null) {			
				
				// get gp.player's solid area position
				gp.player.hitbox.x = gp.player.worldX;
				gp.player.hitbox.y = gp.player.worldY;
				
				// get object's solid area position
				gp.npc[gp.currentMap][i].hitbox.x = gp.npc[gp.currentMap][i].worldX;
				gp.npc[gp.currentMap][i].hitbox.y = gp.npc[gp.currentMap][i].worldY;
				
				// find where gp.player will be after moving in a direction
				// ask if gp.npc and gp.player intersect 
				switch (gp.player.direction) {
					case "up": gp.player.hitbox.y -= gp.tileSize; break;	
					case "down": gp.player.hitbox.y += gp.tileSize; break;
					case "left": gp.player.hitbox.x -= gp.tileSize; break;
					case "right": gp.player.hitbox.x += gp.tileSize; break;	
					default: return index;
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

	// OBJECT COLLISION
	public int checkObject(Entity entity, boolean player) {
		
		int index = -1;
		
		for (int i  = 0; i < gp.obj[1].length; i++) {
			
			if (gp.obj[gp.currentMap][i] != null) {
				
				// get entity's solid area position
				entity.hitbox.x = entity.worldX;
				entity.hitbox.y = entity.worldY;
				
				// get object's solid area position
				gp.obj[gp.currentMap][i].hitbox.x = gp.obj[gp.currentMap][i].worldX;
				gp.obj[gp.currentMap][i].hitbox.y = gp.obj[gp.currentMap][i].worldY;
				
				// find where entity will be after moving in a direction
				// ask if object and entity intersect 
				switch (entity.direction) {
					case "up": entity.hitbox.y -= gp.tileSize;	break;
					case "down": entity.hitbox.y += gp.tileSize; break;
					case "left": entity.hitbox.x -= gp.tileSize; break;
					case "right": entity.hitbox.x += gp.tileSize; break;
					default: entity.collision = true; return index;
				}
				
				if (entity.hitbox.intersects(gp.obj[gp.currentMap][i].hitbox)) {
					
					if (gp.obj[gp.currentMap][i].collision) {
						entity.collisionOn = true;	
						index = i;
					}
//					if (player && gp.obj[gp.currentMap][i].canPickup) {
//						index = i;
//					}
//					else if (gp.obj[gp.currentMap][i].type == gp.obj[gp.currentMap][i].type_collectable) {
//						entity.collisionOn = false;
//					}
				}
				
				// reset entity solid area
				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				
				// reset object solid area
				gp.obj[gp.currentMap][i].hitbox.x = gp.obj[gp.currentMap][i].hitboxDefaultX;
				gp.obj[gp.currentMap][i].hitbox.y = gp.obj[gp.currentMap][i].hitboxDefaultY;
			}
		}		
		return index;
	}
	
	// INTERACTIVE OBJECT COLLISION
	public int checkObject_I(Entity entity) {
		
		int index = -1;
		
		for (int i  = 0; i < gp.obj_i[1].length; i++) {
			
			if (gp.obj_i[gp.currentMap][i] != null) {
				
				// get entity's solid area position
				entity.hitbox.x = entity.worldX;
				entity.hitbox.y = entity.worldY;
				
				// get obj_iect's solid area position
				gp.obj_i[gp.currentMap][i].hitbox.x = gp.obj_i[gp.currentMap][i].worldX;
				gp.obj_i[gp.currentMap][i].hitbox.y = gp.obj_i[gp.currentMap][i].worldY;
				
				// find where entity will be after moving in a direction
				// ask if obj_iect and entity intersect 
				switch (entity.direction) {
					case "up": entity.hitbox.y -= gp.tileSize;	break;
					case "down": entity.hitbox.y += gp.tileSize; break;
					case "left": entity.hitbox.x -= gp.tileSize; break;
					case "right": entity.hitbox.x += gp.tileSize; break;
					default: entity.collision = true; return index;
				}
								
				if (entity.hitbox.intersects(gp.obj_i[gp.currentMap][i].hitbox)) {						
					if (gp.obj_i[gp.currentMap][i].collision) {
						entity.collisionOn = true;	
						index = i;			
					}
				}
				
				// reset entity solid area
				entity.hitbox.x = entity.hitboxDefaultX;
				entity.hitbox.y = entity.hitboxDefaultY;
				
				// reset obj_iect solid area
				gp.obj_i[gp.currentMap][i].hitbox.x = gp.obj_i[gp.currentMap][i].hitboxDefaultX;
				gp.obj_i[gp.currentMap][i].hitbox.y = gp.obj_i[gp.currentMap][i].hitboxDefaultY;
			}
		}		
		
		return index;
	}
	
	// CONTACT PLAYER COLLISION
	public boolean checkPlayer(Entity entity) {
				
		boolean contactPlayer = false;
				
		entity.hitbox.x = entity.worldX + entity.hitbox.x;
		entity.hitbox.y = entity.worldY + entity.hitbox.y;
				
		gp.player.hitbox.x = gp.player.worldX + gp.player.hitbox.x;
		gp.player.hitbox.y = gp.player.worldY + gp.player.hitbox.y;
			
		switch (entity.direction) {
			case "up": entity.hitbox.y -= gp.tileSize;	break;
			case "down": entity.hitbox.y += gp.tileSize; break;
			case "left": entity.hitbox.x -= gp.tileSize; break;
			case "right": entity.hitbox.x += gp.tileSize; break;
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