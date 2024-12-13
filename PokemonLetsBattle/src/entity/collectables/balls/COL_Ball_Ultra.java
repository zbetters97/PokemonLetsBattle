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
		
		pprice = 800;
		sprice = 400;
		catchProbability = 150;		
	}	
	public COL_Ball_Ultra(GamePanel gp, int amount) {		
		super(gp);	

		collectableType = type_ball;
		name = colName;			
		description = "A better ball with a\nhigher catch rate\nthan a Great Ball.";
		
		this.amount = amount;
		pprice = 800;
		sprice = 400;
		catchProbability = 150;		
	}	
	public void getImage() {
		image1 = setup("/collectables/menu/ball_ultra", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));		
		image2 = setup("/collectables/menu/ball_ultra", gp.tileSize, gp.tileSize);		
		image3 = setup("/collectables/battle/ball_ultra", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
	}
	
	public void use() {		
		throwBall();
	}
}