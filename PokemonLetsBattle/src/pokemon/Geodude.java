package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Geodude extends Pokemon {
	
	public Geodude(int level, Entity ball) {
		super(74, "Geodude", level, ball, 40, 80, 100, 30, 30, 20, 25, 75, 73, 1, Growth.MEDIUMSLOW, 255);
		
		id = Pokedex.GEODUDE;
		types = Arrays.asList(Type.ROCK, Type.GROUND);
		ability = Ability.ROCKHEAD;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.DEFENSECURL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(8, Moves.ROCKPOLISH),
				Map.entry(11, Moves.ROCKTHROW),
				Map.entry(15, Moves.MAGNITUDE),
				Map.entry(18, Moves.SELFDESTRUCT),
				Map.entry(22, Moves.ROLLOUT),
				Map.entry(25, Moves.ROCKBLAST),
				Map.entry(29, Moves.EARTHQUAKE),
				Map.entry(32, Moves.EXPLOSION),
				Map.entry(36, Moves.DOUBLEEDGE),
				Map.entry(39, Moves.STONEEDGE)
		);
	}
}