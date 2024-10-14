package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class ITM_Potion_Super extends Entity {
	
	public static final String colName = "Super Potion";

	public ITM_Potion_Super(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Restores the HP of\na Pokémon by 50\npoints.";
		
		value = 50;
	}	
}