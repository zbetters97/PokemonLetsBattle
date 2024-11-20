package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import application.GamePanel;
import entity.Entity.Action;
import moves.Move;
import pokemon.Pokedex;
import pokemon.Pokemon;
import properties.Status;

public class Entity {
	
	public enum Action {
		IDLE, SURFING;
	}
	
	public enum EncounterType {
		COMMON, UNCOMMON, RARE, VERY_RARE,EXTREMELY_RARE,LEGENDARY,SHINY,ELITE
	}		
	
	protected GamePanel gp;
	
	// GENERAL ATTRIBUTES
	public Action action = Action.IDLE;
	public boolean running = false;
	public boolean jumping = false;
	public int worldX, worldY;	
	public int safeWorldX = 0, safeWorldY = 0;
	protected int worldXStart;
	protected int worldYStart;	
	public int tempScreenX;
	public int tempScreenY;
	public int bounds = 999;	
	public String direction = "down";		
	public int type;
	public String name;			
	public int speed, defaultSpeed, animationSpeed, defaultAnimationSpeed;	
	public int money = 0;
	public int trainerClass = 1;
	public boolean drawing = true;
	public boolean temp = false;
	public boolean sleep = false;	
	public boolean collision = true;
	public boolean collisionOn = false;	
	protected boolean hasShadow = true;
	protected boolean battleFound = false;
	
	// SPRITE HANDLING
	public int actionLockCounter;
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public int spriteCycle = 0;
	public BufferedImage image, image1, image2, image3,
							up1, up2, up3, up4,
							down1, down2, down3, down4,  
							left1, left2, left3, left4, 
							right1, right2, right3, right4,
							runUp1, runUp2, runUp3,
							runDown1, runDown2, runDown3,
							runLeft1, runLeft2, runLeft3,
							runRight1, runRight2, runRight3;
	public BufferedImage frontSprite, backSprite;
		
	// CHARACTER ATTRIBUTES	
	public boolean hasBattle = false;
	public boolean isDefeated = false;
	public boolean hasItemToGive = false;	
	public boolean hasCutscene = false;	
	public boolean canMove = true;
	public boolean moving = false;
	public int pixelCounter = 0;
	public boolean inGrass = false;
	public boolean onPath = false;
	public boolean pathCompleted = false;
	protected int steps = 0;
	
	public int skillLevel = 0;
	public final int skill_rookie = 1;
	public final int skill_smart = 2;
	public final int skill_elite = 3;
		
	// DIALOGUE
	public String dialogues[][] = new String[20][20];
	public int dialogueSet = 0;
	public int dialogueIndex = 0;		
	public String responses[][] = new String[10][3];
	protected int battleIconTimer = 0;
					
	// DEFAULT HITBOX
	public Rectangle hitbox = new Rectangle(0, 0, 48, 48);
	public int hitboxDefaultX = hitbox.x;
	public int hitboxDefaultY = hitbox.y;
	public int hitboxDefaultWidth = hitbox.width;
	public int hitboxDefaultHeight = hitbox.height;

	// ITEM VALUES	
	public String description;
	public int amount = 1;
	public int value = 0;
	public int catchProbability;
	public Status status;
	
	// ITEM TYPE
	public int collectableType = 0;
	public final int type_keyItem = 1;
	public final int type_item = 2;
	public final int type_ball = 3;
	public final int type_move = 4;
	
	// INVENTORY
	public ArrayList<Entity> inventory_keyItems = new ArrayList<>();
	public ArrayList<Entity> inventory_items = new ArrayList<>();
	public ArrayList<Entity> inventory_pokeballs = new ArrayList<>();
	public ArrayList<Entity> inventory_moves = new ArrayList<>();
	
	// POKEMON PARTY
	public List<Pokemon> pokeParty = new ArrayList<>(6);
	public final int maxPartySize = 6;
	
	// OBJECT ATTRIBUTES
	public boolean alive = true;
	public boolean opening;
	public String hmType;
	
	// CHARACTER TYPES
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_obstacle = 2;
	public final int type_obstacle_i = 3;
	
	// CONSTRUCTOR
	public Entity(GamePanel gp) {
		this.gp = gp;
		
		pokeParty = new ArrayList<>();
		assignParty();
		
		getImage();
	}
	
