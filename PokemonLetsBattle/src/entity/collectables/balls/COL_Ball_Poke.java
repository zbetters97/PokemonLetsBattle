package entity.collectables.balls;

import application.GamePanel;
import entity.Entity;

public class COL_Ball_Poke extends Entity {
	
	public static final String colName = "Poké Ball";

	public COL_Ball_Poke(GamePanel gp) {		
		super(gp);	

		collectableType = type_ball;
		name = colName;			
		description = "A tool for catching\nwild Pokémon.";
		
		catchProbability = 255;
		
		image = setup("/collectables/battle/ball_poke", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
		menuSprite = setup("/collectables/menu/ball_poke", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {		
		throwPokeball();
	}
}