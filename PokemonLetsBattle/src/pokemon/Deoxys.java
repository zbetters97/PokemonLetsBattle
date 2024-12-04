package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Deoxys extends Pokemon {
	
	public Deoxys(int level, Entity ball) {
		super(386, "Deoxys", level, ball, 50, 150, 50, 150,50, 150, -1, 215, 3, Growth.SLOW, 3);
		
		id = Pokedex.DEOXYS;
		type = Type.PSYCHIC;
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.LEER),
        		new Move(Moves.WRAP)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(9, Moves.NIGHTSHADE),
				Map.entry(17, Moves.TELEPORT),
				Map.entry(25, Moves.KNOCKOFF),
				Map.entry(33, Moves.PURSUIT),
				Map.entry(41, Moves.PSYCHIC),
//				Map.entry(49, Moves.SNATCH),
				Map.entry(57, Moves.PSYCHOSHIFT),
				Map.entry(65, Moves.ZENHEADBUTT),
				Map.entry(73, Moves.COSMICPOWER),
				Map.entry(81, Moves.RECOVER),
				Map.entry(89, Moves.PSYCHOBOOST),
				Map.entry(97, Moves.HYPERBEAM)
		);
	}
}