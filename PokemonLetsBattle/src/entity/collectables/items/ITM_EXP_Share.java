package entity.collectables.items;

import application.GamePanel;
import entity.Entity;

public class ITM_EXP_Share extends Entity {
	
	public static final String colName = "EXP. Share";

	public ITM_EXP_Share(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_keyItem;
		name = colName;			
		description = "A hold item that\ngets EXP. points\nfrom battles.";
		
		pprice = 0;
		sprice = 0;
		
		image1 = setup("/collectables/menu/exp_share", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		gp.ui.bagDialogue = "You can't use this here!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}