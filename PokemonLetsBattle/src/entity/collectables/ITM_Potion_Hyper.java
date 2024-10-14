package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class ITM_Potion_Hyper extends Entity {
	
	public static final String colName = "Hyper Potion";

	public ITM_Potion_Hyper(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Restores the HP of\na Pokémon by 200\npoints.";
		
		value = 200;
	}	
}