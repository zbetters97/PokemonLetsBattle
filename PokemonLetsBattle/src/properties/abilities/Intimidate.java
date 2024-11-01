package properties.abilities;

import pokemon.Pokemon;

public class Intimidate extends Ability {

	public Intimidate() {
		super("Intimidate", Category.ATTRIBUTE, "Attack", 1.5, 
				"Upon entering battle, the\nopponent’s Attack lowers one\nstage.");
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