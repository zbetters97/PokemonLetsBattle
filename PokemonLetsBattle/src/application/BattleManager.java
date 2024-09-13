package application;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import moves.Move;
import pokemon.Pokemon;
import properties.Status;
import properties.Type;

public class BattleManager {
	
	GamePanel gp;
	public Pokemon[] fighter = new Pokemon[2];
	public int winner = -1;
	public int loser = -1;
	public Move move1, move2;
	public boolean canMove;
	public String dialogue = "";
	
	public int damageDealt;
	
	public int currentTurn;
	private int nextTurn;
	private final int playerTurn = 0;
	private final int cpuTurn = 1;	
	
//	private final int cry_SE = 3;
	private final int faint_SE = 4;
	private final int moves_SE = 5;
	private final int battle_SE = 6;

	// BATTLE STATES
	public int battleMode;
	public final int wildBattle = 1;
	public final int trainerBattle = 2;
	public final int gymBattle = 3;
	public final int rivalBattle = 4;
	public final int eliteBattle = 5;
	public final int championBattle = 6;
	public final int legendaryBattle = 7;
	
	// FIGHT STAGES
	public int fightStage = 0;
	public final int fightStage_Encounter = 1;
	public final int fightStage_Turn = 2;
	public final int fightStage_Condition = 3;
	public final int fightStage_Recovery = 4;
	public final int fightStage_Attack = 5;
	public final int fightStage_Hit = 6;	
	public final int fightStage_Status_1 = 7;
	public final int fightStage_Status_2 = 8;
	public final int fightStage_End = 9;
	public final int fightStage_KO = 10;
			
	public BattleManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setupBattle(int currentBattle) {		
		
		fighter[0] = Pokemon.getPokemon(0);
//		fighter[0].setStatus(Status.BURN);
		fighter[1] = Pokemon.getPokemon(3);
//		fighter[1].setStatus(Status.CONFUSE);
		
		gp.ui.fighter_one_HP = fighter[0].getHP();
		gp.ui.fighter_two_HP = fighter[1].getHP();
		
		battleMode = currentBattle;
						
		gp.stopMusic();
		
		if (battleMode == wildBattle) {
			gp.ui.addBattleDialogue("A wild " + fighter[1].getName() + "\nappeared!");
			gp.playMusic(1, 0);		
		}
		else if (battleMode == trainerBattle) {
			gp.playMusic(1, 1);
		}
		else if (battleMode == gymBattle) {
			gp.playMusic(1, 2);
		}
		else if (battleMode == rivalBattle) {
			gp.playMusic(1, 3);
		}
		else if (battleMode == eliteBattle) {
			gp.playMusic(1, 4);
		}
		else if (battleMode == championBattle) {
			gp.playMusic(1, 5);
		}
		else if (battleMode == legendaryBattle) {
			gp.playMusic(1, 6);		
		}
	}
	
	public void update() {				
				
		if (fightStage == fightStage_Encounter) {
			gp.ui.battleSubState = gp.ui.subState_Options;
			fightStage++;
		}	
		// START OF ROUND
		else if (fightStage == fightStage_Turn) {
			
			// PLAYER OR CPU TURN
			if (currentTurn != -1) {
					
				// FIGHTER HAS STATUS
				if (fighter[currentTurn].getStatus() != null) {						
					checkStatus(currentTurn);		
					fightStage = fightStage_Condition;	
				}
				// FIGHTER DOES NOT HAVE STATUS
				else {
					fightStage = fightStage_Attack;	
				}
			}
			// NEITHER TURN
			else {
				fightStage = fightStage_Status_1;
			}
			
		}
		// STATUS CONDITION
		else if (fightStage == fightStage_Condition) {
			getStatus(currentTurn);
				
			// FIGHTER CAN MOVE
			if (fighter[currentTurn].canMove) {	
				fightStage = fightStage_Attack;
			}
			// FIGHTER CANNOT MOVE
			else {
				fighter[currentTurn].canMove = true;
				currentTurn = nextTurn;
				nextTurn = -1;
				fightStage = fightStage_Turn;
			}								
		}
		else if (fightStage == fightStage_Recovery) {
			startMove();	
			fightStage = fightStage_Hit;
		}
		else if (fightStage == fightStage_Attack) {
			startMove();
			fightStage = fightStage_Hit;
		}
		else if (fightStage == fightStage_Hit) {
			attack();		
			fightStage = fightStage_Turn;			
		}
		else if (fightStage == fightStage_Status_1) {
			checkStatusDamage(0);
			fightStage = fightStage_Status_2;			
		}
		else if (fightStage == fightStage_Status_2) {
			checkStatusDamage(1);
			fightStage = fightStage_End;			
		}	
		
		// END OF ROUND
		else if (fightStage == fightStage_End) {
			
			if (hasWinner()) {
				setWinner(winner, loser);
				fightStage = fightStage_KO;
				gp.ui.battleSubState = gp.ui.subState_KO;
			}
			else {
				fightStage = fightStage_Turn;
				gp.ui.battleSubState = gp.ui.subState_Options;					
			}
		}
		else if (fightStage == fightStage_KO) {			
			fightStage = 0;
			gp.gameState = gp.playState;			
		}
		
		gp.ui.dialogueFinished = false;
	}

