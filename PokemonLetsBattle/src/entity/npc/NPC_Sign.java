package entity.npc;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;

public class NPC_Sign extends Entity {
	
	public static final String npcName = "Sign";
	
	public NPC_Sign(GamePanel gp, int worldX, int worldY, int sign, String message) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		name = npcName;
		speed = 0; defaultSpeed = speed;
		animationSpeed = 0; 	
								
		if (sign == 0) direction = "up";
		else if (sign == 1) direction = "down";
		
		dialogues[0][0] = message;
		
		hitbox = new Rectangle(0, 16, 48, 32); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
	}
	
	public void getImage() {			
		up1 = setup("/npc/sign_1"); 
		down1 = setup("/npc/sign_2"); 		
	}	
	
	public void speak() {	
		
		gp.ui.npc = this;
		dialogueSet = 0;	
		
		startDialogue(this, dialogueSet);
	}
	
	public void cycleSprites() {		
						
	}	
}