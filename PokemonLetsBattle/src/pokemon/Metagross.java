package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Metagross extends Pokemon {
	
	public Metagross(int level, Entity ball) {
		super(376, "Metagross", level, ball, 80, 135, 130, 95, 90, 70, -1, 210, 3, Growth.SLOW, 3);
		
		id = Pokedex.METAGROSS;
		types = Arrays.asList(Type.STEEL, Type.PSYCHIC);
		ability = Ability.CLEARBODY;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
//       		new Move(Moves.MAGNETRISE),
        		new Move(Moves.TAKEDOWN),
        		new Move(Moves.METALCLAW),
        		new Move(Moves.CONFUSION)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(20, Moves.METALCLAW),
				Map.entry(21, Moves.CONFUSION),
				Map.entry(24, Moves.SCARYFACE),
				Map.entry(28, Moves.PURSUIT),
				Map.entry(32, Moves.BULLETPUNCH),
				Map.entry(36, Moves.PSYCHIC),
				Map.entry(40, Moves.IRONDEFENSE),
				Map.entry(44, Moves.AGILITY),
				Map.entry(45, Moves.HAMMERARM),
				Map.entry(53, Moves.METEORMASH),
				Map.entry(62, Moves.ZENHEADBUTT),
				Map.entry(71, Moves.HYPERBEAM)
		);
	}
}