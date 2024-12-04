package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Poochyena extends Pokemon {
	
	public Poochyena(int level, Entity ball) {
		super(261, "Poochyena", level, ball, 35, 55, 35, 30, 30, 35, 18, 55, 1, Growth.MEDIUMFAST, 255);
		
		id = Pokedex.POOCHYENA;
		type = Type.DARK;
		ability = Ability.QUICKFEET;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.HOWL),
				Map.entry(9, Moves.SANDATTACK),
				Map.entry(13, Moves.BITE),
				Map.entry(25, Moves.SWAGGER),
//				Map.entry(29, Moves.ASSURANCE),
				Map.entry(33, Moves.SCARYFACE),
				Map.entry(45, Moves.TAKEDOWN),
				Map.entry(49, Moves.SUCKERPUNCH),
				Map.entry(53, Moves.CRUNCH)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Mightyena(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}