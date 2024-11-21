package entity.npc;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;

public class NPC_Nurse extends Entity {
	
	public static final String npcName = "Nurse Joy";
	
	public NPC_Nurse(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		name = npcName;
		direction = "down";
		speed = 0; defaultSpeed = speed;
		animationSpeed = 0; 
		hasShadow = false;
		
		hitbox = new Rectangle(1, 1, 46, 46); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
		
		setDialogue();
	}
	
	public void getImage() {					
		down1 = setup("/npc/nurse_down_1").getSubimage(0, 0, 48, 40); 
		down2 = setup("/npc/nurse_down_2").getSubimage(0, 0, 48, 40);
		left1 = setup("/npc/nurse_left_1").getSubimage(0, 0, 48, 40);
	}	
	public void setDialogue() {
		dialogues[0][0] = "Welcome to the Pokemon Center!";
		
		dialogues[1][0] = "Thank you for waiting!";
		dialogues[1][1] = "Your Pokemon have been fully\nhealed.";
		dialogues[1][2] = "Have a great day!";
		
		dialogues[2][0] = "I'm sorry, but you don't appear\nto have any Pokemon.";
		dialogues[2][1] = "Have a great day!";
		
		dialogues[3][0] = "Have a great day!";
	}
	
	public void speak() {			
		gp.ui.npc = this;
		dialogueSet = 0;		
		startDialogue(this, dialogueSet);
	}
}