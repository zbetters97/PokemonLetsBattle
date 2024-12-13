package entity.collectables.items;

import application.GamePanel;
import entity.Entity;
import pokemon.Pokemon;
import properties.Status;

public class ITM_Heal_Antidote extends Entity {
	
	public static final String colName = "Antidote";

	public ITM_Heal_Antidote(GamePanel gp) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Heals a poisoned\nPokémon.";
		
		pprice = 200;
		sprice = 100;
		damage = 30;
		status = Status.POISON;
	}	
	public ITM_Heal_Antidote(GamePanel gp, int amount) {		
		super(gp);	
		
		collectableType = type_item;
		name = colName;			
		description = "Heals a poisoned\nPokémon.";
		
		this.amount = amount;
		pprice = 200;
		sprice = 100;
		damage = 30;
		status = Status.POISON;
	}	
	public void getImage() {
		image1 = setup("/collectables/menu/heal_antidote", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));
	}
	
	public void use() {
		useItem("Heal a POKEMON");
	}
	
	public void apply(Entity entity, Pokemon p) {
		heal(entity, p);
	}
}