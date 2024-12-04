package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Combusken extends Pokemon {
	
	public Combusken(int level, Entity ball) {
		super(256, "Combusken", level, ball, 60, 85, 60, 85, 60, 55, 36, 142, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.COMBUSKEN;
		types = Arrays.asList(Type.FIRE, Type.FIGHTING);
		ability = Ability.BLAZE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.EMBER), 
        		new Move(Moves.SCRATCH), 
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(13, Moves.EMBER),
				Map.entry(16, Moves.DOUBLEKICK),
				Map.entry(17, Moves.PECK),
				Map.entry(21, Moves.SANDATTACK),
				Map.entry(28, Moves.BULKUP),
				Map.entry(32, Moves.QUICKATTACK),
				Map.entry(39, Moves.SLASH),
				Map.entry(50, Moves.SKYUPPERCUT),
				Map.entry(54, Moves.FLAREBLITZ)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Blaziken(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}