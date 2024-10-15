package entity.collectables.heals;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Ice extends Entity {
	
	public static final String colName = "Ice Heal";

	public ITM_Heal_Ice(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Defrosts a frozen\nPokémon.";
		status = Status.FREEZE;
	}	
	
	public void use() {
		gp.ui.partyDialogue = "Heal a POKEMON.";
		gp.ui.partyItem = this;
		gp.ui.partyState = gp.ui.party_Main_Select;
		gp.gameState = gp.partyState;
	}
	
	public void apply(Entity entity, Pokemon p) {
		heal(entity, p);
	}
}