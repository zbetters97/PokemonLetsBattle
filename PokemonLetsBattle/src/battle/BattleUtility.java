package battle;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import application.GamePanel;
import application.GamePanel.Weather;
import entity.Entity;
import moves.Move;
import moves.Moves;
import moves.Move.MoveType;
import pokemon.Pokemon;
import properties.Type;

public final class BattleUtility {
	
	private static List<Moves> soundMoves = Arrays.asList(
			Moves.GROWL, 
			Moves.HOWL,
			Moves.HYPERBEAM, 
			Moves.HYPERVOICE,
			Moves.SCREECH,
			Moves.SNORE,
			Moves.SUPERSONIC
	);
	private static final Map<Integer, Integer> magnitudeTable = Map.ofEntries(
			Map.entry(4, 5), Map.entry(5, 10), 
			Map.entry(6, 20), 
			Map.entry(7, 30), 
			Map.entry(8, 20), 
			Map.entry(9, 10), 
			Map.entry(10, 5)
	);
	
	private BattleUtility(GamePanel gp) { }
	
	public static Move chooseCPUMove(Entity trainer, Pokemon atk, Pokemon trg, Weather weather) {
		/** SWITCH OUT LOGIC REFERENCE: https://www.youtube.com/watch?v=apuO7pvmGUo **/
		
		Move bestMove = null;
		
		if (trainer == null || trainer.skillLevel  == trainer.skill_rookie) {
			bestMove = chooseCPUMove_Random(atk);
		}
		else if (trainer.skillLevel == trainer.skill_smart) {
			bestMove = chooseCPUMove_Power(atk, trg, weather);
		}
		else if (trainer.skillLevel == trainer.skill_elite) {
			bestMove = chooseCPUMove_Best(atk, trg, weather);
		}		
		
		return bestMove;
	}
 	private static Move chooseCPUMove_Random(Pokemon atk) {
		
		Move bestMove = null;
		
		int ranMove = (int)(Math.random() * (atk.getMoveSet().size()));				
		bestMove = atk.getMoveSet().get(ranMove);
				
		return bestMove;
	}
	private static Move chooseCPUMove_Power(Pokemon atk, Pokemon trg, Weather weather) {
		
		Move bestMove = null;
		
		Map<Move, Integer> damageMoves = new HashMap<>();
		
		for (Move move : atk.getMoveSet()) {
			
			if (move.getPower() > 0 && move.getPP() != 0) {
				
				double power = getPower(move, atk, trg, weather);				
				double type = getEffectiveness(trg, move.getType());				
				
				// CALCULATE POWER OF EACH MOVE
				int result = (int) (power * type);
				
				damageMoves.put(move, result);	
			}		
		}
				
		if (damageMoves.isEmpty()) {		
			
			for (Move move : atk.getMoveSet()) {
				
				if (move.getMType() == MoveType.STATUS) {
					bestMove = move;
					return bestMove;
				}		
			}
			
			int ranMove = (int)(Math.random() * (atk.getMoveSet().size()));				
			bestMove = atk.getMoveSet().get(ranMove);				
		}
		else {

			bestMove = Collections.max(damageMoves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 

			int val = 1 + (int)(Math.random() * 4);
			if (val == 1) {				
				int ranMove = (int)(Math.random() * (atk.getMoveSet().size()));				
				bestMove = atk.getMoveSet().get(ranMove);
			}	
		}
		
		return bestMove;
	}
	private static Move chooseCPUMove_Best(Pokemon atk, Pokemon trg, Weather weather) {
		
		Move bestMove = null;
		
		Map<Move, Integer> damageMoves = new HashMap<>();
		Map<Move, Integer> koMoves = new HashMap<>();
		
		for (Move move : atk.getMoveSet()) {
			
			if (move.getPower() > 0 && move.getPP() != 0) {
				
				int damage = calculateDamage(atk, trg, move, weather);				
				damageMoves.put(move, damage);	
				
				if (damage >= trg.getHP() && move.getPriority() > 0) {
					int accuracy = (int) getAccuracy(move, weather);
					koMoves.put(move, accuracy);
				}
			}		
		}
		
		if (koMoves.isEmpty()) {
			
			if (damageMoves.isEmpty()) {		
				
				for (Move move : atk.getMoveSet()) {
					
					if (move.getMType() == MoveType.STATUS) {
						bestMove = move;
						return bestMove;
					}		
				}
				
				int ranMove = (int)(Math.random() * (atk.getMoveSet().size()));				
				bestMove = atk.getMoveSet().get(ranMove);				
			}
			else {						
				bestMove = Collections.max(damageMoves.entrySet(), 
						Comparator.comparingInt(Map.Entry::getValue)).getKey();			
			}
		}
		else {
			bestMove = Collections.max(koMoves.entrySet(), 
					Comparator.comparingInt(Map.Entry::getValue)).getKey();	
		}
		
		return bestMove;
	}
	
	public static int getFirstTurn(Pokemon fighter1, Pokemon fighter2, Move move1, Move move2) {
		
		int first = 1;
		
		if (move1 == null && move2 == null) {
			first = 0;
		}
		else if (move1 == null) {
			first = 3;
		}
		else if (move2 == null) {
			first = 4;
		}
		else {				
			if (move1.getPriority() > move2.getPriority()) {
				first = 1;
			}
			else if (move1.getPriority() < move2.getPriority()) {
				first = 2;
			}
			else {
				if (fighter1.getSpeed() > fighter2.getSpeed()) {
					first = 1;
				}
				else if (fighter1.getSpeed() < fighter2.getSpeed()) {
					first = 2;
				}
				else {
					Random r = new Random();					
					first = (r.nextFloat() <= ((float) 1 / 2)) ? 1 : 2;
				}
			}
		}
		
		return first;
	}
	
	public static int getDelay(Move move1, Move move2) {		
		
		int delay = 0;
		
		// BOTH MOVES ARE ACTIVE
		if (move1 != null && move2 != null) {
			
			// both fighters are waiting
			if (move1.isWaiting() && move2.isWaiting()) {
				delay = 3;	
			}
			// fighter 2 is waiting;
			else if (move2.isWaiting()) {
				delay = 2;	
			}
			// fighter 1 is waiting
			else if (move1.isWaiting()) {
				delay = 1;
			}	
		}
		// CPU MOVE IS ACTIVE AND WAITING
		else if (move2 != null && move2.isWaiting()) {
			delay = 2;
		}
		// PLAYER MOVE IS ACTIVE AND WAITING
		else if (move1 != null && move1.isWaiting()) {
			delay = 1;
		}
				
		return delay;
	}	
	
	public static int getConfusionDamage(Pokemon pkm) {
 		/** CONFUSION DAMAGE FORMULA REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Confusion_(status_condition)#Effect **/
		 		
 		int damage = 0;
		
		double level = pkm.getLevel();		
		double power = 40.0;
		double A = pkm.getAttack();
		double D = pkm.getDefense();
								
		Random r = new Random();
		double random = (double) (r.nextInt(100 - 85 + 1) + 85) / 100.0;
		
		damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * random);
						
		return damage;
 	}
	
