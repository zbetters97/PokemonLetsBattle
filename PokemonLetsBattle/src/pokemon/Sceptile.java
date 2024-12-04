package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Sceptile extends Pokemon {
	
	public Sceptile(int level, Entity ball) {
		super(254, "Sceptile", level, ball, 70, 85, 65, 105, 85, 120, -1, 208, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.SCEPTILE;
		type = Type.GRASS;
		ability = Ability.OVERGROW;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.POUND),
        		new Move(Moves.ABSORB), 
        		new Move(Moves.QUICKATTACK),
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.ABSORB),
				Map.entry(11, Moves.QUICKATTACK),
				Map.entry(16, Moves.FURYCUTTER),
				Map.entry(17, Moves.PURSUIT),
				Map.entry(23, Moves.SCREECH),
				Map.entry(29, Moves.LEAFBLADE),
				Map.entry(35, Moves.AGILITY),
				Map.entry(43, Moves.SLAM),
				Map.entry(59, Moves.FALSESWIPE),
				Map.entry(67, Moves.LEAFSTORM)
		);
	}
}