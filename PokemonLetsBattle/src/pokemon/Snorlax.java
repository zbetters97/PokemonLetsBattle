package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Snorlax extends Pokemon {
	
	public Snorlax(int level, Entity ball) {
		super(143, "Snorlax", level, ball, 160, 110, 65, 65, 110, 30, -1, 154, 2, Growth.SLOW, 25);
		
		id = Pokedex.SNORLAX;
		type = Type.NORMAL;
		ability = Ability.THICKFAT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.DEFENSECURL),
				Map.entry(9, Moves.AMNESIA),
				Map.entry(12, Moves.LICK),
				Map.entry(20, Moves.YAWN),
				Map.entry(25, Moves.REST),
				Map.entry(28, Moves.SNORE),
				Map.entry(29, Moves.SLEEPTALK),
				Map.entry(33, Moves.BODYSLAM),
				Map.entry(41, Moves.ROLLOUT),
				Map.entry(44, Moves.CRUNCH),
				Map.entry(49, Moves.GIGAIMPACT)
		);
	}
}