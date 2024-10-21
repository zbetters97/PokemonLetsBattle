package properties.abilities;

import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class Torrent extends Ability {

	public Torrent() {
		super("Torrent", Category.ATTACK, 1.5, "When HP is below 1/3rd,\nWater's power increases to\n1.5 times");
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