	public static void getWeatherMoveDelay(Weather weather, Move move) {
		
		switch (weather) {		
			case SUNLIGHT:
				if (move.getMove() == Moves.SOLARBEAM) {
					move.setTurnCount(1);
				}
				break;
			case RAIN:
				break;
			case HAIL:
				break;
			case SANDSTORM:
				break;
			case CLEAR:
				break;
		}		
	}
	
	public static boolean hit(Pokemon atk, Pokemon trg, Move move, Weather weather) {
		/** PROBABILITY FORMULA REFERENCE: https://monster-master.fandom.com/wiki/Evasion **/
		/** PROTECTED MOVES REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Semi-invulnerable_turn **/
		
		boolean hit = false;
		
		switch (trg.getProtectedState()) {
			case BOUNCE, FLY, SKYDROP:				
				if (move.getMove() != Moves.GUST &&
						move.getMove() != Moves.SKYUPPERCUT &&
						move.getMove() != Moves.THUNDER &&
						move.getMove() != Moves.TWISTER) {
					return true;
				}			
			case DIG:
				if (move.getMove() != Moves.EARTHQUAKE &&
						move.getMove() != Moves.FISSURE &&		
						move.getMove() != Moves.MAGNITUDE) {
					return true;
				}
				break;
			case DIVE:
				if (move.getMove() != Moves.SURF) {
					return true;
				}
				break;
			default:			
				break;
		}
		
		if (trg.hasActiveMove(Moves.PROTECT)) {
			hit = false;
		}
		// if move never misses, return true
		else if (move.getAccuracy() == -1) {
			hit = true; 
		}
		else {				
			if (trg.hasActiveMove(Moves.MIRACLEEYE) || trg.hasActiveMove(Moves.ODORSLEUTH)) {
				hit = true;
			}
			else {
				double accuracy = 0;
				
				if (trg.hasActiveMove(Moves.FORESIGHT)) {
					accuracy = getAccuracy(move, weather) * atk.getAccuracy();
				}
				else {
					accuracy = getAccuracy(move, weather) * (atk.getAccuracy() / trg.getEvasion());	
				}				
								
				Random r = new Random();
				float chance = r.nextFloat();
				
				// chance of missing is accuracy / 100
				hit = (chance <= ((float) accuracy / 100)) ? true : false;	
			}
		}
		
		return hit;
	}
	
