package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import entity.Entity;
import entity.collectables.items.ITM_EXP_Share;
import moves.Move;
import moves.Moves;
import moves.Move.MoveType;
import pokemon.Pokemon;
import properties.Status;
import properties.Type;
import properties.abilities.*;

public class BattleManager extends Thread {
	
	private enum Weather {
		/** WEATHER BATTLE REFERENCE: https://www.ign.com/wikis/pokemon-heartgold-soulsilver-version/Weather **/
		
		CLEAR, SUNLIGHT, RAIN, HAIL, SANDSTORM
	}
	
	private GamePanel gp;
	public boolean active = false;
	public boolean running = false;
	
	// GENERAL VALUES		
	public Entity trainer;
	public Pokemon[] fighter = new Pokemon[2];
	private Pokemon[] newFighter = new Pokemon[2];
	private ArrayList<Pokemon> otherFighters = new ArrayList<>();
	private Move playerMove, cpuMove;
	public Move newMove = null;
	public Move oldMove = null;
	private Weather weather = Weather.CLEAR;
	private int weatherDays = -1;
	private int winner = -1, loser = -1;	
	private int escapeAttempts = 0;
		
	public Entity ballUsed;
			
	// TURN VALUES
	private int currentTurn;
	private int nextTurn;
	private final int playerTurn = 0;
	private final int cpuTurn = 1;	
					
	// SOUND LIBRARIES
	private final int cry_SE = 3;
	private final int faint_SE = 4;
	private final int moves_SE = 5;
	private final int battle_SE = 6;
	
	// BATTLE STATES
	public int battleMode;
	public final int wildBattle = 1;
	public final int trainerBattle = 2;
	public final int rivalBattle = 3;
	public final int gymBattle = 4;	
	public final int eliteBattle = 5;
	public final int championBattle = 6;
	public final int legendaryBattle = 7;
	
	// FIGHT STAGES
	public int fightStage;
	public final int fight_Encounter = 1;
	public final int fight_Swap = 2;
	public final int fight_Start = 3;
	public final int fight_Capture = 4;
	public final int fight_Run = 5;
	public final int fight_Evolve = 6;
	
	// CONSTRUCTOR
	public BattleManager(GamePanel gp) {
		this.gp = gp;
	}
	public void setup(int currentBattle, Entity trainer, Pokemon pokemon, String condition) {
		
		battleMode = currentBattle;
		
		if (condition == null) weather = Weather.CLEAR;
		else weather = Weather.valueOf(condition);
		
		weatherDays = -1;
					
		if (trainer != null) this.trainer = trainer;	
		else if (pokemon != null) fighter[1] = pokemon;

		active = true;
		running = true;		
		fightStage = fight_Encounter;
	}
			
