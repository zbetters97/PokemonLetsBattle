package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class ITM_Repel_Super extends Entity {
	
	public static final String colName = "Super Repel";

	public ITM_Repel_Super(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Repels weak wild\nPokémon for 200\nsteps.";
		
		value = 200;
	}	
	
	public void use() {
		gp.player.setRepel(100);
		useItem(gp.player.inventory_items, this);
		
		gp.ui.bagDialogue = "Repel has been used!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}