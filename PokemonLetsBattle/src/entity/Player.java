package entity;

/** IMPORTS **/
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import application.GamePanel;
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
	
	private boolean running = false;
	
	// INVENTORY
	public final int maxItemInventorySize = 10;
	public ArrayList<Entity> inventory_item = new ArrayList<>();
	
/** END PLAYER VARIABLES **/		
	
		
/** PLAYER CONSTRUCTOR **/
	
	public Player(GamePanel gp) {
		super(gp);		
		
		// PLAYER POSITION LOCKED TO CENTER
		screenX = gp.screenWidth / 2 - (gp.tileSize / 2); 
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
						
		// HITBOX (x, y, width, height)
		hitbox = new Rectangle(8, 12, 32, 36); 
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
		pokeParty.add(Pokemon.getPokemon(39));
		pokeParty.add(Pokemon.getPokemon(38));
		pokeParty.add(Pokemon.getPokemon(39));
	}
	public void setDefaultValues() {
					
		speed = 3; defaultSpeed = speed;
		animationSpeed = 10; defaultAnimationSpeed = animationSpeed;
		
		// PLAYER ATTRIBUTES		
		setDefaultPosition();
		setDialogue();
		
		getRunImage();
	}	
	public void setDefaultPosition() {	

		worldX = gp.tileSize * 21;
		worldY = gp.tileSize * 30;		
		defaultWorldX = worldX;
		defaultWorldY = worldY;
		safeWorldX = defaultWorldX;
		safeWorldY = defaultWorldY;
		
		gp.currentMap = 0;
		gp.currentArea = gp.outside;
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
	}
	
	// DIALOGUE
	public void setDialogue() {
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
		
		checkCollision();
				
		if (gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed) { 
			walking(); 	
		}
		else {
			spriteNum = 1;
		}
		
		if (gp.keyH.aPressed) {
			int npcIndex = gp.cChecker.checkNPC();
			if (npcIndex != -1) interactNPC(npcIndex);
		}
				
		manageValues();	
	}

/** END UPDATER **/
	
	
/** PLAYER METHODS **/
	
	// MOVEMENT
	public void walking() {
		
		getDirection();
		
		if (!gp.keyH.debug) checkCollision();
		if (!collisionOn) { 			

			if (gp.keyH.bPressed) running = true;
			else running = false;
			
			if (running) {
				speed = 6;
				animationSpeed = 6;
			}
			else {
				speed = defaultSpeed;
				animationSpeed = defaultAnimationSpeed;
			}
			
			move(direction);	
			
			cycleSprites();	
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
		
		if (gp.keyH.upPressed && gp.keyH.leftPressed) tempDirection = "upleft";
		if (gp.keyH.upPressed && gp.keyH.rightPressed) tempDirection = "upright";
		if (gp.keyH.downPressed && gp.keyH.leftPressed) tempDirection = "downleft";
		if (gp.keyH.downPressed && gp.keyH.rightPressed) tempDirection = "downright";	
		
		direction = tempDirection;
	}
	public void move(String direction) {
		
		if (canMove) {
			switch (direction) {
				case "up": worldY -= speed; break;
				case "upleft": worldY -= speed - 0.5; worldX -= speed - 0.5; break;
				case "upright": worldY -= speed - 0.5; worldX += speed - 0.5; break;
				
				case "down": worldY += speed; break;
				case "downleft": worldY += speed - 0.5; worldX -= speed - 0.5; break;
				case "downright": worldY += speed; worldX += speed - 0.5; break;
				
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
			}
			
			if (inGrass) {
				checkWildEncounter();
			}
		}
	}
	private void checkWildEncounter() {
		// random encounter formula reference: https://bulbapedia.bulbagarden.net/wiki/Wild_Pok%C3%A9mon
		
		int r = new Random().nextInt(255);
		
		if (r < 15) {
			
			inGrass = false;
			
			Pokemon wildPokemon = Pokemon.getPokemon("Pikachu");
			wildPokemon.setAlive(true);
			wildPokemon.setHP(wildPokemon.getBHP());
			gp.btlManager.fighter[1] = wildPokemon;
			gp.btlManager.setBattle(gp.btlManager.wildBattle);
			
			gp.gameState = gp.battleState;
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
	
	// COLLISION
	public void checkCollision() {
				
		collisionOn = false;
		
		if (gp.keyH.debug) return;
		
		// CHECK TILE COLLISION
		gp.cChecker.checkTile(this);	
		gp.cChecker.checkEntity(this, gp.npc);		
	}	
			
	public void interactNPC(int i) {		
		if (i != -1) {				
			resetValues();
			gp.npc[gp.currentMap][i].speak();					
		}	
	}	

	// CHECKERS
	public void manageValues() {
	}

	// IMAGE MANAGER
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
	
	// DRAW HANDLER
	public void draw(Graphics2D g2) {
		
		if (!drawing) return;
		
		offCenter();
							
		switch (direction) {
			case "up":
			case "upleft":
			case "upright":		
				if (running) {
					if (spriteNum == 1) image = runUp1;
					else if (spriteNum == 2) image = runUp2;	
					else if (spriteNum == 3) image = runUp3;	
				}
				else {
					if (spriteNum == 1) image = up1;
					else if (spriteNum == 2) image = up2;	
					else if (spriteNum == 3) image = up3;	
				}				
				break;
			case "down":
			case "downleft":
			case "downright":					
				if (running) {
					if (spriteNum == 1) image = runDown1;
					else if (spriteNum == 2) image = runDown2;	
					else if (spriteNum == 3) image = runDown3;	
				}
				else {
					if (spriteNum == 1) image = down1;
					else if (spriteNum == 2) image = down2;	
					else if (spriteNum == 3) image = down3;	
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
				}		
				break;			
		}
		
		if (inGrass) {
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