package properties.abilities;

import java.util.Random;

import moves.Move;
import moves.Move.MoveType;
import pokemon.Pokemon;
import properties.Status;

public class Static extends Ability {

	public Static() {
		super("Static", Category.STATUS, Status.PARALYZE, "The opponent has a 30%\nchance of being induced with\n"
				+ "PARALYZE when using a\nphysical attack.");
	}
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {
		
		if (move.getMType() == MoveType.PHYSICAL) {
			
			int chance = new Random().nextInt(10);
			chance = 2;
			if (chance >= 1) {
				return true;
			}
			else {
				return false;
			}	
		}
		else {
			return false;
		}
	}
}