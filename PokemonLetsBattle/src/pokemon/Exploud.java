package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Exploud extends Pokemon {
	
	public Exploud(int level, Entity ball) {
		super(295, "Exploud", level, ball, 104, 91, 63, 91, 63, 68, -1, 184, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.EXPLOUD;
		type = Type.NORMAL;
		ability = Ability.SOUNDPROOF;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.THUNDERFANG),
        		new Move(Moves.FIREFANG),
        		new Move(Moves.ICEFANG),
        		new Move(Moves.POUND)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(11, Moves.ASTONISH),
				Map.entry(15, Moves.HOWL),
				Map.entry(20, Moves.BITE),
				Map.entry(23, Moves.SUPERSONIC),
				Map.entry(29, Moves.STOMP),
				Map.entry(37, Moves.SCREECH),
				Map.entry(40, Moves.CRUNCH),
				Map.entry(55, Moves.REST),
				Map.entry(56, Moves.SLEEPTALK),
				Map.entry(63, Moves.HYPERVOICE),
				Map.entry(71, Moves.HYPERBEAM)
		);
	}
}