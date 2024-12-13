package entity.collectables.items;

import java.util.Random;

import application.GamePanel;
import entity.Entity;

public class ITM_Rod_Super extends Entity {
	
	public static final String colName = "Super Rod";

	public ITM_Rod_Super(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_keyItem;
		usable = true;
		name = colName;			
		description = "The best fishing\nrod for catching\nwild Pokémon.";
		
		power = 2;
		pprice = 0;
		sprice = 0;
		
		setDialogue();
	}	
	public void getImage() {
		image1 = setup("/collectables/menu/rod_super", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}
	public void setDialogue() {
		dialogues[0][0] = "You can't use the " + name + "\nhere!";	
	}
	
	public void use() {		
		if (gp.gameState == gp.playState && gp.player.action == Action.IDLE &&
				gp.cChecker.checkWater(gp.player)) {
			
			gp.player.action = Action.FISHING;
			gp.player.activeItem = this;
			
			gp.ui.bagNum = 0;
			gp.ui.bagTab = gp.ui.bag_KeyItems;
			gp.ui.bagState = gp.ui.bag_Main;
			gp.ui.pauseState = gp.ui.pause_Main;
			gp.gameState = gp.playState;
		}
		else if (gp.gameState == gp.playState) {
			dialogueSet = 0;	
			startDialogue(this, dialogueSet);
		}	
		else {
			gp.ui.bagDialogue = "You can't use this here!";
			gp.ui.bagState = gp.ui.bag_Dialogue;	
		}
	}
	
	public int getLevel() {
		int level = 1;
		level = new Random().nextInt(35 - 15 + 1) + 15; 
		return level;
	}
}