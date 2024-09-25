package entity.object;

import java.awt.Graphics2D;

import application.GamePanel;
import entity.Entity;

public class OBJ_Grass extends Entity {

	Entity generator;
	
	public OBJ_Grass(GamePanel gp, int x, int y) {		
		super(gp);
		
		worldX = x;
		worldY = y;	
		
		direction = "down";
		
		down1 = setup("/tiles_interactive/grass_1"); 
		down2 = setup("/tiles_interactive/grass_2"); 
		down3 = setup("/tiles_interactive/grass_3"); 
		down4 = setup("/tiles_interactive/grass_4"); 
	}
	
	public void update() {
		spriteCounter++;
		if (spriteCounter < 5) spriteNum = 1; 
		else if (5 <= spriteCounter && spriteCounter < 10) spriteNum = 2; 
		else if (10 <= spriteCounter && spriteCounter < 15) spriteNum = 3;
		else if (15 <= spriteCounter && spriteCounter < 20) spriteNum = 4;
		else if (20 <= spriteCounter) alive = false;
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