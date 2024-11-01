package properties.abilities;

import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class FlashFire extends Ability {

	public FlashFire() {
		super("Flash Fire", Category.ATTACK, 1.5, 
				"Activates when user is hit\nby a damaging Fire-type\nmove. Once activated, user’s\nFire-type moves deal 1.5\ntimes damage.");
	}
		
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {
		
		if ((double) attacker.getHP() / (double) attacker.getBHP() <= 0.33 &&
				move.getType() == Type.WATER) {
			return true;				
		}
		else {
			return false;
		}
	}
}