package entity.collectables.balls;

import application.GamePanel;
import entity.Entity;

public class COL_Ball_Great extends Entity {
	
	public static final String colName = "Great Ball";

	public COL_Ball_Great(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_ball;
		name = colName;			
		description = "A good ball with a\nhigher catch rate\nthan a Poké Ball.";
		
		catchProbability = 200;
		
		image = setup("/collectables/battle/ball_great", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
		menuSprite = setup("/collectables/menu/ball_great", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {		
		throwPokeball();
	}
}