	// CHILD ONLY		
	public void getImage() { }
	public void assignParty() { }
	public void setAction() { }	
	public void move(String direction) { }
	public void setPath(int c, int r) { }		
	public void interact() { }
	public void speak() { }		
	public void use() { }
	public void apply() { }
	public void apply(Entity entity, Pokemon p) { }
	public void resetValues() { }	
	
	// UPDATER
	public void update() {	
		setAction();					
		manageValues();	
	}
	
	public void move() {
		
		speed = 6;
		checkCollision();
		if (!collisionOn && withinBounds()) { 	
			moving = true;	
			speed = defaultSpeed;
		}
		else {						
			spriteNum = 1;
		}		
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
		}
		
		pixelCounter += speed;		
		if (pixelCounter >= gp.tileSize) {
			moving = false;
			pixelCounter = 0;
			steps++;
		}
	}
	
	// COLLISION CHECKER
	protected void checkCollision() {	
		
		collisionOn = false;
		
		gp.cChecker.checkTile(this);	
		gp.cChecker.checkEntity(this, gp.npc);		
		gp.cChecker.checkPlayer(this);		
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkObject_I(this);
	}
			
	// SPRITE CYCLE
	public void cycleSprites() {
		spriteCounter++;
		if (spriteCounter > animationSpeed && animationSpeed != 0) {
			
			if (spriteNum == 1) spriteNum = 2;
			else if (spriteNum == 2) spriteNum = 1;
			
			spriteCounter = 0;
		}
	}
	
	/** DIRECTION **/
	public void getDirection(int rate) {		
		
		actionLockCounter++;			
		if (actionLockCounter >= rate) {		
						
			int dir = 1 + (int)(Math.random() * 4);
			if (dir == 1) direction = "up";
			else if (dir == 2) direction = "down";
			else if (dir == 3) direction = "left";
			else if (dir == 4) direction = "right";
			
			actionLockCounter = 0;
		}		
	}
	public void getSquareDirection(int length) {
		
		if (steps == length) {
			switch (direction) {
				case "up":
					direction = "right";
					break;
				case "down":
					direction = "left";
					break;
				case "left":
					direction = "up";
					break;
				case "right":
					direction = "down";
					break;
			}			
			steps = 0;
		}		
	}
	protected String getOppositeDirection(String direction) {
		
		String oppositeDirection = "";
		
		switch(direction) {
			case "up": oppositeDirection = "down"; break;
			case "down": oppositeDirection = "up"; break;
			case "left": oppositeDirection = "right"; break;
			case "right": oppositeDirection = "left"; break;
		}
		
		return oppositeDirection;
	}
	/** END DIRECTION **/
	
	// DIALOGUE
	protected void startDialogue(Entity entity, int setNum) {
		spriteNum = 1;
		dialogueSet = setNum;
		gp.ui.npc = entity;		
		gp.gameState = gp.dialogueState;
	}
	
	/** PATH FINDING **/
	public void isOnPath(Entity target, int distance) {
		if (getTileDistance(target) < distance) {
			onPath = true;
		}
	}
	public void isOffPath(Entity target, int distance) {
		if (getTileDistance(target) > distance || !withinBounds()) {
			onPath = false;			
		}
	}	
	public int getTileDistance(Entity target) {
		int tileDistance = (getXdistance(target) + getYdistance(target)) / gp.tileSize;
		return tileDistance;
	}
	public int getXdistance(Entity target) { 
		int xDistance = Math.abs(getCenterX() - target.getCenterX());
		return xDistance;
	}
	public int getYdistance(Entity target) { 
		int yDistance = Math.abs(getCenterY() - target.getCenterY());
		return yDistance;
	}	
	public boolean withinBounds() {
		
		boolean withinBounds = true;
		
		int tempWorldX = worldX;
		int tempWorldY = worldY;	
		
		switch (direction) {
			case "up": tempWorldY -= speed; break;
			case "upleft": tempWorldY -= speed - 1; tempWorldX -= speed - 1; break;
			case "upright": tempWorldY -= speed - 1; tempWorldX += speed - 1; break;
			
			case "down": tempWorldY += speed; break;
			case "downleft": tempWorldY += speed - 1; tempWorldX -= speed - 1; break;
			case "downright": tempWorldY += speed; tempWorldX += speed - 1; break;
			
			case "left": tempWorldX -= speed; break;
			case "right": tempWorldX += speed; break;
		}
		
		int tileDistance = (Math.abs(worldXStart - tempWorldX) + Math.abs(worldYStart - tempWorldY)) / gp.tileSize;

		if (tileDistance > bounds)
			withinBounds = false;
		
		return withinBounds;
	}
	public boolean playerWithinBounds() {
		
		boolean playerWithinBounds = true;
		
		int tileDistance = (Math.abs(worldXStart - gp.player.worldX) + Math.abs(worldYStart - gp.player.worldY)) / gp.tileSize;

		if (tileDistance > bounds)
			playerWithinBounds = false;
		
		return playerWithinBounds;		
	}
	public int getGoalCol(Entity target) {
		int goalCol = (target.worldX + target.hitbox.x) / gp.tileSize;
		return goalCol;
	}
	public int getGoalRow(Entity target) {
		int goalRow = (target.worldY + target.hitbox.y) / gp.tileSize;
		return goalRow;
	}
	public boolean findPath(int goalCol, int goalRow) {
		
		boolean pathFound = false;
		
		int startCol = (worldX + hitbox.x) / gp.tileSize;
		int startRow = (worldY + hitbox.y) / gp.tileSize;
		
		// SET PATH
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if (gp.pFinder.search()) 
			pathFound = true;
			
		return pathFound;
	}	
	public void followPath(int goalCol, int goalRow) {
		
		int startCol = (worldX + hitbox.x) / gp.tileSize;
		int startRow = (worldY + hitbox.y) / gp.tileSize;
		
		// SET PATH
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		// PATH FOUND
		if (gp.pFinder.search()) {
			
			// NEXT WORLDX & WORLDY
			int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
						
			// ENTITY hitbox
			int eLeftX = worldX + hitbox.x;
			int eRightX = worldX + hitbox.x + hitbox.width;
			int eTopY = worldY + hitbox.y;
			int eBottomY = worldY + hitbox.y + hitbox.height;
			
			// FIND DIRECTION TO NEXT NODE
			// UP OR DOWN
			if (eTopY > nextY && eLeftX >= nextX && eRightX < nextX + gp.tileSize) 
				direction = "up";			
			else if (eTopY < nextY && eLeftX >= nextX && eRightX < nextX + gp.tileSize)
				direction = "down";			
			// LEFT OR RIGHT
			else if (eTopY >= nextY && eBottomY < nextY + gp.tileSize) {				
				if (eLeftX > nextX) direction = "left";
				if (eLeftX < nextX) direction = "right";
			}
			// UP OR LEFT
			else if (eTopY > nextY && eLeftX > nextX) {				
				direction = "up";				
				checkCollision();
				if (collisionOn) direction = "left";			
			}
			// UP OR RIGHT
			else if (eTopY > nextY && eLeftX < nextX) {
				direction = "up";
				checkCollision();
				if (collisionOn) direction = "right";		
			}
			// DOWN OR LEFT
			else if (eTopY < nextY && eLeftX > nextX) {
				direction = "down";
				checkCollision();
				if (collisionOn) direction = "left";
			}
			// DOWN OR RIGHT
			else if (eTopY < nextY && eLeftX < nextX) {
				direction = "down";
				checkCollision();
				if (collisionOn) direction = "right";
			}
		}
		// NO PATH FOUND
		else {
			onPath = false;
		}
		
		// GOAL REACHED
		if (gp.pFinder.pathList.size() > 0) {		
			
			int nextCol = gp.pFinder.pathList.get(0).col;
			int nextRow = gp.pFinder.pathList.get(0).row;
			
			if (nextCol == goalCol && nextRow == goalRow) {				
				pathCompleted = true;
			}
		}
	}
	/** END PATH FINDING **/
		
	/** PLAYER INTERACTION **/
	protected boolean lookForBattle(int tileDistance) {
		
		if (battleFound) {
			
			followPath(getGoalCol(gp.player), getGoalRow(gp.player));
				
			if (!moving) move();	
			if (pathCompleted) startBattle(0);	
							
			return true;
		}
		else {	
			if (findPlayer(direction, tileDistance)) { 
								
				gp.player.stopMoving();
				
				battleIconTimer++;
				if (battleIconTimer == 1) {
					gp.stopMusic();
					gp.startMusic(0, gp.se.getFile(0, "May"));	
				}
				else if (battleIconTimer > 60) {
					battleFound = true;
					battleIconTimer = 0;
				}
				
				return true;
			}
		}			
		
		return false;
	}
	protected void startBattle(int dialogueSet) {
		
		gp.player.direction = getOppositeDirection(direction);
		gp.player.canMove = true;
		
		battleFound = false;
		pathCompleted = false;		
		battleIconTimer = 0;
			
		this.dialogueSet = dialogueSet;
		startDialogue(this, this.dialogueSet);
	}
	protected boolean findPlayer(String direction, int tileDistance) {
		
		boolean playerFound = false;
			
		// PLAYER WITHIN 8 TILES
		isOnPath(gp.player, tileDistance);			
		if (onPath) {			
			
			isOffPath(gp.player, tileDistance);				
			
			// FIND IF PLAYER IS IN SIGHTS				
			switch (direction) {
				case "up":
					if (worldY - gp.player.worldY >= 0 && worldX - gp.player.worldX == 0) {
						if (pathOpen(direction)) playerFound = true;
					}					
					break;
				case "down":
					if (worldY - gp.player.worldY < 0 && worldX - gp.player.worldX == 0) {
						if (pathOpen(direction)) playerFound = true;
					}					
					break;
				case "left":
					if (worldX - gp.player.worldX >= 0 && worldY - gp.player.worldY == 0) {
						if (pathOpen(direction)) playerFound = true;
					}							
					break;
				case "right":
					if (worldX - gp.player.worldX < 0 && worldY - gp.player.worldY == 0) {								
						if (pathOpen(direction)) playerFound = true;
					}
							
					break;
			}
		}
		else {
			playerFound = false;
		}
			
		return playerFound;
	}
	private boolean pathOpen(String direction) {		
		
		switch(direction) {
			case "up":
				for (int i = 0; i <= getTileDistance(gp.player); i++) {					
					int wX = worldX / gp.tileSize;
					int wY = (worldY - gp.tileSize * i) / gp.tileSize;
					if (tileHasCollision(wX, wY))
						return false;
				}
				break;
			case "down":
				for (int i = 0; i <= getTileDistance(gp.player); i++) {					
					int wX = worldX / gp.tileSize;
					int wY = (worldY + gp.tileSize * i) / gp.tileSize;
					if (tileHasCollision(wX, wY))
						return false;
				}
				break;
			case "left":				
				for (int i = 0; i <= getTileDistance(gp.player); i++) {					
					int wX = (worldX - gp.tileSize * i) / gp.tileSize;
					int wY = worldY / gp.tileSize;
					if (tileHasCollision(wX, wY))
						return false;
				}				
				break;
			case "right":
				for (int i = 0; i <= getTileDistance(gp.player); i++) {					
					int wX = (worldX + gp.tileSize * i) / gp.tileSize;
					int wY = worldY / gp.tileSize;
					if (tileHasCollision(wX, wY))
						return false;
				}
				break;
		}		
		
		return true;
	}
	private boolean tileHasCollision(int wX, int wY) {
		
		boolean tileCollision = false;
		
		int tileNum = gp.tileM.mapTileNum[gp.currentMap][wX][wY];
		if (gp.tileM.tile[tileNum].collision || gp.tileM.tile[tileNum].water) {
			tileCollision = true;
		}
		
		return tileCollision;
	}
	protected void approachPlayer(int rate) {
		
		actionLockCounter++;
		if (actionLockCounter >= rate) {
			
			if (getXdistance(gp.player) >= getYdistance(gp.player)) {
				if (gp.player.getCenterX() < getCenterX()) {
					direction = "left";
				}
				
				else {
					direction = "right";
				}
			}
			else if (getXdistance(gp.player) < getYdistance(gp.player)) {
				if (gp.player.getCenterY() < getCenterY()) {
					direction = "up";
				}
				else {
					direction = "down";
				}
			}
			
			actionLockCounter = 0;
		}
	}
	/** END PLAYER INTERACTION **/
	
	public boolean hasPokemon() {
		
		boolean hasPokemon = false;	
		
		for (Pokemon p : pokeParty) {
			if (p.isAlive()) {
				hasPokemon = true;
			}
		}
		
		return hasPokemon;
	}	
	public int getAvailablePokemon() {
		
		int availablePokemon = 0;	
		
		for (Pokemon p : pokeParty) {
			if (p.isAlive()) {
				availablePokemon++;
			}
		}
		
		return availablePokemon;
	}
	
	/** WILD ENCOUNTER **/
	protected void checkWildEncounter() {
		// random encounter formula reference: https://bulbapedia.bulbagarden.net/wiki/Wild_Pok%C3%A9mon
						
		int r = new Random().nextInt(255);		
		if (r < 15) {
						
			Pokemon wildPokemon = getWildPokemon();
			
			if (wildPokemon != null) {
				
				gp.btlManager.fighter[1] = wildPokemon;
				gp.btlManager.setup(gp.btlManager.wildBattle, null, wildPokemon, null);
								
				gp.gameState = gp.transitionState;	
			}
		}
	}
	private Pokemon getWildPokemon() {
		
		Pokedex randomPokemon = null;
		Pokemon wildPokemon = null;	
		
		// RANDOM NUM 0-100
		int chance = new Random().nextInt(100);
		int total = 0;
		
		// FOR EACH LIST OF POKEMON FROM LOCATION
		for (Pokedex p : gp.wildEncounters.get(gp.currentMap).keySet()) {
			
			// GET PROBABILITY OF POKEMON ENCOUNTER
			int rate = gp.wildEncounters.get(gp.currentMap).get(p); 
			total += rate;
			
			// POKEMON RANDOMLY SELECTED, ASSIGN NAME AND STOP
			if (chance <= total) {	
				randomPokemon = p;
				break;
			}	
		}
		
		// LEVEL RANGE BASED ON LOCATION
		int minLevel = gp.wildLevels.get(gp.currentMap);
		int maxLevel = minLevel + 3;
		int level = new Random().nextInt(maxLevel - minLevel + 1) + minLevel;
		
		wildPokemon = Pokemon.getPokemon(randomPokemon, level, null);
		
		return wildPokemon;
	}
	/** END WILD ENCOUNTER **/
	
	public boolean healPokemonParty() {
		
		if (pokeParty.size() > 0) {
			for (Pokemon p : pokeParty) {				
				p.setAlive(true);
				p.setHP(p.getBHP());
				p.resetMoves();
				p.setStatus(null);				
			}
			
			return true;
		}
		else {
			return false;
		}		
	}	
	
	public Pokemon pokemonHasHM(String objectType) {
		
		Pokemon hmPokemon = null;
		
		switch (objectType) {
			case "CUT":				
				for (Pokemon p : gp.player.pokeParty) {
					for (Move m : p.getMoveSet()) {
						if (m.getName().equals("Cut")) {
							hmPokemon = p;
							break;	
						}
					}
				}				
				break;
			case "ROCK SMASH":
				for (Pokemon p : gp.player.pokeParty) {
					for (Move m : p.getMoveSet()) {
						if (m.getName().equals("Rock Smash")) {
							hmPokemon = p;
							break;	
						}
					}
				}		
				break;
			case "SURF":
				for (Pokemon p : gp.player.pokeParty) {					
					for (Move m : p.getMoveSet()) {
						if (m.getName().equals("Surf")) {
							hmPokemon = p;
							break;	
						}
					}
				}		
				break;
		}
		
		return hmPokemon;		
	}
	
	/** ITEM FUNCTIONS **/
	public void useItem(String description) {

		gp.ui.partyDialogue = description;
		gp.ui.partyItem = this;
		gp.ui.partyItemApply = true;
		
		gp.gameState = gp.pauseState;
		gp.ui.pauseState = gp.ui.pause_Party;
		gp.ui.partyState = gp.ui.party_Main_Select;
	}	
	public void giveItem(Entity item, Pokemon p) {
		
		if (p.getHeldItem() == null) {
			
			p.giveItem(item);			
			removeItem(this, gp.player);				
			
			gp.ui.bagNum = 0;
			gp.ui.partyDialogue = p.getName() + " was given a\n" + item.name + " to hold.";
			gp.ui.partyState = gp.ui.party_Main_Dialogue;					
		}
		else {
			gp.keyH.playErrorSE();
		}
	}
	public void addItem(Entity item, Entity person) {

		Entity newItem = gp.eGenerator.getItem(item.name);
		
		ArrayList<Entity> inventory = null;
		
		if (item.collectableType == type_keyItem) inventory = person.inventory_keyItems;		
		else if (item.collectableType == type_item) inventory = person.inventory_items;		
		else if (item.collectableType == type_ball) inventory = person.inventory_pokeballs;		
		else if (item.collectableType == type_move) inventory = person.inventory_moves;
		else return;
		
		int index = searchInventory(newItem, inventory);
		if (index != -1) inventory.get(index).amount++;
		else inventory.add(newItem);		
	}
	public void removeItem(Entity item, Entity person) {	
		
		ArrayList<Entity> inventory = null;
		
		if (item.collectableType == type_keyItem) inventory = person.inventory_keyItems;		
		else if (item.collectableType == type_item) inventory = person.inventory_items;		
		else if (item.collectableType == type_ball) inventory = person.inventory_pokeballs;		
		else if (item.collectableType == type_move) inventory = person.inventory_moves;
		else return;
		
		int index = searchInventory(item, inventory);
		if (index != -1) {
			inventory.get(index).amount--;
			if (inventory.get(index).amount <= 0) {
				inventory.remove(index);	
				if (gp.ui.bagNum > 0) {
					gp.ui.bagNum--;
				}
			}
		}
	}
	public int searchInventory(Entity item, ArrayList<Entity> inventory) {
		
		int itemIndex = -1;		
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).name.equals(item.name)) {
				itemIndex = i;
				break;
			}
		}		
		return itemIndex;
	}
	
	protected void revive(Entity entity, Pokemon p) {
						
		if (!p.isAlive()) {
			
			gp.playSE(gp.battle_SE, "heal");
			p.setAlive(true);
			p.setHP((int) (p.getBHP() / value));			
			removeItem(this, gp.player);						
			
			gp.ui.bagNum = 0;
			gp.ui.partyDialogue = p.getName() + " was revived!";
			gp.ui.partyState = gp.ui.party_Main_Dialogue;
		}		
		else {
			gp.keyH.playErrorSE();
		}
	}
	protected void restore(Entity entity, Pokemon p) {
		
		if (p.isAlive() && p.getHP() < p.getBHP()) {
			
			int gainedHP = value;												
			if (p.getHP() + value > p.getBHP()) {
				gainedHP = p.getBHP() - p.getHP();
			}	
			
			gp.playSE(gp.battle_SE, "heal");
			p.addHP(value);			
			removeItem(this, gp.player);				
		
			gp.ui.bagNum = 0;
			gp.ui.partyDialogue = p.getName() + " gained " + gainedHP + " HP.";
			gp.ui.partyState = gp.ui.party_Main_Dialogue;			
		}		
		else {
			gp.keyH.playErrorSE();
		}
	}
	protected void heal(Entity entity, Pokemon p) {
		
		if (p.isAlive() && p.getStatus() != null && (p.getStatus().equals(status) || status == null)) {
						
			gp.playSE(gp.battle_SE, "heal");
			p.setStatus(null);			
			removeItem(this, gp.player);			
			
			gp.ui.bagNum = 0;
			gp.ui.partyDialogue = p.getName() + " was healed.";
			gp.ui.partyState = gp.ui.party_Main_Dialogue;
		}		
		else {
			gp.keyH.playErrorSE();
		}
	}
	
	protected void throwBall() {
		
		if (gp.btlManager.active && 
				gp.btlManager.battleMode == gp.btlManager.wildBattle) {					
				
			gp.player.removeItem(this, gp.player);				
									
			gp.btlManager.ballUsed = this;
			gp.btlManager.fightStage = gp.btlManager.fight_Capture;				
			gp.btlManager.running = true;			
			new Thread(gp.btlManager).start();	
			
			gp.ui.bagNum = 0;
			gp.ui.bagTab = gp.ui.bag_KeyItems;
			gp.ui.bagState = gp.ui.bag_Main;
			gp.ui.pauseState = gp.ui.pause_Main;
			
			gp.ui.battleState = gp.ui.battle_Dialogue;
			gp.gameState = gp.battleState;
		}		
		else {
			gp.ui.bagDialogue = "You can't use this here!";
			gp.ui.bagState = gp.ui.bag_Dialogue;
		}
	}
	
	public int getItemIndex(ArrayList<Entity> items, String name) {
		
		int itemIndex = -1;
		
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).name.equals(name)) {
				itemIndex = i;
				break;
			}
		}
		
		return itemIndex;		
	}
	/** END ITEM FUNCTIONS **/
	
	// MANAGE VALUES
	public void manageValues() {
	}
	public void resetCounter() {
		spriteCounter = 0;
	}
	
	// IMAGE MANAGERS
	public BufferedImage setup(String imagePath) {	
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = GamePanel.utility.scaleImage(image, gp.tileSize, gp.tileSize);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	public BufferedImage setup(String imagePath, int width, int height) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = GamePanel.utility.scaleImage(image, width, height);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}	
	public void changeAlpha(Graphics2D g2, float alphaValue) {		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	
	// DRAW
	public void draw(Graphics2D g2) {
				
		BufferedImage image = null;
								
		// DRAW TILES WITHIN SCREEN BOUNDARY
		if (inFrame() && drawing) {
			
			if (hasShadow) {
				g2.setColor(new Color(0,0,0,100));
				g2.fillOval(tempScreenX + 9, tempScreenY + 40, 30, 10);	
			}
			
			switch (direction) {
				case "up":									
					if (spriteNum == 1) image = up1;
					else if (spriteNum == 2) image = up2;	
					else if (spriteNum == 3) image = up3;			
					else if (spriteNum == 4) image = up4;			
					break;
				case "down":						
					if (spriteNum == 1) image = down1;
					else if (spriteNum == 2) image = down2;	
					else if (spriteNum == 3) image = down3;	
					else if (spriteNum == 4) image = down4;	
					break;
				case "left":										
					if (spriteNum == 1) image = left1;
					else if (spriteNum == 2) image = left2;	
					else if (spriteNum == 3) image = left3;	
					else if (spriteNum == 4) image = left4;	
					break;
				case "right":					
					if (spriteNum == 1) image = right1;
					else if (spriteNum == 2) image = right2;	
					else if (spriteNum == 3) image = right3;			
					else if (spriteNum == 4) image = right4;			
					break;
			}		
		
			g2.drawImage(image, tempScreenX, tempScreenY, null);
			
			if (battleIconTimer > 0) {
				g2.drawImage(gp.ui.battleIcon, tempScreenX, tempScreenY - gp.tileSize, null);
			}
										
			// DRAW HITBOX
			if (gp.keyH.debug) {
				g2.setColor(Color.RED);
				g2.drawRect(tempScreenX + hitbox.x, tempScreenY + hitbox.y, hitbox.width, hitbox.height);
			}
			
			// RESET OPACITY
			changeAlpha(g2, 1f);			
		}
	}
	public boolean inFrame() {
		
		boolean inFrame = false;
		
		// AWAY FROM CENTER OF CAMERA
		offCenter();
		
		// WITHIN SCREEN BOUNDARY
		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
				worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			inFrame = true;
		
		}				
		
		return inFrame;
	}
	public void offCenter() {
		
		tempScreenX = getScreenX();
		tempScreenY = getScreenY();		
		
		if (gp.player.worldX < gp.player.screenX) tempScreenX = worldX;
		if (gp.player.worldY < gp.player.screenY) tempScreenY = worldY;		
		
		// FROM PLAYER TO RIGHT-EDGE OF SCREEN
		int rightOffset = gp.screenWidth - gp.player.screenX;		
		
		// FROM PLAYER TO RIGHT-EDGE OF WORLD
		if (rightOffset > gp.worldWidth - gp.player.worldX) {
			tempScreenX = gp.screenWidth - (gp.worldWidth - worldX);
		}			
		
		// FROM PLAYER TO BOTTOM-EDGE OF SCREEN
		int bottomOffSet = gp.screenHeight - gp.player.screenY;
		
		// FROM PLAYER TO BOTTOM-EDGE OF WORLD
		if (bottomOffSet > gp.worldHeight - gp.player.worldY) {			
			tempScreenY = gp.screenHeight - (gp.worldHeight - worldY);
		}
	}
	
	// GET X,Y
	public int getCenterX() {
		int centerX = worldX + left1.getWidth() / 2;
		return centerX;
	}
	public int getCenterY() {
		int centerY = worldY + up1.getHeight() / 2;
		return centerY;
	}
	public int getScreenX() {
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		return screenX;
	}
	public int getScreenY() {
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		return screenY;
	}
}