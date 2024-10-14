package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class COL_Greatball extends Entity {
	
	public static final String colName = "Great Ball";

	public COL_Greatball(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "A good ball with a\nhigher catch rate\nthan a Poké Ball.";
		
		catchProbability = 200;
		
		image = setup("/collectables/ball-great", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
	}	
}