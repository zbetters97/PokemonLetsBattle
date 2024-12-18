package entity;

/** IMPORTS **/
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import application.GamePanel;
import data.Progress;
import entity.collectables.balls.*;
import entity.collectables.items.*;
import entity.object.object_interactive.OBJ_Boulder;
import entity.object.object_interactive.OBJ_Rock;
import entity.object.object_interactive.OBJ_Tree;
import entity.object.object_interactive.OBJ_Water;
import pokemon.Pokemon;
import pokemon.Pokemon.Pokedex;

/** PLAYER CLASS **/
public class Player extends Entity {
	
/** PLAYER VARIABLES **/
		
	// POSITIONING
	public int screenX;
	public int screenY;	
	private int tempScreenX;
	private int tempScreenY;
	public int defaultWorldX;
	public int defaultWorldY;
	
	public BufferedImage hm1, hm2, hm3, hm4, hm5, 
			surfUp1, surfDown1, surfLeft1, surfRight1,
			fishUp1, fishUp2, fishUp3, fishUp4,
			fishDown1, fishDown2, fishDown3, fishDown4,
			fishLeft1, fishLeft2, fishLeft3, fishLeft4,
			fishRight1, fishRight2, fishRight3, fishRight4;	
	
	public ArrayList<Pokemon> personalDex = new ArrayList<>();
	public Pokemon[][] pcParty = new Pokemon[20][30];
	public ArrayList<Pokemon> pcBox_1 = new ArrayList<Pokemon>(30);
	
	public Action nextAction = Action.IDLE;
	public Entity activeItem = null;
	
	private int jumpCounter = 0;
	private int hmCounter = 0;
	private int hmNum = 1;
	private int surfCounter = 0;	
	private int fishCounter = 0;
	private int fishNum = 1;
	private int fishCatch = 0;
	
	private boolean strengthActive = false;
	private boolean alert = false;
	public boolean repelActive = false;
	private int repelSteps = 0;
	private int repelStepsMax = 0;		
/** END PLAYER VARIABLES **/		
	
		
/** PLAYER CONSTRUCTOR **/	
	public Player(GamePanel gp) {
		super(gp);		
						
		// PLAYER POSITION LOCKED TO CENTER
		screenX = gp.screenWidth / 2 - (gp.tileSize / 2); 
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
						
		// HITBOX (x, y, width, height)
		hitbox = new Rectangle(1, 1, 46, 46);
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
		
		name = "ASH";
		trainerClass = 5;
		skillLevel = skill_elite;
		
		pokeParty = new ArrayList<>();
		assignParty();
	}
/** END PLAYER CONSTRUCTOR **/
		
	
/** DEFAULT HANDLERS **/
	
