package properties.abilities;

import java.util.Arrays;
import java.util.List;

import moves.Move;
import moves.Moves;
import pokemon.Pokemon;

public class SoundProof extends Ability {

	private static List<Moves> soundMoves = Arrays.asList(
			Moves.ASTONISH,
			Moves.GROWL,
			Moves.HYPERBEAM,
			Moves.SCREECH,
			Moves.SUPERSONIC
	);
	
	public SoundProof() {
		super("Sound Proof", Category.DEFENSE, 0.0, "Unaffected by sound moves.");
	}	
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {	
		
		if (soundMoves.contains(move.getMove())) {
			return true;
		}
		else {
			return false;
		}
	}
}