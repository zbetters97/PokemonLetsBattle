package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Dragonair extends Pokemon {
	
	public Dragonair(int level, Entity ball) {
		super(148, "Dragonair", level, ball, 61, 84, 65, 70, 70, 70, 55, 144, 2, Growth.SLOW, 45);
		
		id = Pokedex.DRAGONAIR;
		type = Type.DRAGON;
		ability = Ability.SHEDSKIN;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WRAP),
        		new Move(Moves.LEER),
        		new Move(Moves.THUNDERWAVE),
        		new Move(Moves.TWISTER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.THUNDERWAVE),
				Map.entry(11, Moves.TWISTER),
				Map.entry(15, Moves.DRAGONRAGE),
				Map.entry(21, Moves.SLAM),
				Map.entry(25, Moves.AGILITY),
				Map.entry(33, Moves.AQUATAIL),
				Map.entry(39, Moves.DRAGONRUSH),
				Map.entry(47, Moves.SAFEGUARD),
				Map.entry(53, Moves.DRAGONDANCE),
				Map.entry(61, Moves.OUTRAGE),
				Map.entry(67, Moves.HYPERBEAM)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Dragonite(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}