	// DEFAULT VALUES
	public void assignParty() {							
		pokeParty.add(Pokemon.get(Pokedex.MUDKIP, 5, new COL_Ball_Poke(gp)));	
		pokeParty.add(Pokemon.get(Pokedex.MARSHTOMP, 16, new COL_Ball_Great(gp)));
		pokeParty.add(Pokemon.get(Pokedex.SWAMPERT, 36, new COL_Ball_Ultra(gp)));
	}
	public void setDefaultValues() {
				
		speed = 4; defaultSpeed = speed;
		animationSpeed = 8; defaultAnimationSpeed = animationSpeed;
		
		money = 4123;
		dexSeen = 0;
		dexOwn = 0;
		
		// PLAYER ATTRIBUTES		
		setDefaultPosition();		
		setDialogue();
		setItems();
		
		getRunImage();
		getHMImage();
		getSurfImage();
		getFishImage();
				
		int c = 0;
		int x = 0;
		for (int i = 0; i < Pokedex.values().length; i++) {
			pcParty[c][x] = Pokemon.get(Pokedex.values()[i], 50, new COL_Ball_Poke(gp));
			personalDex.add(pcParty[c][x]);		
			x++;
			if (i == 29 || i == 59 || i == 89) { c++; x = 0; } 		
		}
		
	}
	public void setDefaultPosition() {			
		worldX = gp.tileSize * 24;
		worldY = gp.tileSize * 22;		
		defaultWorldX = worldX;
		defaultWorldY = worldY;
		safeWorldX = defaultWorldX;
		safeWorldY = defaultWorldY;
		
		gp.currentMap = 0;
		gp.currentArea = gp.town;
	}
	public void setDialogue() {
		dialogues[0][0] = "Repel affect has worn off.";	
	}		
	public void setItems() {
		inventory_keyItems.add(new ITM_EXP_Share(gp));
		inventory_keyItems.add(new ITM_Rod_Old(gp));
		inventory_keyItems.add(new ITM_Rod_Good(gp));
		inventory_keyItems.add(new ITM_Rod_Super(gp));
		
		inventory_items.add(new ITM_Potion(gp, 20));		
		inventory_items.add(new ITM_Potion_Super(gp, 15));	
		inventory_items.add(new ITM_Potion_Hyper(gp, 10));		
		inventory_items.add(new ITM_Potion_Max(gp, 5));
		
		inventory_items.add(new ITM_Repel(gp));
		inventory_items.add(new ITM_Full_Restore(gp));	
		inventory_items.add(new ITM_Heal_Full(gp));			
		inventory_items.add(new ITM_Revive(gp));
		inventory_items.add(new ITM_Revive_Max(gp));	
		inventory_items.add(new ITM_Heal_Burn(gp));		
		inventory_items.add(new ITM_Heal_Ice(gp));
		inventory_items.add(new ITM_Heal_Antidote(gp));
		inventory_items.add(new ITM_Heal_Awakening(gp));
				
		inventory_pokeballs.add(new COL_Ball_Poke(gp, 15));		
		inventory_pokeballs.add(new COL_Ball_Great(gp, 10));	
		inventory_pokeballs.add(new COL_Ball_Ultra(gp, 5));
		
		inventory_pokeballs.add(new COL_Ball_Master(gp));
	}
	public void restoreStatus() {
		speed = defaultSpeed;		
		keyItem = null;
		Progress.canSave = true;
		resetValues();
	}	
	public void resetValues() {		
		gp.keyH.aPressed = false;
		gp.keyH.bPressed = false;		
		action = Action.IDLE;
		nextAction = Action.IDLE;
		activeItem = null;	
		canMove = true;
		moving = false;
		running = false;
		jumping = false;
		alert = false;		
		strengthActive = false;
		pixelCounter = 0;
		jumpCounter = 0;
		fishCounter = 0;
		surfCounter = 0;		
		hmNum = 1;
		fishNum = 1;
	}	
	
