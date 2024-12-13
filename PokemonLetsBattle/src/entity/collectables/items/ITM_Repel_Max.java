package entity.collectables.items;

import application.GamePanel;
import entity.Entity;

public class ITM_Repel_Max extends Entity {
	
	public static final String colName = "Max Repel";

	public ITM_Repel_Max(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Repels weak wild\nPokémon for 250\nsteps.";
		
		power = 250;
		pprice = 700;
		sprice = 350;
		damage = 30;
	}
	public ITM_Repel_Max(GamePanel gp, int amount) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Repels weak wild\nPokémon for 250\nsteps.";
		
		this.amount = amount;
		power = 250;
		pprice = 700;
		sprice = 350;
		damage = 30;
	}
	public void getImage() {
		image1 = setup("/collectables/menu/repel_max", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}
	
	public void use() {
		gp.player.setRepel(power);
		removeItem(this, gp.player);
		
		gp.ui.bagDialogue = "Repel has been used!";
		gp.ui.bagState = gp.ui.bag_Dialogue;
	}
}