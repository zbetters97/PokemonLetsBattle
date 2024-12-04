package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Ninjask extends Pokemon {
	
	public Ninjask(int level, Entity ball) {
		super(291, "Ninjask", level, ball, 61, 90, 45, 50, 50, 160, 35, 155, 2, Growth.ERATIC, 120);
		
		id = Pokedex.NINJASK;
		types = Arrays.asList(Type.BUG, Type.FLYING);
		ability = Ability.SPEEDBOOST;
		
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
				Map.entry(20, Moves.DOUBLETEAM),
				Map.entry(21, Moves.FURYCUTTER),
				Map.entry(22, Moves.SCREECH),
				Map.entry(25, Moves.SWORDSDANCE),
				Map.entry(31, Moves.SLASH),
				Map.entry(38, Moves.AGILITY),
//				Map.entry(45, Moves.BATONPASS),
				Map.entry(52, Moves.XSCISSOR)
		);
	}
	
	public Pokemon evolve() {
		
		Pokemon evolvedForm = null;
		
		evolvedForm = new Shedinja(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}