	/** RUN METHOD **/
	public void run() {		
		
		while (running) {
			
			switch (fightStage) {	
			
				case fight_Encounter:
					try { setBattle(); } 
					catch (InterruptedException e) { e.printStackTrace(); }					
					break;
			
				case fight_Swap:
					try { swapFighters(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;
			
				case fight_Start:					
					try { runBattle(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;	
					
				case fight_Capture:
					try { throwPokeball(); } 
					catch (InterruptedException e) { e.printStackTrace(); }				
					break;
					
				case fight_Run:
					try { escapeBattle(); } 
					catch (InterruptedException e) { e.printStackTrace(); }				
					break;
					
				case fight_Evolve:
					try { checkEvolve(); } 
					catch (InterruptedException e) { e.printStackTrace(); }				
					break;
			}		
		}		
	}
		
	// SETUP BATTLE METHOD
 	private void setBattle() throws InterruptedException {		
		gp.stopMusic();	
				
		switch(battleMode) {
			case wildBattle:
				
				gp.startMusic(1, 1);				
				pause(1400);
				
				gp.playSE(cry_SE, fighter[1].toString());	
				typeDialogue("A wild " + fighter[1].getName() + "\nappeared!", true);	
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
				
				break;
			case trainerBattle: 
				
				gp.startMusic(1, 4);				
				pause(1400);		
				
				typeDialogue("Trainer " + trainer.name + "\nwould like to battle!", true);	
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
				
				fighter[1] = trainer.pokeParty.get(0);
				
				gp.playSE(cry_SE, fighter[1].toString());	
				typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
				pause(100);
				
				break;			
		}
		
		fighter[0] = gp.player.pokeParty.get(0);
		getOtherFighters();
		
		gp.playSE(cry_SE, fighter[0].toString());	
		typeDialogue("Go, " + fighter[0].getName() + "!");					
		pause(100);
		
		getWeatherAbility();	
		checkWeatherCondition();
		
		running = false;		
		
		gp.ui.battleState = gp.ui.battle_Options;
	}	
	private void getOtherFighters() {
		
		for (Pokemon p : gp.player.pokeParty) {
			if (!otherFighters.contains(p) &&
					p.getHeldItem() != null && 
					p.getHeldItem().name.equals(ITM_EXP_Share.colName)) {
				otherFighters.add(p);
			}
		}
	}
	
	// PRINT WEATHER IF CONDITION IS ACTIVE
	private void checkWeatherCondition() throws InterruptedException {
			
		switch (weather) {	
			case CLEAR:
				break;
			case SUNLIGHT:
				gp.playSE(battle_SE, "sunlight");
				typeDialogue("The sun shines intensely!");	
				break;
			case RAIN:
				gp.playSE(battle_SE, "rain");
				typeDialogue("Rain started to fall!");	
				break;
			case HAIL:
				gp.playSE(battle_SE, "hail");
				typeDialogue("Hail started to fall!");	
				break;
			case SANDSTORM:
				gp.playSE(battle_SE, "sandstorm");
				typeDialogue("A sandstorm started to\n rage!");	
				break;
		}		
	}
	
	// CHANGE WEATHER BASED ON FIGHTER ABILITY
	private void getWeatherAbility() {
			
		if (fighter[0].getAbility().getCategory() == Ability.Category.WEATHER &&
				fighter[1].getAbility().getCategory() == Ability.Category.WEATHER) {
			
			// if fighter 1 is faster
			if (fighter[0].getSpeed() > fighter[1].getSpeed()) {
				weather = Weather.valueOf(fighter[1].getAbility().getAttribute());
			}
			// if fighter 2 is faster
			else if (fighter[0].getSpeed() < fighter[1].getSpeed()) {
				weather = Weather.valueOf(fighter[0].getAbility().getAttribute());
			}
			// if both fighters have equal speed, coin flip decides
			else {
				Random r = new Random();					
				if (r.nextFloat() <= ((float) 1 / 2)) {
					weather = Weather.valueOf(fighter[1].getAbility().getAttribute());
				}
				else {
					weather = Weather.valueOf(fighter[0].getAbility().getAttribute());
				}
			}					
		}
		else if (fighter[0].getAbility().getCategory() == Ability.Category.WEATHER) {									
			weather = Weather.valueOf(fighter[0].getAbility().getAttribute());
		}
		else if (fighter[1].getAbility().getCategory() == Ability.Category.WEATHER) {									
			weather = Weather.valueOf(fighter[1].getAbility().getAttribute());
		}
		
	}
	
	// SWAP POKEMON METHODS
	private void swapFighters() throws InterruptedException {
				
		// WINNER NOT YET DECIDED
		if (winner == -1) {
			
			if (!fighter[0].isAlive()) fighter[0] = null;
			if (!fighter[1].isAlive()) fighter[1] = null;
									
			gp.ui.battleState = gp.ui.battle_Dialogue;
			
			// CPU SWAP OUT
			if (fighter[1] == null) {
				
				// PLAYER SWAP OUT
				if (newFighter[0] != null) {
					
					if (otherFighters.contains(newFighter[0])) {
						otherFighters.remove(newFighter[0]);
					}
					
					fighter[0] = newFighter[0];		
					
					gp.playSE(cry_SE, fighter[0].toString());	
					typeDialogue("Go, " + fighter[0].getName() + "!");					
					pause(100);
				}
				
				fighter[1] = newFighter[1];
				
				gp.playSE(cry_SE, fighter[1].toString());	
				typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
				pause(100);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				running = false;				
				gp.ui.battleState = gp.ui.battle_Options;				
			}
			// PLAYER FORCE SWAP OUT
			else if (fighter[0] == null) {
				
				if (otherFighters.contains(newFighter[0])) {
					otherFighters.remove(newFighter[0]);
				}
				
				fighter[0] = newFighter[0];		
				
				gp.playSE(cry_SE, fighter[0].toString());	
				typeDialogue("Go, " + fighter[0].getName() + "!");					
				pause(100);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				running = false;				
				gp.ui.battleState = gp.ui.battle_Options;	
			}
			// MID BATTLE SWAP OUT
			else {			
				
				if (otherFighters.contains(newFighter[0])) {
					otherFighters.remove(newFighter[0]);
				}
				
				if (!otherFighters.contains(fighter[0])) {
					otherFighters.add(fighter[0]);	
				}

				fighter[0] = newFighter[0];		
				
				gp.playSE(cry_SE, fighter[0].toString());
				typeDialogue("Go, " + fighter[0].getName() + "!");	
				pause(100);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				fightStage = fight_Start;
			}
		}				
		// TRAINER HAS WON
		else {							
			getWinningTrainer();
		}
	}
	
	// SWAP FIGHTER METHODS
	public boolean swapPokemon(int partySlot) {
		
		if (fighter[0] == gp.player.pokeParty.get(partySlot)) {
			return false;
		}
		
		if (gp.player.pokeParty.get(partySlot).isAlive()) {
			
			newFighter[0] = gp.player.pokeParty.get(partySlot);
						
			if (gp.player.pokeParty.size() > 1) {
				Collections.swap(gp.player.pokeParty, 0, partySlot);	
			}
						
			return true;
		}
		else {
			return false;
		}		
	}
	private Pokemon cpuSelectNextFighter() {
				
		if (fighter[0] == null) return trainer.pokeParty.get(0);
				
		int index = trainer.pokeParty.indexOf(fighter[1]);
		if (index < 0 || index + 1 == trainer.pokeParty.size()) {
			return null;
		}
		else {
			 return trainer.pokeParty.get(index + 1);
		}
		
		/*
		int available = 0;
		
		// list to hold all candidates based on type effectiveness
		Map<Pokemon, Integer> pokemonList = new HashMap<>();
		
		for (Pokemon p : trainer.pokeParty) {
			if (p.isAlive()) {
				available++;
			}
		}
		
		// if more than 1 pokemon in CPU party
		if (available > 1) {
			
			// loop through each pokemon in party
			for (Pokemon p : trainer.pokeParty) {
				
				if (p.isAlive()) {
				
					// if party is single type
					if (p.getTypes() == null) {
												
						// if target is single type
						if (fighter[0].getTypes() == null) {	
							
							// loop through each type in target pokemon
							for (Type vulnType : fighter[0].getType().getVulnerability().keySet()) {	
								
								// if type matches target's vulnerability
								if (vulnType.getName().equals(p.getType().getName()))
									pokemonList.put(p, p.getLevel());
							}
						}					
						// if target is multi type
						else {			
							
							// for each type in target
							for (Type type : fighter[0].getTypes()) {
								
								// loop through each vulnerability in type
								for (Type vuln : type.getVulnerability().keySet()) {									
	
									// if type matches target's vulnerability
									if (vuln.getName().equals(p.getType().getName()))
										pokemonList.put(p, p.getLevel());
								}
							}						
						}					
					}				
					// if party is multi type
					else { 
											
						// if target is single type
						if (fighter[0].getTypes() == null) {	
							
							// for each type in party
							for (Type type : p.getTypes()) {
								
								// loop through each vulnerability in target pokemon
								for (Type vulnType : fighter[0].getType().getVulnerability().keySet()) {	
								
									// if type matches target's vulnerability
									if (vulnType.getName().equals(type.getName()))
										pokemonList.put(p, p.getLevel());
								}
							}
							
							
						}					
						// if target is multi type
						else {			
							
							// for each type in party
							for (Type parType : p.getTypes()) {
	
								// for each type in target
								for (Type tarType : fighter[0].getTypes()) {
									
									// loop through each vulnerability in type
									for (Type vuln : tarType.getVulnerability().keySet()) {									
		
										// if type matches target's vulnerability
										if (vuln.getName().equals(parType.getName()))
											pokemonList.put(p, p.getLevel());
									}
								}		
							}
						}
					}
				}				
			}
		}
		// if 1 pokemon remaining in party
		else if (available == 1) {
			for (Pokemon p : trainer.pokeParty) {				
				if (p.isAlive()) {
					return p;
				}
			}
		}
		
		// if 0 pokemon remaining in party
		else {
			return null;
		}
			
		Pokemon bestPokemon;
		
		// find best pokemon based on max level
		if (!pokemonList.isEmpty()) {
			bestPokemon = Collections.max(pokemonList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			return bestPokemon;
		}
		else {			
			// loop through party and find highest level pokemon
			for (Pokemon p : trainer.pokeParty) {
				pokemonList.put(p, p.getLevel());
			}
			
			bestPokemon = Collections.max(pokemonList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			return bestPokemon;
		}
		
		*/
	}
			
	// RUN BATTLE METHOD
	private void runBattle() throws InterruptedException {
		
		getCPUMove();
		setRotation();	
		
		startTurn();
		
		if (hasWinningPokemon()) {
			getWinningPokemon();
		}		
		else {
			checkStatusDamage();	
			checkWeatherDamage();
			
			if (hasWinningPokemon()) {
				getWinningPokemon();
			}
			else {
				turnReset();
			}
		}
	}
	
	// GET MOVES METHOD
	private void getCPUMove() throws InterruptedException {
		
		// GET CPU MOVE IF NO CPU DELAY
		int delay = getDelayedMove();
		
		if (delay == 0 || delay == 1) {
			cpuMove = cpuSelectMove();
		}
	}
	public int getDelayedMove() {		
		
		// both fighters are waiting
		if (fighter[0].isWatiing() && fighter[1].isWatiing())
			return 3;	
		// fighter 1 is waiting
		else if (fighter[0].isWatiing())
			return 1;
		// fighter 2 is waiting;
		else if (fighter[1].isWatiing())
			return 2;	
		
		return 0;
	}	
	private Move cpuSelectMove() throws InterruptedException {
		
		Move bestMove;
		
		// holds Map of Move and Damage Points
		Map<Move, Integer> moves = new HashMap<>();
		
		// for each move in attacker's move set
		for (Move move : fighter[1].getMoveSet()) {
			
			if (!move.isToSelf() && move.getpp() != 0) {
				
				// find damage value of each move (no crit is assumed)
				int damage = calculateDamage(1, 0, move, 1.0, true);
				
				// add move and corresponding damage value to k/v list
				moves.put(move, damage);	
			}		
		}
		
		// find max value in moves list based on value
		if (!moves.isEmpty()) {
			
			bestMove = Collections.max(moves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 
			
			// if best move does not cause KO
			int damage = calculateDamage(1, 0, bestMove, 1.0, true);
			if (damage < fighter[0].getHP()) {
				
				// 33% chance CPU selects random move instead of most powerful			
				int val = 1 + (int)(Math.random() * 4);
				if (val == 1) {				
					int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
					bestMove = fighter[1].getMoveSet().get(ranMove);
				}	
			}			
		}
		// if list is empty, select random move
		else {
			int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
			bestMove = fighter[1].getMoveSet().get(ranMove);
		}
		
		return bestMove;	
	}
	public void setPlayerMove(int selection) {
		playerMove = fighter[0].getMoveSet().get(selection);	
		fightStage = fight_Start;
	}
	
	// SET ROTATION METHODS
	private void setRotation() {		
		
		// if both pokemon are alive
		if (fighter[0].isAlive() && fighter[1].isAlive()) {
			
			// 1 if trainer 1 moves first, 2 if trainer 2 moves first
			// 3 if only trainer 2, 4 if only trainer 1, 5 if neither
			int numTurn = getFirst();	
			
			if (numTurn == 1) { 
				currentTurn = playerTurn;
				nextTurn = cpuTurn;
			}	
			else if (numTurn == 2) { 
				currentTurn = cpuTurn;
				nextTurn = playerTurn;
			}				
			else if (numTurn == 3) { 
				currentTurn = cpuTurn;
				nextTurn = -1;
			}				
			else if (numTurn == 4) { 
				currentTurn = playerTurn;
				nextTurn = -1;
			}
			else if (numTurn == 5) {
				currentTurn = -1;
				nextTurn = -1;
			}
		}
	}
	private int getFirst() {
		
		if (playerMove == null && cpuMove == null) {
			return 5;
		}
		else if (playerMove == null) {
			return 3;
		}
		else if (cpuMove == null) {
			return 4;
		}
		else {				
			
			double speed1 = fighter[0].getSpeed();
			double speed2 = fighter[1].getSpeed();
			
			if (fighter[0].getAbility().getCategory() == Ability.Category.ATTRIBUTE &&
					fighter[0].getAbility().isValid(fighter[0])) {
				
				speed1 *= fighter[0].getAbility().getFactor();
			}
			if (fighter[1].getAbility().getCategory() == Ability.Category.ATTRIBUTE &&
					fighter[1].getAbility().isValid(fighter[1])) {
				
				speed2 *= fighter[1].getAbility().getFactor();
			}
			
			// if both moves go first (EX: Quick Attack)
			if (playerMove.getGoFirst() && cpuMove.getGoFirst()) {			
				
				// if fighter_one is faster (fighter_one has advantage if equal)
				if (speed1 >= speed2) 
					return 1;
				else 
					return 2;
			}
			// if only playerMove goes first (EX: Quick Attack)
			else if (playerMove.getGoFirst()) 
				return 1;
			// if only cpuMove goes first (EX: Quick Attack)
			else if (cpuMove.getGoFirst()) 
				return 2;
			else {
				// if fighter 1 is faster
				if (speed1 > speed2) {
					return 1;
				}
				// if fighter 2 is faster
				else if (speed1 < speed2) {
					return 2;
				}
				// if both fighters have equal speed, coin flip decides
				else {
					Random r = new Random();					
					return (r.nextFloat() <= ((float) 1 / 2)) ? 1 : 2;
				}
			}
		}
	}
	
	// START TURN METHOD
	private void startTurn() throws InterruptedException {	
		
		if (currentTurn != -1) {		

			if (canMove()) {					
				move();					
			}
			else {
				currentTurn = nextTurn;
				nextTurn = -1;
			}
			
			startTurn();
		}	
	}		
	private boolean canMove() throws InterruptedException {
		
		boolean canMove = true;
		
		// if attacker has status effect
		if (fighter[currentTurn].getStatus() != null) {
		
			// check which status
			switch (fighter[currentTurn].getStatus().getAbreviation()) {			
				case "PAR":	canMove = paralyzed(currentTurn); break;					
				case "FRZ": canMove = frozen(currentTurn); break;			
				case "SLP": canMove = asleep(currentTurn); break;
				case "CNF": canMove = confused(currentTurn); break;
			}
		}	
		
		return canMove;
	}
	
	// STATUS CONDITION METHODS
	private boolean paralyzed(int atk) throws InterruptedException {
		
		// 1/4 chance can't move due to PAR
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {		
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			return false;
		} 
		else {
			return true;
		}
	}
	private boolean frozen(int atk) throws InterruptedException {
		
		// 1/4 chance attacker can thaw from ice
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {
			Status status = fighter[atk].getStatus();
			fighter[atk].setStatus(null);
			typeDialogue(fighter[atk].getName() + "" + status.printRecover());			
			return true;
		}
		else {	
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			return false;		
		}
	}
	private boolean asleep(int atk) throws InterruptedException {
				
		// if number of moves under status hit limit, remove status
		if (recoverStatus(atk)) {			
			return true;
		}
		// pokemon still under status status
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			
			return false;
		}
	}
	private boolean confused(int atk) throws InterruptedException {
		
		gp.playSE(battle_SE, fighter[atk].getStatus().getStatus());
		typeDialogue(fighter[atk].getName() + " is\n" + fighter[atk].getStatus().getStatus() + "...");
		
		// if number of moves under status hit limit, remove status
		if (recoverStatus(atk)) {			
			return true;
		}
		// pokemon still under status status
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
									
			// if pokemon hurt itself in confusion
			if (confusionDamage(atk)) {
				return false;
			}
			else { 
				return true;
			}			
		}
	}
	private boolean confusionDamage(int atk) throws InterruptedException {

		// 1/2 chance of hurting self
		int val = 1 + (int)(Math.random() * 2);	
				
		if (val == 1) {					
			
			double level = fighter[atk].getLevel();
			double power = 1.0;
			double A = fighter[atk].getAttack();
			double D = fighter[atk].getDefense();
					
			// confusion damage reference: https://fighterlp.fandom.com/wiki/Confusion_(status)
			int damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
				power * (A / D)) / 50)) + 2));
		
