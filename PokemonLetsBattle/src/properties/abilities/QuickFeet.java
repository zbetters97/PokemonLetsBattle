package properties.abilities;

import pokemon.Pokemon;

public class QuickFeet extends Ability {

	public QuickFeet() {
		super("Quick Feet", Category.ATTRIBUTE, "Speed", 1.5, "Speed raises to 1.5 times\nwhen induced with a status.");
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