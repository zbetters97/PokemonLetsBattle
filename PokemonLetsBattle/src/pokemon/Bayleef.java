package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Bayleef extends Pokemon {
	
	public Bayleef(int level, Entity ball) {
		super(153, "Bayleef", level, ball, 60, 62, 80, 63, 80, 60, 16, 141, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.BAYLEEF;
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
				Map.entry(32, Moves.SWEETSCENT),
				Map.entry(36, Moves.LIGHTSCREEN),
				Map.entry(40, Moves.BODYSLAM),
				Map.entry(46, Moves.SAFEGUARD),
//				Map.entry(50, Moves.AROMATHERAPY),
				Map.entry(54, Moves.SOLARBEAM)			
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Meganium(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}