	// PLAYER IMAGES
	public void getImage() {			
		up1 = setup("/player/boy_up_1"); 
		up2 = setup("/player/boy_up_2"); 
		up3 = setup("/player/boy_up_3"); 
		down1 = setup("/player/boy_down_1"); 
		down2 = setup("/player/boy_down_2");
		down3 = setup("/player/boy_down_3");
		left1 = setup("/player/boy_left_1"); 
		left2 = setup("/player/boy_left_2");
		left3 = setup("/player/boy_left_3");
		right1 = setup("/player/boy_right_1"); 
		right2 = setup("/player/boy_right_2");
		right3 = setup("/player/boy_right_3");		
		
		frontSprite = setup("/player/boy_battle_front", gp.tileSize * 4, gp.tileSize * 4);
		backSprite = setup("/player/boy_battle_back", gp.tileSize * 4, gp.tileSize * 4);
	}	
	public void getRunImage() {			
		runUp1 = setup("/player/boy_run_up_1"); 
		runUp2 = setup("/player/boy_run_up_2"); 
		runUp3 = setup("/player/boy_run_up_3"); 
		runDown1 = setup("/player/boy_run_down_1"); 
		runDown2 = setup("/player/boy_run_down_2");
		runDown3 = setup("/player/boy_run_down_3");
		runLeft1 = setup("/player/boy_run_left_1"); 
		runLeft2 = setup("/player/boy_run_left_2");
		runLeft3 = setup("/player/boy_run_left_3");
		runRight1 = setup("/player/boy_run_right_1"); 
		runRight2 = setup("/player/boy_run_right_2");
		runRight3 = setup("/player/boy_run_right_3");
	}	
	public void getHMImage() {			
		hm1 = setup("/player/boy_hm_1"); 
		hm2 = setup("/player/boy_hm_2"); 
		hm3 = setup("/player/boy_hm_3"); 
		hm4 = setup("/player/boy_hm_4"); 
		hm5 = setup("/player/boy_hm_5"); 
	}	
	public void getSurfImage() {		
		surfUp1 = setup("/player/boy_surf_up_1", (int) (gp.tileSize * 1.3), (int) (gp.tileSize * 1.3)); 		
		surfDown1 = setup("/player/boy_surf_down_1", (int) (gp.tileSize * 1.3), (int) (gp.tileSize * 1.3)); 		
		surfLeft1 = setup("/player/boy_surf_left_1", (int) (gp.tileSize * 1.3), (int) (gp.tileSize * 1.3)); 		
		surfRight1 = setup("/player/boy_surf_right_1", (int) (gp.tileSize * 1.3), (int) (gp.tileSize * 1.3)); 		
	}	
	public void getFishImage() {			
		fishUp1 = setup("/player/boy_fish_up_1", gp.tileSize, gp.tileSize * 2); 
		fishUp2 = setup("/player/boy_fish_up_2", gp.tileSize, gp.tileSize * 2); 
		fishUp3 = setup("/player/boy_fish_up_3", gp.tileSize, gp.tileSize * 2); 
		fishUp4 = setup("/player/boy_fish_up_4", gp.tileSize, gp.tileSize * 2); 
		fishDown1 = setup("/player/boy_fish_down_1", gp.tileSize - 1, gp.tileSize * 2); 
		fishDown2 = setup("/player/boy_fish_down_2", gp.tileSize - 1, gp.tileSize * 2);
		fishDown3 = setup("/player/boy_fish_down_3", gp.tileSize - 1, gp.tileSize * 2);
		fishDown4 = setup("/player/boy_fish_down_4", gp.tileSize - 1, gp.tileSize * 2);
		fishLeft1 = setup("/player/boy_fish_left_1", gp.tileSize * 2, gp.tileSize * 2); 
		fishLeft2 = setup("/player/boy_fish_left_2", gp.tileSize * 2, gp.tileSize * 2);
		fishLeft3 = setup("/player/boy_fish_left_3", gp.tileSize * 2, gp.tileSize * 2);
		fishLeft4 = setup("/player/boy_fish_left_4", gp.tileSize * 2, gp.tileSize * 2);
		fishRight1 = setup("/player/boy_fish_right_1", gp.tileSize * 2, gp.tileSize * 2); 
		fishRight2 = setup("/player/boy_fish_right_2", gp.tileSize * 2, gp.tileSize * 2);
		fishRight3 = setup("/player/boy_fish_right_3", gp.tileSize * 2, gp.tileSize * 2);
		fishRight4 = setup("/player/boy_fish_right_4", gp.tileSize * 2, gp.tileSize * 2);
	}	
/** END DEFAULT HANDLERS **/
	
	
/** UPDATER **/
	@Override
	public void update() {
		
		if (jumping) {
			speed = 3;
			animationSpeed = 6;
			moving = true;
			running = false;
		}		
		else if (action == Action.HM) {
			hm();
		}
		else if (action == Action.FISHING) {
			fishing();
		}
		else if (!moving && canMove) {			
			
			if (gp.keyH.startPressed) {				
				gp.keyH.startPressed = false;
				gp.keyH.bPressed = false;
				gp.keyH.playMenuOpenSE();
				
				spriteNum = 1;
				running = false;
				
				gp.gameState = gp.pauseState; 
			}
			else {
				gp.eHandler.checkEvent();	
			
				running = false;
				
				if (gp.keyH.aPressed) {		
					gp.keyH.aPressed = false;
					gp.keyH.bPressed = false;
					action();
				}			
				
				if (gp.keyH.xPressed) {
					gp.keyH.xPressed = false;
					if (keyItem != null) keyItem.use();					
				}
				
				if (gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed) { 		
					move();
				}
				else {
					spriteNum = 1;
				}
			}			
		}	
		if (moving) {
			walking();
		}
				
		manageValues();	
	}
/** END UPDATER **/
	
	
/** PLAYER METHODS **/	
	public void action() {
		
		int npcIndex = gp.cChecker.checkNPC();
		int objIIndex = gp.cChecker.checkObject_I(this);
				
		if (npcIndex != -1) interactNPC(npcIndex);
		else if (objIIndex != -1) interactObject_I(objIIndex);
		
		if (action != Action.SURFING && gp.cChecker.checkWater(this)) {			
			Entity water = new OBJ_Water(gp);	
			water.interact();
		}
	}	
	public void interactNPC(int i) {		
		if (i != -1 && !gp.npc[gp.currentMap][i].moving) {		
			resetValues();
			gp.npc[gp.currentMap][i].speak();					
		}	
	}	
	public void interactObject_I(int i) {		
		if (i != -1) {		
			if (gp.obj_i[gp.currentMap][i].type == type_obstacle_i) {							
				gp.obj_i[gp.currentMap][i].interact();				
			}				
		}
	}	
		
