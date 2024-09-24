package entity.npc;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class NPC_Rival extends Entity {
	
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
		animationSpeed = 12; 
		
		hasBattle = true;
		trainerClass = 5;
		
		hitbox = new Rectangle(1, 1, 46, 46); 		
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
		pokeParty.add(Pokemon.getPokemon("Torchic", 5));
		pokeParty.add(Pokemon.getPokemon("Combusken", 16));
		pokeParty.add(Pokemon.getPokemon("Blaziken", 36));
	}
	public void setDialogue() {
		dialogues[0][0] = "My dad says that my Pokemon\nparty might be the best!";
		dialogues[0][1] = "I promise I'll go easy on you!";
		
		dialogues[1][0] = "Argh! How could I lose to\na little kid like you?";
		
		dialogues[2][0] = "You're too tough for your age!";
	}
	
	public void speak() {	
		
		gp.ui.npc = this;
		
		if (hasBattle) {
			dialogueSet = 0;		
		}
		else {
			dialogueSet = 2;
		}
		
		direction = getOppositeDirection(gp.player.direction);
		startDialogue(this, dialogueSet);
	}
	
	public void update() {

		if (moving) {			
			walking();
		}
		else {			
			setAction();		
		}
				
		manageValues();	
	}	
	
	public void setAction() {
		getDirection(60);	
	}
	
	public void move(String direction) {
		
		switch (direction) {
			case "up": worldY -= speed; break;
			case "upleft": worldY -= speed - 1; worldX -= speed - 1; break;
			case "upright": worldY -= speed - 1; worldX += speed - 1; break;
			
			case "down": worldY += speed; break;
			case "downleft": worldY += speed - 1; worldX -= speed - 1; break;
			case "downright": worldY += speed; worldX += speed - 1; break;
			
			case "left": worldX -= speed; break;
			case "right": worldX += speed; break;
		}
		
		cycleSprites();		
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