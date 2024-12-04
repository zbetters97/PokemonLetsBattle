package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Dratini extends Pokemon {
	
	public Dratini(int level, Entity ball) {
		super(147, "Dratini", level, ball, 41, 64, 45, 50, 50, 50, 35, 67, 1, Growth.SLOW, 45);
		
		id = Pokedex.DRATINI;
		type = Type.DRAGON;
		ability = Ability.SHEDSKIN;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WRAP),
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.THUNDERWAVE),
				Map.entry(11, Moves.TWISTER),
				Map.entry(15, Moves.DRAGONRAGE),
				Map.entry(21, Moves.SLAM),
				Map.entry(25, Moves.AGILITY),
				Map.entry(31, Moves.AQUATAIL),
				Map.entry(35, Moves.DRAGONRUSH),
				Map.entry(41, Moves.SAFEGUARD),
				Map.entry(45, Moves.DRAGONDANCE),
				Map.entry(51, Moves.OUTRAGE),
				Map.entry(55, Moves.HYPERBEAM)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Dragonair(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}