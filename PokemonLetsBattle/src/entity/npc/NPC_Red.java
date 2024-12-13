package entity.npc;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import application.GamePanel;
import entity.Entity;
import moves.Moves;
import pokemon.Pokemon;
import pokemon.Pokemon.Pokedex;

public class NPC_Red extends Entity {
	
	public static final String npcName = "Red";
	
	public NPC_Red(GamePanel gp, int worldX, int worldY) {		
		super(gp);	
		this.worldX = worldX * gp.tileSize;
		this.worldY = worldY * gp.tileSize;	
		worldXStart = this.worldX;
		worldYStart = this.worldY;
		
		type = type_npc;
		name = npcName;
		direction = "down";
		speed = 2; defaultSpeed = speed;
		animationSpeed = 8; 
		
		hasBattle = true;
		music = 10;
		trainerClass = 5;
		iv = 30;
		skillLevel = skill_elite;
		
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
		up1 = setup("/npc/red_up_1"); 
		down1 = setup("/npc/red_down_1"); 
		left1 = setup("/npc/red_left_1"); 
		right1 = setup("/npc/red_right_1"); 	
		
		frontSprite = setup("/npc/red_battle_front", gp.tileSize * 4, gp.tileSize * 4);
	}	
	public void setDialogue() {
		dialogues[0][0] = "...";
		
		dialogues[1][0] = "...";
	}
	public void assignParty() {
		/** RED PARTY REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Red_(game) **/
				
		pokeParty.add(Pokemon.get(Pokedex.PIKACHU, 88, null));
		pokeParty.get(0).addMoves(Arrays.asList(
				Moves.VOLTTACKLE,
				Moves.IRONTAIL,
				Moves.THUNDERBOLT,
				Moves.QUICKATTACK
		));				
		pokeParty.add(Pokemon.get(Pokedex.VENUSAUR, 84, null));
		pokeParty.get(1).addMoves(Arrays.asList(
				Moves.FRENZYPLANT,
				Moves.SLUDGEBOMB,
				Moves.SLEEPPOWDER,
				Moves.GIGADRAIN
		));			
		
		pokeParty.add(Pokemon.get(Pokedex.CHARIZARD, 84, null));
		pokeParty.get(2).addMoves(Arrays.asList(
				Moves.BLASTBURN,
				Moves.AIRSLASH,
				Moves.FLAREBLITZ,
				Moves.DRAGONPULSE
		));				
		pokeParty.add(Pokemon.get(Pokedex.BLASTOISE, 84, null));
		pokeParty.get(3).addMoves(Arrays.asList(
				Moves.HYDROCANNON,
				Moves.FOCUSBLAST,
				Moves.BLIZZARD,
				Moves.FLASHCANNON
		));				
		pokeParty.add(Pokemon.get(Pokedex.LAPRAS, 80, null));
		pokeParty.get(4).addMoves(Arrays.asList(
				Moves.BODYSLAM,
				Moves.BLIZZARD,
				Moves.PSYCHIC,
				Moves.BRINE
		));				
		pokeParty.add(Pokemon.get(Pokedex.SNORLAX, 82, null));
		pokeParty.get(5).addMoves(Arrays.asList(
				Moves.CRUNCH,
				Moves.SHADOWBALL,
				Moves.BLIZZARD,
				Moves.GIGAIMPACT
		));		
		
		for (Pokemon p : pokeParty) p.setIV(iv);		
	}
	
	public void speak() {	
		
		dialogueSet = 0;		
		
		direction = getOppositeDirection(gp.player.direction);
		startDialogue(this, dialogueSet);
	}
	
	public void setAction() { }
	
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