	public void move() {	
		
		if (gp.keyH.bPressed && action == Action.IDLE) {
			running = true;
			speed = 6;
			animationSpeed = 6;			
		}
		else {
			speed = defaultSpeed;
			animationSpeed = defaultAnimationSpeed;
		}
		
		getDirection();
		
		checkCollision();
		if (!collisionOn) { 	
			moving = true;				
		}
		else {			
			running = false;
			spriteNum = 1;
		}			
	}
	public void getDirection() {
		
		String tempDirection = "";
		
		if (gp.keyH.upPressed) tempDirection = "up";
		if (gp.keyH.downPressed) tempDirection = "down";
		if (gp.keyH.leftPressed) tempDirection = "left";
		if (gp.keyH.rightPressed) tempDirection = "right";			
		
		direction = tempDirection;
	}
			
	public void walking() {
		
		if (canMove) {			
			switch (direction) {
				case "up": worldY -= speed; break;				
				case "down": worldY += speed; break;				
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
			}
			
			cycleSprites();	
			
			if (action == Action.SURFING && gp.cChecker.checkGround(this)) {					
				action = Action.IDLE;	
				surfCounter = 0;
				Progress.canSave = true;
				
				gp.stopMusic();
				gp.setupMusic();
			}
		}
		
		pixelCounter += speed;		
		if (pixelCounter >= gp.tileSize) {
															
			moving = false;
			pixelCounter = 0;
			
			gp.cChecker.checkGrass(this);
			
			if (!hasRepel() && inGrass) {
				checkWildEncounter_Grass();					
			}
			
			gp.eHandler.checkEvent();	
			
			if (jumping) {
				jumpCounter++;
				if (jumpCounter != 2) {
					moving = true;
				}
				else {
					jumpCounter = 0;
					jumping = false;
				}
			}			
		}
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
	public void checkCollision() {
				
		collisionOn = false;
		
		if (gp.keyH.debug) return;
		
		gp.eHandler.checkEvent();		
		gp.cChecker.checkTile(this);	
		gp.cChecker.checkEntity(this, gp.npc);			
		
		int objIndex = gp.cChecker.checkObject(this, true);
		int objIIndex = gp.cChecker.checkObject_I(this);
		
		if (objIndex != -1) interactObject(objIndex);	
		else if (objIIndex != -1) moveObject_I(objIIndex);
	}	
	public void interactObject(int i) {
		if (i != -1) {
			if (gp.obj[gp.currentMap][i].type == type_obstacle) {
				gp.obj[gp.currentMap][i].interact();	
			}
		}		
	}	
	public void moveObject_I(int i) {
		if (i != -1) {			
			if (gp.obj_i[gp.currentMap][i].name.equals(OBJ_Boulder.objName) && strengthActive && 
					!gp.obj_i[gp.currentMap][i].moving) {
				gp.obj_i[gp.currentMap][i].move(direction);								
			}				
		}
	}
	
	private void hm() {
		
		hmCounter++;
		if (5 > hmCounter) hmNum = 1;
		else if (10 > hmCounter && hmCounter > 5) hmNum = 2;		
		else if (15 > hmCounter && hmCounter > 10) hmNum = 3;
		else if (20 > hmCounter && hmCounter > 15) hmNum = 4;		
		else if (25 > hmCounter && hmCounter > 20) hmNum = 5;		
		else if (hmCounter > 60) {
			
			hmNum = 1;
			hmCounter = 0;
			
			action = Action.IDLE;
			
			switch (activeItem.name) {			
				case OBJ_Boulder.objName:
					gp.playSE(gp.moves_SE, "Strength");
					strengthActive = true;	
					break;			
				case OBJ_Rock.objName:
					activeItem.opening = true;
					gp.playSE(gp.moves_SE, "Rock Smash");
					break;
				case OBJ_Tree.objName:
					activeItem.opening = true;
					gp.playSE(gp.moves_SE, "Cut");
					break;
				case OBJ_Water.objName:
					moving = true;
					action = Action.SURFING;
					Progress.canSave = false;
					gp.stopMusic();
					gp.startMusic(0, "surfing");
					break;				
				default:
					break;
			}
			
			activeItem = null;
		}		
	}
	private void fishing() {		
		
		fishCounter++;
		if (25 > fishCounter) fishNum = 1;
		else if (30 > fishCounter && fishCounter > 25) fishNum = 2;		
		else if (35 > fishCounter && fishCounter > 30) fishNum = 3;
		else if (40 > fishCounter && fishCounter > 35) fishNum = 4;		
		else if (fishCounter > 40) {				
			catchFish();
		}		
	}
	private void catchFish() {
		
		int length = 30;
		
		if (activeItem.power == 0) length = 60;
		else if (activeItem.power == 1) length = 40;
		else if (activeItem.power == 2) length = 20;
		
		if (fishCounter == 41) {		
			
			int min = 0;
			int max = 0;
			
			if (activeItem.power == 0) { min = 80; max = 150; }
			else if (activeItem.power == 1) { min = 100; max = 250; }
			else if (activeItem.power == 2) { min = 100; max = 350; }
			
			fishCatch = new Random().nextInt(max - min + 1) + min;
		}
		
		if (fishCounter == fishCatch - 5) {
			gp.playSE(gp.entity_SE, "alert");
		}
		
		if (fishCatch + length > fishCounter && fishCounter > fishCatch) {		
		
			alert = true;
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				
				Pokemon wildPokemon = getWildPokemon_Water();
				
				if (wildPokemon != null) {
					
					gp.btlManager.fighter[1] = wildPokemon;
					gp.btlManager.setup(gp.btlManager.wildBattle, 1, null, wildPokemon, null, true, false);
									
					alert = false;
					action = Action.IDLE;
					fishNum = 1;
					fishCounter = 0;
					fishCatch = 0;
					
					gp.gameState = gp.transitionState;	
				}
			}
		}
		else if (fishCounter == (fishCatch + length + 15) || gp.keyH.aPressed || gp.keyH.bPressed) {
			gp.keyH.aPressed = false;
			gp.keyH.bPressed = false;
			alert = false;
			action = Action.IDLE;
			fishNum = 1;
			fishCounter = 0;
			fishCatch = 0;				
		}		
	}
	
