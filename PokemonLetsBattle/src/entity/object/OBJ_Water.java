package entity.object;

import application.GamePanel;
import entity.Entity;

public class OBJ_Water extends Entity {
	
	public static final String objName = "Water";
	
	public OBJ_Water(GamePanel gp) {
		super(gp);
		
		type = type_obstacle_i;
		name = objName;
		hmType = hmSurf;
		
		dialogues[0][0] = "This water looks deep enough to\nbe surfed on by a Pokemon.";
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
}