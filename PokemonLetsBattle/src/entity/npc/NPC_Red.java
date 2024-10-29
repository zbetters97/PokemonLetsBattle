package entity.npc;

import java.awt.Rectangle;
import java.util.Arrays;

import application.GamePanel;
import entity.Entity;
import moves.Moves;
import pokemon.Pokedex;
import pokemon.Pokemon;

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
		trainerClass = 5;
		
		hitbox = new Rectangle(1, 1, 46, 46); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
		
		setDialogue();
	}
	
	public void getImage() {			
		up1 = setup("/npc/red_up_1"); 
		down1 = setup("/npc/red_down_1"); 
		left1 = setup("/npc/red_left_1"); 
		right1 = setup("/npc/red_right_1"); 	
		
		frontSprite = setup("/npc/red_battle_front", gp.tileSize * 4, gp.tileSize * 4);
	}	
	public void assignParty() {
		
		pokeParty.add(Pokemon.getPokemon(Pokedex.PIKACHU, 88, null));
		pokeParty.get(0).addMoves(Arrays.asList(
				Moves.THUNDERBOLT,
				Moves.EXTREMESPEED,
				Moves.THUNDERWAVE,
				Moves.QUICKATTACK)
		);				
		pokeParty.add(Pokemon.getPokemon(Pokedex.VENUSAUR, 84, null));
		pokeParty.get(1).addMoves(Arrays.asList(
				Moves.GIGADRAIN,
				Moves.YAWN,
				Moves.BODYSLAM,
				Moves.LEAFSTORM)
		);			
		
		pokeParty.add(Pokemon.getPokemon(Pokedex.CHARIZARD, 84, null));
		pokeParty.get(2).addMoves(Arrays.asList(
				Moves.FLAREBLITZ,
				Moves.SLASH,
				Moves.FLY,
				Moves.DRAGONPULSE)
		);				
		pokeParty.add(Pokemon.getPokemon(Pokedex.BLASTOISE, 84, null));
		pokeParty.get(3).addMoves(Arrays.asList(
				Moves.BLIZZARD,
				Moves.HYDROPUMP,
				Moves.FLASHCANNON,
				Moves.SURF)
		);				
		pokeParty.add(Pokemon.getPokemon(Pokedex.LAPRAS, 80, null));
		pokeParty.get(4).addMoves(Arrays.asList(
				Moves.BODYSLAM,
				Moves.BLIZZARD,
				Moves.PSYCHIC,
				Moves.CALMMIND)
		);				
		pokeParty.add(Pokemon.getPokemon(Pokedex.SNORLAX, 82, null));
		pokeParty.get(5).addMoves(Arrays.asList(
				Moves.CRUNCH,
				Moves.SHADOWBALL,
				Moves.YAWN,
				Moves.BLIZZARD)
		);		
	}
	public void setDialogue() {
		dialogues[0][0] = "...";
		
		dialogues[1][0] = "...";
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