package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;

public class ITM_Revive extends Entity {
	
	public static final String colName = "Revive";

	public ITM_Revive(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Revives a fainted\nPokémon with half\nits HP.";
		
		power = 2;
		pprice = 2000;
		sprice = 1000;
		damage = 30;
		
		image1 = setup("/collectables/menu/revive", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Revive a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		revive(entity, p);
	}
}