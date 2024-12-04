package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Growlithe extends Pokemon {
	
	public Growlithe(int level, Entity ball) {
		super(58, "Growlithe", level, ball, 55, 70, 45, 70, 50, 60, 26, 91, 1, Growth.SLOW, 190);
		
		id = Pokedex.GROWLITHE;
		type = Type.FIRE;
		ability = Ability.FLASHFIRE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.BITE)
//        		new Move(Moves.ROAR)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.EMBER),
				Map.entry(9, Moves.LEER),
				Map.entry(14, Moves.ODORSLEUTH),
				Map.entry(20, Moves.FLAMEWHEEL),
				Map.entry(25, Moves.REVERSAL),
				Map.entry(28, Moves.FIREFANG),
				Map.entry(31, Moves.TAKEDOWN),
				Map.entry(34, Moves.FLAMETHROWER),
				Map.entry(39, Moves.AGILITY),
				Map.entry(42, Moves.CRUNCH),
				Map.entry(45, Moves.HEATWAVE),
				Map.entry(48, Moves.FLAREBLITZ)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Arcanine(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}