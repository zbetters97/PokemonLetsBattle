package properties.abilities;

import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class Blaze extends Ability {

	public Blaze() {
		super("Blaze", Category.ATTACK, 1.5, "When HP is below 1/3rd,\nFire’s power increases to\n1.5 times");
	}
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {
		
		if ((double) attacker.getHP() / (double) attacker.getBHP() <= 0.33 &&
				move.getType() == Type.FIRE) {
			return true;				
		}
		else {
			return false;
		}
	}
}