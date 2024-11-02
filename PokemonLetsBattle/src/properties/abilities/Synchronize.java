package properties.abilities;

import pokemon.Pokemon;
import properties.Status;

public class Synchronize extends Ability {

	public Synchronize() {
		super("Synchronize", Category.STATUS, Status.PARALYZE, 
				"When this Pokémon becomes\nPOISON, PARALYZE, or BURN,\nso does the opponent.");
	}
	
	public boolean isValid(Pokemon pokemon) {		
		if (pokemon.hasStatus(Status.POISON) || pokemon.hasStatus(Status.PARALYZE) || pokemon.hasStatus(Status.BURN)) {
			return true;
		}
		else {
			return false;
		}
	}
}