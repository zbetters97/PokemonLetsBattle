package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class ITM_Potion extends Entity {
	
	public static final String colName = "Potion";

	public ITM_Potion(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Restores the HP of\na Pokémon by 20\npoints.";
		
		value = 20;
	}	
}