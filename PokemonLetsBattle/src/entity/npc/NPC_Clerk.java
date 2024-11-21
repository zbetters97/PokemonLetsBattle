package entity.npc;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;

public class NPC_Clerk extends Entity {
	
	public static final String npcName = "Store Clerk";
	
	public NPC_Clerk(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		name = npcName;
		direction = "right";
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
		up1 = setup("/npc/clerk_up_1").getSubimage(0, 0, 48, 40); 
		down1 = setup("/npc/clerk_down_1").getSubimage(0, 0, 48, 40); 
		left1 = setup("/npc/clerk_left_1").getSubimage(0, 0, 48, 40); 
		right1 = setup("/npc/clerk_right_1").getSubimage(0, 0, 48, 40); 	
	}	
	public void setDialogue() {
		dialogues[0][0] = "Welcome to the Pokemon Market!";
	}
	
	public void speak() {			
		gp.ui.npc = this;
		dialogueSet = 0;		
		startDialogue(this, dialogueSet);
	}
}