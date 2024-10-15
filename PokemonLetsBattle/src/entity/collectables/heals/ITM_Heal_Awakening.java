package entity.collectables.heals;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Awakening extends Entity {
	
	public static final String colName = "Awakening";

	public ITM_Heal_Awakening(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Awakens a sleeping\nPokémon.";
		status = Status.SLEEP;
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