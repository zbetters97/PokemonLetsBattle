package person;

import java.awt.Rectangle;

import application.GamePanel;
import pokemon.Pokemon;

public class NPC_Rival extends NPC {
	
	public static final String npcName = "May";
	
	public NPC_Rival(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		name = npcName;
		direction = "down";
		speed = 1; defaultSpeed = speed;
		animationSpeed = 10; 
		
		hasBattle = true;
		
		hitbox = new Rectangle(8, 16, 32, 32); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
		
		setDialogue();
	}
	
	public void getImage() {			
		up1 = setup("/npc/rival_up_1"); 
		up2 = setup("/npc/rival_up_2"); 
		up3 = setup("/npc/rival_up_3"); 
		down1 = setup("/npc/rival_down_1"); 
		down2 = setup("/npc/rival_down_2");
		down3 = setup("/npc/rival_down_3");
		left1 = setup("/npc/rival_left_1"); 
		left2 = setup("/npc/rival_left_2");
		left3 = setup("/npc/rival_left_3");
		right1 = setup("/npc/rival_right_1"); 
		right2 = setup("/npc/rival_right_2");
		right3 = setup("/npc/rival_right_3");		
		
		frontSprite = setup("/npc/rival_battle_front", gp.tileSize * 4, gp.tileSize * 4);
		backSprite = setup("/npc/rival_battle_back", gp.tileSize * 4, gp.tileSize * 4);
	}	
	public void assignParty() {
		pokeParty.add(Pokemon.getPokemon(3));
		pokeParty.add(Pokemon.getPokemon(4));
		pokeParty.add(Pokemon.getPokemon(5));
	}
	public void setDialogue() {
		dialogues[0][0] = "Hey, you!\nYou looked at me funny!";
		dialogues[0][1] = "Let's BATTLE!!!";
	}
	
	public void speak() {	
		
		gp.ui.npc = this;
		dialogueSet = 0;		
		
		direction = getOppositeDirection(gp.player.direction);
		startDialogue(this, dialogueSet);
	}
	
	public void setAction() {			
		getDirection(60);	
	}
	
	public void cycleSprites() {
		
		spriteCounter++;
		if (spriteCounter > animationSpeed) {			
										
			// CYLCE WALKING/SWIMMING SPRITES
			if (spriteNum == 1 && spriteCycle == 0) {
				spriteNum = 2;	
				spriteCycle = 1;
			}
			else if (spriteNum == 1 && spriteCycle == 1) {
				spriteNum = 3;
				spriteCycle = 0;
			}
			else if (spriteNum == 2) spriteNum = 1;
			else if (spriteNum == 3) spriteNum = 1;		
							
			spriteCounter = 0;
		}					
	}	
}