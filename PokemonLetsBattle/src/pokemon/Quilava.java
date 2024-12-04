package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Quilava extends Pokemon {
	
	public Quilava(int level, Entity ball) {
		super(156, "Quilava", level, ball, 58, 64, 58, 80, 65, 80, 36, 142, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.QUILAVA;
		type = Type.FIRE;
		ability = Ability.BLAZE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.LEER),
        		new Move(Moves.SMOKESCREEN)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.SMOKESCREEN),
				Map.entry(10, Moves.EMBER),
				Map.entry(13, Moves.QUICKATTACK),
				Map.entry(20, Moves.FLAMEWHEEL),
				Map.entry(24, Moves.DEFENSECURL),
				Map.entry(31, Moves.SWIFT),
				Map.entry(35, Moves.LAVAPLUME),
				Map.entry(42, Moves.FLAMETHROWER),
				Map.entry(46, Moves.ROLLOUT),
				Map.entry(53, Moves.DOUBLEEDGE),
				Map.entry(57, Moves.ERUPTION)		
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Typhlosion(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}