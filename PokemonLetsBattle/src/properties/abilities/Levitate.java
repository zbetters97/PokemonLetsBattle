package properties.abilities;

import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class Levitate extends Ability {

	public Levitate() {
		super("Levitate", Category.DEFENSE, 0.0, "Damage dealing Ground-type\nmoves have no effect.");
	}
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {
		
		if (move.getType() == Type.GROUND) {
			return true;				
		}
		else {
			return false;
		}
	}
}