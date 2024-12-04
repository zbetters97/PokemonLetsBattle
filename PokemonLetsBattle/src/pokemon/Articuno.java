package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Articuno extends Pokemon {
	
	public Articuno(int level, Entity ball) {
		super(144, "Articuno", level, ball, 90, 85, 100, 95, 125, 85, -1, 215, 3, Growth.SLOW, 3);
		
		id = Pokedex.ARTICUNO;
		types = Arrays.asList(Type.FLYING, Type.ICE);
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.GUST),
        		new Move(Moves.POWDERSNOW)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(8, Moves.MIST),
				Map.entry(15, Moves.ICESHARD),
//				Map.entry(22, Moves.MINDREADER),
				Map.entry(29, Moves.ANCIENTPOWER),
				Map.entry(36, Moves.AGILITY),
				Map.entry(43, Moves.ICEBEAM),
				Map.entry(50, Moves.REFLECT),
				Map.entry(57, Moves.ROOST),
//				Map.entry(64, Moves.TAILWIND),
				Map.entry(71, Moves.BLIZZARD),
				Map.entry(78, Moves.SHEERCOLD),
				Map.entry(85, Moves.HAIL)
		);
	}
}