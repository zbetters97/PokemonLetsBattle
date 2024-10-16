package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class ITM_Repel_Max extends Entity {
	
	public static final String colName = "Max Repel";

	public ITM_Repel_Max(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Repels weak wild\nPokémon for 250\nsteps.";
		
		value = 250;
	}	
	
	public void use() {
		gp.player.setRepel(value);
		useItem(gp.player.inventory_items, this);
		
		gp.ui.bagDialogue = "Repel has been used!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}