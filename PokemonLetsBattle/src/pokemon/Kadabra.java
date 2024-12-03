package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Kadabra extends Pokemon {
	
	public Kadabra(int level, Entity ball) {
		super(64, "Kadabra", level, ball, 40, 35, 30, 120, 70, 105, 36, 65, 145, 2, Growth.MEDIUMSLOW, 100);
		
		id = Pokedex.KADABRA;
		type = Type.PSYCHIC;
		ability = Ability.INNERFOCUS;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.CONFUSION),
        		new Move(Moves.KINESIS), 
        		new Move(Moves.TELEPORT)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(16, Moves.CONFUSION),
				Map.entry(24, Moves.PSYBEAM),
				Map.entry(30, Moves.RECOVER),
				Map.entry(34, Moves.PSYCHOCUT),
				Map.entry(40, Moves.PSYCHIC)
		);
	}
}