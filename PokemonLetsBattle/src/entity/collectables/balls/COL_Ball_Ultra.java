package entity.collectables.balls;

import application.GamePanel;
import entity.Entity;

public class COL_Ball_Ultra extends Entity {
	
	public static final String colName = "Ultra Ball";

	public COL_Ball_Ultra(GamePanel gp) {		
		super(gp);	

		collectableType = type_ball;
		name = colName;			
		description = "A better ball with a\nhigher catch rate\nthan a Great Ball.";
		
		catchProbability = 150;
		
		image = setup("/collectables/battle/ball_ultra", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
		menuSprite = setup("/collectables/menu/ball_ultra", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {		
		throwPokeball();
	}
}