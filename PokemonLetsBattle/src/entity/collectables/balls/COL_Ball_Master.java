package entity.collectables.balls;

import application.GamePanel;
import entity.Entity;

public class COL_Ball_Master extends Entity {
	
	public static final String colName = "Master Ball";

	public COL_Ball_Master(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_ball;
		name = colName;			
		description = "The best ball\ncatches a Pokémon\nwithout fail.";
		
		catchProbability = 0;
		
		image1 = setup("/collectables/menu/ball_master", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));		
		image2 = setup("/collectables/menu/ball_master", gp.tileSize, gp.tileSize);		
		image3 = setup("/collectables/battle/ball_master", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));	
	}	
	
	public void use() {		
		throwBall();
	}
}