	/** WILD ENCOUNTER **/
	private void checkWildEncounter_Grass() {
		// random encounter formula reference: https://bulbapedia.bulbagarden.net/wiki/Wild_Pok%C3%A9mon
						
		int r = new Random().nextInt(255);		
		if (r < 15) {
						
			Pokemon wildPokemon = getWildPokemon(gp.wildEncounters_Grass, gp.wildLevels_Grass);
			
			if (wildPokemon != null) {
				
				gp.btlManager.fighter[1] = wildPokemon;
				gp.btlManager.setup(gp.btlManager.wildBattle, 1, null, wildPokemon, null, true, false);
								
				gp.gameState = gp.transitionState;	
			}
		}
	}
	private Pokemon getWildPokemon(Map<Integer, Map<Pokedex, Integer>> wildEncounters, 
			Map<Integer, Integer> wildLevels) {
		
		Pokemon wildPokemon = null;	
		Pokedex randomPokemon = null;
		
		int minLevel = 1;
		int maxLevel = 1;
		int level = 1;
		
		int chance = 1;
		int total = 0;
		
		// LEVEL RANGE BASED ON LOCATION
		minLevel = wildLevels.get(gp.currentMap);
		maxLevel = minLevel + 3;
		level = new Random().nextInt(maxLevel - minLevel + 1) + minLevel;
		
		// RANDOM NUM 0-100
		chance = new Random().nextInt(100);
		
		// FOR EACH LIST OF POKEMON FROM LOCATION
		for (Pokedex p : wildEncounters.get(gp.currentMap).keySet()) {
			
			// GET PROBABILITY OF POKEMON ENCOUNTER
			int rate = wildEncounters.get(gp.currentMap).get(p); 
			total += rate;
			
			// POKEMON RANDOMLY SELECTED, ASSIGN NAME AND STOP
			if (chance <= total) {	
				randomPokemon = p;
				break;
			}	
		}
		
		// GENERATE NEW POKEMON
		if (randomPokemon != null) {
			wildPokemon = Pokemon.get(randomPokemon, level, null);	
		}		
		
		return wildPokemon;
	}
	private Pokemon getWildPokemon_Water() {
		
		Pokemon wildPokemon = null;	
		Pokedex randomPokemon = null;
		int level = activeItem.getLevel();
		
		int chance = 1;
		int total = 0;
					
		// RANDOM NUM 0-100
		chance = new Random().nextInt(100);
		
		// FOR EACH LIST OF POKEMON FROM LOCATION BY ROD
		for (Pokedex p : gp.wildEncounters_Fishing.get(gp.currentMap).get(activeItem.power).keySet()) {
			
			// GET PROBABILITY OF POKEMON ENCOUNTER
			int rate = gp.wildEncounters_Fishing.get(gp.currentMap).get(activeItem.power).get(p); 
			total += rate;
			
			// POKEMON RANDOMLY SELECTED, ASSIGN NAME AND BREAK
			if (chance <= total) {	
				randomPokemon = p;
				break;
			}	
		}
		
		// GENERATE NEW POKEMON
		if (randomPokemon != null) {
			wildPokemon = Pokemon.get(randomPokemon, level, null);
		}		
		
		return wildPokemon;
	}
	/** END WILD ENCOUNTER **/
	
