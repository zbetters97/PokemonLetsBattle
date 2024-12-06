package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Potion_Hyper extends Entity {
	
	public static final String colName = "Hyper Potion";

	public ITM_Potion_Hyper(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Restores the HP of\na Pokémon by 200\npoints.";
		
		power = 200;
		pprice = 1500;
		sprice = 750;
		damage = 30;
		
		image1 = setup("/collectables/menu/potion_hyper", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		restore(entity, p);
	}
}