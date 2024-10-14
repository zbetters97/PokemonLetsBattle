package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class COL_Pokeball extends Entity {
	
	public static final String colName = "Poké Ball";

	public COL_Pokeball(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "A tool for catching\nwild Pokémon.";
		
		catchProbability = 255;
		
		image = setup("/collectables/ball-poke", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
	}	
}