package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Jirachi extends Pokemon {
	
	public Jirachi(int level, Entity ball) {
		super(385, "Jirachi", level, ball, 100, 100, 100, 100, 100, 100, -1, 215, 3, Growth.SLOW, 3);
		
		id = Pokedex.JIRACHI;
		types = Arrays.asList(Type.STEEL, Type.PSYCHIC);
		ability = Ability.SERENEGRACE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WISH),
        		new Move(Moves.CONFUSION)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.REST),
				Map.entry(10, Moves.SWIFT),
				Map.entry(20, Moves.PSYCHIC),
				Map.entry(25, Moves.REFRESH),
				Map.entry(30, Moves.REST),
				Map.entry(35, Moves.ZENHEADBUTT),
				Map.entry(40, Moves.DOUBLEEDGE),
//				Map.entry(45, Moves.GRAVITY),
//				Map.entry(50, Moves.HEALINGWISH),
				Map.entry(55, Moves.FUTURESIGHT),
				Map.entry(60, Moves.COSMICPOWER)
//				Map.entry(65, Moves.LASTRESORT),
//				Map.entry(70, Moves.DOOMDESIRE)
		);
	}
}