 	public static int calculateDamage(Pokemon atk, Pokemon trg, Move move, Weather weather) {
		/** DAMAGE FORMULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Damage **/
		
		int damage = 0;
		
		double level = atk.getLevel();		
		double power = getPower(move, atk, trg, weather);
		double A = getAttack(move, atk, trg, weather);
		double D = getDefense(move, atk, trg, weather);
		double STAB = atk.checkType(move.getType()) ? 1.5 : 1.0;
		double type = getEffectiveness(trg, move.getType());
								
		Random r = new Random();
		double random = (double) (r.nextInt(100 - 85 + 1) + 85) / 100.0;
				
		damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * STAB * type * random);
										
		switch (move.getMove()) {
			case ENDEAVOR: 		
				if (trg.getHP() < atk.getHP()) damage = 0;			
				else damage = trg.getHP() - atk.getHP();	
				break;
			case DRAGONRAGE:
				damage = 40;
				break;
			case SEISMICTOSS:
				damage = trg.getLevel();
				break;
			default:
				break;
		}
		
		switch (trg.getAbility()) {
			case FLASHFIRE:
				if (move.getType() == Type.FIRE) damage = 0;			
				break;
			case LEVITATE:
				if (move.getType() == Type.GROUND) damage = 0; 				
				break;
			case SOUNDPROOF:
				if (soundMoves.contains(move.getMove())) damage = 0;				
				break;
			case THICKFAT:
				if (move.getType() == Type.FIRE || move.getType() == Type.ICE) damage *= 0.5;				
				break;
			default:
				break;
		}
		
