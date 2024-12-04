package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Mightyena extends Pokemon {
	
	public Mightyena(int level, Entity ball) {
		super(262, "Mightyena", level, ball, 70, 90, 70, 60, 60, 70, -1, 128, 2, Growth.MEDIUMFAST, 127);
		
		id = Pokedex.MIGHTYENA;
		type = Type.DARK;
		ability = Ability.QUICKFEET;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE),
        		new Move(Moves.HOWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.HOWL),
				Map.entry(9, Moves.SANDATTACK),
				Map.entry(13, Moves.BITE),
				Map.entry(27, Moves.SWAGGER),
//				Map.entry(32, Moves.ASSURANCE),
				Map.entry(37, Moves.SCARYFACE),
				Map.entry(52, Moves.TAKEDOWN),
				Map.entry(62, Moves.SUCKERPUNCH)
		);
	}
}