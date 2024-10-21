package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Awakening extends Entity {
	
	public static final String colName = "Awakening";

	public ITM_Heal_Awakening(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Awakens a sleeping\nPokémon.";
		status = Status.SLEEP;
		
		image1 = setup("/collectables/menu/heal_awakening", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
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