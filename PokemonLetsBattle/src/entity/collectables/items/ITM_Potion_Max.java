package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Potion_Max extends Entity {
	
	public static final String colName = "Max Potion";

	public ITM_Potion_Max(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Fully restores the\nHP of a Pokémon.";
		
		value = 999;
		
		image1 = setup("/collectables/menu/potion_max", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		restore(entity, p);
	}
}