package tile.tile_interactive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import application.GamePanel;
import entity.Entity;

public class InteractiveTile extends Entity {
	
	public InteractiveTile(GamePanel gp) {
		super(gp);	
	}
	public InteractiveTile(GamePanel gp, int col, int row) {
		super(gp);
	}
	
	public void update() {
		
		
	}
		
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		boolean offCenter = false;
		
		if (getScreenX() > worldX) {
			screenX = worldX;
			offCenter = true;
		}
		if (getScreenY() > worldY) {
			screenY = worldY;
			offCenter = true;
		}
		
		// FROM PLAYER TO RIGHT-EDGE OF SCREEN
		int rightOffset = gp.screenWidth - getScreenX();		
		
		// FROM PLAYER TO RIGHT-EDGE OF WORLD
		if (rightOffset > gp.worldWidth - worldX) {
			screenX = gp.screenWidth - (gp.worldWidth - worldX);
			offCenter = true;
		}			
		
		// FROM PLAYER TO BOTTOM-EDGE OF SCREEN
		int bottomOffSet = gp.screenHeight - getScreenY();
		
		// FROM PLAYER TO BOTTOM-EDGE OF WORLD
		if (bottomOffSet > gp.worldHeight - worldY) {
			screenY = gp.screenHeight - (gp.worldHeight - worldY);
			offCenter = true;
		}
		
		switch (direction) {
			case "up":
			case "upleft":
			case "upright": image = up1; break;
			case "down": 
			case "downleft":
			case "downright": image = down1; break;
			case "left": image = left1; break;
			case "right": image = right1; break;
		}
		
		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {				
						
			g2.drawImage(image, screenX, screenY, null);
		}
		else if (offCenter) {
			g2.drawImage(image, screenX, screenY, null);
		}
		
		if (gp.keyH.debug) {
			g2.setColor(Color.RED);
			g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
		}
	}
}