			int hp = fighter[atk].getHP() - damage;
			
			// pokemon defeated itself in confusion damage
			if (hp <= 0) {
				hp = 0;					
			}									
			
			fighter[atk].setHit(true);
			gp.playSE(battle_SE, "hit-normal");	
			decreaseHP(atk, hp, damage);	
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			
			return true;	
		}
		else {
			return false;
		}
	}
	private boolean recoverStatus(int atk) throws InterruptedException {
		
		// if first move under status, set number of moves until free (1-5)
		if (fighter[atk].getStatusLimit() == 0) {
			fighter[atk].setStatusLimit((int)(Math.random() * 5));	
		}
		
		// if number of moves under status hit limit, remove status
		if (fighter[atk].getStatusCounter() >= fighter[atk].getStatusLimit()) {									
			
			Status status = fighter[atk].getStatus();
			fighter[atk].setStatusCounter(0); 
			fighter[atk].setStatusLimit(0);			
			fighter[atk].setStatus(null);
			typeDialogue(fighter[atk].getName() + status.printRecover());				
			
			return true;
		}
		else {
			return false;
		}
	}
	
	// MOVE METHOD
	private void move() throws InterruptedException {		
				
		int atk = currentTurn == playerTurn ? 0 : 1;
		int trg = currentTurn == playerTurn ? 1 : 0;		
		Move move = currentTurn == playerTurn ? playerMove : cpuMove;
		
		getWeatherMoveDelay(move);
			
		if (move.getTurns() <= 1 ^ move.getCoolDown() || move.getMType() == MoveType.WEATHER) {				
			
			typeDialogue(fighter[atk].getName() + " used\n" + move.toString() + "!", false); 
			
			fighter[atk].setAttacking(true);
			playSE(moves_SE, move.getName());	
									
			if (move.getCoolDown()) {
				
				// reduce number of turns to wait
				move.setTurns(move.getTurns() - 1);	
				
				fighter[atk].setWaiting(true);
			}
			else {
				// reset turns to wait
				move.setTurns(move.getNumTurns());	
				
				fighter[atk].setWaiting(false);	
			}
			
			// decrease move pp
			if (fighter[trg].getAbility().getCategory() == Ability.Category.PP) {
				move.setpp(move.getpp() - (int) fighter[trg].getAbility().getFactor());
			}
			else {				
				move.setpp(move.getpp() - 1);
			}						
			
			attack(atk, trg, move);				
		}		
		else if (move.getTurns() == move.getNumTurns()) {				
			
			typeDialogue(fighter[atk].getName() + " used\n" + move.toString() + "!");
			typeDialogue(move.getDelay(fighter[atk].getName()));				
			
			// reduce number of turns to wait
			move.setTurns(move.getTurns() - 1);	
			
			if (move.getIsProtected()) {
				fighter[atk].setProtected(true);
			}
			
			fighter[atk].setWaiting(true);
				
			currentTurn = nextTurn;	
			nextTurn = -1;							
		}	
		else {
			typeDialogue(move.getDelay(fighter[atk].getName()));				
			
			// reduce number of turns to wait
			move.setTurns(move.getTurns() - 1);	
			if (move.getTurns() == 0) {
				fighter[atk].setWaiting(false);
				move.setTurns(move.getNumTurns());
			}
				
			currentTurn = nextTurn;	
			nextTurn = -1;	
		}
	}		
	private void getWeatherMoveDelay(Move move) {
		if (weather == Weather.SUNLIGHT && move.getMove() == Moves.SOLARBEAM) {
			move.setTurns(1);
		}
	}
	
	// ATTACK METHOD
	public void attack(int atk, int trg, Move move) throws InterruptedException {
			
		if (hit(atk, trg, move)) {
			
			switch (move.getMType()) {
			
				case STATUS:
					statusMove(atk, trg, move);		
					currentTurn = nextTurn;	
					nextTurn = -1;
					break;
					
				case ATTRIBUTE:
					attributeMove(atk, trg, move);
					currentTurn = nextTurn;	
					nextTurn = -1;
					break;
					
				case WEATHER:
					weatherMove(atk, move);
					currentTurn = nextTurn;	
					nextTurn = -1;
					break;
					
				case OTHER:
					if (move.getMove() == Moves.TELEPORT) {
						
					}
					break;
					
				default:
					damageMove(atk, trg, move);
					break;
			}		
		}
		else {
			typeDialogue("The attack missed!");
			currentTurn = nextTurn;	
			nextTurn = -1;
		}			
	}		
	private boolean hit(int atk, int trg, Move atkMove) {
				
		if (fighter[trg].isProtected()) {
			return false;
		}
		
		// if move never misses, return true
		if (atkMove.getAccuracy() == -1) {
			return true; 
		}
		
		double accuracy = getMoveAccuracy(atkMove) * fighter[atk].getAccuracy();
		
		Random r = new Random();
		float chance = r.nextFloat();
		
		// chance of missing is accuracy / 100
		return (chance <= ((float) accuracy / 100)) ? true : false;
	}
	private double getMoveAccuracy(Move move) {
		
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
	
	// MOVE TYPE METHODS
	private void statusMove(int atk, int trg, Move move) throws InterruptedException {
		
		// if pokemon does not already have status affect
		if (fighter[trg].getStatus() == null) {			
			setStatus(fighter[atk], move.getEffect());	
		}
		// pokemon already has status affect
		else {
			typeDialogue(fighter[trg].getName() + " is\nalready " + 
					fighter[trg].getStatus().getStatus() + "!");
		}
	}
	private void attributeMove(int atk, int trg, Move move) throws InterruptedException {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			if (move.getMove() == Moves.REST) {
				
				int gainedHP = fighter[atk].getBHP() - fighter[atk].getHP();
				int result = fighter[atk].getHP() + gainedHP;
				increaseHP(fighter[atk], result, gainedHP);
				
				setStatus(fighter[atk], Status.SLEEP);		
			}
			else {
				
				// loop through each specified attribute to be changed
				for (String stat : move.getStats()) {				
					
					// attributes raised
					if (move.getLevel() > 0) {
						gp.playSE(battle_SE, "stat-up");
					}
					// attributes lowered
					else  {
						gp.playSE(battle_SE, "stat-down");
					}
					
					typeDialogue(fighter[atk].changeStat(stat, move.getLevel()));	
				}				
			}
		}
		// if move changes target attributes
		else {
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) {
								
				// attributes raised
				if (move.getLevel() > 0) {
					gp.playSE(battle_SE, "stat-up");
				}
				// attributes lowered
				else  {
					gp.playSE(battle_SE, "stat-down");
				}
				
				typeDialogue(fighter[trg].changeStat(stat, move.getLevel()));
			}
		}			
	}
	private void weatherMove(int atk, Move move) throws InterruptedException {		
		weather = Weather.valueOf(move.getWeather());
		checkWeatherCondition();
		weatherDays = move.getNumTurns();
	}
	private void damageMove(int atk, int trg, Move move) throws InterruptedException {
		
		// get critical damage (1 or 1.5)
		double crit = isCritical(atk, trg, move);
				
		int damage = 1;
		
		// logic for seismic toss
		if (move.getPower() == -1) {
			 damage = fighter[atk].getLevel();
		}
		else {
			damage = calculateDamage(atk, trg, move, crit, false);
		}
	
		// no damage dealt
		if (damage <= 0) {
			typeDialogue("It had no effect!");
			currentTurn = nextTurn;	
			nextTurn = -1;	
		}
		else {							
			dealDamage(atk, trg, move, damage, crit);
			absorbHP(atk, trg, move, damage);
			getRecoil(atk, trg, move, damage);
		}	
	}	
	
	// CALCULATION METHODS
	private double isCritical(int atk, int trg, Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		double impact  = 1.5;
						
		if (move.getCrit() == 1) {
			chance = 4;
		}
		
		if (fighter[trg].getAbility().getCategory() == Ability.Category.CRITICAL) {
			chance *= fighter[trg].getAbility().getFactor();
		}
		
		if (fighter[atk].getAbility().getCategory() == Ability.Category.CRITICAL) {
			impact = fighter[atk].getAbility().getFactor();
		}
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? impact : 1.0;					
	}
	private int calculateDamage(int atk, int trg, Move move, double crit, boolean cpu) throws InterruptedException {
		// DAMAGE FORMULA REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Damage (GEN IV)
		
		int damage = 0;
		
		double level = fighter[atk].getLevel();		
		double power = getPower(move, level);
		double A = getAttack(atk, move);
		double D = getDefense(trg, move);
		double STAB = fighter[atk].checkType(move.getType()) ? 1.5 : 1.0;
		double type1 = 1.0, type2 = 1.0, ability = 1.0;		
								
		Random r = new Random();
		double random = (double) (r.nextInt(100 - 85 + 1) + 85) / 100.0;
		
		if (fighter[trg].getTypes() == null) {
			Type trgType = fighter[trg].getType();
			type1 = effectiveness(trgType, move.getType());	
		}
		else {
			Type trgType = fighter[trg].getTypes().get(0);
			type1 = effectiveness(trgType, move.getType());	
			
			trgType = fighter[trg].getTypes().get(1);
			type2 = effectiveness(trgType, move.getType());	
		}
		
		if (fighter[atk].getAbility().getCategory() == Ability.Category.ATTACK &&
				fighter[atk].getAbility().isValid(fighter[atk], fighter[trg], move)) {
			
			A *= fighter[atk].getAbility().getFactor();
		}
		
		damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * crit * STAB * type1 * type2 * ability * random);
		
		if (fighter[trg].getAbility().getCategory() == Ability.Category.DEFENSE &&
				fighter[trg].getAbility().isValid(fighter[atk], fighter[trg], move)) {
			
			damage *= fighter[trg].getAbility().getFactor();
		}
		
		// keep damage dealt less than or equal to remaining HP
		if (fighter[trg].getHP() < damage) {
			damage = fighter[trg].getHP();
		}
		else if (damage < 0) {
			damage = 0;
		}
		
		return damage;
	}
	private double getPower(Move move, double level) {
		
		double power = 1.0; 
		
		power = (move.getPower() == -1) ? level : move.getPower();	
		
		switch (weather) {	
			case CLEAR:
				break;
			case SUNLIGHT:
				if (move.getType() == Type.FIRE) {
					power *= 1.5;
				}
				else if (move.getType() == Type.WATER) {
					power *= 0.5;
				}
				break;
			case RAIN:
				if (move.getType() == Type.WATER) {
					power *= 1.5;
				}
				else if (move.getType() == Type.FIRE) {
					power *= 0.5;
				}
				if (move.getMove() == Moves.SOLARBEAM) {
					power *= 0.5;
				}
				break;
			case HAIL:
				if (move.getMove() == Moves.SOLARBEAM) {
					power *= 0.5;
				}
				break;
			case SANDSTORM:
				if (move.getMove() == Moves.SOLARBEAM) {
					power *= 0.5;
				}
				break;
		}		
		
		return power;
	}	
	private double getAttack(int atk, Move move) {
		
		double attack = 1.0;
		
		if (move.getMType().equals(MoveType.SPECIAL)) {
			attack = fighter[atk].getSpAttack();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			attack = fighter[atk].getAttack();
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
	private double getDefense(int trg, Move move) {
		
		double defense = 1.0;
		
		if (move.getMType().equals(MoveType.SPECIAL)) {
			defense = fighter[trg].getSpDefense();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			defense = fighter[trg].getDefense();
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
				if (fighter[trg].getType() == Type.ROCK &&
						move.getMType().equals(MoveType.SPECIAL)) {
					defense *= 1.5;
				}
				break;
		}			
		
		return defense;
	}
	private double effectiveness(Type trgType, Type type) {
		
		// default value
		double effect = 1.0;
		
		// if vulnerable, retrieve and return vulnerable value		
		for (Type vulnType : trgType.getVulnerability().keySet()) {		
			if (vulnType.getName().equals(type.getName())) {
				effect = trgType.getVulnerability().get(vulnType);
				return effect;
			}
		}			
		// if resistant, retrieve and return resistance value
		for (Type resType : trgType.getResistance().keySet()) {			
			if (resType.getName().equals(type.getName())) {
				effect = trgType.getResistance().get(resType);
				return effect;
			}			
		}
		
		if (effect == 0.75)	{
			effect = 1;		
		}
								
		return effect;
	}
	
	// POST MOVE METHODS
	private void dealDamage(int atk, int trg, Move move, int damage, double crit) throws InterruptedException {		

		String hitEffectiveness = "";
		
		if (fighter[trg].getTypes() != null) {
			hitEffectiveness = getHitSE(effectiveness(fighter[trg].getTypes().get(0), move.getType()));	
		}
		else {
			hitEffectiveness = getHitSE(effectiveness(fighter[trg].getType(), move.getType()));		
		}		
		
		gp.playSE(battle_SE, hitEffectiveness);
		fighter[trg].setHit(true);
		
		decreaseHP(trg, fighter[trg].getHP() - damage, damage);
		
		if (hitEffectiveness.equals("hit-super")) {
			typeDialogue("It's super effective!");
		}
		else if (hitEffectiveness.equals("hit-weak")) {
			typeDialogue("It's not very effective...");
		}
		
		// if critical hit
		if (crit == 1.5) {
			typeDialogue("A critical hit!");
		}
		
		typeDialogue(fighter[trg].getName() + " took\n" + damage + " damage!");	
		
		if (fighter[trg].getHP() > 0) {
			
			applyEffect(atk, trg, move);			
			if (nextTurn != -1) {
				getFlinch(trg, move);
			}
			else {
				currentTurn = nextTurn;	
				nextTurn = -1;	
			}			
		}
		else {
			currentTurn = -1;
			nextTurn = -1;		
		}	
	}
	private String getHitSE(double effectiveness) throws InterruptedException {
		
		String hit = "";
		
		switch (Double.toString(effectiveness)) {
			case "0.25": hit = "hit-weak"; break;			
			case "0.5": hit = "hit-weak"; break;
			case "1.0": hit = "hit-normal"; break;
			case "1.5": hit = "hit-super"; break;
			case "2.25": hit = "hit-super"; break;			
			default: hit = "hit-normal"; break;
		}
		
		return hit;
	}
	private void absorbHP(int atk, int trg, Move move, int damage) throws InterruptedException {
		
		if (move.getName().equals(Moves.ABSORB.getName()) || 
				move.getName().equals(Moves.GIGADRAIN.getName())) {
			
			int gainedHP = (damage / 2);
			int result = fighter[atk].getHP() + gainedHP;
			
			// if attacker not at full health
			if (fighter[atk].getHP() != fighter[atk].getBHP()) {
				
				// if gained hp is greater than total hp
				if (gainedHP + fighter[atk].getHP() > fighter[atk].getBHP()) {
					
					// gained hp is set to amount need to hit hp limit									
					gainedHP = fighter[atk].getBHP() - fighter[atk].getHP();					
					
					increaseHP(fighter[atk], fighter[atk].getBHP(), gainedHP);					
				}
				else {		
					increaseHP(fighter[atk], result, gainedHP);
				}
				
				typeDialogue(fighter[atk].getName() + "\nabsorbed " + gainedHP + " HP!");
			}
		}
	}
	private void getRecoil(int atk, int trg, Move move, int damage) throws InterruptedException {
		
		if (move.getSelfInflict() != 0.0) {	
			
			int recoilDamage = (int)(Math.ceil(damage * move.getSelfInflict()));	

			// subtract damage dealt from total HP
			int result = fighter[atk].getHP() - recoilDamage;		
			
			if (fighter[atk].getAbility().getCategory() == Ability.Category.RECOIL) {
				result *= fighter[atk].getAbility().getFactor();
			}

			// set HP to 0 if below 0
			if (result <= 0) {
				result = 0;
				currentTurn = -1;
				nextTurn = -1;		
			}
			else {
				decreaseHP(atk, result, recoilDamage);
				typeDialogue(fighter[atk].getName() + " was hit\nwith recoil damage!");		
			}			
		}
	}
	private void applyEffect(int atk, int trg, Move move) throws InterruptedException {

		// if not already affected by a status effect
		if (fighter[trg].getStatus() == null) {
			
			// move causes attribute or status effect
			if (move.getProbability() != 0.0) {								
											
				// chance for effect to apply
				if (new Random().nextDouble() <= move.getProbability()) {
					
					if (move.getStats() != null) {
						attributeMove(atk, trg, move);
					}
					else {			
						setStatus(fighter[trg], move.getEffect());						
					}
				}							
			}	
			else if (fighter[trg].getAbility().getCategory() == Ability.Category.STATUS) {				
				setStatus(fighter[atk], fighter[trg].getAbility().getEffect());
			}
		}		
	}
	private void getFlinch(int trg, Move move) throws InterruptedException {
		
		if (move.getFlinch() != 0.0 && 
				fighter[trg].getAbility().getCategory() != Ability.Category.FLINCH) {
			
			// chance for move to cause flinch
			if (new Random().nextDouble() <= move.getFlinch()) {
				typeDialogue(fighter[trg].getName() + " flinched!");
				
				currentTurn = -1;	
				nextTurn = -1;	
				
				return;
			}
		}
	
		currentTurn = nextTurn;	
		nextTurn = -1;		
	}
			
	// STATUS METHODS
	private void checkStatusDamage() throws InterruptedException {					
		if (fighter[0].isAlive()) {
			getStatusDamage(0);
		}	
		if (fighter[1].isAlive()) {
			getStatusDamage(1);
		}	
	}
	private void getStatusDamage(int atk) throws InterruptedException {
		// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects		
		
		if (fighter[atk].getStatus() != null) {				
			
			pause(500);
			
			if (fighter[atk].getStatus().getAbreviation().equals("PSN") || 
					fighter[atk].getStatus().getAbreviation().equals("BRN")) {
									
				int damage = (int) Math.ceil((fighter[atk].getHP() * 0.16));
				int newHP = fighter[atk].getHP() - damage;		
				
				if (newHP <= 0) {
					newHP = 0;
				}
				
				gp.playSE(battle_SE, fighter[atk].getStatus().getStatus());
				fighter[atk].setHit(true);
				decreaseHP(atk, newHP, damage);
				
				fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			}		
		}	
	}
	
	// WEATHER METHODS
	private void checkWeatherDamage() throws InterruptedException {
					
		if (weatherDays != -1) {
						
			if (weatherDays == 0) {
												
				switch (weather) {		
					case CLEAR:
						break;
					case SUNLIGHT:
						typeDialogue("The sunlight grew gentle!");	
						break;
					case RAIN:
						typeDialogue("The rain stopped falling!");	
						break;
					case HAIL:
						typeDialogue("The hail stopped falling!");					
						break;
					case SANDSTORM:
						typeDialogue("The sandstorm subsided!");					
						break;
				}
				
				weather = Weather.CLEAR;
				weatherDays = -1;
			}
			else {
				weatherDays--;
			}
		}
		
		switch (weather) {		
			case CLEAR:
				break;
			case SUNLIGHT:
				gp.playSE(battle_SE, "sunlight");
				typeDialogue("The sunlight continues\nto shine intensely!");	
				break;
			case RAIN:
				gp.playSE(battle_SE, "rain");
				typeDialogue("Rain continues to fall!");	
				break;
			case HAIL:
				gp.playSE(battle_SE, "hail");
				typeDialogue("Hail continues to fall!");	
				
				if (fighter[0].isAlive() &&
						!fighter[0].checkType(Type.ICE)) {
					getWeatherDamage(0);
				}
				if (fighter[1].isAlive() &&
						!fighter[1].checkType(Type.ICE)) {
					getWeatherDamage(1);
				}	
				
				break;
			case SANDSTORM:
				gp.playSE(battle_SE, "sandstorm");
				typeDialogue("The sandstorm continues to rage!");	
				
				if (fighter[0].isAlive() &&
						!fighter[0].checkType(Type.ROCK) && 
						!fighter[0].checkType(Type.STEEL) &&
						!fighter[0].checkType(Type.GROUND)) {
					getWeatherDamage(0);
				}
				if (fighter[1].isAlive() &&
						!fighter[1].checkType(Type.ROCK) && 
						!fighter[1].checkType(Type.STEEL) &&
						!fighter[1].checkType(Type.GROUND)) {
					getWeatherDamage(1);
				}
				
				break;
		}
	}
	private void getWeatherDamage(int atk) throws InterruptedException {
		
		int damage = (int) Math.ceil((fighter[atk].getHP() * 0.0625));
		int newHP = fighter[atk].getHP() - damage;		
		
		if (newHP <= 0) {
			newHP = 0;
		}
		
		gp.playSE(battle_SE, "hit-normal");
		fighter[atk].setHit(true);
		decreaseHP(atk, newHP, damage);
		
		typeDialogue(fighter[atk].getName() + " was hurt\nby the " + weather.toString().toLowerCase() + "!");
	}
	
	// GET WINNING POKEMON METHODS
	private boolean hasWinningPokemon() {
		
		for (int i = 0; i <= 1; i++) {
			if (fighter[i].getHP() <= 0) {
				fighter[i].setHP(0);
				fighter[i].setAlive(false);
			}
		}	
		
		// TIE
		if (!fighter[0].isAlive() && !fighter[1].isAlive()) {
			winner = 2;
			loser = 2;
			return true;
		}			
		// PLAYER 2 WINS
		else if (!fighter[0].isAlive()) {	
			otherFighters.remove(fighter[0]);
			winner = 1;
			loser = 0;
			return true;
		}
		// PLAYER 1 WINS
		else if (!fighter[1].isAlive()) {
			winner = 0;
			loser = 1;
			return true;
		}
		else {
			return false;
		}
	}
	public void getWinningPokemon() throws InterruptedException {
		
		// TRAINER 1 WINNER
		if (winner == 0) {			
			
			gp.playSE(faint_SE, fighter[1].toString());
			typeDialogue(fighter[1].getName() + " fainted!");	
			
			gainXP();
									
			fightStage = fight_Swap;
			gp.ui.battleState = gp.ui.battle_Dialogue;
		}
		// TRAINER 2 WINNER
		else if (winner == 1) {			
			
			gp.playSE(faint_SE, fighter[0].toString());			
			typeDialogue(fighter[0].getName() + " fainted!");	

			getWinningTrainer();
		}
		// TIE GAME
		else if (winner == 2) {
		
			gp.playSE(faint_SE, fighter[0].toString());
			typeDialogue(fighter[0].getName() + " fainted!");
			
			gp.playSE(faint_SE, fighter[1].toString());
			typeDialogue(fighter[1].getName() + " fainted!");

			getWinningTrainer();
		}
	}		
	private void gainXP() throws InterruptedException {
		
		int gainedXP = calculateXP(loser);
		
		if (otherFighters.size() > 0) {
			
			gainedXP = (int) Math.ceil(gainedXP / (otherFighters.size() + 1));
			
			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2500.0 / (double) gainedXP);
			increaseXP(fighter[0], xp, expTimer);	
			
			for (Pokemon p : otherFighters) {
								
				typeDialogue(p.getName() + " gained\n" + gainedXP + " Exp. Points!");
				
				xp = p.getXP() + gainedXP;				
				increaseXP(p, xp, 0);
			}
			
			otherFighters.clear();
		}
		else {
			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2500.0 / (double) gainedXP);
			increaseXP(fighter[0], xp, expTimer);	
		}
		
		getOtherFighters();	
		pause(500);
	}
	private int calculateXP(int lsr) {
		// EXP FORMULA REFERENCE (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		
		double b = fighter[lsr].getEXPYeild();
		double L = fighter[lsr].getLevel();
		double s = 1.0;
		double e = 1.0;
		double a = battleMode == wildBattle ? 1.0 : 1.5;
		double t = 1.0;
		
		double exp = Math.floor( (b * L) / 7 ) * Math.floor(1 / s) * e * a * t;
						
		return (int) exp;
	}
	private void increaseXP(Pokemon p, int xp, int timer) throws InterruptedException {
		
		while (p.getXP() < xp) {
			
			p.setXP(p.getXP() + 1);
			
			pause(timer);
												
			// FIGHTER LEVELED UP
			if (p.getXP() >= p.getBXP() + p.getNextXP()) {			
				
				gp.pauseMusic();
				gp.playSE(battle_SE, "level-up");
				
				p.levelUp();					
				gp.ui.battleState = gp.ui.battle_LevelUp;
			
				gp.playSE(battle_SE, "upgrade");
				typeDialogue(p.getName() + " grew to\nLv. " + 
						(p.getLevel()) + "!", true);
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
								
				checkNewMove(p);
				
				gp.playMusic();
			}		
		}				
	}
	private void checkNewMove(Pokemon pokemon) throws InterruptedException {
		
		newMove = pokemon.getNewMove();				
		if (newMove != null) {
			
			if (pokemon.learnMove(newMove)) {
				
				gp.playSE(battle_SE, "upgrade");
				typeDialogue(pokemon.getName() + " learned\n" + 
						newMove.getName() + "!", true);
				
				newMove = null;
			}
			else {
				gp.playMusic();
				
				typeDialogue(pokemon.getName() + " is trying to\nlearn " + newMove.getName() + ".", true);	
				typeDialogue("But, " + pokemon.getName() + " can't learn\nmore than four moves.", true);	
				typeDialogue("Delete a move to make\nroom for " + newMove.getName() + "?", false);
				
				gp.ui.battleState = gp.ui.battle_Confirm;					
				waitForKeyPress();
				getMoveAnswer(pokemon);
			}					
		}
	}
	private void getMoveAnswer(Pokemon pokemon) throws InterruptedException {
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;	
			
			if (fightStage == fight_Evolve) {
				gp.ui.battleState = gp.ui.battle_Evolve;	
			}
			else {
				fightStage = fight_Swap;	
				gp.ui.battleState = gp.ui.battle_Dialogue;	
			}
			
			if (gp.ui.commandNum == 0) {	
				
				for (int i = 0; i < gp.player.pokeParty.size(); i++) {
					if (gp.player.pokeParty.get(i) == pokemon) {						
						gp.ui.fighterNum = i;
						break;
					}
				}
				
				gp.ui.partyState = gp.ui.party_MoveSwap;
				gp.gameState = gp.partyState;	
				
				while (oldMove == null && !gp.keyH.bPressed) {
					pause(5);
				}
				
				if (oldMove != null) {					
					typeDialogue("1, 2, and.. .. ..\nPoof!", true);
					typeDialogue(pokemon.getName() + " forgot\n" + oldMove.getName() + ".", true);	
					typeDialogue("And...", true);
					
					gp.pauseMusic();
					gp.playSE(battle_SE, "upgrade");
					
					typeDialogue(pokemon.getName() + " learned\n" + newMove.getName() + "!", true);	
					
					gp.playMusic();
				}
				else {
					typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
				}
			}
			else {
				typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
			}
			
			newMove = null;
			oldMove = null;
			gp.ui.commandNum = 0;			
		}			
		else if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			
			if (fightStage == fight_Evolve) {
				gp.ui.battleState = gp.ui.battle_Evolve;	
			}
			else {
				fightStage = fight_Swap;	
				gp.ui.battleState = gp.ui.battle_Dialogue;	
			}
			
			typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
			
			newMove = null;
			gp.ui.commandNum = 0;	
		}
	}
	
	// GET WINNING TRAINER METHODS
	private void getWinningTrainer() throws InterruptedException {
		
		// WILD BATTLE
		if (battleMode == wildBattle) {
			
			// TRAINER 1 WINNER
			if (winner == 0) {		
				pause(1000);
				fightStage = fight_Evolve;
			}
			// WILD POKEMON WINNER
			else if (winner == 1) {			
				
				// TRAINER 1 HAS MORE POKEMON
				if (gp.player.hasPokemon()) {
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.ui.partyState = gp.ui.party_Main_Select;
					gp.gameState = gp.partyState;
				}
				// TRAINER 1 OUT OF POKEMON
				else {					
					announceWinner();
				}			
			}			
		}
		// TRAINER BATTLE
		else {			
			gp.ui.battleState = gp.ui.battle_Dialogue;
			
			// TRAINER 1 WINNER
			if (winner == 0) {	
				
				// TRAINER 2 HAS MORE POKEMON
				if (trainer.hasPokemon()) {
					
					winner = -1;
					
					newFighter[1] = cpuSelectNextFighter();	
					
					if (gp.player.getAvailablePokemon() > 1 && !fighter[0].isWatiing()) {
						
						typeDialogue("Trainer " + trainer.name + " is about\nto sent out " + 
								newFighter[1].getName() + "!", true);
						
						typeDialogue("Will " + gp.player.name + " swap\nPokemon?", false);										
						
						gp.ui.battleState = gp.ui.battle_Confirm;					
						waitForKeyPress();
						getSwapAnswer();
					}
					else {
						gp.ui.resetFighterPositions();	
					}
				}
				// TRAINER 2 OUT OF POKEMON
				else {								
					announceWinner();
				}					
			}
			// TRAINER 2 WINNER
			else if (winner == 1) {
				
				// TRAINER 1 HAS MORE POKEMON
				if (gp.player.hasPokemon()) {
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.ui.partyState = gp.ui.party_Main_Select;
					gp.gameState = gp.partyState;
				}
				// TRAINER 1 OUT OF POKEMON
				else {				
					announceWinner();
				}							
			}		
			// TIE GAME
			else if (winner == 2) {
				
				// TRAINER 1 AND 2 HAVE MORE POKEMON
				if (gp.player.hasPokemon() && trainer.hasPokemon()) {
					
					// GET NEW FIGHTER
					newFighter[1] = cpuSelectNextFighter();	
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.ui.partyState = gp.ui.party_Main_Select;
					gp.gameState = gp.partyState;
				}
				// ONLY TRAINER 1 HAS MORE POKEMON
				else if (gp.player.hasPokemon()) {
					winner = 0;
					announceWinner();
				}
				// ONLY TRAINER 2 HAS MORE POKEMON
				else if (trainer.hasPokemon()) {
					winner = 1;
					announceWinner();					
				}
				// BOTH TRAINERS DEFEATED
				else {
					pause(500);
					endBattle();
				}
			}
		}
	}
	private void getSwapAnswer() {
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			gp.ui.resetFighterPositions();
			
			fightStage = fight_Swap;
			gp.ui.battleState = gp.ui.battle_Dialogue;						
			
			if (gp.ui.commandNum == 0) {		
				running = false;
				
				gp.ui.partyState = gp.ui.party_Main_Select;
				gp.gameState = gp.partyState;								
			}
			
			gp.ui.commandNum = 0;
		}			
	}
	private void announceWinner() throws InterruptedException {
		
		// TRAINER 1 VICTORY
		if (winner == 0) {
			
			gp.ui.fighter_two_X = gp.screenWidth + gp.tileSize;
			
			gp.ui.battleState = gp.ui.battle_End;
			
			gp.stopMusic();
			gp.startMusic(1, 5);					
			
			typeDialogue("Player defeated\nTrainer " + trainer.name + "!", true);
			
			trainer.isDefeated = true;
			trainer.hasBattle = false;
			
			int dialogueSet = trainer.dialogueSet + 1;
			typeDialogue(trainer.dialogues[dialogueSet][0], true);
			
			int moneyEarned = getMoney();
			gp.player.money += moneyEarned;
			typeDialogue(gp.player.name + " got $" + moneyEarned + "\nfor winning!", true);	
			
			fightStage = fight_Evolve;
		}
		// TRAINER 2 VICTORY
		else {
			typeDialogue(gp.player.name + " is out\nof Pokemon!", true);			
			pause(1000);
			
			endBattle();
		}		
	}	
	private int getMoney() {
		// MONEY EARNED FORMULA REFERENCE (ALL GEN): https://bulbapedia.bulbagarden.net/wiki/Prize_money
		
		int level = trainer.pokeParty.get(trainer.pokeParty.size() - 1).getLevel();
		int base = trainer.trainerClass;		
		int payout = base * level;
		
		return payout;
	}
	
	private void turnReset() {
		
		// RESET NON-DELAYED MOVES
		int delay = getDelayedMove();			
		if (delay == 0) { playerMove = null; cpuMove = null; }
		else if (delay == 1) cpuMove = null;			
		else if (delay == 2) playerMove = null;
		
		if (delay == 1 || delay == 3) {		
			fightStage = fight_Start;
		}
		else {				
			running = false;
			fightStage = fight_Start;
			gp.ui.battleState = gp.ui.battle_Options;
		}
	}
	
	// CAPTURE POKEMON METHODS
	private void throwPokeball() throws InterruptedException {
		
		if (battleMode == wildBattle) {			
			
			typeDialogue(gp.player.name + " used a\n" + ballUsed.name + "!", false);
			playSE(battle_SE, "ball-throw");
			gp.ui.isFighterCaptured = true;
			playSE(battle_SE, "ball-open");
			playSE(battle_SE, "ball-bounce");
			
			for (int i = 0; i < 3; i++) {
				playSE(battle_SE, "ball-shake");
				Thread.sleep(800);	
			}	
			
			if (isCaptured()) {				
				capturePokemon();
			}
			else {
				gp.playSE(battle_SE, "ball-open");
				gp.ui.isFighterCaptured = false;		
				gp.playSE(battle_SE, "ball-open");
				typeDialogue("Oh no!\n" + fighter[1].getName() + " broke free!", true);
				fightStage = fight_Start;			
			}	
		}
		else if (battleMode == trainerBattle) {
			typeDialogue("You can't use\nthat here!", true);
			gp.ui.battleState = gp.ui.battle_Options;
			running = false;
		}
		
		ballUsed = null;
	}
	private void capturePokemon() throws InterruptedException {
		
		gp.stopMusic();
		gp.startMusic(1, 3);										
		typeDialogue("Gotcha!\n" + fighter[1].getName() + " was caught!", true);
		
		if (gp.player.pokeParty.size() < 6) {
			fighter[1].resetMovesPP();
			fighter[1].setBall(ballUsed);
			gp.player.pokeParty.add(fighter[1]);
			typeDialogue(fighter[1].getName() + " was added\nto your party!", true);
		}
		else {
			
		}
		
		endBattle();
	}
	private boolean isCaptured() {
		/** CATCH RATE FOMRULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Catch_rate#Capture_method_(Generation_III-IV) **/
		
		boolean isCaptured = false;
		
		double catchOdds;
		double maxHP = 1.0;
		double hp = 1.0;
		double catchRate = 1.0;
		double statusBonus = 1.0;
		
		maxHP = fighter[1].getBHP(); if (maxHP == 0.0) maxHP = 1.0; 		
		hp = fighter[1].getHP(); if (hp == 0.0) hp = 1.0;
		catchRate = fighter[1].getCatchRate();
		
		if (fighter[1].getStatus() != null) {			
			switch(fighter[1].getStatus().getAbreviation()) {			
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
		int roll = (int) (r.nextInt(ballUsed.catchProbability - 0 + 1) + 0);
		
		if (roll <= catchOdds) {
			isCaptured = true;
		}
		
		return isCaptured;
	}
	
	// RUN AWAY METHOD
	public void escapeBattle() throws InterruptedException {
		/** ESCAPE FORMULA REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Escape **/
		
		boolean escape = false;
		
		if (battleMode == wildBattle) {
			double playerSpeed = fighter[0].getSpeed();
			double wildSpeed = fighter[1].getSpeed();
			
			if (playerSpeed > wildSpeed) {
				escape = true;
			}
			else {
				escapeAttempts++;
				double attempts = (double) escapeAttempts;				
				double escapeOdds = ((((playerSpeed * 128) / wildSpeed) + 30) * attempts);
				
				Random r = new Random();
				int roll = (int) (r.nextInt(255 - 0 + 1) + 0);
				
				if (roll <= escapeOdds) {
					escape = true;
				}
			}	
			
			if (escape) {
				gp.playSE(battle_SE, "run");
				typeDialogue("Got away safely!", true);
				endBattle();
			}
			else {				
				typeDialogue("Oh no!\nYou can't escape!", true);
				fightStage = fight_Start;
			}
		}
		else if (battleMode == trainerBattle) {
			typeDialogue("You can't flee\na trainer battle!", true);
			gp.ui.battleState = gp.ui.battle_Options;
			running = false;
		}
	}
	
	// EVOLVE METHODS
	private void checkEvolve() throws InterruptedException {
		gp.stopMusic();
		
		for (int i = 0; i < gp.player.pokeParty.size(); i++) {					
			if (gp.player.pokeParty.get(i).canEvolve()) {
				
				Pokemon oldEvolve = gp.player.pokeParty.get(i);
				Pokemon newEvolve = Pokemon.evolvePokemon(oldEvolve);
				
				gp.ui.evolvePokemon = oldEvolve;
				gp.ui.battleState = gp.ui.battle_Evolve;
				
				gp.playSE(3, gp.se.getFile(3, oldEvolve.toString()));
				
				typeDialogue("What?\n" + oldEvolve.getName() + " is evolving?");
												
				startEvolve(oldEvolve, newEvolve, i);
				
				break;
			}					
		}
		
		endBattle();			
	}
	private void startEvolve(Pokemon oldEvolve, Pokemon newEvolve, int index) throws InterruptedException {
		
		gp.startMusic(1, 0);	
		pause(1000);
		
		for (int i = 0; i < 30; i++) {
									
			if (i % 2 == 0) {
				gp.ui.evolvePokemon = oldEvolve;
			}
			else {
				gp.ui.evolvePokemon = newEvolve;
			}
			
			pause(500);
			
			if (gp.keyH.bPressed) {				
				gp.ui.evolvePokemon = oldEvolve;
				gp.stopMusic();				
				typeDialogue("Oh?\n" + oldEvolve.getName() + " stopped evolving...", true);
				return;
			}
		}
		gp.stopMusic();	
		playSE(3, newEvolve.toString());
		
		gp.player.pokeParty.set(index, newEvolve);				
		gp.ui.evolvePokemon = newEvolve;		
		
		gp.startMusic(1, 2);
		typeDialogue("Congratulations! Your " +  oldEvolve.getName() + 
				"\nevolved into " + newEvolve.getName() + "!", true);
		
		checkNewMove(newEvolve);
		
		return;
	}
	
	// BATTLE END METHOD
	public void endBattle() {
		gp.stopMusic();
		gp.setupMusic();
		
		gp.ui.isFighterCaptured = false;
		gp.ui.commandNum = 0;
		
		gp.particleList.clear();
		gp.gameState = gp.playState;
		
		fightStage = fight_Encounter;
		
		active = false;
		running = false;
		
		for (Pokemon p : gp.player.pokeParty) {
			p.setWaiting(false);
			p.setProtected(false);
			
			for (Move m : p.getMoveSet()) {
				m.setTurns(m.getTurns());
			}
		}
				
		currentTurn = -1;
		nextTurn = -1;
		
		trainer = null;
		fighter[0] = null;
		fighter[1] = null;
		newFighter[0] = null;
		newFighter[1] = null;
		otherFighters.clear();
		
		playerMove = null;
		cpuMove = null;		
		newMove = null;
		ballUsed = null;
		
		weather = Weather.CLEAR;
		weatherDays = -1;
		
		winner = -1;
		loser = -1;
		
		escapeAttempts = 0;
	}
	
	// MISC HANDLERS
	private void setStatus(Pokemon pkm, Status status) throws InterruptedException {
		
		pkm.setStatus(status);		
		
		gp.playSE(battle_SE, status.getStatus());
		typeDialogue(pkm.getName() + status.printCondition());	
	}
	public void typeDialogue(String dialogue) throws InterruptedException {
		
		gp.ui.battleDialogue = "";
		
		for (char letter : dialogue.toCharArray()) {
			gp.ui.battleDialogue += letter;
			pause(35);
		}		
		
		pause(1000);
	}
	public void typeDialogue(String dialogue, boolean canSkip) throws InterruptedException {
		
		gp.ui.battleDialogue = "";
		
		for (char letter : dialogue.toCharArray()) {
			gp.ui.battleDialogue += letter;
			pause(35);
		}		
		
		if (canSkip) {
			gp.ui.canSkip = true;
			waitForKeyPress();
			gp.ui.canSkip = false;
			gp.keyH.aPressed = false;
		}
	}
	private void waitForKeyPress() throws InterruptedException {
		while (!gp.keyH.aPressed) {
			pause(5);
		}
	}	
	private void increaseHP(Pokemon pkm, int newHP, int damage) throws InterruptedException {
		
		int hpTimer = getHPTimer(damage);
		
		while (pkm.getHP() < newHP) {			
			pkm.setHP(pkm.getHP() + 1);
			pause(hpTimer);
		}	 
	}
	private void decreaseHP(int target, int newHP, int damage) throws InterruptedException {
		
		int hpTimer = getHPTimer(damage);
		
		while (newHP < fighter[target].getHP()) {			
			fighter[target].setHP(fighter[target].getHP() - 1);
			pause(hpTimer);
		}	 
	}
	private int getHPTimer(int damage) {
		
		int hpTimer = 1;
		
		if (damage < 25) hpTimer = 70;
		else if (25 <= damage && damage < 50) hpTimer = 55;
		else if (50 <= damage && damage < 100) hpTimer = 40;
		else if (100 <= damage && damage < 200) hpTimer = 25;
		else if (200 <= damage && damage < 300) hpTimer = 15;
		else if (300 <= damage) hpTimer = 10;
						
		return hpTimer;		
	}
	
	private void pause(int time) throws InterruptedException {
		Thread.sleep(time); 
	}
	public void playSE(int cat, String name) throws InterruptedException {
		
		gp.playSE(cat, name);
		
		double soundDuration = (double)gp.se.getSoundDuration(cat, gp.se.getFile(cat, name));	
		soundDuration /= 0.06;	
		
		Thread.sleep((int) soundDuration);
	}
}