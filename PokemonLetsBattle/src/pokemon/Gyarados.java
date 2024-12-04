package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Gyarados extends Pokemon {
	
	public Gyarados(int level, Entity ball) {
		super(130, "Gyarados", level, ball, 95, 125, 79, 60, 100, 81, -1, 214, 2, Growth.SLOW, 45);
		
		id = Pokedex.GYARADOS;
		types = Arrays.asList(Type.WATER, Type.DRAGON);
		ability = Ability.INTIMIDATE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.THRASH)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(20, Moves.BITE),
				Map.entry(23, Moves.DRAGONRAGE),
				Map.entry(26, Moves.LEER),
				Map.entry(29, Moves.TWISTER),
				Map.entry(32, Moves.ICEFANG),
				Map.entry(35, Moves.AQUATAIL),
				Map.entry(38, Moves.RAINDANCE),
				Map.entry(41, Moves.HYDROPUMP),
				Map.entry(44, Moves.DRAGONDANCE),
				Map.entry(47, Moves.HYPERBEAM)
		);
	}
}