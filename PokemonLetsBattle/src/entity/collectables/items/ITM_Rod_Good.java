package entity.collectables.items;

import java.util.Random;

import application.GamePanel;
import entity.Entity;

public class ITM_Rod_Good extends Entity {
	
	public static final String colName = "Good Rod";

	public ITM_Rod_Good(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_keyItem;
		name = colName;			
		description = "A decent fishing\nrod for catching\nwild Pokémon.";
		
		power = 1;
		pprice = 0;
		sprice = 0;
		
		image1 = setup("/collectables/menu/rod_Good", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
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
	
	public int getLevel() {
		int level = 1;
		level = new Random().nextInt(15 - 5 + 1) + 5; 
		return level;
	}
}