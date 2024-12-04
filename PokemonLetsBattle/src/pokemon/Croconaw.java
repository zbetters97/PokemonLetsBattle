package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Croconaw extends Pokemon {
	
	public Croconaw(int level, Entity ball) {
		super(159, "Croconaw", level, ball, 65, 80, 80, 59, 63, 58, 35, 143, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.CROCONAW;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.SCRATCH), 
        		new Move(Moves.LEER),
        		new Move(Moves.WATERGUN)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.WATERGUN),
				Map.entry(8, Moves.RAGE),
				Map.entry(13, Moves.BITE),
				Map.entry(15, Moves.SCARYFACE),
				Map.entry(21, Moves.ICEFANG),
				Map.entry(24, Moves.THRASH),
				Map.entry(30, Moves.CRUNCH),
				Map.entry(33, Moves.SLASH),
				Map.entry(39, Moves.SCREECH),
				Map.entry(42, Moves.AQUATAIL),
//				Map.entry(48, Moves.SUPERPOWER),
				Map.entry(51, Moves.HYDROPUMP)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Feraligatr(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}