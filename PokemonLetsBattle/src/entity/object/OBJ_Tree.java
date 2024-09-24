package entity.object;

import application.GamePanel;
import entity.Entity;

public class OBJ_Tree extends Entity {
	
	public static final String objName = "Tree";
	
	public OBJ_Tree(GamePanel gp, int worldX, int worldY) {
		super(gp);
		this.worldX = worldX *= gp.tileSize;
		this.worldY = worldY *= gp.tileSize;
		
		type = type_obstacle_i;
		name = objName;
		collision = true;
	}	
	
	public void getImage() {
		down1 = setup("/objects/tree_1");
		down2 = setup("/objects/tree_2");
		down3 = setup("/objects/tree_3");
		down4 = setup("/objects/tree_4");
	}
	
	public void interact() {
		System.out.println("called");
	}
}