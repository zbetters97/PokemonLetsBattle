package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Venusaur extends Pokemon {
	
	public Venusaur(int level, Entity ball) {
		super(3, "Venusaur", level, ball, 80, 82, 83, 100, 100, 80, -1, -1, 208, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.VENUSAUR;
		types = Arrays.asList(Type.GRASS, Type.POISON);
		ability = Ability.OVERGROW;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.VINEWHIP), 
				new Move(Moves.TACKLE), 
				new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(3, Moves.GROWL),
				Map.entry(7, Moves.LEECHSEED),
				Map.entry(9, Moves.VINEWHIP),
				Map.entry(13, Moves.POISONPOWDER),
				Map.entry(14, Moves.SLEEPPOWDER),
				Map.entry(15, Moves.TAKEDOWN),
				Map.entry(20, Moves.RAZORLEAF),
				Map.entry(23, Moves.SWEETSCENT),
				Map.entry(28, Moves.GROWTH),
				Map.entry(31, Moves.DOUBLEEDGE),
				Map.entry(32, Moves.PETALDANCE),
				Map.entry(37, Moves.SEEDBOMB),
				Map.entry(53, Moves.SOLARBEAM)
		);
	}
}