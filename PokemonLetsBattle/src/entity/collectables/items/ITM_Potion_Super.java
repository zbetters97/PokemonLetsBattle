package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Potion_Super extends Entity {
	
	public static final String colName = "Super Potion";

	public ITM_Potion_Super(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Restores the HP of\na Pokémon by 50\npoints.";
		
		power = 50;
		pprice = 700;
		sprice = 350;
		damage = 30;
		
		image1 = setup("/collectables/menu/potion_super", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		restore(entity, p);
	}
}