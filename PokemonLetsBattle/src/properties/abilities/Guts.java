package properties.abilities;

import moves.Move;
import pokemon.Pokemon;

public class Guts extends Ability {

	public Guts() {
		super("Guts", Category.ATTACK, 1.5, "Attack raises to 1.5 times\nwhen induced with a status.");
	}	
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {	
		
		if (attacker.getStatus() != null) {
			return true;				
		}
		else {
			return false;
		}
	}
}