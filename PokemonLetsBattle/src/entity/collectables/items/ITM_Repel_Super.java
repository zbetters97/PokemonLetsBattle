package entity.collectables.items;

import application.GamePanel;
import entity.Entity;

public class ITM_Repel_Super extends Entity {
	
	public static final String colName = "Super Repel";

	public ITM_Repel_Super(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Repels weak wild\nPokémon for 200\nsteps.";
		
		power = 200;
		pprice = 500;
		sprice = 250;
		damage = 30;
	}	
	public ITM_Repel_Super(GamePanel gp, int amount) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Repels weak wild\nPokémon for 200\nsteps.";
		
		this.amount = amount;
		power = 200;
		pprice = 500;
		sprice = 250;
		damage = 30;
	}	
	public void getImage() {
		image1 = setup("/collectables/menu/repel_super", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));		
	}
	
	public void use() {
		gp.player.setRepel(power);
		removeItem(this, gp.player);
		
		gp.ui.bagDialogue = "Repel has been used!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}