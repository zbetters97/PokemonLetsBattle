package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Burn extends Entity {
	
	public static final String colName = "Burn Heal";

	public ITM_Heal_Burn(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Heals a burned\nPokémon.";
		
		pprice = 200;
		sprice = 100;
		status = Status.BURN;
		
		image1 = setup("/collectables/menu/heal_burn", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		heal(entity, p);
	}
}