	public void getMoves(int selection) {
		
		move1 = playerSelectMove(selection);
		move2 = cpuSelectMove();

		setRotation();
	}
	private Move playerSelectMove(int selection) {
		return fighter[0].getMoveSet().get(selection);
	}
	private Move cpuSelectMove() {

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
	private void setRotation() {		
		
		// if both pokemon are alive
		if (fighter[0].isAlive() && fighter[1].isAlive()) {
			
			// 1 if trainer 1 moves first, 2 if trainer 2 moves first
			// 3 if only trainer 2, 4 if only trainer 1, 5 if neither
			int numTurn = getFirst(move1, move2);	
			
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
	private int getFirst(Move move1, Move move2) {
		
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
	
	private void checkStatus(int atk) {
		
		// if attacker has status effect
		if (fighter[atk].getStatus() != null) {
			
			// check which status
			switch (fighter[atk].getStatus().getAbreviation()) {
								
				case "SLP": 
					gp.ui.addBattleDialogue(fighter[atk].getName() + " is\nfast asleep!");
					gp.ui.setSoundFile(battle_SE, "asleep", 5, 120);	
					break;
				case "CNF": 
					gp.ui.addBattleDialogue(fighter[atk].getName() + " is\nconfused!");
					gp.ui.setSoundFile(battle_SE, "confused", 5, 120);	
					break;
				case "FRZ":
					gp.ui.addBattleDialogue(fighter[atk].getName() + " is\nfrozen solid!");
					gp.ui.setSoundFile(battle_SE, "frozen", 5, 120);	
					break;
			}
		}	
	}
	private void getStatus(int atk) {
		
		// if attacker has status effect
		if (fighter[currentTurn].getStatus() != null) {
			
			// check which status
			switch (fighter[currentTurn].getStatus().getAbreviation()) {			
				case "PAR":	paralyzed(currentTurn);	break;					
				case "FRZ": frozen(currentTurn); break;			
				case "SLP": asleep(currentTurn); break;
				case "CNF": confused(currentTurn); break;
			}
		}			
	}
	private void paralyzed(int atk) {
		
		// 1/4 chance can't move due to PAR
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {		
			fighter[atk].canMove = false;
		} 
	}
	private void frozen(int atk) {
		
		// 1/4 chance attacker can thaw from ice
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {
			gp.ui.addBattleDialogue(fighter[atk].getName() + "" + fighter[atk].getStatus().printRecover());
			fighter[atk].setStatus(null);
		}
		else {	
			fighter[atk].canMove = false;				
		}
	}
	private void asleep(int atk) {
				
		// if number of moves under condition hit limit, remove condition
		if (recoverCondition(atk)) {			
			fighter[atk].canMove = true;
		}
		// pokemon still under status condition
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
			
			fighter[atk].canMove = false;		
		}
	}
	private void confused(int atk) {
		
		// if number of moves under condition hit limit, remove condition
		if (recoverCondition(atk)) {			
			fighter[atk].canMove = true;
		}
		// pokemon still under status condition
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
									
			// if pokemon hurt itself in confusion
			if (confusionDamage(atk)) {
				fighter[atk].canMove = false;
			}
			else { 
				fighter[atk].canMove = true;			
			}
		}
	}
	private boolean recoverCondition(int atk) {
		
		// if first move under condition, set number of moves until free (1-5)
		if (fighter[atk].getStatusLimit() == 0) {
			fighter[atk].setStatusLimit((int)(Math.random() * 5));	
		}
		
		// if number of moves under condition hit limit, remove condition
		if (fighter[atk].getStatusCounter() >= fighter[atk].getStatusLimit()) {
									
			gp.ui.addBattleDialogue(fighter[atk].getName() + fighter[atk].getStatus().printRecover());	
		
			fighter[atk].setStatusCounter(0); fighter[atk].setStatusLimit(0);
			fighter[atk].setStatus(null);
			
			return true;
		}
		else {
			return false;
		}
	}
	private boolean confusionDamage(int atk) {

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
				fighter[atk].setAlive(false);				
			}
			
			fighter[atk].setHP(hp);					
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			
			return true;	
		}
		else {
			return false;
		}
	}
	
