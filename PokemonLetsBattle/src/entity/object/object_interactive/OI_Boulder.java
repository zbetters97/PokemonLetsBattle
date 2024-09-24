package entity.object.object_interactive;

import java.awt.Rectangle;

import application.GamePanel;
import entity.Entity;

public class OI_Boulder extends Entity {
	
	public static final String obj_iName = "Boulder";
	public int pushCounter = 0;
	
	public OI_Boulder(GamePanel gp, int worldX, int worldY) {		
		super(gp);
		this.worldX = worldX *= gp.tileSize;
		this.worldY = worldY *= gp.tileSize;
		
		type = type_obstacle;
		name = obj_iName;
		direction = "down";
		collision = true;	
		speed = 1; defaultSpeed = speed;
		
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
		gp.cChecker.checkPlayer(this);
		gp.cChecker.checkTile(this);		
		gp.cChecker.checkEntity(this, gp.iTile);
		gp.cChecker.checkEntity(this, gp.obj);	
		gp.cChecker.checkEntity(this, gp.npc);		
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