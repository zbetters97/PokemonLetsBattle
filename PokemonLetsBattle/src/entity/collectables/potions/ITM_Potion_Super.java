package entity.collectables.potions;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Potion_Super extends Entity {
	
	public static final String colName = "Super Potion";

	public ITM_Potion_Super(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Restores the HP of\na Pokémon by 50\npoints.";
		
		value = 50;
	}	
	
	public void use() {
		gp.ui.partyDialogue = "Heal a POKEMON.";
		gp.ui.partyItem = this;
		gp.ui.partyState = gp.ui.party_Main_Select;
		gp.gameState = gp.partyState;
	}
	
	public void apply(Entity entity, Pokemon p) {
		restore(entity, p);
	}
}