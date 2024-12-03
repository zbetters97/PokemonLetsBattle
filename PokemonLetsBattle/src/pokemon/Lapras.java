package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Lapras extends Pokemon {
	
	public Lapras(int level, Entity ball) {
		super(131, "Lapras", level, ball, 130, 85, 80, 85, 95, 60, -1, -1, 219, 2, Growth.SLOW, 45);
		
		id = Pokedex.LAPRAS;
		types = Arrays.asList(Type.WATER, Type.ICE);
		ability = Ability.SHELLARMOR;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WATERGUN), 
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(7, Moves.CONFUSERAY),
				Map.entry(10, Moves.ICESHARD),
				Map.entry(14, Moves.WATERPULSE),
				Map.entry(18, Moves.BODYSLAM),
				Map.entry(22, Moves.RAINDANCE),
				Map.entry(32, Moves.ICEBEAM),
				Map.entry(37, Moves.BRINE),
				Map.entry(49, Moves.HYDROPUMP),
				Map.entry(55, Moves.SHEERCOLD)
		);
	}
}