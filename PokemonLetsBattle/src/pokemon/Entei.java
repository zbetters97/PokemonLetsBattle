package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Entei extends Pokemon {
	
	public Entei(int level, Entity ball) {
		super(244, "Entei", level, ball, 115, 115, 85, 90, 75, 100, -1, 217, 3, Growth.SLOW, 3);
		
		id = Pokedex.ENTEI;
		type = Type.FIRE;
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(8, Moves.EMBER),
				Map.entry(22, Moves.FIRESPIN),
				Map.entry(29, Moves.STOMP),
				Map.entry(36, Moves.FLAMETHROWER),
				Map.entry(43, Moves.SWAGGER),
				Map.entry(50, Moves.FIREFANG),
				Map.entry(57, Moves.LAVAPLUME),
				Map.entry(64, Moves.EXTRASENSORY),
				Map.entry(71, Moves.FIREBLAST),
				Map.entry(78, Moves.CALMMIND)
		);
	}
}