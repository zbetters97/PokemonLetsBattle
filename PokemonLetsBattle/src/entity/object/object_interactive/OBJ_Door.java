package entity.object.object_interactive;

import application.GamePanel;
import entity.Entity;

public class OBJ_Door extends Entity {
	
	public static final String objName = "Door";
	
	public OBJ_Door(GamePanel gp, int worldX, int worldY, int door) {
		super(gp);
		this.worldX = worldX *= gp.tileSize;
		this.worldY = worldY *= gp.tileSize;
		
		type = type_obstacle_i;
		name = objName;
		direction = "down";
		collision = true;
		
		if (door == 0) getStoreImage();
		else if (door == 1) getGymImage();
	}	
	
	public void getStoreImage() {
		down1 = setup("/objects_interactive/door_store_1");
		down2 = setup("/objects_interactive/door_store_2");
		down3 = setup("/objects_interactive/door_store_3");	
	}
	public void getGymImage() {
		down1 = setup("/objects_interactive/door_gym_1");
		down2 = setup("/objects_interactive/door_gym_2");
		down3 = setup("/objects_interactive/door_gym_3");	
	}
	
	public void update() {
		if (opening) {
			open();
		}
	}
	
	public void open() {
		
		openCounter++;
		if (openCounter < 5) { 
			spriteNum = 2; 
		}
		else if (5 <= openCounter && openCounter < 10) { 
			spriteNum = 3; 
		}
		else if (10 <= openCounter) {
			openCounter = 0;
			opening = false;
			alive = false;
		}
	}
	
	public void interact() {	
		if (!opening && collision) {
			playOpenSE();
			opening = true;		
			collision = false;
		}				
	}
	
	public void playOpenSE() {
	
	}
	public void playCloseSE() {
		
	}
}