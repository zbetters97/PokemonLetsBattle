package entity.npc;

import java.awt.Rectangle;
import java.util.ArrayList;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import pokemon.Pokemon.Pokedex;

public class NPC_Rival extends Entity {
	
	public static final String npcName = "May";
	
	public NPC_Rival(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		skillLevel = skill_rookie;
		name = npcName;
		direction = "right";
		speed = 1; defaultSpeed = speed;
		animationSpeed = 8; 
		
		hasBattle = true;
		music = 4;
		trainerClass = 5;
		iv = 15;
		
		hitbox = new Rectangle(1, 1, 46, 46); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
		
		setDialogue();
		
		pokeParty = new ArrayList<>();
		assignParty();
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
	public void setDialogue() {
		dialogues[0][0] = "My dad says that my Pokemon\nparty might be the best!";
		dialogues[0][1] = "I promise I'll go easy on you!";
		
		dialogues[1][0] = "Argh! How could I lose to\na little kid like you?";
		
		dialogues[2][0] = "You're suspsiciously tough for\nyour age!";
	}
	public void assignParty() {
		pokeParty.add(Pokemon.get(Pokedex.TORCHIC, 5, null));
		pokeParty.add(Pokemon.get(Pokedex.COMBUSKEN, 16, null));
		pokeParty.add(Pokemon.get(Pokedex.BLAZIKEN, 36, null));
		for (Pokemon p : pokeParty) p.setIV(iv);	
	}
		
	public void speak() {			
		if (hasBattle) {
			gp.stopMusic();
			gp.startMusic(0, gp.se.getFile(0, name));	
			dialogueSet = 0;				
		}
		else {
			dialogueSet = 2;
		}
				
		direction = getOppositeDirection(gp.player.direction);
		startDialogue(this, dialogueSet);
	}
	
	public void setAction() {	
				
/*		
  		if (hasBattle) {			
			if (!moving && !lookForBattle(5)) {
				getDirection(45);	
			}	
		} 
*/
		if (!moving) {
			getDirection(60);	
		}		
			
		if (moving) {
			walking();
		}
		else {
			spriteNum = 1;
		}
	}
		
	@Override
	public void getDirection(int rate) {	
		
		actionLockCounter++;			
		if (actionLockCounter >= rate) {		
						
			int dir = 1 + (int)(Math.random() * 4);
			if (dir == 1) direction = "up";
			else if (dir == 2) direction = "down";
			else if (dir == 3) direction = "left";
			else if (dir == 4) direction = "right";
			
			actionLockCounter = 0;
			steps = 0;
			move();	
		}		
	}
}