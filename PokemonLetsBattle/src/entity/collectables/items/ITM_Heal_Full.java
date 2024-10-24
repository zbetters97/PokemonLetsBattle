package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Heal_Full extends Entity {
	
	public static final String colName = "Full Heal";

	public ITM_Heal_Full(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Heals all the\nstatus problems of\none Pokémon.";		
		status = null;
		
		image1 = setup("/collectables/menu/heal_full", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		gp.ui.partyDialogue = "Heal a POKEMON.";
		gp.ui.partyItem = this;
		gp.ui.partyItemApply = true;
		gp.ui.partyState = gp.ui.party_Main_Select;
		gp.gameState = gp.partyState;
	}
	
	public void apply(Entity entity, Pokemon p) {
		heal(entity, p);
	}
}