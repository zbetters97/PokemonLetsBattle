package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Ponyta extends Pokemon {
	
	public Ponyta(int level, Entity ball) {
		super(77, "Ponyta", level, ball, 50, 85, 55, 65, 65, 90, 45, 152, 1, Growth.MEDIUMFAST, 190);
		
		id = Pokedex.PONYTA;
		type = Type.FIRE;
		ability = Ability.FLASHFIRE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(7, Moves.GROWL),
				Map.entry(10, Moves.TAILWHIP),
				Map.entry(16, Moves.EMBER),
				Map.entry(19, Moves.STOMP),
				Map.entry(25, Moves.FIRESPIN),
				Map.entry(28, Moves.TAKEDOWN),
				Map.entry(34, Moves.AGILITY),
				Map.entry(38, Moves.FIREBLAST),
				Map.entry(44, Moves.BOUNCE),
				Map.entry(48, Moves.FLAREBLITZ)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Rapidash(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}