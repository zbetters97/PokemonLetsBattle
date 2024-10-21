package properties.abilities;

import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class Overgrow extends Ability {

	public Overgrow() {
		super("Overgrow", Category.ATTACK, 1.5, "When HP is below 1/3rd,\\nGrass's power increases\\nto 1.5 times.");
	}
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {
		
		if ((double) attacker.getHP() / (double) attacker.getBHP() <= 0.33 &&
				move.getType() == Type.GRASS) {
			return true;				
		}
		else {
			return false;
		}
	}
}