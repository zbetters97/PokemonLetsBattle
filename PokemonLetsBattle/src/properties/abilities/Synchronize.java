package properties.abilities;

import moves.Move;
import moves.Move.MoveType;
import properties.Status;

public class Synchronize extends Ability {

	public Synchronize() {
		super("Synchronize", Category.STATUS, Status.PARALYZE, 
				"When this Pokémon becomes\nPOISON, PARALYZE, or BURN,\nso does the opponent.");
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