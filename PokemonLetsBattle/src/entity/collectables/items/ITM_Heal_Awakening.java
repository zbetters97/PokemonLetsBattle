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
		
		pprice = 200;
		sprice = 100;
		damage = 30;
		status = Status.SLEEP;
		
		image1 = setup("/collectables/menu/heal_awakening", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}	
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		heal(entity, p);
	}
}