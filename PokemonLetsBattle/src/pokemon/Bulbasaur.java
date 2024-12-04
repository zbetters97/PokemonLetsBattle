package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Bulbasaur extends Pokemon {
	
	public Bulbasaur(int level, Entity ball) {
		super(1, "Bulbasaur", level, ball, 45, 49, 49, 65, 65, 45, 16, 64, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.BULBUSAUR;
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
				Map.entry(3, Moves.GROWL),
				Map.entry(7, Moves.LEECHSEED),
				Map.entry(9, Moves.VINEWHIP),
				Map.entry(13, Moves.POISONPOWDER),
				Map.entry(14, Moves.SLEEPPOWDER),
				Map.entry(15, Moves.TAKEDOWN),
				Map.entry(19, Moves.RAZORLEAF),
				Map.entry(21, Moves.SWEETSCENT),
				Map.entry(25, Moves.GROWTH),
				Map.entry(27, Moves.DOUBLEEDGE),
				Map.entry(37, Moves.SEEDBOMB)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Ivysaur(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}