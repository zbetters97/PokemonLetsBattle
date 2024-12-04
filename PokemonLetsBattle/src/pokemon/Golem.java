package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Golem extends Pokemon {
	
	public Golem(int level, Entity ball) {
		super(76, "Golem", level, ball, 80, 120, 130, 55, 65, 45, -1, 177, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.GOLEM;
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
				Map.entry(27, Moves.ROCKBLAST),
				Map.entry(33, Moves.EARTHQUAKE),
				Map.entry(38, Moves.EXPLOSION),
				Map.entry(44, Moves.DOUBLEEDGE),
				Map.entry(49, Moves.STONEEDGE)
		);
	}
}