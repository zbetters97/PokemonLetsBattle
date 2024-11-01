package properties.abilities;

import pokemon.Pokemon;

public class SwiftSwim extends Ability {

	public SwiftSwim() {
		super("Swift Swim", Category.ATTRIBUTE, "Speed", 1.5, 
				"When rainy, The Pokémon’s\nSpeed doubles. However,\nSpeed will not double on the\n"
				+ "turn weather becomes Heavy\nRain.");
	}	
	
	public boolean isValid(Pokemon pokemon) {	
		
		if (pokemon.getStatus() != null) {
			return true;				
		}
		else {
			return false;
		}
	}
}