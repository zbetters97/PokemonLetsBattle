package entity.collectables;

import application.GamePanel;
import entity.Entity;

public class COL_Masterball extends Entity {
	
	public static final String colName = "Master Ball";

	public COL_Masterball(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "The best ball\ncatches a Pokémon\nwithout fail.";
		
		catchProbability = 0;
		
		image = setup("/collectables/ball-master", (int) (gp.tileSize * 0.8), (int) (gp.tileSize * 0.8));
	}	
}