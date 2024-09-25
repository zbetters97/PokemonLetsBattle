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
		hmType = "CUT";
		
		dialogues[0][0] = "This tree looks like it could be\ncut by a Pokemon.";
	}	
	
	public void getImage() {
		down1 = setup("/objects/tree_1");
		down2 = setup("/objects/tree_2");
		down3 = setup("/objects/tree_3");
		down4 = setup("/objects/tree_4");
	}
	
	public void interact() {
		gp.ui.npc = this;
		dialogueSet = 0;	
		
		startDialogue(this, dialogueSet);
	}
	
	public void update() {
		if (opening) {
			open();
		}
	}
	
	public void open() {
		openCounter++;
		if (openCounter < 5) { spriteNum = 2; }
		else if (5 <= openCounter && openCounter < 10) { spriteNum = 3; }
		else if (10 <= openCounter && openCounter < 15) {
			spriteNum = 4;			
		}
		else if (15 <= openCounter) {
			openCounter = 0;						
			opening = false;
			collision = false;			
			alive = false;	
		}
	}
}