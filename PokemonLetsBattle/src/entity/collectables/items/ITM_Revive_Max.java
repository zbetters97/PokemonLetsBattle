package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Revive_Max extends Entity {
	
	public static final String colName = "Max Revive";

	public ITM_Revive_Max(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Revives a fainted\nPokémon with all\nits HP.";
		
		value = 1;

		image1 = setup("/collectables/menu/revive_max", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		gp.ui.partyDialogue = "Revive a POKEMON.";
		gp.ui.partyItem = this;
		gp.ui.partyItemApply = true;
		gp.ui.partyState = gp.ui.party_Main_Select;
		gp.gameState = gp.partyState;
	}
	
	public void apply(Entity entity, Pokemon p) {
		revive(entity, p);
	}
}