	public void setRepel(int steps) {
		repelActive = true;
		repelSteps = 0;
		repelStepsMax = steps;
	}
	private boolean hasRepel() {
		
		if (repelActive) {
			
			repelSteps++;
			if (repelStepsMax <= repelSteps) {				
				repelActive = false;
				repelSteps = 0;
				repelStepsMax = 0;
				
				dialogueSet = 0;	
				startDialogue(this, dialogueSet);
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public void trackSeenPokemon(Pokemon pkm) {			
		if (!hasPokemonDex(pkm.getIndex())) {
			personalDex.add(pkm);
			dexSeen++;
		}
	}
	public void trackOwnPokemon(int index) {
		if (!ownsPokemon(index)) {
			dexOwn++;
		}
	}
	public boolean hasPokemonDex(int index) {
		return gp.player.personalDex.stream().anyMatch(p -> p.getIndex() == index);
	}
	public boolean ownsPokemon(int index) {
		
		boolean ownsPokemon = false;
		
		if (gp.player.pokeParty.stream().anyMatch(p -> p.getIndex() == index)) {
			ownsPokemon = true;
		}
		else {
			for (int i = 0; i < pcParty.length; i++) {				
				if (pcParty[i] != null) {				
					for (int c = 0; c  < pcParty[i].length; c++) {						
						if (pcParty[i][c] != null && pcParty[i][c].getIndex() == index) {
							ownsPokemon = true;
							break;
						}						
					}
				}
			}
		}
		
		return ownsPokemon;
	}
	
	public void stopMoving() {		
		if (!moving) {
			canMove = false;
			running = false;
			action = Action.IDLE;
			spriteNum = 1;	
		}		
	}
	
	public void manageValues() {		
		switch (action) {
			case SURFING:
				surfCounter++;					
				if (surfCounter > 60) {
					surfCounter = 0;	
				}
				break;
			default:
				break;
		}
	}	

	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	
	public void offCenter() {

		tempScreenX = screenX;
		tempScreenY = screenY;
		
		if (worldX < screenX) tempScreenX = worldX;		
		if (worldY < screenY) tempScreenY = worldY;
		
		// FROM PLAYER TO RIGHT-EDGE OF SCREEN
		int rightOffset = gp.screenWidth - screenX;		
		
		// FROM PLAYER TO RIGHT-EDGE OF WORLD
		if (rightOffset > gp.worldWidth - worldX) {
			tempScreenX = gp.screenWidth - (gp.worldWidth - worldX);
		}			
		
		// FROM PLAYER TO BOTTOM-EDGE OF SCREEN
		int bottomOffSet = gp.screenHeight - screenY;
		
		// FROM PLAYER TO BOTTOM-EDGE OF WORLD
		if (bottomOffSet > gp.worldHeight - worldY) {
			tempScreenY = gp.screenHeight - (gp.worldHeight - worldY);
		}	
	}
	
	public void draw(Graphics2D g2) {
		
		if (!drawing) return;
		
		offCenter();
		
		if (!inGrass && action != Action.SURFING) {
			g2.setColor(new Color(0,0,0,100));
			g2.fillOval(tempScreenX + 9, tempScreenY + 40, 30, 10);
		}
									
		switch (direction) {
			case "up":
				switch (action) {
					case HM:
						if (hmNum == 1) image = hm1;
						else if (hmNum == 2) image = hm2;
						else if (hmNum == 3) image = hm3;
						else if (hmNum == 4) image = hm4;
						else if (hmNum == 5) image = hm5;
						break;
					case FISHING:
						tempScreenY -= 16;
						if (alert) g2.drawImage(gp.ui.battleIcon, tempScreenX - 1, tempScreenY - gp.tileSize, null);	
						
						if (fishNum == 1) image = fishUp1;
						else if (fishNum == 2) image = fishUp2;
						else if (fishNum == 3) image = fishUp3;
						else if (fishNum == 4) image = fishUp4;						
						break;
					case SURFING:
						image = surfUp1;	
						tempScreenX -= 7;
						tempScreenY -= 7;
						if (surfCounter > 30) tempScreenY -= 5;							
						break;
					default:
						if (running) {
							if (spriteNum == 1) image = runUp1;
							else if (spriteNum == 2) image = runUp2;	
							else if (spriteNum == 3) image = runUp3;	
						}
						else {
							if (spriteNum == 1) image = up1;
							else if (spriteNum == 2) image = up2;	
							else if (spriteNum == 3) image = up3;	
							
							if (jumping) {
								tempScreenY -= 15;
							}
						}
						break;
					}		
				break;
			case "down":	
				switch (action) {
					case HM:
						if (hmNum == 1) image = hm1;
						else if (hmNum == 2) image = hm2;
						else if (hmNum == 3) image = hm3;
						else if (hmNum == 4) image = hm4;
						else if (hmNum == 5) image = hm5;
						break;
					case FISHING:
						
						if (alert) g2.drawImage(gp.ui.battleIcon, tempScreenX + 1, tempScreenY - gp.tileSize, null);
						tempScreenX -= 2;
						tempScreenY -= 16;
						
						if (fishNum == 1) image = fishDown1;
						else if (fishNum == 2) image = fishDown2;
						else if (fishNum == 3) image = fishDown3;
						else if (fishNum == 4) image = fishDown4;
						break;
					case SURFING:
						image = surfDown1;
						tempScreenX -= 7;
						tempScreenY -= 7;
						if (surfCounter > 30) tempScreenY -= 5;		
						break;
					default:
						if (running) {
							if (spriteNum == 1) image = runDown1;
							else if (spriteNum == 2) image = runDown2;	
							else if (spriteNum == 3) image = runDown3;	
						}
						else {
							if (spriteNum == 1) image = down1;
							else if (spriteNum == 2) image = down2;	
							else if (spriteNum == 3) image = down3;	
							
							if (jumping) {
								tempScreenY -= 15;
							}
						}
						break;
				}		
				break;
			case "left":	
				switch (action) {
					case HM:
						if (hmNum == 1) image = hm1;
						else if (hmNum == 2) image = hm2;
						else if (hmNum == 3) image = hm3;
						else if (hmNum == 4) image = hm4;
						else if (hmNum == 5) image = hm5;
						break;
					case FISHING:						
						
						tempScreenY -= gp.tileSize;							
						if (alert) g2.drawImage(gp.ui.battleIcon, tempScreenX, tempScreenY, null);							
						tempScreenX -= 41;						
						
						if (fishNum == 1) image = fishLeft1;
						else if (fishNum == 2) image = fishLeft2;
						else if (fishNum == 3) image = fishLeft3;
						else if (fishNum == 4) image = fishLeft4;												
						break;
					case SURFING:
						image = surfLeft1;
						tempScreenX -= 7;
						tempScreenY -= 7;
						if (surfCounter > 30) tempScreenY -= 5;			
						break;
					default:
						if (running) {
							if (spriteNum == 1) image = runLeft1;
							else if (spriteNum == 2) image = runLeft2;	
							else if (spriteNum == 3) image = runLeft3;	
						}
						else {
							if (spriteNum == 1) image = left1;
							else if (spriteNum == 2) image = left2;	
							else if (spriteNum == 3) image = left3;	
							
							if (jumping) {
								tempScreenY -= 15;
							}
						}
						break;
				}
				break;
			case "right":
				switch (action) {
					case HM:
						if (hmNum == 1) image = hm1;
						else if (hmNum == 2) image = hm2;
						else if (hmNum == 3) image = hm3;
						else if (hmNum == 4) image = hm4;
						else if (hmNum == 5) image = hm5;
						break;
					case FISHING:
						
						tempScreenY -= gp.tileSize;	
						if (alert) g2.drawImage(gp.ui.battleIcon, tempScreenX, tempScreenY, null);						
						tempScreenX -= 7;
											
						if (fishNum == 1) image = fishRight1;
						else if (fishNum == 2) image = fishRight2;
						else if (fishNum == 3) image = fishRight3;
						else if (fishNum == 4) image = fishRight4;
						break;
					case SURFING:
						image = surfRight1;
						tempScreenX -= 7;
						tempScreenY -= 7;
						if (surfCounter > 30) tempScreenY -= 5;		
						break;
					default:
						if (running) {
							if (spriteNum == 1) image = runRight1;
							else if (spriteNum == 2) image = runRight2;	
							else if (spriteNum == 3) image = runRight3;	
						}
						else {
							if (spriteNum == 1) image = right1;
							else if (spriteNum == 2) image = right2;	
							else if (spriteNum == 3) image = right3;	
							
							if (jumping) {
								tempScreenY -= 15;
							}
						}
						break;
				}			
				break;			
		}
		
		if (inGrass && !moving) {
			BufferedImage grassImg = image.getSubimage(0, 0, 48, 36);
			g2.drawImage(grassImg, tempScreenX, tempScreenY, null);
		}
		else {
			g2.drawImage(image, tempScreenX, tempScreenY, null);
		}		
		
		// RESET OPACITY
		changeAlpha(g2, 1f);
	}	
}
/** END PLAYER METHODS **/

/** END PLAYER CLASS **/