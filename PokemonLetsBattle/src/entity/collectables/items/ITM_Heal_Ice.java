package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Ice extends Entity {
	
	public static final String colName = "Ice Heal";

	public ITM_Heal_Ice(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Defrosts a frozen\nPokémon.";
		status = Status.FREEZE;
		
		image1 = setup("/collectables/menu/heal_ice", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
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