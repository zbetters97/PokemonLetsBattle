package properties.abilities;

import moves.Move;
import moves.Move.MoveType;
import properties.Status;

public class Static extends Ability {

	public Static() {
		super("Static", Category.STATUS, Status.PARALYZE, "The opponent has a 30%\nchance of being induced with\n"
				+ "PARALYZE when using a\nphysical attack.");
	}
	
	public boolean isValid(Move move) {
		
		if (move.getMType() == MoveType.PHYSICAL) {
			
			if (Math.random() < 0.30) {
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