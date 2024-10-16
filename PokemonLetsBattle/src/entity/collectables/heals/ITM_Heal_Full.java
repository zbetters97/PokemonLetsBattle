package entity.collectables.heals;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Heal_Full extends Entity {
	
	public static final String colName = "Full Heal";

	public ITM_Heal_Full(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Heals all the\nstatus problems of\none Pokémon.";		
		status = null;
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