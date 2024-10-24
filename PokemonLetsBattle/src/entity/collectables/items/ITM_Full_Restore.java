package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Full_Restore extends Entity {
	
	public static final String colName = "Full Restore";

	public ITM_Full_Restore(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Fully restores the\nHP and status of\na Pokémon.";
		
		image1 = setup("/collectables/menu/restore_full", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		gp.ui.partyDialogue = "Restore a POKEMON.";
		gp.ui.partyItem = this;
		gp.ui.partyItemApply = true;
		gp.ui.partyState = gp.ui.party_Main_Select;
		gp.gameState = gp.partyState;
	}
	
	public void apply(Entity entity, Pokemon p) {
		
		if (p.isAlive() && (p.getHP() < p.getBHP() || p.getStatus() != null)) {
						
			gp.playSE(6, "heal");
			p.setHP(p.getBHP());
			p.setStatus(null);					
			entity.useItem(this, entity);
			
			gp.ui.partyDialogue = p.getName() + " was fully restored.";
			gp.ui.partyState = gp.ui.party_Main_Dialogue;
		}		
		else {
			gp.keyH.playErrorSE();
		}
	}
}