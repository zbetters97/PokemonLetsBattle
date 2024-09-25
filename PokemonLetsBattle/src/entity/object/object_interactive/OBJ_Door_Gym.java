package entity.object.object_interactive;

import application.GamePanel;
import entity.Entity;

public class OBJ_Door_Gym extends Entity {
	
	public static final String objName = "Gym Door";
	
	public OBJ_Door_Gym(GamePanel gp, int worldX, int worldY) {
		super(gp);
		this.worldX = worldX *= gp.tileSize;
		this.worldY = worldY *= gp.tileSize;
		
		type = type_obstacle_i;
		name = objName;
		direction = "down";
		collision = true;
	}	
	
	public void getImage() {
		down1 = setup("/objects_interactive/door_gym_1");
		down2 = setup("/objects_interactive/door_gym_2");
		down3 = setup("/objects_interactive/door_gym_3");
		down4 = setup("/objects_interactive/door_gym_4");		
	}
	
	public void update() {
		if (opening) {
			spriteNum = 2;
			open();
		}
	}
	
	public void open() {
		openCounter++;
		if (openCounter < 5) { spriteNum = 2; }
		else if (5 <= openCounter && openCounter < 10) { spriteNum = 3; }
		else if (10 <= openCounter) {
			spriteNum = 4;
			openCounter = 0;				
			opening = false;
			collision = false;
		}
	}
	
	public void interact() {	
		if (!opening && collision) {
			playOpenSE();
			opening = true;			
		}			
	}
	
	public void playOpenSE() {
	
	}
	public void playCloseSE() {
		
	}
}