package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Ivysaur extends Pokemon {
	
	public Ivysaur(int level, Entity ball) {
		super(2, "Ivysaur", level, ball, 60, 62, 63, 80, 80, 60, 32, 3, 141, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.IVYSAUR;
		types = Arrays.asList(Type.GRASS, Type.POISON);
		ability = Ability.OVERGROW;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(3, Moves.GROWL),
				Map.entry(7, Moves.LEECHSEED),
				Map.entry(13, Moves.POISONPOWDER),
				Map.entry(14, Moves.SLEEPPOWDER),
				Map.entry(15, Moves.TAKEDOWN),
				Map.entry(20, Moves.RAZORLEAF),
				Map.entry(23, Moves.SWEETSCENT),
				Map.entry(28, Moves.GROWTH),
				Map.entry(31, Moves.DOUBLEEDGE),
				Map.entry(44, Moves.SOLARBEAM)
		);
	}
}