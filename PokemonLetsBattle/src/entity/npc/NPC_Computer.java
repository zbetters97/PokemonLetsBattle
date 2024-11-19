package entity.npc;

import application.GamePanel;
import entity.Entity;

public class NPC_Computer extends Entity {
	
	public static final String npcName = "Computer";
	
	public NPC_Computer(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		name = npcName;
		speed = 0; defaultSpeed = speed;
		animationSpeed = 0; 	
		hasShadow = false;
		direction = "down";
						
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
	}
	
	public void getImage() {			
		down1 = setup("/npc/computer_down_1"); 
	}	
	
	public void speak() {			
		gp.playSE(gp.world_SE, "pc-open");
		gp.gameState = gp.pcState;		
	}
}