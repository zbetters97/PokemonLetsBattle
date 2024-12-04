package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Kingdra extends Pokemon {
	
	public Kingdra(int level, Entity ball) {
		super(230, "Kingdra", level, ball, 75, 95, 95, 95, 95, 85, -1, 207, 3, Growth.MEDIUMFAST, 45);
		
		id = Pokedex.KINGDRA;
		types = Arrays.asList(Type.WATER, Type.DRAGON);
		ability = Ability.SNIPER;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WATERGUN), 
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.YAWN),
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