		return damage;
	}
 			
 	private static double getAccuracy(Move move, Weather weather) {
		
		double accuracy = move.getAccuracy();
		
		switch (weather) {
			case CLEAR:
				break;
			case SUNLIGHT:
				if (move.getMove() == Moves.THUNDER) {
					accuracy *= 0.5;
				}
				break;
			case RAIN:
				break;
			case HAIL:
				if (move.getMove() == Moves.BLIZZARD) {
					accuracy = 100;
				}
				break;
			case SANDSTORM:
				if (move.getMove() == Moves.THUNDER) {
					accuracy *= 0.5;
				}
				break;
		}
		
		return accuracy;
	}
	private static double getPower(Move move, Pokemon atk, Pokemon trg, Weather weather) {
		/** FLAIL POWER FORMULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Flail_(move) **/
				
		double power = 1.0; 
		
		switch (move.getMove()) {
		case FLAIL, REVERSAL: 			
			double remainHP = atk.getHP() / atk.getBHP();
			
			if (remainHP >= 0.672) power = 20;
			else if (0.672 > remainHP && remainHP >= 0.344) power = 40;
			else if (0.344 > remainHP && remainHP >= 0.203) power = 80;
			else if (0.203 > remainHP && remainHP >= 0.094) power = 100;
			else if (0.094 > remainHP && remainHP >= 0.031) power = 150;
			else if (0.031 > remainHP) power = 200;		
			
			break;
		case MAGNITUDE:
			int strength = 4;
			
			// RANDOM NUM 0-100
			int chance = new Random().nextInt(100);
			int total = 0;
					
			// FOR EACH MAGNITUDE VALUE
			for (Integer magnitude : magnitudeTable.keySet()) {
				
				// GET PROBABILITY OF MAGNITUDE
				int rate = magnitudeTable.get(magnitude); 
				total += rate;
				
				// MAGNITUDE RANDOMLY SELECTED, ASSIGN TO STRENGTH
				if (chance <= total) {	
					strength = magnitude;
					break;
				}	
			}
			
			if (strength == 4) power = 10;
			else if (strength == 5) power = 30;
			else if (strength == 6) power = 50;
			else if (strength == 7) power = 70;
			else if (strength == 8) power = 90;
			else if (strength == 9) power = 110;
			else if (strength == 10) power = 150;
			
			break;
		case ERUPTION:
			power = (atk.getHP() * 150.0) / atk.getBHP();
			break;
		case WATERSPOUT:
			power = Math.ceil((atk.getHP() * move.getPower()) / atk.getBHP());
			break;
		default:
			if (move.getPower() == -1) power = atk.getLevel();		
			else if (move.getPower() == 1) power = trg.getLevel();		
			else power = move.getPower();				
			break;
		}
		
		switch (weather) {	
			case CLEAR:
				break;
			case SUNLIGHT:
				if (move.getType() == Type.FIRE) power *= 1.5;
				else if (move.getType() == Type.WATER) power *= 0.5;				
				break;
			case RAIN:
				if (move.getType() == Type.WATER) power *= 1.5;				
				else if (move.getType() == Type.FIRE) power *= 0.5;		
				
				if (move.getMove() == Moves.SOLARBEAM) power *= 0.5;				
				break;
			case HAIL:
				if (move.getMove() == Moves.SOLARBEAM) power *= 0.5;
				break;
			case SANDSTORM:
				if (move.getMove() == Moves.SOLARBEAM) power *= 0.5;
				break;
		}		
		
		switch (atk.getAbility()) {
			case BLAZE:
				if (move.getType() == Type.FIRE && ((double) atk.getHP() / (double) atk.getBHP() <= 0.33)) {
					power *= 1.5;				
				}
				break;
			case FLASHFIRE:
				if (atk.getAbility().isActive()) {
					power *= 1.5;
				}
				break;
			case OVERGROW:
				if (move.getType() == Type.GRASS && ((double) atk.getHP() / (double) atk.getBHP() <= 0.33)) {
					power *= 1.5;				
				}
				break;
			case TORRENT:
				if (move.getType() == Type.WATER && ((double) atk.getHP() / (double) atk.getBHP() <= 0.33)) {
					power *= 1.5;				
				}
				break;
			default:
				break;
		}
		
		return power;
	}	
	private static double getAttack(Move move, Pokemon atk, Pokemon trg, Weather weather) {
		
		double attack = 1.0;
		
		if (move.getMType().equals(MoveType.SPECIAL)) {
			attack = atk.getSpAttack();
			
			if (trg.hasActiveMove(Moves.LIGHTSCREEN)) {
				attack /= 2.0;
			}
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			attack = atk.getAttack();
		}
		
		switch (weather) {
			case CLEAR:
				break;
			case SUNLIGHT:					
				break;
			case RAIN:					
				break;
			case HAIL:					
				break;
			case SANDSTORM:
				break;
		}				
						
		return attack;
	}
	private static double getDefense(Move move, Pokemon atk, Pokemon trg, Weather weather) {
		
		double defense = 1.0;
		
		if (move.getMType().equals(MoveType.SPECIAL)) {
			defense = trg.getSpDefense();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			defense = trg.getDefense();
		}
		
		switch (weather) {
			case CLEAR:
				break;
			case SUNLIGHT:					
				break;
			case RAIN:					
				break;
			case HAIL:					
				break;
			case SANDSTORM:
				if (trg.isType(Type.ROCK) && move.getMType().equals(MoveType.SPECIAL)) {
					defense *= 1.5;
				}
				break;
		}			
		
		return defense;
	}
	public static double getEffectiveness(Pokemon pkm, Type type) {
		
		double effect = 1.0;
		
		if ((type == Type.NORMAL || type == Type.FIGHTING) && 
				pkm.checkType(Type.GHOST) &&
				(pkm.hasActiveMove(Moves.ODORSLEUTH) || pkm.hasActiveMove(Moves.FORESIGHT))) {
			return effect;							
		}
		else if (type == Type.PSYCHIC && pkm.checkType(Type.DARK) && 				 
				pkm.hasActiveMove(Moves.MIRACLEEYE)) {
			return effect;							
		}
		
		// if trg is single type
		if (pkm.getTypes() == null) {
			
			// if vulnerable, retrieve and return vulnerable value		
			for (Type vulnType : pkm.getType().getVulnerability().keySet()) {		
				if (vulnType.getName().equals(type.getName())) {
					effect = pkm.getType().getVulnerability().get(vulnType);
					return effect;
				}
			}			
			// if resistant, retrieve and return resistance value
			for (Type resType : pkm.getType().getResistance().keySet()) {			
				if (resType.getName().equals(type.getName())) {
					effect = pkm.getType().getResistance().get(resType);
					return effect;
				}			
			}		
		}
		// if pkm is multi type
		else {
			
			// for each type 
			for (Type trgType : pkm.getTypes()) {		
				
				// for each vulnerability			
				vulnerabilityLoop:
				for (Type vulnType : trgType.getVulnerability().keySet()) {		
					
					// if found, multiply by effect and move to next loop
					if (vulnType.getName().equals(type.getName())) {						
						effect *= trgType.getVulnerability().get(vulnType);		
						break vulnerabilityLoop;
					}
				}	
				
				// for each resistance
				resistanceLoop:
				for (Type resType : trgType.getResistance().keySet()) {		
					
					// if found, multiply by effect and move to next loop
					if (resType.getName().equals(type.getName())) {
						effect *= trgType.getResistance().get(resType);
						break resistanceLoop;
					}
				}
			}			
			
			// vulnerable and resistant cancel out
			if (effect == 0.75)	{
				effect = 1.0;
			}
		}			
						
		return effect;
	}
	
	public static Pokemon getCPUFighter(Entity trainer, Pokemon fighter, Pokemon trg, Weather weather) {
		
		Pokemon nextFighter = null;
		
		if (trainer == null || trainer.skillLevel == trainer.skill_rookie) {
			nextFighter = getCPUFighter_Next(trainer, fighter);
		}
		else if (trainer.skillLevel == trainer.skill_smart) {
			nextFighter = getCPUFighter_Power(trainer, trg, weather);
		}
		else if (trainer.skillLevel == trainer.skill_elite) {
			nextFighter = getCPUFighter_Best(trainer, trg, weather);
		}
		
		return nextFighter;		
	}	
 	private static Pokemon getCPUFighter_Next(Entity trainer, Pokemon fighter) {
		
 		Pokemon nextFighter = null;
 		
 		if (fighter == null) { 
 			nextFighter = trainer.pokeParty.get(0); 
 		}
 		else {
 			int index = trainer.pokeParty.indexOf(fighter);
 			if (index < 0 || index + 1 == trainer.pokeParty.size()) {
 				nextFighter = null;
 			}
 			else {
 				nextFighter = trainer.pokeParty.get(index + 1);
 			}	
 		}	
 		
 		return nextFighter;
	}
 	private static Pokemon getCPUFighter_Power(Entity trainer, Pokemon trg, Weather weather) {
 		
 		Pokemon bestFighter = null;
 		
 		Map<Pokemon, Integer> fighters = new HashMap<>();
 		Map<Move, Integer> powerMoves = new HashMap<>();
 		
 		for (Pokemon p : trainer.pokeParty) {
 			
 			if (p.isAlive()) {
 				
 				for (Move m : p.getMoveSet()) {
 					
 					if (m.getPower() > 0 && m.getPP() != 0) {
 						
 						double power = getPower(m, p, trg, weather);				
 						double type = getEffectiveness(trg, m.getType());				
 						
 						// CALCULATE POWER OF EACH MOVE
 						int result = (int) (power * type); 	 
 						
 						powerMoves.put(m, result);
 					}		 					
 				} 	
 				
 				if (!powerMoves.isEmpty()) {
 					
 					int power = Collections.max(powerMoves.entrySet(), 
 							Comparator.comparingInt(Map.Entry::getValue)).getValue(); 
 					
 					fighters.put(p, power);
 				} 			
 				
 				powerMoves.clear();
 			} 			
 		}
 		
 		if (fighters.isEmpty()) {
 			
 			// loop through party and find highest level pokemon
			for (Pokemon p : trainer.pokeParty) {
				fighters.put(p, p.getLevel());
			}
			
			bestFighter = Collections.max(fighters.entrySet(), 
					Comparator.comparingInt(Map.Entry::getValue)).getKey();
		
 		}
 		else {
 			bestFighter = Collections.max(fighters.entrySet(), 
					Comparator.comparingInt(Map.Entry::getValue)).getKey(); 
 		}
 		 		 		
 		return bestFighter; 		
 	}
 	private static Pokemon getCPUFighter_Best(Entity trainer, Pokemon trg, Weather weather) {
		
		Pokemon bestFighter = null;
 		
 		Map<Pokemon, Integer> fighters = new HashMap<>();
 		Map<Move, Integer> damageMoves = new HashMap<>();
 		
 		for (Pokemon p : trainer.pokeParty) {
 			
 			if (p.isAlive()) {
 				
 				for (Move m : p.getMoveSet()) {
 					
 					if (m.getPower() > 0 && m.getPP() != 0) {
 						
 						int damage = calculateDamage(p, trg, m, weather);				
 						damageMoves.put(m, damage);	
 					}		 					
 				} 	
 				
 				if (!damageMoves.isEmpty()) {
 					
 					int damage = Collections.max(damageMoves.entrySet(), 
 							Comparator.comparingInt(Map.Entry::getValue)).getValue(); 
 					
 					fighters.put(p, damage);
 				} 			
 				
 				damageMoves.clear();
 			} 			
 		}
 		
 		if (fighters.isEmpty()) {
 			
 			// loop through party and find highest level pokemon
			for (Pokemon p : trainer.pokeParty) {
				fighters.put(p, p.getLevel());
			}
			
			bestFighter = Collections.max(fighters.entrySet(), 
					Comparator.comparingInt(Map.Entry::getValue)).getKey();
		
 		}
 		else {
 			bestFighter = Collections.max(fighters.entrySet(), 
					Comparator.comparingInt(Map.Entry::getValue)).getKey(); 
 		}
 		 		 		
 		return bestFighter; 		
	}
 	
 	public static boolean isCaptured(Pokemon pokemon, Entity ball) {
		/** CATCH RATE FOMRULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Catch_rate#Capture_method_(Generation_III-IV) **/
			
		boolean isCaptured = false;
		
		double catchOdds;
		double maxHP = 1.0;
		double hp = 1.0;
		double catchRate = 1.0;
		double statusBonus = 1.0;
		
		maxHP = pokemon.getBHP(); if (maxHP == 0.0) maxHP = 1.0; 		
		hp = pokemon.getHP(); if (hp == 0.0) hp = 1.0;
		catchRate = pokemon.getCatchRate();
		
		if (pokemon.getStatus() != null) {			
			switch(pokemon.getStatus().getAbreviation()) {			
				case "PAR": statusBonus = 1.5; break;
				case "PSN": statusBonus = 1.5; break;
				case "CNF": statusBonus = 1.0; break;
				case "BRN": statusBonus = 1.5; break;
				case "FRZ": statusBonus = 2.0; break;
				case "SLP": statusBonus = 2.0; break;
			}			
		}
		
		catchOdds = ( (((3 * maxHP) - (2 * hp)) / (3 * maxHP) ) * catchRate * statusBonus);
		
		Random r = new Random();
		int roll = (int) (r.nextInt(ball.catchProbability - 0 + 1) + 0);
		
		if (roll <= catchOdds) {
			isCaptured = true;
		}
		
		return isCaptured;		
	}
	
	public static int calculateEXPGain(Pokemon pkm, boolean trainerBattle) {
		// EXP FORMULA REFERENCE (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		
		int exp = 0;
		
		double b = pkm.getEXPYeild();
		double L = pkm.getLevel();
		double s = 1.0;
		double e = 1.0;
		double a = trainerBattle ? 1.5 : 1.0;
		double t = 1.0;
		
		exp = (int) (Math.floor( (b * L) / 7 ) * Math.floor(1 / s) * e * a * t);
						
		return exp;
	}
	
	public static int calculateMoneyEarned(Entity trainer) {
		// MONEY EARNED FORMULA REFERENCE (ALL GEN): https://bulbapedia.bulbagarden.net/wiki/Prize_money
				
		int payout = 0;
		
		int level = trainer.pokeParty.get(trainer.pokeParty.size() - 1).getLevel();
		int base = trainer.trainerClass;		
		payout = base * level;
		
		return payout;
	}
}