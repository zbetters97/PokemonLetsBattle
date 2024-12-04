package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Mew extends Pokemon {
	
	public Mew(int level, Entity ball) {
		super(151, "Mew", level, ball, 100, 100, 100, 100, 100, 100, -1, 64, 5, Growth.SLOW, 45);
		
		id = Pokedex.MEW;
		type = Type.PSYCHIC;
		ability = Ability.SYNCHRONIZE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.POUND)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(10, Moves.MEGAPUNCH),
				Map.entry(20, Moves.METRONOME),
				Map.entry(30, Moves.PSYCHIC),
				Map.entry(40, Moves.BARRIER),
				Map.entry(50, Moves.ANCIENTPOWER),
				Map.entry(60, Moves.AMNESIA),
				Map.entry(90, Moves.NASTYPLOT),
				Map.entry(100, Moves.AURASPHERE)
		);
	}
}