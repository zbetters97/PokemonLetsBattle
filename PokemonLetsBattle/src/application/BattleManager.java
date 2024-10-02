package application;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import entity.Entity;
import moves.Move;
import moves.Moves;
import moves.Move.MoveType;
import pokemon.Pokemon;
import properties.Type;

public class BattleManager extends Thread {
	
	private GamePanel gp;
	public boolean active = false;
	public boolean running = true;
	
	// GENERAL VALUES		
	public Entity[] trainer = new Entity[2];
	public Pokemon[] fighter = new Pokemon[2];
	public Pokemon[] newFighter = new Pokemon[2];
	public Move move1, move2;
	public int winner = -1, loser = -1;	
			
	// TURN VALUES
	public int currentTurn;
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
	public final int fight_Move = 4;
	public final int fight_Over = 5;	
			
	// CONSTRUCTOR
	public BattleManager(GamePanel gp) {
		this.gp = gp;
	}
	public void setup(int currentBattle, Entity trainer, Pokemon pokemon) {
		
		battleMode = currentBattle;
						
		if (trainer != null) this.trainer[1] = trainer;	
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
					try { checkTurn(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;
					
				case fight_Move:
					try { startMove(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;
					
				case fight_Over:
					try { getWinningTrainer(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;					
			}		
		}
		
	}
	
	private void setBattle() throws InterruptedException {
		
		gp.stopMusic();	
		
		trainer[0] = gp.player;
		newFighter[0] = trainer[0].pokeParty.get(0);			
				
		getBattleMode();
	}	
	private void getBattleMode() throws InterruptedException {
		
		switch(battleMode) {
			case wildBattle:
				
				gp.playMusic(1, 2);				
				pause(1400);
				
				gp.ui.addBattleDialogue("A wild " + fighter[1].getName() + "\nappeared!", true);	
				gp.playSE(cry_SE, fighter[1].toString());	
				
				break;
			case trainerBattle: 
				
				gp.playMusic(1, 3);				
				pause(1400);		
				
				gp.ui.addBattleDialogue("Trainer " + trainer[1].name + "\nwould like to battle!", true);				
				newFighter[1] = trainer[1].pokeParty.get(0);
				
				break;			
		}
				
		running = false;
		fightStage = fight_Swap;
	}
	
	// SWAP POKEMON METHODS
	private void swapFighters() throws InterruptedException {
				
		// WINNER NOT YET DECIDED
		if (winner == -1) {
			
			gp.ui.battleState = gp.ui.battle_Turn;
									
			// TRAINER 2 FORCE SWAP OUT
			if (fighter[1] == null || !fighter[1].isAlive()) {		
												
				fighter[1] = newFighter[1];
				
				gp.ui.addBattleDialogue("Trainer " + trainer[1].name + "\nsent out " + fighter[1].getName() + "!");
				gp.playSE(cry_SE, fighter[1].toString());	
				pause(1800);
			}		
			// TRAINER 1 FORCE SWAP OUT
			if ((fighter[0] == null || !fighter[0].isAlive()) ||
					(newFighter[0] != null && newFighter[1] != null)) {	
				
				fighter[0] = newFighter[0];
				
				gp.ui.addBattleDialogue("GO, " + fighter[0].getName() + "!");
				gp.playSE(cry_SE, fighter[0].toString());		
				pause(1400);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				running = false;		
				
				gp.ui.battleState = gp.ui.battle_Options;
			}	
			// TRAINER 1 SWAP OUT (SAME TRAINER 2 FIGHTER)
			else if (newFighter[0] != null && newFighter[1] == null) {
				
				fighter[0] = newFighter[0];
				
				gp.ui.addBattleDialogue("GO, " + fighter[0].getName() + "!");
				gp.playSE(cry_SE, fighter[0].toString());
				pause(1400);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				fightStage = fight_Start;
			}			
			else {
				newFighter[0] = null;
				newFighter[1] = null;
				
				running = false;	
				
				gp.ui.battleState = gp.ui.battle_Options;
			}
		}				
		// TRAINER HAS WON
		else {							
			getWinningTrainer();
		}
	}
	private void getWinningTrainer() throws InterruptedException {
		
		if (battleMode == wildBattle) {
			
			// TRAINER 1 WINNER
			if (winner == 0) {				
				endBattle();	
			}
			// WILD POKEMON WINNER
			else if (winner == 1){
				
				// TRAINER 1 HAS MORE POKEMON
				if (trainer[0].hasPokemon()) {				
					winner = -1;					
					gp.ui.partyState = gp.ui.party_Main;
					gp.gameState = gp.partyState;
				}
				// TRAINER 1 OUT OF POKEMON
				else {
					
				}	
			}			
		}
		else {			
			gp.ui.battleState = gp.ui.battle_Turn;
			
			if (winner == 0) {
				
				// GET NEW FIGHTER
				newFighter[1] = cpuSelectNextFighter();	
				
				// TRAINER 2 HAS MORE POKEMON
				if (newFighter[1] != null) {
					
					winner = -1;
					
					if (trainer[0].getAvailablePokemon() > 1) {
						gp.ui.addBattleDialogue("Trainer " + trainer[1].name + " is about\nto sent out " + newFighter[1].getName() + "!");
						pause(1800);						
						
						running = false;
						gp.ui.battleState = gp.ui.battle_Swap;
					}
				}
				// TRAINER 2 OUT OF POKEMON
				else {									
					gp.ui.addBattleDialogue("Player defeated\nTrainer " + trainer[1].name + "!");
					pause(1600);
					
					gp.stopMusic();
					gp.playMusic(1, 0);
				}	
			}
			else if (winner == 1) {
				
				// TRAINER 1 HAS MORE POKEMON
				if (trainer[0].hasPokemon()) {
					
					winner = -1;				
					
					running = false;
					fightStage = fight_Swap;
					gp.ui.partyState = gp.ui.party_Main;
					gp.gameState = gp.partyState;
				}
				// TRAINER 1 OUT OF POKEMON
				else {
					
				}		
			}		
		}
	}
	
	// CHECK TURN METHODS
	private void checkTurn() throws InterruptedException {
						
		// GET CPU MOVE IF NO CPU DELAY
		int delay = getDelayedMove();
		
		if (delay == 0 || delay == 1) {
			move2 = cpuSelectMove();
		}
		
		// SET ORDER OF TURNS
		setRotation();
		
		fightStage = fight_Move;
	}
	public int getDelayedMove() {		
		
		// 3 if both, 1 if player 1, 2 if player 2, 0 if neither
		
		// if both moves are active
		if (move1 != null && move2 != null) {	
			
			//both players are waiting
			if (move1.getTurns() != move1.getNumTurns() && move2.getTurns() != move2.getNumTurns())
				return 3;			
			else if (move1.getTurns() != move1.getNumTurns())
				return 1;
			else if (move2.getTurns() != move2.getNumTurns())
				return 2;
		}
		// if only player 1 is active
		else if (move1 != null) {
			if (move1.getTurns() != move1.getNumTurns())
				return 1;
		}
		// if only player 2 is active
		else if (move2 != null) {
			if (move2.getTurns() != move2.getNumTurns())
				return 2;
		}
		
		return 0;
	}	
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
		
		if (move1 == null && move2 == null) {
			return 5;
		}
		else if (move1 == null) {
			return 3;
		}
		else if (move2 == null) {
			return 4;
		}
		else {		
			// if both moves go first (EX: Quick Attack)
			if (move1.getGoFirst() && move2.getGoFirst()) {			
				
				// if fighter_one is faster (fighter_one has advantage if equal)
				if (fighter[0].getSpeed() >= fighter[1].getSpeed()) 
					return 1;
				else 
					return 2;
			}
			// if only move1 goes first (EX: Quick Attack)
			else if (move1.getGoFirst()) 
				return 1;
			// if only move2 goes first (EX: Quick Attack)
			else if (move2.getGoFirst()) 
				return 2;
			else {
				// if fighter_one is faster
				if (fighter[0].getSpeed() > fighter[1].getSpeed())
					return 1;
				// if both pokemon have equal speed, coin flip decides
				else if (fighter[0].getSpeed() == fighter[1].getSpeed()) {
					Random r = new Random();
					return (r.nextFloat() <= ((float) 1 / 2)) ? 1 : 2;
				}
				else
					return 2;
			}
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
	
	// STATUS CONDITIONS
	private boolean paralyzed(int atk) throws InterruptedException {
		
		// 1/4 chance can't move due to PAR
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {		
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			pause(1400);
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
			gp.ui.addBattleDialogue(fighter[atk].getName() + "" + fighter[atk].getStatus().printRecover());
			fighter[atk].setStatus(null);
			return true;
		}
		else {	
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			pause(1400);
			return false;		
		}
	}
	private boolean asleep(int atk) throws InterruptedException {
		
		// if number of moves under condition hit limit, remove condition
		if (recoverCondition(atk)) {			
			return true;
		}
		// pokemon still under status condition
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			pause(1400);
			
			return false;
		}
	}
	private boolean confused(int atk) throws InterruptedException {
		
		gp.ui.addBattleDialogue(fighter[atk].getName() + " is\n" + fighter[atk].getStatus().getCondition() + "...");
		gp.playSE(battle_SE, fighter[atk].getStatus().getCondition());
		pause(1400);
		
		// if number of moves under condition hit limit, remove condition
		if (recoverCondition(atk)) {			
			return true;
		}
		// pokemon still under status condition
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
			
			fighter[atk].setHP(hp);					
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			pause(1400);
			
			return true;	
		}
		else {
			return false;
		}
	}
	private boolean recoverCondition(int atk) throws InterruptedException {
		
		// if first move under condition, set number of moves until free (1-5)
		if (fighter[atk].getStatusLimit() == 0) {
			fighter[atk].setStatusLimit((int)(Math.random() * 5));	
		}
		
		// if number of moves under condition hit limit, remove condition
		if (fighter[atk].getStatusCounter() >= fighter[atk].getStatusLimit()) {
									
			gp.ui.addBattleDialogue(fighter[atk].getName() + fighter[atk].getStatus().printRecover());	
			fighter[atk].setStatusCounter(0); fighter[atk].setStatusLimit(0);
			fighter[atk].setStatus(null);
			pause(1400);
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean swapPokemon(int partySlot) {
		
		if (fighter[0] == trainer[0].pokeParty.get(partySlot)) {
			return false;
		}
		
		if (trainer[0].pokeParty.get(partySlot).isAlive()) {
			
			newFighter[0] = trainer[0].pokeParty.get(partySlot);
						
			if (trainer[0].pokeParty.size() > 1) {
				Collections.swap(trainer[0].pokeParty, 0, partySlot);	
			}
						
			return true;
		}
		else {
			return false;
		}		
	}
	private Pokemon cpuSelectNextFighter() {
		
		if (fighter[0] == null) return trainer[1].pokeParty.get(0);
		
		int available = 0;
		
		// list to hold all candidates based on type effectiveness
		Map<Pokemon, Integer> pokemonList = new HashMap<>();
		
		for (Pokemon p : trainer[1].pokeParty) {
			if (p.isAlive()) {
				available++;
			}
		}
		
		// if more than 1 pokemon in CPU party
		if (available > 1) {
			
			// loop through each pokemon in party
			for (Pokemon p : trainer[1].pokeParty) {
				
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
			for (Pokemon p : trainer[1].pokeParty) {
				
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
			for (Pokemon p : trainer[1].pokeParty) {
				pokemonList.put(p, p.getLevel());
			}
			
			bestPokemon = Collections.max(pokemonList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			return bestPokemon;
		}
	}
	
	// SELECT MOVE METHODS
	public void setPlayerMove(int selection) {
		move1 = fighter[0].getMoveSet().get(selection);	
		fightStage = fight_Start;
	}
	private Move cpuSelectMove() throws InterruptedException {
		
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
		
		Move bestMove;
		
		// find max value in moves list based on value
		if (!moves.isEmpty()) {
			
			// 33% chance CPU selects random move instead of most powerful			
			int val = 1 + (int)(Math.random() * 4);
			if (val == 1) {				
				int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
				bestMove = fighter[1].getMoveSet().get(ranMove);
			}
			else
				bestMove = Collections.max(moves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 	
		}
		// if list is empty, select random move
		else {
			int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
			bestMove = fighter[1].getMoveSet().get(ranMove);
		}
		
		return bestMove;	
	}
			
	// START MOVE METHODS
	private void startMove() throws InterruptedException {	
		
		if (currentTurn != -1) {		

			if (canMove()) {					
				if (currentTurn == playerTurn) {
					useMove(0, 1, move1, move2);	
				}
				else if (currentTurn == cpuTurn) {
					useMove(1, 0, move2, move1);
				}					
			}
			else {
				currentTurn = nextTurn;
				nextTurn = -1;
			}
		}
		else {			
			
			// RESET NON-DELAYED MOVES
			int delay = getDelayedMove();			
			if (delay == 0) { move1 = null; move2 = null; }
			else if (delay == 1) move2 = null;			
			else if (delay == 2) move1 = null;
			
			checkStatusDamage();
		}			
	}	
	private void useMove(int atk, int trg, Move atkMove, Move trgMove) throws InterruptedException {		
				
		gp.ui.addBattleDialogue(fighter[atk].getName() + " used\n" + atkMove.toString() + "!"); 
		pause(800); 
		
		// if not delayed move or delayed move is ready
		if (1 >= atkMove.getTurns()) {	
			playSE(moves_SE, atkMove.getName());
			
			// reset turns to wait
			atkMove.setTurns(atkMove.getNumTurns());
			
			// decrease move pp
			atkMove.setpp(atkMove.getpp() - 1);
			
			attack();
		}
		// delayed move is used for first time
		else if (atkMove.getTurns() == atkMove.getNumTurns()) {
					
			gp.ui.addBattleDialogue(atkMove.getDelay(fighter[atk].getName()));	
			pause(1300);
									
			// reduce number of turns to wait
			atkMove.setTurns(atkMove.getTurns() - 1);	
			
			currentTurn = nextTurn;	
			nextTurn = -1;
		}	
	}		
	
	// ATTACK METHOD
	public void attack() throws InterruptedException {
		
		int atk = 0;
		int trg = 0;
		
		Move move = null;
		Move trgMove = null;
		
		if (currentTurn == playerTurn) {
			atk = 0;
			trg = 1;
			move = move1;
			trgMove = move2;
		}
		else if (currentTurn == cpuTurn){
			atk = 1;
			trg = 0;
			move = move2;
			trgMove = move1;
		}
		
        // if attack lands
		if (hit(atk, move, trgMove)) {
			
			// move has a status affect
			if (move.getMType().equals(MoveType.STATUS)) {
				statusMove(trg, move);		
				currentTurn = nextTurn;	
				nextTurn = -1;
			}
			
			// move has an attribute affect
			else if (move.getMType().equals(MoveType.ATTRIBUTE)) {
				attributeMove(atk, trg, move);
				currentTurn = nextTurn;	
				nextTurn = -1;
			}
			
			// move is in other category
			else if (move.getMType().equals(MoveType.OTHER)) {
				
				switch (move.getName()) {
					case "Teleport":
						break;						
					default:
						break;
				}		
			}
			// move is damage-dealing
			else {								
				damageMove(atk, trg, move);
			}				
		}
		// attack missed
		else {
			gp.ui.addBattleDialogue("The attack missed!");
			pause(1400);
			currentTurn = nextTurn;	
			nextTurn = -1;
		}			
	}		
	private boolean hit(int atk, Move move, Move trgMove) {
				
		if (trgMove == null)
			return true;
		
		// if target used delayed move and delayed move protects target		
		if (trgMove.getTurns() == 1 && !trgMove.getIsProtected())
			return false;
				
		// if move never misses, return true
		if (move.getAccuracy() == -1) 
			return true; 
		
		double accuracy = move.getAccuracy() * fighter[atk].getAccuracy();
		
		Random r = new Random();
		float chance = r.nextFloat();
		
		// chance of missing is accuracy value / 100
		return (chance <= ((float) accuracy / 100)) ? true : false;
	}
	
	// MOVE TYPE METHODS
	private void statusMove(int trg, Move move) throws InterruptedException {
		
		// if pokemon does not already have status affect
		if (fighter[trg].getStatus() == null) {
			
			fighter[trg].setStatus(move.getEffect());	
			
			
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\n" + 
					fighter[trg].getStatus().getCondition() + "!");
			
			gp.playSE(battle_SE, fighter[trg].getStatus().getCondition());
		}
		// pokemon already has status affect
		else {
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\nalready " + 
					fighter[trg].getStatus().getCondition() + "!");
		}
		
		pause(1400);
	}
	private void attributeMove(int atk, int trg, Move move) throws InterruptedException {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) {
				gp.ui.addBattleDialogue(fighter[atk].changeStat(stat, move.getLevel()));	
				
				// attributes raised
				if (move.getLevel() > 0) {
					gp.playSE(battle_SE, "stat-up");
				}
				// attributes lowered
				else  {
					gp.playSE(battle_SE, "stat-down");
				}
				
				pause(1400);
			}
		}
		// if move changes target attributes
		else {
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) {
				gp.ui.addBattleDialogue(fighter[trg].changeStat(stat, move.getLevel()));
				
				// attributes raised
				if (move.getLevel() > 0) {
					gp.playSE(battle_SE, "stat-up");
				}
				// attributes lowered
				else  {
					gp.playSE(battle_SE, "stat-down");
				}
				
				pause(1400);
			}
		}
	}
	private void damageMove(int atk, int trg, Move move) throws InterruptedException {
		
		// get critical damage (1 or 1.5)
		double crit = isCritical(move);
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
			gp.ui.addBattleDialogue("It had no effect!");
			pause(1400);
		}
		else {							
			dealDamage(atk, trg, move, damage, crit);
			absorbHP(atk, trg, move, damage);
			getRecoil(atk, trg, move, damage);
		}	
	}	
	
	// CALCULATION METHODS
	private double isCritical(Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		if (move.getCrit() == 1) 
			chance = 4;
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? 1.5 : 1;
	}
	private int calculateDamage(int atk, int trg, Move move, double crit, boolean cpu) throws InterruptedException {
		
		double level = fighter[atk].getLevel();		
		double power = (move.getPower() == -1) ? level : move.getPower();		
		double A = 1.0, D = 1.0, STAB = 1.0, type = 1.0;

		if (move.getMType().equals(MoveType.SPECIAL)) {
			A = fighter[atk].getSpAttack();
			D = fighter[trg].getSpDefense();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			A = fighter[atk].getAttack();
			D = fighter[trg].getDefense();
		}
		
		// if attacker has more than 1 type
		if (fighter[atk].getTypes() != null) {	
			
			// cycle through each type of attacker
			for (Type t : fighter[atk].getTypes()) {
				
				// if same type move
				if (move.getType() == t) {
					STAB = 1.5;
					break;
				}
			}
		}
		else
			STAB = move.getType() == fighter[atk].getType() ? 1.5 : 1.0;

		

		// damage formula reference: https://bulbapedia.bulbagarden.net/wiki/Damage (GEN IV)
		int damageDealt = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * crit * STAB * type);

		// keep damage dealt less than or equal to remaining HP
		if (damageDealt > fighter[trg].getHP())
			damageDealt = fighter[trg].getHP();
		
		return damageDealt;
	}
	private double effectiveness(int trg, Type type) {
		
		// default value
		double effect = 1.0;
		
		// if target is single types
		if (fighter[trg].getTypes() == null) {
			
			// if vulnerable, retrieve and return vulnerable value		
			for (Type vulnType : fighter[trg].getType().getVulnerability().keySet()) {		
				if (vulnType.getName().equals(type.getName())) {
					effect = fighter[trg].getType().getVulnerability().get(vulnType);
					return effect;
				}
			}			
			// if resistant, retrieve and return resistance value
			for (Type resType : fighter[trg].getType().getResistance().keySet()) {			
				if (resType.getName().equals(type.getName())) {
					effect = fighter[trg].getType().getResistance().get(resType);
					return effect;
				}			
			}		
		}
		// if target is multi type
		else {
			
			// for each type in target
			for (Type targetType : fighter[trg].getTypes()) {		
				
				// for each vulnerability			
				vulnerabilityLoop:
				for (Type vulnType : targetType.getVulnerability().keySet()) {		
					
					// if found, multiply by effect and move to next loop
					if (vulnType.getName().equals(type.getName())) {						
						effect *= targetType.getVulnerability().get(vulnType);		
						break vulnerabilityLoop;
					}
				}	
				
				// for each resistance
				resistanceLoop:
				for (Type resType : targetType.getResistance().keySet()) {		
					
					// if found, multiply by effect and move to next loop
					if (resType.getName().equals(type.getName())) {
						effect *= targetType.getResistance().get(resType);
						break resistanceLoop;
					}
				}
			}			
			// vulnerable and resistant cancel out
			if (effect == 0.75)	
				effect = 1;
		}			
						
		return effect;
	}
	
	// POST MOVE METHODS
	private void dealDamage(int atk, int trg, Move move, int damage, double crit) throws InterruptedException {		

		// subtract damage dealt from total hp
		int result = fighter[trg].getHP() - (int)damage;								
		
		// set HP to 0 if below 0
		if (result <= 0) {
			result = 0;
			currentTurn = -1;
			nextTurn = -1;		
		}
		else {
			applyEffect(atk, trg, move);
			if (nextTurn != -1) {
				getFlinch(trg, move);
			}
			else {
				currentTurn = nextTurn;	
				nextTurn = -1;	
			}			
		}
		
		String hitEffectiveness = getHitSE(effectiveness(trg, move.getType()));
		gp.playSE(battle_SE, hitEffectiveness);
		
		fighter[trg].isHit = true;
		while (fighter[trg].getHP() > result) {			
			fighter[trg].setHP(fighter[trg].getHP() - 1);
			pause(50);
		}		
		
		if (hitEffectiveness.equals("hit-super")) {
			gp.ui.addBattleDialogue("It's super effective!");
			pause(1400);
		}
		else if (hitEffectiveness.equals("hit-weak")) {
			gp.ui.addBattleDialogue("It's not very effective...");
			pause(1400);
		}
		
		// if critical hit
		if (crit == 1.5) {
			gp.ui.addBattleDialogue("A critical hit!");
			pause(1400);
		}
		
		gp.ui.addBattleDialogue(fighter[trg].getName() + " took\n" + damage + " damage!");	
		pause(1200);
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
			
			// if attacker not at full health
			if (fighter[atk].getHP() != fighter[atk].getBHP()) {
				
				// if gained hp is greater than total hp
				if (gainedHP + fighter[atk].getHP() > fighter[atk].getBHP()) {
					
					// gained hp is set to amount need to hit hp limit									
					gainedHP = fighter[atk].getBHP() - fighter[atk].getHP();
					
					// refill hp to limit
					fighter[atk].setHP(fighter[atk].getBHP());
				}
				else 
					fighter[atk].setHP(gainedHP + fighter[atk].getHP()); 
				
				gp.ui.addBattleDialogue(fighter[atk].getName() + "\nabsorbed " + gainedHP + " HP!");
				pause(1400);
			}
		}
	}
	private void getRecoil(int atk, int trg, Move move, int damage) {
		
		if (move.getSelfInflict() != 0.0) {	
			
			damage = (int)(Math.ceil(damage * move.getSelfInflict()));	

			// subtract damage dealt from total HP
			int result = fighter[atk].getHP() - (int)damage;		

			// set HP to 0 if below 0
			if (result <= 0) {
				result = 0;
				currentTurn = -1;
				nextTurn = -1;		
			}
			
			fighter[atk].setHP(result);
			gp.ui.addBattleDialogue(fighter[atk].getName() + " was hit\nwith recoil damage!");	
		}
	}
	private void applyEffect(int atk, int trg, Move move) throws InterruptedException {
		
		// move causes attribute or status effect
		if (move.getProbability() != 0.0) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) {
					attributeMove(atk, trg, move);
				}
				else {			
					// if not already affected by a status effect
					if (fighter[trg].getStatus() == null) {
						fighter[trg].setStatus(move.getEffect());
						
						gp.ui.addBattleDialogue(fighter[trg].getName() + " is\n" + 
							fighter[trg].getStatus().getCondition() + "!");
						
						pause(1400);
					}
				}
			}							
		}
	}
	private void getFlinch(int trg, Move move) {
		
		if (move.getFlinch() != 0.0) {
			
			// chance for move to cause flinch
			if (new Random().nextDouble() <= move.getFlinch()) {
				gp.ui.addBattleDialogue(fighter[trg].getName() + " flinched!");
				
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
					
		// STATUS CONDITIONS CHECKED		
		if (nextTurn == 1) {	
			
			nextTurn = -1;
			
			if (getDelayedMove() == 1 || getDelayedMove() == 3) {		
				fightStage = fight_Start;
			}
			else {				
				running = false;
				fightStage = fight_Start;
				gp.ui.battleState = gp.ui.battle_Options;
			}
		}
		// CHECK BOTH STATUS CONDITIONS
		else {		
			nextTurn++;
									
			if (hasWinningPokemon()) {
				nextTurn = -1;
				getWinningPokemon();	
			}
			else {
				if (fighter[nextTurn].isAlive()) {
					getStatusDamage(nextTurn);
				}	
				if (hasWinningPokemon()) {
					nextTurn = -1;
					getWinningPokemon();	
				}
				else {

				}
			}
		}
	}
	private void getStatusDamage(int atk) throws InterruptedException {
		
		if (fighter[atk].getStatus() != null) {				
			
			pause(500);
			
			if (fighter[atk].getStatus().getAbreviation().equals("PSN") || 
					fighter[atk].getStatus().getAbreviation().equals("BRN")) {
				
				// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects			
				int damage = (int) Math.ceil((fighter[atk].getHP() * 0.16));
				int newHP = fighter[atk].getHP() - damage;		
				
				if (newHP <= 0) {
					newHP = 0;
				}
				
				fighter[atk].setHP(newHP);	
				fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
				pause(1400);
			}		
		}	
	}
	
	// GET WINNER METHODS
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
		
		// TIE GAME
		if (winner == 2) {
			gp.ui.addBattleDialogue(fighter[0].getName() + " fainted!");
			gp.ui.addBattleDialogue(fighter[1].getName() + " fainted!");
			gp.ui.addBattleDialogue("It's a draw!");
		}
		else if (winner == 0) {
			int newXP = calculateXP(loser);
			
			gp.playSE(faint_SE, fighter[loser].toString());
			gp.ui.addBattleDialogue(fighter[loser].getName() + " fainted!");		
			gp.ui.battleState = gp.ui.battle_KO;
			pause(1400);			
			
			int xp = fighter[winner].getXP() + newXP;
			gp.ui.addBattleDialogue(fighter[winner].getName() + " gained\n" + newXP + " Exp. Points!");	
			
			while (fighter[winner].getXP() < xp) {
				
				fighter[winner].setXP(fighter[winner].getXP() + 1);
				pause(25);
				
				// FIGHTER LEVELED UP
				if (fighter[winner].getXP() >= gp.btlManager.fighter[0].getBXP() + gp.btlManager.fighter[0].getNextXP()) {			
					
					gp.ui.addBattleDialogue(fighter[0].getName() + " grew to\nLv. " + 
							(fighter[0].getLevel() + 1) + "!");
					
					gp.btlManager.fighter[0].levelUp();			
					gp.ui.battleState = gp.ui.battle_LevelUp;
					pause(1600);
				}	
			}
			
			gp.ui.battleState = gp.ui.battle_Turn;
			fightStage = fight_Swap;
		}
		else if (winner == 1) {			
			gp.playSE(faint_SE, fighter[loser].toString());			
			gp.ui.addBattleDialogue(fighter[loser].getName() + " fainted!");	
			gp.ui.battleState = gp.ui.battle_KO;
			pause(1400);

			fightStage = fight_Over;
		}
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
	
	// BATTLE END METHODS
	private void setVictory() {
		
		// TRAINER 1 WINNER
		if (winner == 0) {
			
			// WILD BATTLE
			if (battleMode == wildBattle || battleMode == legendaryBattle) {
				fightStage = 0;
				gp.gameState = gp.playState;
			}			
			// TRAINER BATTLE
			else {			
				trainer[loser].isDefeated = true;
				trainer[loser].hasBattle = false;
				
				int dialogueSet = trainer[loser].dialogueSet + 1;
				gp.ui.addBattleDialogue(trainer[loser].dialogues[dialogueSet][0]);
				
				int moneyEarned = getMoney();
				trainer[winner].money += moneyEarned;
				gp.ui.addBattleDialogue(trainer[winner].name + " got $" + moneyEarned + "\nfor winning!" );								
			}	
		}
		// TRAINER 2 WINNER
		else {
			// WILD BATTLE
			if (battleMode == wildBattle || battleMode == legendaryBattle) {				
			}			
			// TRAINER BATTLE
			else {							
			}	
		}
	}
	private int getMoney() {
		// money earned formula reference (ALL GEN): https://bulbapedia.bulbagarden.net/wiki/Prize_money
		
		int level = trainer[loser].pokeParty.get(trainer[loser].pokeParty.size() - 1).getLevel();
		int base = trainer[loser].trainerClass;		
		int payout = base * level;
		
		return payout;
	}
	public void endBattle() {
		gp.stopMusic();
		
		currentTurn = -1;
		nextTurn = -1;
		
		fighter[0] = null;
		fighter[1] = null;
		newFighter[0] = null;
		newFighter[1] = null;
		
		move1 = null;
		move2 = null;
		
		winner = -1;
		loser = -1;
		
		gp.particleList.clear();
		
		gp.gameState = gp.evolveState;
	}
	
	// MISC HANDLERS
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