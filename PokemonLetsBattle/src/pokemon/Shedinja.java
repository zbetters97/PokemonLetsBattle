package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Shedinja extends Pokemon {
	
	public Shedinja(int level, Entity ball) {
		super(292, "Shedinja", level, ball, 1, 90, 45, 30, 30, 40, -1, 95, 2, Growth.ERATIC, 45);
		
		id = Pokedex.SHEDINJA;
		types = Arrays.asList(Type.BUG, Type.GHOST);
		ability = Ability.WONDERGUARD;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.SCRATCH),
        		new Move(Moves.HARDEN),
        		new Move(Moves.LEECHLIFE),
        		new Move(Moves.SANDATTACK)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.LEECHLIFE),
				Map.entry(9, Moves.SANDATTACK),
				Map.entry(14, Moves.FURYSWIPES),
//				Map.entry(19, Moves.MINDREADER),
				Map.entry(25, Moves.SPITE),
				Map.entry(31, Moves.CONFUSERAY),
				Map.entry(38, Moves.SHADOWSNEAK),
//				Map.entry(45, Moves.GRUDGE),
//				Map.entry(52, Moves.HEALBLOCK),
				Map.entry(59, Moves.SHADOWBALL)
		);
	}
}