package entity.object;

import application.GamePanel;
import entity.Entity;

public class OBJ_Ledge extends Entity {
	
	public static final String objName = "Ledge";
	
	public OBJ_Ledge(GamePanel gp, int x, int y) {		
		super(gp);
		
		type = type_obstacle_i;
		worldX = x * gp.tileSize;
		worldY = y * gp.tileSize;	
		
		name = objName;
		direction = "down";
		hasShadow = false;
		
		down1 = setup("/objects/ledge_down_1"); 
	}
	
	public OBJ_Ledge(GamePanel gp, int x, int y, String direction, int tile) {		
		super(gp);
		
		type = type_obstacle_i;
		worldX = x * gp.tileSize;
		worldY = y * gp.tileSize;	
		
		name = objName;
		this.direction = direction;
		hasShadow = false;
		
		switch (direction) {
			case "up": 
				if (tile == 0) up1 = setup("/objects_interactive/ledge_up_1"); 
				else if (tile == 1) up1 = setup("/objects_interactive/ledge_up_2");
				else if (tile == 2) up1 = setup("/objects_interactive/ledge_up_3");
				break;
			case "down":
				if (tile == 0) down1 = setup("/objects_interactive/ledge_down_1"); 
				else if (tile == 1) down1 = setup("/objects_interactive/ledge_down_2");
				else if (tile == 2) down1 = setup("/objects_interactive/ledge_down_3");
				break;
			case "left":
				if (tile == 0) left1 = setup("/objects_interactive/ledge_left_1"); 
				else if (tile == 1) left1 = setup("/objects_interactive/ledge_left_2");
				else if (tile == 2) left1 = setup("/objects_interactive/ledge_left_3");
				break;
			case "right":
				if (tile == 0) right1 = setup("/objects_interactive/ledge_right_1"); 
				else if (tile == 1) right1 = setup("/objects_interactive/ledge_right_2");
				else if (tile == 2) right1 = setup("/objects_interactive/ledge_right_3");
				break;
		}
	}
	
	public void interact() {	
	
		// PLAYER MUST BE BEHIND LEDGE
		if (gp.player.direction.equals(direction)) {
			gp.playSE(gp.entity_SE, "jump");
			gp.player.jumping = true;
		}
		else {
		}
	}
}