package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Chikorita extends Pokemon {
	
	public Chikorita(int level, Entity ball) {
		super(152, "Chikorita", level, ball, 45, 49, 65, 49, 65, 45, 16, 64, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.CHIKORITA;
		type = Type.GRASS;
		ability = Ability.OVERGROW;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.RAZORLEAF),
				Map.entry(9, Moves.POISONPOWDER),
//				Map.entry(12, Moves.SYNTHESIS),
				Map.entry(17, Moves.REFLECT),
				Map.entry(20, Moves.MAGICALLEAF),
//				Map.entry(23, Moves.NATURALGIFT),
				Map.entry(28, Moves.SWEETSCENT),
				Map.entry(31, Moves.LIGHTSCREEN),
				Map.entry(34, Moves.BODYSLAM),
				Map.entry(39, Moves.SAFEGUARD),
//				Map.entry(42, Moves.AROMATHERAPY),
				Map.entry(45, Moves.SOLARBEAM)			
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Bayleef(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}