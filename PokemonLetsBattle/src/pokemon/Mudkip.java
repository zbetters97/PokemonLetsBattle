package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Mudkip extends Pokemon {
	
	public Mudkip(int level, Entity ball) {
		super(258, "Mudkip", level, ball, 50, 70, 50, 50, 50, 40, 16, 259, 65, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.MUDKIP;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.MUDSLAP),
				Map.entry(10, Moves.WATERGUN),
				Map.entry(28, Moves.TAKEDOWN),
				Map.entry(37, Moves.PROTECT),
				Map.entry(42, Moves.HYDROPUMP),
				Map.entry(46, Moves.ENDEAVOR)
		);
	}
}