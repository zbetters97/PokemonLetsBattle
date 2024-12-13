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
		
		pprice = 200;
		sprice = 100;
		catchProbability = 255;
	}
	public COL_Ball_Poke(GamePanel gp, int amount) {		
		super(gp);	

		collectableType = type_ball;
		name = colName;			
		description = "A tool for catching\nwild Pokémon.";
		
		this.amount = amount;
		pprice = 200;
		sprice = 100;
		catchProbability = 255;
	}
	public void getImage() {
		image1 = setup("/collectables/menu/ball_poke", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));		
		image2 = setup("/collectables/menu/ball_poke", gp.tileSize, gp.tileSize);		
		image3 = setup("/collectables/battle/ball_poke", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));	
	}
	
	public void use() {		
		throwBall();
	}
}