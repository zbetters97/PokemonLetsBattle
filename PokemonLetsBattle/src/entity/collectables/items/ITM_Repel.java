package entity.collectables.items;

import application.GamePanel;
import entity.Entity;

public class ITM_Repel extends Entity {
	
	public static final String colName = "Repel";

	public ITM_Repel(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Repels weak wild\nPokémon for 100\nsteps.";
		
		value = 100;
		
		image1 = setup("/collectables/menu/repel", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		gp.player.setRepel(value);
		removeItem(this, gp.player);
		
		gp.ui.bagDialogue = "Repel has been used!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}