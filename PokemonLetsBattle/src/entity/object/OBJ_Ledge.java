package entity.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import application.GamePanel;
import entity.Entity;

public class OBJ_Ledge extends Entity {
	
	public static final String objName = "Ledge";
	
	public OBJ_Ledge(GamePanel gp, int x, int y) {		
		super(gp);
		
		type = type_obstacle;
		worldX = x * gp.tileSize;
		worldY = y * gp.tileSize;	
		
		name = objName;		
		hasShadow = false;
		direction = "down";
	}	
	public OBJ_Ledge(GamePanel gp, int x, int y, String direction, int tiles) {		
		super(gp);
		
		type = type_obstacle;
		worldX = x * gp.tileSize;
		worldY = y * gp.tileSize;	
		
		name = objName;
		hasShadow = false;
		this.direction = direction;	
		power = tiles;
		if (power < 2) power = 2;
							
		switch (direction) {
			case "up": up1 = getImageHor(power, up1, up2, up3); break;
			case "down": down1 = getImageHor(power, down1, down2, down3); break;
			case "left": left1 = getImageVer(power, left1, left2, left3); break;
			case "right": right1 = getImageVer(power, right1, right2, right3); break;
		}
	}	
	public void getImage() {		
		up1 = setup("/objects/ledge_up_1"); 
		up2 = setup("/objects/ledge_up_2");
		up3 = setup("/objects/ledge_up_3");	
		
		down1 = setup("/objects/ledge_down_1"); 
		down2 = setup("/objects/ledge_down_2");
		down3 = setup("/objects/ledge_down_3");	
		
		left1 = setup("/objects/ledge_left_1"); 
		left2 = setup("/objects/ledge_left_2");
		left3 = setup("/objects/ledge_left_3");	
		
		right1 = setup("/objects/ledge_right_1"); 
		right2 = setup("/objects/ledge_right_2");
		right3 = setup("/objects/ledge_right_3");	
	}	
	public BufferedImage getImageHor(int tiles, BufferedImage left, BufferedImage middle, BufferedImage right) {
		
		hitbox = new Rectangle(0, 0, gp.tileSize * tiles, gp.tileSize);
		
		BufferedImage combinedImg = new BufferedImage(gp.tileSize * tiles, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g1 = combinedImg.createGraphics();

		g1.drawImage(left, 0, 0, null);
		for (int i = 1; i < (tiles - 1); i++) {
			g1.drawImage(middle, gp.tileSize * i, 0, null);
		}
		g1.drawImage(right, gp.tileSize * (tiles - 1), 0, null);			
		g1.dispose();
				
		return GamePanel.utility.scaleImage(combinedImg, gp.tileSize * tiles, gp.tileSize);
	}
	public BufferedImage getImageVer(int tiles, BufferedImage top, BufferedImage middle, BufferedImage bottom) {
		
		hitbox = new Rectangle(0, 0, gp.tileSize, gp.tileSize * tiles);
		
		BufferedImage combinedImg = new BufferedImage(gp.tileSize, gp.tileSize * tiles, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g1 = combinedImg.createGraphics();
		
		g1.drawImage(top, 0, 0, null);
		for (int i = 1; i < (tiles - 1); i++) {
			g1.drawImage(middle, 0, gp.tileSize * i, null);					
		}
		g1.drawImage(bottom, 0, gp.tileSize * (tiles - 1), null);			
		g1.dispose();
				
		return GamePanel.utility.scaleImage(combinedImg, gp.tileSize, gp.tileSize * tiles);
	}
	
	public void interact() {	
	
		// PLAYER MUST BE BEHIND LEDGE
		if (gp.player.direction.equals(direction)) {
			gp.playSE(gp.entity_SE, "jump");
			gp.player.jumping = true;
		}
	}
}