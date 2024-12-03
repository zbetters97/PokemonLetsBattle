package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Abra extends Pokemon {
	
	public Abra(int level, Entity ball) {
		super(63, "Abra", level, ball, 25, 20, 15, 105, 55, 90, 16, 64, 75, 1, Growth.MEDIUMSLOW, 200);
		
		id = Pokedex.ABRA;
		type = Type.PSYCHIC;
		ability = Ability.INNERFOCUS;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TELEPORT)
		));
		
		moveLevels = Map.ofEntries(
				
		);
	}
}