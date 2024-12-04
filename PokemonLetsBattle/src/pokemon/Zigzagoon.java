package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Zigzagoon extends Pokemon {
	
	public Zigzagoon(int level, Entity ball) {
		super(263, "Zigzagoon", level, ball, 38, 30, 41, 30, 41, 60, 25, 60, 1, Growth.MEDIUMFAST, 255);
		
		id = Pokedex.ZIGZAGOON;
		type = Type.NORMAL;
		ability = Ability.QUICKFEET;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.TAILWHIP), 
				Map.entry(9, Moves.HEADBUTT),
				Map.entry(13, Moves.SANDATTACK),
				Map.entry(33, Moves.FLAIL),
				Map.entry(37, Moves.REST)
//				Map.entry(45, Moves.FLING)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Linoone(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}