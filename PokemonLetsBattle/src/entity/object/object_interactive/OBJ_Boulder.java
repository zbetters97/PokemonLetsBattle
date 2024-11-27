package entity.object.object_interactive;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;

public class OBJ_Boulder extends Entity {
	
	public static final String objName = "Boulder";
	public int pushCounter = 0;
	
	public OBJ_Boulder(GamePanel gp, int worldX, int worldY) {		
		super(gp);
		this.worldX = worldX *= gp.tileSize;
		this.worldY = worldY *= gp.tileSize;
		
		type = type_obstacle_i;
		name = objName;
		direction = "down";
		collision = true;	
		speed = 1; defaultSpeed = speed;
		
		hmType = hmStrength;		
		dialogues[0][0] = "This boulder looks like it could\nbe moved by a Pokemon.";
		
		hitbox = new Rectangle(1, 1, 46, 46); 		
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		hitboxDefaultWidth = hitbox.width;
		hitboxDefaultHeight = hitbox.height;
	}	
	public void getImage() {		
		up1 = down1 = left1 = right1 = setup("/objects_interactive/boulder"); 
		up2 = down2 = left2 = right2 = setup("/objects_interactive/boulder"); 
	}
	
	public void interact() {
		gp.ui.npc = this;
		dialogueSet = 0;	
		
		startDialogue(this, dialogueSet);
	}
	
	public void useHM() {
		gp.player.action = Action.HM;
		gp.player.activeItem = this;
	}
	
	public void move(String dir) {	
		
		if (!moving) {
			
			// INCREASE SPEED TO DETECT COLLISION
			direction = dir;
			speed = gp.tileSize;			
			
			checkCollision();
							
			if (!collisionOn) {
				playSE();
				moving = true;
				speed = 1;
			}
		}
	}
	
	public void checkCollision() {
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkPlayer(this);
		gp.cChecker.checkEntity(this, gp.npc);	
		gp.cChecker.checkEntity(this, gp.iTile);		
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkObject_I(this);
	}
	
	public void update() { 
		
		if (moving) {
			pushCounter++;
			if (pushCounter <= gp.tileSize) {	
				push();
			}
			else {
				moving = false;
				pushCounter = 0;
			}
		}
	}
	
	private void push() {
		
		// MOVE IN DIRECTION PUSHED
		switch (direction) {
			case "up": worldY -= speed; break;				
			case "down": worldY += speed; break;				
			case "left": worldX -= speed; break;
			case "right": worldX += speed; break;
		}		
	}
		
	public void playSE() {
		
	}
}