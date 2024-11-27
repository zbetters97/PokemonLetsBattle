package entity.npc;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;
import entity.collectables.balls.COL_Ball_Poke;
import entity.collectables.items.ITM_Full_Restore;
import entity.collectables.items.ITM_Heal_Antidote;
import entity.collectables.items.ITM_Heal_Awakening;
import entity.collectables.items.ITM_Heal_Burn;
import entity.collectables.items.ITM_Heal_Full;
import entity.collectables.items.ITM_Heal_Ice;
import entity.collectables.items.ITM_Potion;
import entity.collectables.items.ITM_Potion_Hyper;
import entity.collectables.items.ITM_Potion_Super;
import entity.collectables.items.ITM_Repel;
import entity.collectables.items.ITM_Revive;
import entity.collectables.items.ITM_Revive_Max;

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
		setItems();
	}
	public void getImage() {			
		up1 = setup("/npc/clerk_up_1").getSubimage(0, 0, 48, 40); 
		down1 = setup("/npc/clerk_down_1").getSubimage(0, 0, 48, 40); 
		left1 = setup("/npc/clerk_left_1").getSubimage(0, 0, 48, 40); 
		right1 = setup("/npc/clerk_right_1").getSubimage(0, 0, 48, 40); 	
	}		
	public void setDialogue() {
		dialogues[0][0] = "Welcome to the Pokemon Market!";
		dialogues[1][0] = "Hey! You don't have enough money!";
		dialogues[2][0] = "I think you should hold onto that!";
		dialogues[3][0] = "Come back soon!";		
	}	
	public void setItems() {		
		inventory_items.add(new COL_Ball_Poke(gp));
		inventory_items.add(new ITM_Potion(gp));
		inventory_items.add(new ITM_Potion_Super(gp));
		inventory_items.add(new ITM_Potion_Hyper(gp));
		inventory_items.add(new ITM_Repel(gp));
		inventory_items.add(new ITM_Full_Restore(gp));	
		inventory_items.add(new ITM_Heal_Full(gp));			
		inventory_items.add(new ITM_Revive(gp));
		inventory_items.add(new ITM_Revive_Max(gp));	
		inventory_items.add(new ITM_Heal_Burn(gp));		
		inventory_items.add(new ITM_Heal_Ice(gp));
		inventory_items.add(new ITM_Heal_Antidote(gp));
		inventory_items.add(new ITM_Heal_Awakening(gp));
	}
	
	public void speak() {			
		gp.ui.npc = this;
		gp.gameState = gp.tradeState;
	}
}