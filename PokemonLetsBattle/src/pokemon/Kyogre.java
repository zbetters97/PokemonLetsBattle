package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Kyogre extends Pokemon {
	
	public Kyogre(int level, Entity ball) {
		super(382, "Kyogre", level, ball, 100, 100, 90, 150, 140, 90, -1, 218, 3, Growth.SLOW, 3);
		
		id = Pokedex.KYOGRE;
		type = Type.WATER;
		ability = Ability.DRIZZLE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WATERPULSE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.SCARYFACE),
				Map.entry(15, Moves.ANCIENTPOWER),
				Map.entry(20, Moves.BODYSLAM),
				Map.entry(30, Moves.CALMMIND),
				Map.entry(35, Moves.ICEBEAM),
				Map.entry(45, Moves.HYDROPUMP),
				Map.entry(50, Moves.REST),
				Map.entry(60, Moves.SHEERCOLD),
				Map.entry(65, Moves.DOUBLEEDGE),
				Map.entry(75, Moves.AQUATAIL),
				Map.entry(80, Moves.WATERSPOUT)
		);
	}
}