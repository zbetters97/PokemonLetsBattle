package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Meganium extends Pokemon {
	
	public Meganium(int level, Entity ball) {
		super(154, "Meganium", level, ball, 80, 82, 100, 83, 100, 80, -1, 208, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.MEGANIUM;
		type = Type.GRASS;
		ability = Ability.OVERGROW;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.GROWL),
        		new Move(Moves.RAZORLEAF),
        		new Move(Moves.POISONPOWDER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.RAZORLEAF),
				Map.entry(9, Moves.POISONPOWDER),
//				Map.entry(12, Moves.SYNTHESIS),
				Map.entry(18, Moves.REFLECT),
				Map.entry(22, Moves.MAGICALLEAF),
//				Map.entry(26, Moves.NATURALGIFT),
				Map.entry(34, Moves.SWEETSCENT),
				Map.entry(40, Moves.LIGHTSCREEN),
				Map.entry(46, Moves.BODYSLAM),
				Map.entry(54, Moves.SAFEGUARD),
//				Map.entry(60, Moves.AROMATHERAPY),
				Map.entry(66, Moves.SOLARBEAM)			
		);
	}
}