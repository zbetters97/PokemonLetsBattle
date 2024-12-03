package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Grovyle extends Pokemon {
	
	public Grovyle(int level, Entity ball) {
		super(253, "Grovyle", level, ball, 50, 65, 45, 85, 65, 95, 36, 254, 141, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.GROVYLE;
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
				Map.entry(41, Moves.SLAM),
				Map.entry(53, Moves.FALSESWIPE),
				Map.entry(59, Moves.LEAFSTORM)
		);
	}
}