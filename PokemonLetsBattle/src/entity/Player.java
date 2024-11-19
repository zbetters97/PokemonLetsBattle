package entity;

/** IMPORTS **/
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import application.GamePanel;
import entity.collectables.balls.*;
import entity.collectables.items.*;
import moves.Moves;
import pokemon.Pokedex;
import pokemon.Pokemon;

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
	
	public ArrayList<Pokedex> personalDex = new ArrayList<>();
	public Pokemon[][] pcParty = new Pokemon[10][30];
	public ArrayList<Pokemon> pcBox_1 = new ArrayList<Pokemon>(30);
	
	public boolean jumping = false;
	private int jumpCounter = 0;
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
	}

/** END PLAYER CONSTRUCTOR **/
		
	
/** DEFAULT HANDLERS **/
	
	// DEFAULT VALUES
	public void assignParty() {		
		pokeParty.add(Pokemon.getPokemon(Pokedex.MUDKIP, 5, new COL_Ball_Poke(gp)));
		pokeParty.add(Pokemon.getPokemon(Pokedex.MARSHTOMP, 16, new COL_Ball_Great(gp)));
		pokeParty.add(Pokemon.getPokemon(Pokedex.SWAMPERT, 36, new COL_Ball_Master(gp)));
	}
	public void setDefaultValues() {
				
		int c = 0;
		int x = 0;
		for (int i = 0; i < Pokedex.values().length; i++) {
			pcParty[c][x] = Pokemon.getPokemon(Pokedex.values()[i], 36, new COL_Ball_Master(gp));
			x++;
			if (i == 29 || i == 59 || i == 92) { c++; x = 0; } 	
		}
		
		speed = 4; defaultSpeed = speed;
		animationSpeed = 8; defaultAnimationSpeed = animationSpeed;
		
		// PLAYER ATTRIBUTES		
		setDefaultPosition();
		setDialogue();
		
		getRunImage();
		
		personalDex.add(pokeParty.get(0).getPokemon());
		personalDex.add(pokeParty.get(1).getPokemon());
		personalDex.add(pokeParty.get(2).getPokemon());
		
		inventory_keyItems.add(new ITM_EXP_Share(gp));
		
		inventory_items.add(new ITM_Potion(gp));
		inventory_items.get(0).amount = 10;
		
		inventory_items.add(new ITM_Potion_Super(gp));
		inventory_items.get(1).amount = 5;
	
		inventory_items.add(new ITM_Potion_Hyper(gp));
		inventory_items.get(2).amount = 3;
		
		inventory_items.add(new ITM_Potion_Max(gp));
		
		inventory_items.add(new ITM_Repel(gp));
		inventory_items.add(new ITM_Full_Restore(gp));	
		inventory_items.add(new ITM_Heal_Full(gp));			
		inventory_items.add(new ITM_Revive(gp));
		inventory_items.add(new ITM_Revive_Max(gp));	
		inventory_items.add(new ITM_Heal_Burn(gp));
		
		inventory_items.add(new ITM_Heal_Ice(gp));
		inventory_items.add(new ITM_Heal_Antidote(gp));
		inventory_items.add(new ITM_Heal_Awakening(gp));
		
		
		inventory_pokeballs.add(new COL_Ball_Poke(gp));
		inventory_pokeballs.get(0).amount = 10;
		
		inventory_pokeballs.add(new COL_Ball_Great(gp));
		inventory_pokeballs.get(1).amount = 5;
	
		inventory_pokeballs.add(new COL_Ball_Ultra(gp));
		inventory_pokeballs.get(2).amount = 3;
		
		inventory_pokeballs.add(new COL_Ball_Master(gp));
	}
	public void setDefaultPosition() {	
		worldX = gp.tileSize * 30;
		worldY = gp.tileSize * 26;		
		defaultWorldX = worldX;
		defaultWorldY = worldY;
		safeWorldX = defaultWorldX;
		safeWorldY = defaultWorldY;
		
		gp.currentMap = 0;
		gp.currentArea = gp.town;
	}
	public void restoreStatus() {
		speed = defaultSpeed;		
		resetValues();
	}	
	public void resetValues() {			
		gp.keyH.aPressed = false;
		gp.keyH.bPressed = false;
		canMove = true;
		running = false;
		moving = false;
		jumping = false;
		pixelCounter = 0;
		jumpCounter = 0;
	}
	
	// DIALOGUE
	public void setDialogue() {
		dialogues[0][0] = "Repel affect has worn off.";		
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

/** END DEFAULT HANDLERS **/
	
	
/** UPDATER **/
	public void update() {
		
		if (jumping) {
			speed = 3;
			animationSpeed = 6;
			moving = true;
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
		int objIndex = gp.cChecker.checkObject(this, true);
		
		if (npcIndex != -1) interactNPC(npcIndex);
		else if (objIndex != -1) interactObject(objIndex);
	}	
	public void interactNPC(int i) {		
		if (i != -1 && !gp.npc[gp.currentMap][i].moving) {		
			resetValues();
			gp.npc[gp.currentMap][i].speak();					
		}	
	}	
	public void interactObject(int i) {
		if (i != -1 && gp.obj[gp.currentMap][i].type == type_obstacle_i) {
			gp.obj[gp.currentMap][i].interact();	
		}
	}
	
	public void move() {		
		if (gp.keyH.bPressed) {
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
		}
		
		pixelCounter += speed;		
		if (pixelCounter >= gp.tileSize) {
									
			moving = false;
			pixelCounter = 0;
			
			gp.cChecker.checkGrass(this);
			
			if (!hasRepel() && inGrass) {
				checkWildEncounter();					
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
		gp.cChecker.checkObject(this, true);
		
		// CHECK INTERACTIVE OBJECTS COLLISION
		int objIIndex = gp.cChecker.checkObject_I(this);
		if (objIIndex != -1) interactObjectI(objIIndex);	
	}	
	public void interactObjectI(int i) {
		if (gp.obj_i[gp.currentMap][i].type == type_obstacle) {				
			if (!gp.obj_i[gp.currentMap][i].moving) {	
				gp.obj_i[gp.currentMap][i].move(direction);	
			}
		}	
		else if (gp.obj_i[gp.currentMap][i].type == type_obstacle_i) {				
			gp.obj_i[gp.currentMap][i].interact();	
		}	
	}	
	
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
	
	public void stopMoving() {		
		if (!moving) {
			canMove = false;
			running = false;
			spriteNum = 1;	
		}		
	}
	
	public void manageValues() {
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
		
		if (!inGrass) {
			g2.setColor(new Color(0,0,0,100));
			g2.fillOval(tempScreenX + 9, tempScreenY + 40, 30, 10);
		}
									
		switch (direction) {
			case "up":
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
			case "down":				
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
			case "left":					
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
			case "right":					
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