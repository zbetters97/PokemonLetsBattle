package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Groudon extends Pokemon {
	
	public Groudon(int level, Entity ball) {
		super(383, "Groudon", level, ball, 100, 150, 140, 100, 90, 90, -1, 218, 3, Growth.SLOW, 3);
		
		id = Pokedex.GROUDON;
		type = Type.GROUND;
		ability = Ability.DROUGHT;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.MUDSHOT)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.SCARYFACE),
				Map.entry(15, Moves.ANCIENTPOWER),
				Map.entry(20, Moves.SLASH),
				Map.entry(30, Moves.BULKUP),
				Map.entry(35, Moves.EARTHQUAKE),
				Map.entry(45, Moves.FIREBLAST),
				Map.entry(50, Moves.REST),
				Map.entry(60, Moves.FISSURE),
				Map.entry(65, Moves.SOLARBEAM),
				Map.entry(75, Moves.EARTHPOWER),
				Map.entry(80, Moves.ERUPTION)
		);
	}
}