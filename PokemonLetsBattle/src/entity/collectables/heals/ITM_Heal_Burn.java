package entity.collectables.heals;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Burn extends Entity {
	
	public static final String colName = "Burn Heal";

	public ITM_Heal_Burn(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Heals a burned\nPokémon.";
		status = Status.BURN;
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