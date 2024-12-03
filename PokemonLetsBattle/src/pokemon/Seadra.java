package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Seadra extends Pokemon {
	
	public Seadra(int level, Entity ball) {
		super(117, "Seadra", level, ball, 55, 65, 95, 95, 45, 85, 45, 230, 155, 2, Growth.MEDIUMFAST, 75);
		
		id = Pokedex.SEADRA;
		type = Type.WATER;
		ability = Ability.SNIPER;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WATERGUN), 
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.SMOKESCREEN),
				Map.entry(8, Moves.LEER),
				Map.entry(11, Moves.WATERGUN),
				Map.entry(18, Moves.BUBBLEBEAM),
				Map.entry(23, Moves.AGILITY),
				Map.entry(26, Moves.TWISTER),
				Map.entry(30, Moves.BRINE),
				Map.entry(40, Moves.HYDROPUMP),
				Map.entry(48, Moves.DRAGONDANCE),
				Map.entry(57, Moves.DRAGONPULSE)
		);
	}
}