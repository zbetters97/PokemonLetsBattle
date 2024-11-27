package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Potion extends Entity {
	
	public static final String colName = "Potion";

	public ITM_Potion(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Restores the HP of\na Pokémon by 20\npoints.";
		
		power = 20;
		pprice = 200;
		sprice = 100;
		
		image1 = setup("/collectables/menu/potion", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		restore(entity, p);
	}
}