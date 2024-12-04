package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Wartortle extends Pokemon {
	
	public Wartortle(int level, Entity ball) {
		super(8, "Wartortle", level, ball, 59, 63, 80, 65, 80, 58, 36, 143, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.WARTORTLE;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.BUBBLE), 
        		new Move(Moves.TACKLE), 
        		new Move(Moves.TAILWHIP)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.TAILWHIP),
				Map.entry(7, Moves.BUBBLE),
				Map.entry(10, Moves.WITHDRAW),
				Map.entry(13, Moves.WATERGUN),
				Map.entry(16, Moves.BITE),
				Map.entry(20, Moves.RAPIDSPIN),
				Map.entry(24, Moves.PROTECT),
				Map.entry(28, Moves.WATERPULSE),
				Map.entry(32, Moves.AQUATAIL),
				Map.entry(36, Moves.SKULLBASH),
				Map.entry(40, Moves.RAINDANCE),
				Map.entry(44, Moves.HYDROPUMP)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Blastoise(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}