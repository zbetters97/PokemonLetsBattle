package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Hitmonlee extends Pokemon {
	
	public Hitmonlee(int level, Entity ball) {
		super(106, "Hitmonlee", level, ball, 50, 120, 53, 35, 110, 87, -1, 139, 2, Growth.MEDIUMFAST, 45);
		
		id = Pokedex.HITMONLEE;
		type = Type.FIGHTING;
		ability = Ability.KEENEYE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
//       		new Move(Moves.REVENGE),
        		new Move(Moves.DOUBLEKICK)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.MEDITATE),
				Map.entry(9, Moves.ROLLINGKICK),
				Map.entry(13, Moves.JUMPKICK),
				Map.entry(17, Moves.BRICKBREAK),
//				Map.entry(21, Moves.FOCUSENERGY),
				Map.entry(25, Moves.FEINT),
				Map.entry(29, Moves.HIGHJUMPKICK),
//				Map.entry(33, Moves.MINDREADER),
				Map.entry(37, Moves.FORESIGHT),
				Map.entry(41, Moves.BLAZEKICK),
				Map.entry(45, Moves.ENDURE),
				Map.entry(49, Moves.MEGAKICK),
				Map.entry(53, Moves.CLOSECOMBAT),
				Map.entry(57, Moves.REVERSAL)
		);
	}
}