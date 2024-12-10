package entity.npc;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import pokemon.Pokemon.Pokedex;

public class NPC_Steve extends Entity {
	
	public static final String npcName = "Steve";
	
	public NPC_Steve(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		skillLevel = skill_smart;
		name = npcName;
		direction = "right";
		speed = 2; defaultSpeed = speed;
		animationSpeed = 8; 
		
		hasBattle = true;
		music = 6;
		trainerClass = 5;
		
		hitbox = new Rectangle(1, 1, 46, 46); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
		
		setDialogue();
	}
	
	public void getImage() {			
		up1 = setup("/npc/steve_up_1"); 
		up2 = setup("/npc/steve_up_2"); 
		up3 = setup("/npc/steve_up_3"); 
		down1 = setup("/npc/steve_down_1"); 
		down2 = setup("/npc/steve_down_2");
		down3 = setup("/npc/steve_down_3");
		left1 = setup("/npc/steve_left_1"); 
		left2 = setup("/npc/steve_left_2");
		left3 = setup("/npc/steve_left_3");
		right1 = setup("/npc/steve_right_1"); 
		right2 = setup("/npc/steve_right_2");
		right3 = setup("/npc/steve_right_3");		
		
		frontSprite = setup("/npc/steve_battle_front", gp.tileSize * 4, gp.tileSize * 4);
	}	
	public void assignParty() {
		pokeParty.add(Pokemon.get(Pokedex.MACHOP, 5, null));
		pokeParty.add(Pokemon.get(Pokedex.MACHOKE, 16, null));
		pokeParty.add(Pokemon.get(Pokedex.MACHAMP, 36, null));
	}
	public void setDialogue() {
		dialogues[0][0] = "Don't ask me why I have every\nevolved form of Machop!";
		
		dialogues[1][0] = "What? Fighting types are not\nimpossible to defeat?";
		
		dialogues[2][0] = "Maybe I should diversify my party...";
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
			
		if (hasBattle) {
			if (!moving && !lookForBattle(6)) {
				getSquareDirection(2);	
				move();
			}		
		}
		else if (!moving) {
			getDirection(60);
		}		
		
		if (moving) {
			walking();		
		}
	}
}