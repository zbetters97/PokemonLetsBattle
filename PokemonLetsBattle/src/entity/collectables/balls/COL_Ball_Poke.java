package entity.collectables.balls;

import application.GamePanel;
import entity.Entity;

public class COL_Ball_Poke extends Entity {
	
	public static final String colName = "Poké Ball";

	public COL_Ball_Poke(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "A tool for catching\nwild Pokémon.";
		
		catchProbability = 255;
		
		image = setup("/collectables/ball-poke", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
	}	
	
	public void use() {		
		throwPokeball();
	}
}