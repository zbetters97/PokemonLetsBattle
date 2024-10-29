package entity.collectables.items;

import application.GamePanel;
import entity.Entity;

public class ITM_Repel_Super extends Entity {
	
	public static final String colName = "Super Repel";

	public ITM_Repel_Super(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Repels weak wild\nPokémon for 200\nsteps.";
		
		value = 200;

		image1 = setup("/collectables/menu/repel_super", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));		
	}	
	
	public void use() {
		gp.player.setRepel(100);
		removeItem(this, gp.player);
		
		gp.ui.bagDialogue = "Repel has been used!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}