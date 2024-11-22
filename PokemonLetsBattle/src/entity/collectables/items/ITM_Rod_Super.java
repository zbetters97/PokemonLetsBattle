package entity.collectables.items;

import application.GamePanel;
import entity.Entity;

public class ITM_Rod_Super extends Entity {
	
	public static final String colName = "Super Rod";

	public ITM_Rod_Super(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_keyItem;
		name = colName;			
		description = "The best fishing\nrod for catching\nwild Pokémon.";
		power = 2;
		
		image1 = setup("/collectables/menu/rod_super", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {		
		if (!gp.btlManager.active && gp.player.action == Action.IDLE &&
				gp.cChecker.checkWater(gp.player)) {
			
			gp.player.action = Action.FISHING;
			gp.player.activeItem = this;
			
			gp.ui.bagNum = 0;
			gp.ui.bagTab = gp.ui.bag_KeyItems;
			gp.ui.bagState = gp.ui.bag_Main;
			gp.ui.pauseState = gp.ui.pause_Main;
			gp.gameState = gp.playState;
		}
		else {
			gp.ui.bagDialogue = "You can't use this here!";
			gp.ui.bagState = gp.ui.bag_Dialogue;	
		}
	}
	
	public void getLevel() {
		
	}
}