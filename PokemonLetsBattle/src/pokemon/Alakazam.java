package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Alakazam extends Pokemon {
	
	public Alakazam(int level, Entity ball) {
		super(65, "Alakazam", level, ball, 55, 50, 45, 135, 95, 120, -1, -1, 186, 3, Growth.MEDIUMSLOW, 50);
		
		id = Pokedex.ALAKAZAM;
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
				Map.entry(36, Moves.CALMMIND),
				Map.entry(40, Moves.PSYCHIC)
		);
	}
}