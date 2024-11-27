package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Revive_Max extends Entity {
	
	public static final String colName = "Max Revive";

	public ITM_Revive_Max(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Revives a fainted\nPokémon with all\nits HP.";
		
		power = 1;
		pprice = 4000;
		sprice = 2000;

		image1 = setup("/collectables/menu/revive_max", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Revive a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		revive(entity, p);
	}
}