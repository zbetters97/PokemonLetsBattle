package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Raikou extends Pokemon {
	
	public Raikou(int level, Entity ball) {
		super(243, "Raikou", level, ball, 90, 85, 75, 115, 100, 115, -1, 216, 3, Growth.SLOW, 3);
		
		id = Pokedex.RAIKOU;
		type = Type.ELECTRIC;
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(8, Moves.THUNDERSHOCK),
				Map.entry(22, Moves.QUICKATTACK),
				Map.entry(29, Moves.SPARK),
				Map.entry(36, Moves.REFLECT),
				Map.entry(43, Moves.CRUNCH),
				Map.entry(50, Moves.THUNDERFANG),
				Map.entry(57, Moves.DISCHARGE),
				Map.entry(64, Moves.EXTRASENSORY),
				Map.entry(71, Moves.THUNDER),
				Map.entry(78, Moves.CALMMIND)
		);
	}
}