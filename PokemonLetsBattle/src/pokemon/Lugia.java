package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Lugia extends Pokemon {
	
	public Lugia(int level, Entity ball) {
		super(249, "Lugia", level, ball, 106, 90, 130, 90, 154, 110, -1, 220, 3, Growth.SLOW, 3);
		
		id = Pokedex.LUGIA;
		types = Arrays.asList(Type.PSYCHIC, Type.FLYING);
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(9, Moves.SAFEGUARD),
				Map.entry(15, Moves.GUST),
				Map.entry(23, Moves.RECOVER),
				Map.entry(29, Moves.HYDROPUMP),
				Map.entry(37, Moves.RAINDANCE),
				Map.entry(43, Moves.SWIFT),
//				Map.entry(51, Moves.NAUTRALGIFT),
				Map.entry(57, Moves.ANCIENTPOWER),
				Map.entry(65, Moves.EXTRASENSORY),
				Map.entry(71, Moves.PUNISHMENT),
				Map.entry(79, Moves.FUTURESIGHT),
				Map.entry(85, Moves.AEROBLAST),
				Map.entry(93, Moves.CALMMIND),
				Map.entry(99, Moves.SKYATTACK)
		);
	}
}