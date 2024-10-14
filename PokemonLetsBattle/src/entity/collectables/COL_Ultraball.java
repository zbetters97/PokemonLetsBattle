package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class COL_Ultraball extends Entity {
	
	public static final String colName = "Ultra Ball";

	public COL_Ultraball(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "A better ball with a\nhigher catch rate\nthan a Great Ball.";
		
		catchProbability = 150;
		
		image = setup("/collectables/ball-ultra", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
	}	
}