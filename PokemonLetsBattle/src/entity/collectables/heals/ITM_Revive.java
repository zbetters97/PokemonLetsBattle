package entity.collectables.heals;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Revive extends Entity {
	
	public static final String colName = "Revive";

	public ITM_Revive(GamePanel gp) {		
		super(gp);	
		
		name = colName;			
		description = "Revives a fainted\nPokémon with half\nits HP.";
		
		value = 2;
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