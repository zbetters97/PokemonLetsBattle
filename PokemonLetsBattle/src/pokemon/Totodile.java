package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Totodile extends Pokemon {
	
	public Totodile(int level, Entity ball) {
		super(158, "Totodile", level, ball, 50, 65, 64, 44, 48, 43, 18, 66, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.TOTODILE;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.WATERGUN),
				Map.entry(8, Moves.RAGE),
				Map.entry(13, Moves.BITE),
				Map.entry(15, Moves.SCARYFACE),
				Map.entry(20, Moves.ICEFANG),
				Map.entry(22, Moves.THRASH),
				Map.entry(27, Moves.CRUNCH),
				Map.entry(29, Moves.SLASH),
				Map.entry(34, Moves.SCREECH),
				Map.entry(36, Moves.AQUATAIL),
//				Map.entry(41, Moves.SUPERPOWER),
				Map.entry(43, Moves.HYDROPUMP)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Croconaw(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}