	private void startMove() {		
		
		if (currentTurn == playerTurn) {
			useTurn(0, 1, move1, move2);
		}
		else if (currentTurn == cpuTurn) {
			useTurn(1, 0, move2, move1);
		}	
	}
		
	private void useTurn(int atk, int trg, Move atkMove, Move trgMove) {		
				
		gp.ui.addBattleDialogue(fighter[atk].toString() + " used\n" + atkMove.toString() + "!"); 		
		
		// if not delayed move or delayed move is ready
		if (1 >= atkMove.getTurns()) {	
			
			gp.ui.setSoundFile(moves_SE, atkMove.getName(), 45);
			
			// reset turns to wait
			atkMove.setTurns(atkMove.getNumTurns());
		}
		// delayed move is used for first time
		else if (atkMove.getTurns() == atkMove.getNumTurns()) {
						
			gp.ui.addBattleDialogue(atkMove.getDelay(fighter[atk].getName()));	
									
			// reduce number of turns to wait
			atkMove.setTurns(atkMove.getTurns() - 1);	

			return;
		}
		
		// decrease move pp
		atkMove.setpp(atkMove.getpp() - 1);
	}		
	
	public void attack() {
		
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
			if (move.getMType().equals("Status")) {
				statusMove(trg, move);		
				currentTurn = nextTurn;	
				nextTurn = -1;
			}
			
			// move has an attribute affect
			else if (move.getMType().equals("Attribute")) {
				attributeMove(atk, trg, move);
				currentTurn = nextTurn;	
				nextTurn = -1;
			}
			
			// move is in other category
			else if (move.getMType().equals("Other")) {
				
				switch (move.getName()) {
					case "TELEPORT":	
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
			currentTurn = nextTurn;	
			nextTurn = -1;
		}				
	}		
	private boolean hit(int atk, Move move, Move trgMove) {
		
		if (trgMove == null)
			return true;
		
		// if target used delayed move and delayed move protects target		
		if (trgMove.getTurns() == 1 && !trgMove.getCanHit())
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
	
	private void statusMove(int trg, Move move) {
		
		// if pokemon does not already have status affect
		if (fighter[trg].getStatus() == null) {
			
			fighter[trg].setStatus(move.getEffect());	
			
			gp.ui.setSoundFile(battle_SE, fighter[trg].getStatus().getCondition(), 5);
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\n" + 
					fighter[trg].getStatus().getCondition() + "!");
		}
		// pokemon already has status affect
		else {
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\nalready " + 
					fighter[trg].getStatus().getCondition() + "!");
		}
	}
	private void attributeMove(int atk, int trg, Move move) {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) 
				gp.ui.addBattleDialogue(fighter[atk].changeStat(stat, move.getLevel()));	
		}
		// if move changes target attributes
		else {
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) 
				gp.ui.addBattleDialogue(fighter[trg].changeStat(stat, move.getLevel()));	
		}
		
		// attributes raised
		if (move.getLevel() > 0) {
			gp.playSE(battle_SE, "stat-up");
		}
		// attributes lowered
		else  {
			gp.playSE(battle_SE, "stat-down");
		}
	}
	
	private void damageMove(int atk, int trg, Move move) {
		
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
		if (damage == 0) {
			gp.ui.addBattleDialogue("It had no effect!");
		}
		else {				
			
			// if critical hit
			if (crit == 1.5) {
				gp.ui.addBattleDialogue("A critical hit!");
			}
		
			gp.ui.addBattleDialogue(fighter[trg].getName() + "\ntook " + damage + " damage!");	
			
//			absorbHP(atk, trg, move, damage);
									
			// if damage is fatal
/*			if (damage >= fighter[trg].getHP())	{
				dealDamage(trg, damage);
				isRecoil(atk, trg, move, damage);
				setWinner(atk, trg);
			}						
			// fighter survives hit
			else {							
				dealDamage(trg, damage);										
				applyEffect(atk, trg, move);				
				
				currentTurn = nextTurn;	
				nextTurn = -1;
				isRecoil(atk, trg, move, damage);
			}
*/
			dealDamage(atk, trg, move, damage);
		}	
	}	
	private double isCritical(Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		if (move.getCrit() == 1) 
			chance = 4;
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? 1.5 : 1;
	}
	private int calculateDamage(int atk, int trg, Move move, double crit, boolean cpu) {
		
		double level = fighter[atk].getLevel();		
		double power = (move.getPower() == -1) ? level : move.getPower();		
		double A = 1.0, D = 1.0, STAB = 1.0, type = 1.0;

		if (move.getMType().equals("Special")) {
			A = fighter[atk].getSpAttack();
			D = fighter[trg].getSpDefense();
		}
		else if (move.getMType().equals("Physical")) {
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

		type = effectiveness(trg, move.getType());	

		// damage formula reference: https://bulbapedia.bulbagarden.net/wiki/Damage (GEN IV)
		int damageDealt = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * crit * STAB * type);

		// keep damage dealt less than or equal to remaining HP
		if (damageDealt > fighter[trg].getHP())
			damageDealt = fighter[trg].getHP();
		
		// don't play sound if cpu is calling method
		if (!cpu) {
			gp.ui.setSoundFile(battle_SE, getHitSE(type), 5);
		}
		
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
	private void dealDamage(int atk, int trg, Move move, int damage) {		
		
		// subtract damage dealt from total hp
		int result = fighter[trg].getHP() - (int)damage;		
		
		// set HP to 0 if below 0
		if (result <= 0) {
			result = 0;
			fighter[trg].setAlive(false);
			currentTurn = -1;
			nextTurn = -1;			
		}
		else {
			applyEffect(atk, trg, move);
			currentTurn = nextTurn;	
			nextTurn = -1;
		}
		
		fighter[trg].setHP(result);
	}
	private void applyEffect(int atk, int trg, Move move) {
		
		// move causes attribute or status effect
		if (move.getProbability() != null) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) {
					attributeMove(atk, trg, move);
				}
				else {			
					// if not already affected by a status effect
					if (fighter[trg].getStatus() == null) {
						fighter[trg].setStatus(move.getEffect());
						
						gp.ui.addBattleDialogue(fighter[trg].getName() + " is " + 
							fighter[trg].getStatus().getCondition() + "!");
					}
				}
			}							
		}
	}

	private void checkStatusDamage(int atk) {
		
		if (fighter[atk].isAlive()) {
			getStatusDamage(atk);

			if (!fighter[atk].isAlive()) { 
				hasWinner();
			}
		}	
	}
	private void getStatusDamage(int atk) {
		
		if (fighter[atk].getStatus() != null) {				
			
			if (fighter[atk].getStatus().getAbreviation().equals("PSN") || 
					fighter[atk].getStatus().getAbreviation().equals("BRN")) {
				
				// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects			
				int damage = (int) Math.ceil((fighter[atk].getHP() * 0.16));
				int newHP = fighter[atk].getHP() - damage;		
				
				if (newHP <= 0) {
					newHP = 0;
					fighter[atk].setAlive(false);
				}
	
				gp.ui.setSoundFile(battle_SE, fighter[atk].getStatus().getName(), 5);
				
				fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());				
				fighter[atk].setHP(newHP);		
			}		
		}	
	}
		
	private boolean hasWinner() {
			
		if (!fighter[0].isAlive()) {	
			winner = 1;
			loser = 0;
			return true;
		}
		else if (!fighter[1].isAlive()) {
			winner = 0;
			loser = 1;
			return true;
		}
		else {
			return false;
		}
	}
	public void setWinner(int winner, int loser) {
		
		this.winner = winner;
		this.loser = loser;
		
		fighter[loser].setAlive(false);
		
		int xp = calculateXP(loser);
		fighter[winner].setXP(fighter[winner].getBXP() + xp);
		
		gp.ui.setSoundFile(faint_SE, fighter[loser].getName(), 5);
		
		gp.ui.addBattleDialogue(fighter[loser].getName() + " fainted!");			
		gp.ui.addBattleDialogue(fighter[winner].getName() + "\ngained " + xp + " Exp. Points!");
				
		return;
	}
	private int calculateXP(int lsr) {
		
		// exp formula reference (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		int exp = (int) (((( fighter[lsr].getXP() * fighter[lsr].getLevel() ) / 7)) * 1.5);		
		return exp;
	}
	
	private String getHitSE(double effectiveness) {
		
		String hit = "";
		
		switch (Double.toString(effectiveness)) {
			case "0.25": hit = "hit-weak"; break;			
			case "0.5": hit = "hit-weak"; break;
			case "1.0": hit = "hit-normal"; break;
			case "1.5": hit = "hit-super"; break;
			case "2.25": hit = "hit-super"; break;			
			default: hit = "hit-normal"; break;
		}
		
		if (effectiveness == 1.5 || effectiveness == 2.25) 
			gp.ui.addBattleDialogue("It's super effective!");
		else if (effectiveness == 0.25 || effectiveness == 0.5)
			gp.ui.addBattleDialogue("It's not very effective...");
		else if (effectiveness == 0) 
			gp.ui.addBattleDialogue("It has no effect!");
		
		return hit;
	}
}