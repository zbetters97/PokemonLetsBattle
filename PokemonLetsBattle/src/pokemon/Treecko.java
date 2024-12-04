package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Treecko extends Pokemon {
	
	public Treecko(int level, Entity ball) {
		super(252, "Treecko", level, ball, 40, 45, 35, 65, 55, 70, 16, 65, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.TREECKO;
		type = Type.GRASS;
		ability = Ability.OVERGROW;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.POUND), 
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.ABSORB),
				Map.entry(11, Moves.QUICKATTACK),
				Map.entry(16, Moves.PURSUIT),
				Map.entry(21, Moves.SCREECH),
				Map.entry(26, Moves.MEGADRAIN),
				Map.entry(31, Moves.AGILITY),
				Map.entry(36, Moves.SLAM),
				Map.entry(46, Moves.GIGADRAIN),
				Map.entry(51, Moves.ENERGYBALL)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Grovyle(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}