package entity.object;

import java.awt.Graphics2D;

import application.GamePanel;
import entity.Entity;

public class OBJ_Grass extends Entity {

	Entity generator;
	
	public OBJ_Grass(GamePanel gp, int x, int y) {		
		super(gp);
		
		worldX = x * gp.tileSize;
		worldY = y * gp.tileSize;	
		
		direction = "down";
		hasShadow = false;
		
		down1 = setup("/objects/grass_1"); 
		down2 = setup("/objects/grass_2"); 
		down3 = setup("/objects/grass_3"); 
	}
	
	public void update() {
		
		spriteCounter++;
		if (spriteCounter < 8) {
			spriteNum = 1; 
		}
		else if (8 <= spriteCounter && spriteCounter < 16) {
			spriteNum = 2; 
		}
		else if (16 <= spriteCounter && spriteCounter < 24) {
			spriteNum = 3;
		}
		else if (24 <= spriteCounter) {
			alive = false;
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if (inFrame() && drawing) {
			if (spriteNum == 1) image = down1;
			else if (spriteNum == 2) image = down2;	
			else if (spriteNum == 3) image = down3;	
			else if (spriteNum == 4) image = down4;		
			
			g2.drawImage(image, screenX, screenY, null);
		}
	}
}