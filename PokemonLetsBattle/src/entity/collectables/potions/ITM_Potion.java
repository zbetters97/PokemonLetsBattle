package entity.collectables.potions;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Potion extends Entity {
	
	public static final String colName = "Potion";

	public ITM_Potion(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Restores the HP of\na Pokémon by 20\npoints.";
		
		value = 20;
	}	
	
	public void use() {
		gp.ui.partyDialogue = "Heal a POKEMON.";
		gp.ui.partyItem = this;
		gp.ui.partyItemApply = true;
		gp.ui.partyState = gp.ui.party_Main_Select;
		gp.gameState = gp.partyState;
	}
	
	public void apply(Entity entity, Pokemon p) {
		restore(entity, p);
	}
}