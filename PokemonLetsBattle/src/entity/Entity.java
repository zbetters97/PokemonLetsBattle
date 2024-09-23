package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import application.GamePanel;
import pokemon.Pokemon;

public class Entity {
	
	protected GamePanel gp;
	
	// GENERAL ATTRIBUTES
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
	public boolean hasBattle = false;
	public boolean isDefeated = false;
	
	// SPRITE HANDLING
	public int actionLockCounter;
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public int spriteCycle = 0;
	public BufferedImage image, image1, image2, image3,
							up1, up2, up3, 
							down1, down2, down3, 
							left1, left2, left3, 
							right1, right2, right3,
							runUp1, runUp2, runUp3,
							runDown1, runDown2, runDown3,
							runLeft1, runLeft2, runLeft3,
							runRight1, runRight2, runRight3;
	public BufferedImage frontSprite, backSprite;
		
	// CHARACTER ATTRIBUTES	
	public boolean hasItemToGive = false;	
	public boolean hasCutscene = false;	
	public boolean canMove = true;
	public boolean moving = false;
	public boolean onPath = false;
	public boolean pathCompleted = false;
		
	// DIALOGUE
	public String dialogues[][] = new String[20][20];
	public int dialogueSet = 0;
	public int dialogueIndex = 0;		
	public String responses[][] = new String[10][3];
					
	// DEFAULT HITBOX
	public Rectangle hitbox = new Rectangle(0, 0, 60, 60);
	public int hitboxDefaultX = hitbox.x;
	public int hitboxDefaultY = hitbox.y;
	public int hitboxDefaultWidth = hitbox.width;
	public int hitboxDefaultHeight = hitbox.height;

	// INVENTORY
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	
	// CHARACTER TYPES
	public final int type_player = 0;
	public final int type_npc = 1;
	
	// POKEMON PARTY
	public ArrayList<Pokemon> pokeParty = new ArrayList<>();
	public final int maxPartySize = 6;
	
	// CONSTRUCTOR
	public Entity(GamePanel gp) {
		this.gp = gp;
		
		pokeParty = new ArrayList<>();
		assignParty();
		
		getImage();
	}
	
	/*
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
	*/
	
	// CHILD ONLY		
	public void getImage() { }
	public void assignParty() { }
	
	public void setAction() { }	
	public void move(String direction) { }
	public void setPath(int c, int r) { }		
	public void interact() { }
	public void speak() { }		
	public void resetValues() { }	
	
	// UPDATER
	public void update() {

		// CHILD CLASS
		setAction();
		
		move();
		manageValues();
	}	
			
	public void move() {
		
		checkCollision();
		
		if (!collisionOn && withinBounds()) { 	
			
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
		else {
			spriteNum = 1;
		}
	}	
	
	// COLLISION CHECKER
	protected void checkCollision() {	
		
		collisionOn = false;
		
		gp.cChecker.checkTile(this);	
		gp.cChecker.checkEntity(this, gp.npc);	
		gp.cChecker.checkPlayer(this);				
	}

	public void walking() {
		getDirection();
		
		checkCollision();
		if (!collisionOn) {
			switch (direction) {
				case "up": worldY -= speed; break;		
				case "down": worldY += speed; break;		
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
			}	
			
		}

		cycleSprites();
	}
	public void getDirection() {
		
			if (gp.keyH.upPressed) direction = "up";
			if (gp.keyH.downPressed) direction = "down";
			if (gp.keyH.leftPressed) direction = "left";
			if (gp.keyH.rightPressed) direction = "right";				
			
	}
	public void cycleSprites() {
		spriteCounter++;
		if (spriteCounter > animationSpeed && animationSpeed != 0) {
			
			if (spriteNum == 1) spriteNum = 2;
			else if (spriteNum == 2) spriteNum = 1;
			
			spriteCounter = 0;
		}
	}
	
	protected String getOppositeDirection(String direction) {
		
		String oppositeDirection = "";
		
		switch(direction) {
			case "up": 
			case "upleft": 
			case "upright": oppositeDirection = "down"; break;
			case "down": 
			case "downleft": 
			case "downright": oppositeDirection = "up"; break;
			case "left": oppositeDirection = "right"; break;
			case "right": oppositeDirection = "left"; break;
		}
		
		return oppositeDirection;
	}
	public void startDialogue(Entity entity, int setNum) {
		spriteNum = 1;
		dialogueSet = setNum;
		gp.ui.npc = entity;		
		gp.gameState = gp.dialogueState;
	}
	
	// PATH FINDING
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
	public void approachPlayer(int rate) {
		
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
	public int getGoalCol(Entity target) {
		int goalCol = (target.worldX + target.hitbox.x) / gp.tileSize;
		return goalCol;
	}
	public int getGoalRow(Entity target) {
		int goalRow = (target.worldY + target.hitbox.y) / gp.tileSize;
		return goalRow;
	}
	
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
									
			switch (direction) {
				case "up":
				case "upleft":
				case "upright":										
					if (spriteNum == 1) image = up1;
					else if (spriteNum == 2) image = up2;	
					else if (spriteNum == 3) image = up3;							
					break;
				case "down":
				case "downleft":
				case "downright":						
					if (spriteNum == 1) image = down1;
					else if (spriteNum == 2) image = down2;	
					else if (spriteNum == 3) image = down3;							
					break;
				case "left":										
					if (spriteNum == 1) image = left1;
					else if (spriteNum == 2) image = left2;	
					else if (spriteNum == 3) image = left3;						
					break;
				case "right":
					
					if (spriteNum == 1) image = right1;
					else if (spriteNum == 2) image = right2;	
					else if (spriteNum == 3) image = right3;												
					break;
			}		
		
			g2.drawImage(image, tempScreenX, tempScreenY, null);
								
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