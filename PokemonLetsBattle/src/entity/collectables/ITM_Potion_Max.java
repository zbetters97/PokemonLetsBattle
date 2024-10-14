package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class ITM_Potion_Max extends Entity {
	
	public static final String colName = "Max Potion";

	public ITM_Potion_Max(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Fully restores the\nHP of a Pokémon.";
		
		value = 999;
	}	
}