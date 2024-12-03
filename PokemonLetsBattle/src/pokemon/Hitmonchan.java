package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Hitmonchan extends Pokemon {
	
	public Hitmonchan(int level, Entity ball) {
		super(107, "Hitmonchan", level, ball, 50, 105, 79, 35, 110, 76, -1, -1, 140, 2, Growth.MEDIUMFAST, 45);
		
		id = Pokedex.HITMONCHAN;
		type = Type.FIGHTING;
		ability = Ability.LIMBER;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
//				new Move(Moves.REVENGE),
        		new Move(Moves.COMETPUNCH)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.AGILITY),
				Map.entry(11, Moves.PURSUIT),
				Map.entry(16, Moves.MACHPUNCH),
				Map.entry(17, Moves.BULLETPUNCH),
				Map.entry(21, Moves.FEINT),
				Map.entry(26, Moves.VACUUMWAVE),
				Map.entry(32, Moves.THUNDERPUNCH),
				Map.entry(33, Moves.ICEPUNCH),
				Map.entry(34, Moves.FIREPUNCH),
				Map.entry(36, Moves.SKYUPPERCUT),
				Map.entry(41, Moves.MEGAPUNCH),
				Map.entry(46, Moves.DETECT),
//				Map.entry(51, Moves.COUNTER),
				Map.entry(56, Moves.CLOSECOMBAT)
		);
	}
}