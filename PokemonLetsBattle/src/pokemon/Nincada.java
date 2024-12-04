package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Nincada extends Pokemon {
	
	public Nincada(int level, Entity ball) {
		super(290, "Nincada", level, ball, 31, 45, 90, 30, 30, 40, 20, 65, 1, Growth.ERATIC, 255);
		
		id = Pokedex.NINCADA;
		types = Arrays.asList(Type.BUG, Type.GROUND);
		ability = Ability.COMPOUNDEYES;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.SCRATCH),	
        		new Move(Moves.HARDEN)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.LEECHLIFE),
				Map.entry(9, Moves.SANDATTACK),
				Map.entry(14, Moves.FURYSWIPES),
//				Map.entry(19, Moves.MINDREADER),
				Map.entry(25, Moves.FALSESWIPE),
				Map.entry(31, Moves.MUDSLAP),
				Map.entry(38, Moves.METALCLAW),
				Map.entry(45, Moves.DIG)
		);
	}
	
	public Pokemon evolve() {
		
		Pokemon evolvedForm = null;
		
		evolvedForm = new Ninjask(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}