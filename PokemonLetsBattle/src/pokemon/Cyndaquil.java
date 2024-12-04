package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Cyndaquil extends Pokemon {
	
	public Cyndaquil(int level, Entity ball) {
		super(155, "Cyndaquil", level, ball, 39, 52, 43, 60, 50, 65, 14, 65, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.CYNDAQUIL;
		type = Type.FIRE;
		ability = Ability.BLAZE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.SMOKESCREEN),
				Map.entry(10, Moves.EMBER),
				Map.entry(13, Moves.QUICKATTACK),
				Map.entry(19, Moves.FLAMEWHEEL),
				Map.entry(22, Moves.DEFENSECURL),
				Map.entry(28, Moves.SWIFT),
				Map.entry(31, Moves.LAVAPLUME),
				Map.entry(37, Moves.FLAMETHROWER),
				Map.entry(40, Moves.ROLLOUT),
				Map.entry(46, Moves.DOUBLEEDGE),
				Map.entry(49, Moves.ERUPTION)		
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Quilava(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}