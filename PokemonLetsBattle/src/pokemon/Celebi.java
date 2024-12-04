package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Celebi extends Pokemon {
	
	public Celebi(int level, Entity ball) {
		super(251, "Celebi", level, ball, 100, 100, 100, 100, 100, 100, -1, 64, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.CELEBI;
		types = Arrays.asList(Type.PSYCHIC, Type.GRASS);
		ability = Ability.NATURALCURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.LEECHSEED),
        		new Move(Moves.CONFUSION),
        		new Move(Moves.RECOVER),
        		new Move(Moves.HEALBELL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(10, Moves.SAFEGUARD),
				Map.entry(19, Moves.MAGICALLEAF),
				Map.entry(28, Moves.ANCIENTPOWER),
//				Map.entry(37, Moves.BATONPASS),
//				Map.entry(46, Moves.NATURALGIFT),
//				Map.entry(55, Moves.HEALBLOCK),
				Map.entry(64, Moves.FUTURESIGHT),
//				Map.entry(73, Moves.HEALINGWISH),
				Map.entry(82, Moves.LEAFSTORM),
				Map.entry(91, Moves.PERISHSONG)
		);
	}
}