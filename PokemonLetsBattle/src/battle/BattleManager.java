package battle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import application.GamePanel;
import application.GamePanel.Weather;
import entity.Entity;
import entity.collectables.items.ITM_EXP_Share;
import entity.npc.NPC_Red;
import moves.Move;
import moves.Move.MoveType;
import moves.Moves;
import pokemon.Pokemon;
import pokemon.Pokemon.Protection;
import properties.Ability;
import properties.Status;
import properties.Type;

public class BattleManager extends Thread {
	
	private static final List<Moves> absorbMoves = Arrays.asList(Moves.ABSORB, Moves.DREAMEATER, Moves.GIGADRAIN, 
			Moves.LEECHLIFE, Moves.MEGADRAIN);
	
	private GamePanel gp;
	public boolean set = true;
	public boolean active = false;
	public boolean running = false;
	
	// GENERAL VALUES				
	public Entity trainer;
	
	public Pokemon[] fighter = new Pokemon[2];
	private Pokemon[] newFighter = new Pokemon[2];
	private ArrayList<Pokemon> otherFighters = new ArrayList<>();
	
	private Move playerMove, cpuMove;
	public Move newMove = null, oldMove = null;
	
	private Weather weather = Weather.CLEAR;
	private int weatherDays = -1;
	private int winner = -1, loser = -1;	
	private int escapeAttempts = 0;
		
	public Entity ballUsed;
						
	// BATTLE QUEUE
	public Deque<Integer> battleQueue = new ArrayDeque<>();	
	private final int queue_GetCPUMove = 1;
	private final int queue_Rotation = 2;
	private final int queue_PlayerMove = 3;
	private final int queue_CPUMove = 4;
	private final int queue_ActiveMoves = 5;
	private final int queue_StatusDamage = 6;
	private final int queue_WeatherDamage = 7;
	private final int queue_TurnReset = 8;
	
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
				
		switch (battleMode) {
			case wildBattle:
				
				gp.startMusic(1, 1);				
				pause(1400);
				
				gp.playSE(gp.cry_SE, fighter[1].toString());	
				typeDialogue("A wild " + fighter[1].getName() + "\nappeared!", true);	
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
				
				break;
			case trainerBattle: 
				
				if (trainer.name.equals(NPC_Red.npcName)) {
					gp.startMusic(1, 10);
				}
				else {
					gp.startMusic(1, 4);	
				}
				
				pause(1400);		
				
				typeDialogue("Trainer " + trainer.name + "\nwould like to battle!", true);	
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
				
				fighter[1] = trainer.pokeParty.get(0);
				
				gp.playSE(gp.cry_SE, fighter[1].toString());	
				typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
				pause(100);
				
				break;			
		}
		
		fighter[0] = gp.player.pokeParty.get(0);
		getOtherFighters();
		
		gp.playSE(gp.cry_SE, fighter[0].toString());	
		typeDialogue("Go, " + fighter[0].getName() + "!");					
		pause(100);
								
		getFighterAbility();	
		checkWeatherCondition();
		
		if (weather == Weather.RAIN) {
			if (fighter[0].getAbility() == Ability.SWIFTSWIM) {
				setAttribute(fighter[0], Arrays.asList("speed"), 2);
			}
			if (fighter[1].getAbility() == Ability.SWIFTSWIM) {
				setAttribute(fighter[1], Arrays.asList("speed"), 2);
			}
		}
		
		running = false;		
		
		gp.ui.battleState = gp.ui.battle_Options;
	}	
 	private void getFighterAbility() throws InterruptedException {
 		
 		if (fighter[0].getAbility() == Ability.INTIMIDATE) {
			setAttribute(fighter[1], Arrays.asList("attack"), -1);
		}
		if (fighter[1].getAbility() == Ability.INTIMIDATE) {
			setAttribute(fighter[0], Arrays.asList("attack"), -1);
		}
		
		else {
			if (fighter[0].getAbility().getWeather() != null &&	fighter[1].getAbility().getWeather() != null) {
				
				// if fighter 1 is faster
				if (fighter[0].getSpeed() > fighter[1].getSpeed()) {
					weather = fighter[1].getAbility().getWeather();
				}
				// if fighter 2 is faster
				else if (fighter[0].getSpeed() < fighter[1].getSpeed()) {
					weather = fighter[0].getAbility().getWeather();
				}
				// if both fighters have equal speed, coin flip decides
				else {
					Random r = new Random();					
					if (r.nextFloat() <= ((float) 1 / 2)) {
						weather = fighter[1].getAbility().getWeather();
					}
					else {
						weather = fighter[0].getAbility().getWeather();
					}
				}					
			}
			else if (fighter[0].getAbility().getWeather() != null) {									
				weather = fighter[0].getAbility().getWeather();
			}
			else if (fighter[1].getAbility().getWeather() != null) {									
				weather = fighter[1].getAbility().getWeather();
			}	
		}
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
	
	// SWAP POKEMON METHODS
	private void swapFighters() throws InterruptedException {
			
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
				
				gp.playSE(gp.cry_SE, fighter[0].toString());	
				typeDialogue("Go, " + fighter[0].getName() + "!");					
				pause(100);
			}
			
			fighter[1] = newFighter[1];
			
			gp.playSE(gp.cry_SE, fighter[1].toString());	
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
			
			gp.playSE(gp.cry_SE, fighter[0].toString());	
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
			
			gp.playSE(gp.cry_SE, fighter[0].toString());
			typeDialogue("Go, " + fighter[0].getName() + "!");	
			pause(100);
			
			newFighter[0] = null;
			newFighter[1] = null;
			
			fightStage = fight_Start;
		}		
		
		getFighterAbility();
	}
	
	// SWAP FIGHTER METHODS
	public boolean swapPokemon(int partySlot) {
		
		if (fighter[0] == gp.player.pokeParty.get(partySlot)) {
			return false;
		}
		
		if (gp.player.pokeParty.get(partySlot).isAlive()) {
						
			fighter[0].resetStats();
			fighter[0].resetStatStages();
			fighter[0].resetMoveTurns();
			fighter[0].clearActiveMoves();
			
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
						
	// RUN BATTLE METHOD
	private void runBattle() throws InterruptedException {
					
		battleQueue.addAll(Arrays.asList(
				queue_GetCPUMove,
				queue_Rotation,
				queue_ActiveMoves,
				queue_StatusDamage,
				queue_WeatherDamage,
				queue_TurnReset
		));
		
		while (!battleQueue.isEmpty()) {
			
			int action = battleQueue.poll();
			
			switch (action) {
			
				case queue_GetCPUMove: 
					getCPUMove();
					break;			
					
				case queue_Rotation: 
					setRotation();	
					break;		
					
				case queue_PlayerMove: 
					playerMove(); 
					break;			
					
				case queue_CPUMove: 
					cpuMove(); 
					break;			
					
				case queue_ActiveMoves: 
					checkActiveMoves(0, 1);
					checkActiveMoves(1, 0);
					break;				
					
				case queue_StatusDamage:
					checkStatusDamage();
					break;			
					
				case queue_WeatherDamage:
					checkWeatherDamage();	
					break;		
					
				case queue_TurnReset:
					getDelayedTurn();
					break;
			}
			
			if (hasWinningPokemon()) {
				getWinningPokemon();
				getWinningTrainer();
				battleQueue.clear();
			}		
		}
	}
	
	// GET MOVES METHOD
	public Move getPlayerMove(int selection) {
		
		playerMove = fighter[0].getMoveSet().get(selection);
		
		boolean struggle = true;
		for (Move m : fighter[0].getMoveSet()) {
			if (m.getPP() > 0) {
				struggle = false;
				break;
			}
		}
		
		if (struggle) {
			playerMove = new Move(Moves.STRUGGLE);
		}
		
		return playerMove;
	}
	private void getCPUMove() throws InterruptedException {
		
		// GET CPU MOVE IF NO CPU DELAY
		int delay = BattleUtility.getDelay(playerMove, cpuMove);
		
		if (delay == 0 || delay == 1) {						
			cpuMove = BattleUtility.chooseCPUMove(trainer, fighter[1], fighter[0], weather);		
		}
	}
	
	// SET ROTATION METHODS
	private void setRotation() {		
		// 0 if neither	goes first
		// 1 if player moves first
		// 2 if cpu moves first
		// 3 if only cpu moves
		// 4 if only player	moves	
		
		if (fighter[0].isAlive() && fighter[1].isAlive()) {
							
			int firstTurn = BattleUtility.getFirstTurn(fighter[0], fighter[1], playerMove, cpuMove);	
			
			if (firstTurn == 1) { 				
				battleQueue.addFirst(queue_CPUMove);
				battleQueue.addFirst(queue_PlayerMove);
			}	
			else if (firstTurn == 2) { 				
				battleQueue.addFirst(queue_PlayerMove);
				battleQueue.addFirst(queue_CPUMove);
			}				
			else if (firstTurn == 3) { 
				battleQueue.addFirst(queue_CPUMove);
			}				
			else if (firstTurn == 4) { 
				battleQueue.addFirst(queue_PlayerMove);
			}
		}
	}
		
	private void playerMove() throws InterruptedException {
		
		if (canMove(fighter[0], playerMove)) {
			move(fighter[0], fighter[1], playerMove, cpuMove);	
		}	
		else {
			playerMove.resetMoveTurns();
		}
	}	
	private void cpuMove() throws InterruptedException {
		
		if (canMove(fighter[1], cpuMove)) {
			move(fighter[1], fighter[0], cpuMove, playerMove);
		}		
		else {
			cpuMove.resetMoveTurns();
		}
	}
	
	// STATUS CONDITION METHODS
 	private boolean canMove(Pokemon pkm, Move move) throws InterruptedException {
		
		boolean canMove = true;
		
		// if attacker has status effect
		if (pkm.getStatus() != null) {
		
			// check which status
			switch (pkm.getStatus().getAbreviation()) {			
				case "PAR":	canMove = paralyzed(pkm); break;					
				case "FRZ": canMove = frozen(pkm); break;			
				case "SLP": canMove = asleep(pkm, move); break;
				case "CNF": canMove = confused(pkm); break;
			}
		}	
		
		return canMove;
	}
	private boolean paralyzed(Pokemon pkm) throws InterruptedException {
		
		// 1/4 chance can't move due to PAR
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {		
			pkm.getStatus().printStatus(gp, pkm.getName());
			return false;
		} 
		else {
			return true;
		}
	}
	private boolean frozen(Pokemon pkm) throws InterruptedException {
		
		// 1/4 chance attacker can thaw from ice
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {
			removeStatus(pkm);	
			return true;
		}
		else {	
			pkm.getStatus().printStatus(gp, pkm.getName());
			return false;		
		}
	}
	private boolean asleep(Pokemon pkm, Move move) throws InterruptedException {
				
		if (move.getMove() == Moves.SNORE || move.getMove() == Moves.SLEEPTALK) {
			return true;
		}		
		// if number of moves under status hit limit, remove status
		else if (recoverStatus(pkm)) {			
			return true;
		}
		// pokemon still under status status
		else {
			
			// increase counter
			pkm.setStatusCounter(pkm.getStatusCounter() + 1);
			pkm.getStatus().printStatus(gp, pkm.getName());
			
			return false;
		}
	}
	private boolean confused(Pokemon pkm) throws InterruptedException {
		
		gp.playSE(gp.battle_SE, pkm.getStatus().getStatus());
		typeDialogue(pkm.getName() + " is\n" + pkm.getStatus().getStatus() + "...");
		
		// if number of moves under status hit limit, remove status
		if (recoverStatus(pkm)) {			
			return true;
		}
		// pokemon still under status status
		else {
			
			// increase counter
			pkm.setStatusCounter(pkm.getStatusCounter() + 1);
									
			// if pokemon hurt itself in confusion
			if (confusionDamage(pkm)) {
				return false;
			}
			else { 
				return true;
			}			
		}
	}
	private boolean confusionDamage(Pokemon pkm) throws InterruptedException {
				
		// 1/2 chance of hurting self
		int val = 1 + (int)(Math.random() * 2);	
				
		if (val == 1) {					
			
			int damage = BattleUtility.getConfusionDamage(pkm);		
			if (damage > pkm.getHP()) damage = pkm.getHP();
			
			pkm.setHit(true);
			gp.playSE(gp.battle_SE, "hit-normal");	
			decreaseHP(pkm, damage);			
			
			pkm.getStatus().printStatus(gp, pkm.getName());
			
			return true;	
		}
		else {
			return false;
		}
	}
	private boolean recoverStatus(Pokemon pkm) throws InterruptedException {
		
		// if first move under status, set number of moves until free (1-5)
		if (pkm.getStatusLimit() == 0) {
			pkm.setStatusLimit((int)(Math.random() * 5));	
		}
		
		// if number of moves under status hit limit, remove status
		if (pkm.getStatusCounter() >= pkm.getStatusLimit()) {									
			
			removeStatus(pkm);			
			return true;
		}
		else {
			return false;
		}
	}
		
	// MOVE METHOD
	private void move(Pokemon atk, Pokemon trg, Move atkMove, Move trgMove) throws InterruptedException {		
						
		checkSleepTalk(atk, atkMove);				
		BattleUtility.getWeatherMoveDelay(weather, atkMove);
						
		if (atkMove.isReady()) {				
			
			typeDialogue(atk.getName() + " used\n" + atkMove.toString() + "!", false); 
			
			atk.setProtectedState(Protection.NONE);			
			atk.setAttacking(true);
			playSE(gp.moves_SE, atkMove.getName());
			
			if (validMove(atk, trg, atkMove)) {
				
				atkMove.resetMoveTurns();
				atkMove.setPP(atkMove.getPP() - 1);								
				attack(atk, trg, atkMove);		
			}
			else {
				typeDialogue("It had no effect!");
			}
		}		
		else if (atkMove.getRecharge()) {			
			typeDialogue(atkMove.getDelay(atk.getName()));				
			atkMove.setTurnCount(atkMove.getTurns());
		}
		else {						
			typeDialogue(atk.getName() + " used\n" + atkMove.toString() + "!");
			atk.setProtectedState(atkMove.getProtection());				
			typeDialogue(atkMove.getDelay(atk.getName()));	
			
			atkMove.setTurnCount(atkMove.getTurnCount() - 1);
		}
	}		
	private void checkSleepTalk(Pokemon atk, Move move) throws InterruptedException {
		
		if (move.getMove() == Moves.SLEEPTALK) {
			
			typeDialogue(atk.getName() + " used\n" + move.toString() + "!"); 
			
			if (atk.hasStatus(Status.SLEEP)) {	
				
				Move randomMove = null; 
				
				do { randomMove = atk.getMoveSet().get(new Random().nextInt(atk.getMoveSet().size())); }
				while (randomMove.getMove() == Moves.SLEEPTALK);
				
				move = randomMove;
			}
			else {				
				typeDialogue("It had no effect!");
				return;		
			}
		}
	}
	private boolean validMove(Pokemon atk, Pokemon trg, Move move) {
		
		if ((move.getMove() == Moves.SNORE && atk.hasStatus(Status.SLEEP)) ||
			(move.getMove() == Moves.DREAMEATER && trg.hasStatus(Status.SLEEP)) ||
			(move.getMove() == Moves.SUCKERPUNCH && battleQueue.peek() == queue_ActiveMoves)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	// ATTACK METHOD
	public void attack(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
			
		if (BattleUtility.hit(atk, trg, move, weather)) {
			
			switch (move.getMType()) {
			
				case STATUS:
					statusMove(atk, trg, move);					
					break;
					
				case ATTRIBUTE:
					attributeMove(atk, trg, move);					
					break;
					
				case WEATHER:
					weatherMove(move);					
					break;
					
				case OTHER:
					otherMove(atk, trg, move);					
					break;
					
				default:
					damageMove(atk, trg, move);
					break;
			}		
			
			if (trg.getAbility() == Ability.PRESSURE) {
				move.setPP(move.getPP() - 1);
			}
		}
		else {
			typeDialogue("The attack missed!");
		}			
	}		
	
	// STATUS MOVE
	private void statusMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {		
		setStatus(trg, move.getEffect());	
	}
	private void setStatus(Pokemon pkm, Status status) throws InterruptedException {
		
		if (pkm.getHP() > 0) {
			
			if (pkm.getStatus() == null) {
				
				pkm.setStatus(status);		
				
				gp.playSE(gp.battle_SE, status.getStatus());
				typeDialogue(pkm.getName() + status.printCondition());
				
				if (pkm.getAbility() == Ability.QUICKFEET && !pkm.getAbility().isActive()) {
					pkm.getAbility().setActive(true);
					setAttribute(pkm, Arrays.asList("speed"), 2);
				}
				else if (pkm.getAbility() == Ability.GUTS && !pkm.getAbility().isActive()) {
					pkm.getAbility().setActive(true);
					setAttribute(pkm, Arrays.asList("attack"), 2);
				}
			}
			else {				
				typeDialogue(pkm.getName() + " is\nalready " + 
						pkm.getStatus().getStatus() + "!");
			}			
		}
	}
	private void removeStatus(Pokemon pkm) throws InterruptedException {
		Status status = pkm.getStatus();
		pkm.setStatus(null);
		pkm.setStatusCounter(0); 
		pkm.setStatusLimit(0);			
		typeDialogue(pkm.getName() + status.printRecover());	
	}

	// ATTRIBUTE MOVE
	private void attributeMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			if (move.getMove() == Moves.REST) {
				
				int gainedHP = atk.getBHP() - atk.getHP();
				increaseHP(atk, gainedHP);
				
				setStatus(atk, Status.SLEEP);		
			}
			else {				
				int level = move.getLevel();
								
				if (weather == Weather.SUNLIGHT && move.getMove() == Moves.GROWTH) {
					level++;
				}
				
				setAttribute(atk, move.getStats(), level);		
			}
		}
		// if move changes target attributes
		else {
			setAttribute(trg, move.getStats(), move.getLevel());
			
			if (move.getMove() == Moves.SWAGGER) {
				setStatus(trg, Status.CONFUSE);
			}
		}			
	}
	private void setAttribute(Pokemon pkm, List<String> stats, int level) throws InterruptedException {
		
		// loop through each specified attribute to be changed
		for (String stat : stats) {				
			
			if (level > 0) gp.playSE(gp.battle_SE, "stat-up");
			else gp.playSE(gp.battle_SE, "stat-down");			
			
			typeDialogue(pkm.changeStat(stat, level));	
		}			
	}
	
	// WEATHER MOVE
	private void weatherMove(Move move) throws InterruptedException {		
		
		weather = Weather.valueOf(move.getWeather());
		checkWeatherCondition();
		weatherDays = move.getTurns();
	}
	private void checkWeatherCondition() throws InterruptedException {
		
		switch (weather) {	
			case CLEAR:
				break;
			case SUNLIGHT:
				gp.playSE(gp.battle_SE, "sunlight");
				typeDialogue("The sun shines intensely!");	
				break;
			case RAIN:
				gp.playSE(gp.battle_SE, "rain");
				typeDialogue("Rain started to fall!");									
				break;
			case HAIL:
				gp.playSE(gp.battle_SE, "hail");
				typeDialogue("Hail started to fall!");	
				break;
			case SANDSTORM:
				gp.playSE(gp.battle_SE, "sandstorm");
				typeDialogue("A sandstorm started to\n rage!");	
				break;
		}		
	}
	
	// OTHER MOVE
	private void otherMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		switch (move.getMove()) {		
			case LEECHSEED:
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + " planted\na seed on " + trg.getName() + "!");	
				}
				break;
			case ODERSLEUTH:
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + " identified\n" + trg.getName() + "!");					
				}	
				break;			
			case PROTECT:
				atk.addActiveMove(Moves.PROTECT);
				typeDialogue(atk.getName() + " protected\nitself!");
				break;
			case RECOVER:
				if (atk.getHP() < atk.getBHP()) {
					
					int gainedHP = atk.getBHP() - atk.getHP();
					int halfHP = (int) Math.floor(atk.getBHP() / 2.0);
					if (gainedHP > halfHP) gainedHP = halfHP;
					
					increaseHP(atk, gainedHP);				
					typeDialogue(atk.getName() + "\nregained health!");
				}
				else {
					typeDialogue("It had no effect!");
				}
				break;
			case REFLECT:
				if (atk.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					atk.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + "'s " + move.toString() + "\nraised DEFENSE!");					
				}			
				break;			
			case TELEPORT:
				if (trainer == null) {
					typeDialogue(atk.getName() + " teleported\naway!");	
					endBattle();
				}
				else {
					typeDialogue("It had no effect!");
				}		
				break;
			default:
				break;		
		}
	}
	
	// DAMAGE MOVE
	private void damageMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		setDamage(atk, trg, move);
		
		// BOTH POKEMON ALIVE
		if (trg.getHP() > 0 && atk.getHP() > 0) {
			
			applyEffect(atk, trg, move);
							
			if (battleQueue.peek() != queue_ActiveMoves && flinched(trg, move)) {
				battleQueue.removeFirst();		
			}		
		}
		// BOTH POKEMON FAINTED
		else if (trg.getHP() <= 0 && atk.getHP() <= 0) {
			battleQueue.remove(queue_PlayerMove);
			battleQueue.remove(queue_CPUMove);
		}
		// TARGET FAINTED
		else if (trg.getHP() <= 0) {			
			applyEffect(atk, trg, move);
			battleQueue.remove(queue_PlayerMove);
			battleQueue.remove(queue_CPUMove);
		}
		// ATTACKER FAINTED
		else if (atk.getHP() <= 0) {			
			applyEffect(atk, trg, move);		
			battleQueue.remove(queue_PlayerMove);
			battleQueue.remove(queue_CPUMove);
		}	
	}	
	private void setDamage(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		String hitSE = getHitSE(BattleUtility.getEffectiveness(trg, move.getType()));		
		
		gp.playSE(gp.battle_SE, hitSE);
		trg.setHit(true);
					
		double crit = getCritical(atk, trg, move);		
		int damage = (int) (BattleUtility.calculateDamage(atk, trg, move, weather) * crit);	
				
		if (move.getMove() == Moves.PURSUIT) { }
		
		if (trg.hasActiveMove(Moves.REFLECT) && move.getMType() == MoveType.PHYSICAL) {
			damage /= 2;			
		}		
		
		if (damage <= 0) {
			typeDialogue("It had no effect!");
		}
		else {							
			if (damage >= trg.getHP()) {					
				damage = trg.getHP();			
				if (move.getMove() == Moves.FALSESWIPE) {
					damage--;			
				}
			}
			
			decreaseHP(trg, damage);		
			
			if (crit >= 1.5) typeDialogue("A critical hit!");
			
			if (hitSE.equals("hit-super")) typeDialogue("It's super effective!");			
			else if (hitSE.equals("hit-weak")) typeDialogue("It's not very effective...");			
			
			typeDialogue(trg.getName() + " took\n" + damage + " damage!");		
		}
		
		absorbHP(atk, trg, move, damage);
		getRecoil(atk, move, damage);		
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
	private double getCritical(Pokemon atk, Pokemon trg, Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		double damage  = 1.5;
		if (atk.getAbility() == Ability.SNIPER) damage = 3.0;
		if (trg.getAbility() == Ability.SHELLARMOR) damage = 1.0;
		
		if (move.getCrit() == 1) {
			chance = 4;
		}
						
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? damage : 1.0;					
	}
	
	private void absorbHP(Pokemon atk, Pokemon trg, Move move, int damage) throws InterruptedException {
		
		if (absorbMoves.contains(move.getMove())) {
								
			int gainedHP = (damage / 2);
			
			// if attacker not at full health
			if (atk.getHP() != atk.getBHP()) {
				
				// if gained hp is greater than total hp
				if (gainedHP + atk.getHP() > atk.getBHP()) {
					
					// gained hp is set to amount need to hit hp limit									
					gainedHP = atk.getBHP() - atk.getHP();					
					
					increaseHP(atk, gainedHP);					
				}
				else {		
					increaseHP(atk, gainedHP);
				}
				
				typeDialogue(atk.getName() + "\nabsorbed " + gainedHP + " HP!");
			}
		}
	}
	private void getRecoil(Pokemon pkm, Move move, int damage) throws InterruptedException {
		
		if (move.getMove() == Moves.EXPLOSION || move.getMove() == Moves.SELFDESTRUCT) {			
			decreaseHP(pkm, pkm.getHP());
			typeDialogue(pkm.getName() + " was hit\nwith recoil damage!");									
		}					
		else if (move.getSelfInflict() != 0.0 && pkm.getAbility() != Ability.ROCKHEAD) {	
			
			int recoilDamage = (int)(Math.ceil(damage * move.getSelfInflict()));	
			if (recoilDamage > pkm.getHP()) recoilDamage = pkm.getHP();
			
			if (recoilDamage > 0) {
				decreaseHP(pkm, recoilDamage);
				typeDialogue(pkm.getName() + " was hit\nwith recoil damage!");		
			}					
		}
	}
	private void applyEffect(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {

		if (move.getMove() == Moves.RAPIDSPIN) {
			
			if (trg.hasActiveMove(Moves.LEECHSEED)) {					
				typeDialogue(trg.getName() + " broke free\nof LEECH SEED !");		
				trg.removeActiveMove(Moves.LEECHSEED);
			}
		}
		else if (move.getMove() == Moves.OUTRAGE || move.getMove() == Moves.PETALDANCE) {
			if (!move.isWaiting()) {
				setStatus(atk, Status.CONFUSE);				
			}
		}
		else if (move.getMove() == Moves.WAKEUPSLAP) {
			if (trg.hasStatus(Status.SLEEP)) {
				removeStatus(trg);
			}
		}
		// move causes attribute or status effect
		else if (move.getProbability() != 0.0) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) {
					
					if (move.isToSelf()) {
						setAttribute(atk, move.getStats(), move.getLevel());
					}
					else {
						setAttribute(trg, move.getStats(), move.getLevel());	
					}					
				}
				else {			
					setStatus(trg, move.getEffect());						
				}
			}							
		}
		
		if (atk.getStatus() == null && trg.getAbility() == Ability.STATIC && 
				move.getMType() == MoveType.PHYSICAL && Math.random() < 0.30) {
			setStatus(atk, Status.PARALYZE);						
		}
	}
	private boolean flinched(Pokemon pkm, Move move) throws InterruptedException {
		
		boolean flinched = false;
		
		if (move.getFlinch() != 0.0 && pkm.getAbility() != Ability.INNERFOCUS) {
			
			if (new Random().nextDouble() <= move.getFlinch()) {				
				typeDialogue(pkm.getName() + " flinched!");
				flinched = true;
			}
		}		
		
		return flinched;
	}
	
	private void checkActiveMoves(int trg, int atk) throws InterruptedException {
			
		Iterator<Move> iterator = fighter[trg].getActiveMoves().iterator();
		while (iterator.hasNext()) {
			
			Move move = iterator.next();
			
			switch (move.getMove()) {
				case LEECHSEED:
					leechSeed(fighter[trg], fighter[atk]);
					break;
				case REFLECT:
					move.setTurnCount(move.getTurnCount() - 1);
					
					if (move.getTurnCount() <= 0) {		
						iterator.remove();						
						typeDialogue(move.getDelay(move.getName()));
					}
					break;
				default: 
					break;
			}
		}
	}	
	private void leechSeed(Pokemon trg, Pokemon atk) throws InterruptedException {
		
		if (trg.isAlive() && atk.isAlive()) {
			
			int stolenHP = (int) (trg.getHP() * 0.125);						
			if (stolenHP > trg.getHP()) stolenHP = trg.getHP();
			
			decreaseHP(trg, stolenHP);
				
			if (atk.getHP() != atk.getBHP()) {
				
				if (stolenHP + atk.getHP() > atk.getBHP()) {					
					stolenHP = atk.getBHP() - atk.getHP();										
					increaseHP(atk, stolenHP);					
				}
				else {		
					increaseHP(atk, stolenHP);
				}
			}
			else {
				stolenHP = 0;
			}
			
			typeDialogue(atk.getName() + "\nabsorbed " + stolenHP + " HP!");
		}
	}
	
	// STATUS METHODS
	private void checkStatusDamage() throws InterruptedException {					
		if (fighter[0].isAlive()) {
			setStatusDamage(fighter[0]);
		}	
		if (fighter[1].isAlive()) {
			setStatusDamage(fighter[1]);
		}	
	}
	private void setStatusDamage(Pokemon pkm) throws InterruptedException {
		// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects		
		
		if (pkm.getStatus() != null) {				
			
			pause(500);
			
			if (pkm.getStatus().getAbreviation().equals("PSN") || 
					pkm.getStatus().getAbreviation().equals("BRN")) {
									
				int damage = (int) Math.ceil((pkm.getHP() * 0.16));
				if (damage > pkm.getHP()) damage = pkm.getHP();
				
				gp.playSE(gp.battle_SE, pkm.getStatus().getStatus());
				pkm.setHit(true);
				decreaseHP(pkm, damage);
				
				pkm.getStatus().printStatus(gp, pkm.getName());
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
				gp.playSE(gp.battle_SE, "sunlight");
				typeDialogue("The sunlight continues\nto shine intensely!");	
				break;
			case RAIN:
				gp.playSE(gp.battle_SE, "rain");
				typeDialogue("Rain continues to fall!");	
				break;
			case HAIL:
				gp.playSE(gp.battle_SE, "hail");
				typeDialogue("Hail continues to fall!");	
				
				if (fighter[0].isAlive() && !fighter[0].checkType(Type.ICE)) {
					setWeatherDamage(fighter[0]);
				}
				if (fighter[1].isAlive() &&	!fighter[1].checkType(Type.ICE)) {
					setWeatherDamage(fighter[1]);
				}	
				
				break;
			case SANDSTORM:
				gp.playSE(gp.battle_SE, "sandstorm");
				typeDialogue("The sandstorm continues to rage!");	
				
				if (fighter[0].isAlive() &&
						!fighter[0].checkType(Type.ROCK) && 
						!fighter[0].checkType(Type.STEEL) &&
						!fighter[0].checkType(Type.GROUND)) {
					setWeatherDamage(fighter[0]);
				}
				if (fighter[1].isAlive() &&
						!fighter[1].checkType(Type.ROCK) && 
						!fighter[1].checkType(Type.STEEL) &&
						!fighter[1].checkType(Type.GROUND)) {
					setWeatherDamage(fighter[1]);
				}
				
				break;
		}
	}
	private void setWeatherDamage(Pokemon pkm) throws InterruptedException {
		
		int damage = (int) Math.ceil((pkm.getHP() * 0.0625));
		if (damage > pkm.getHP()) damage = pkm.getHP();
		
		gp.playSE(gp.battle_SE, "hit-normal");
		pkm.setHit(true);
		decreaseHP(pkm, damage);
		
		typeDialogue(pkm.getName() + " was hurt\nby the " + weather.toString().toLowerCase() + "!");
	}
	
	// GET WINNING POKEMON METHODS
	private boolean hasWinningPokemon() {
		
		boolean hasWinningPokemon = false;
		
		if (fighter[0].getHP() <= 0) {
			fighter[0].setAlive(false);
			playerMove = null;
		}
		if (fighter[1].getHP() <= 0) {
			fighter[1].setAlive(false);	
			cpuMove = null;
		}
		
		// TIE
		if (!fighter[0].isAlive() && !fighter[1].isAlive()) {
			winner = 2;
			loser = 2;
			hasWinningPokemon = true;
		}			
		// PLAYER 2 WINS
		else if (!fighter[0].isAlive()) {	
			otherFighters.remove(fighter[0]);
			winner = 1;
			loser = 0;
			hasWinningPokemon = true;
		}
		// PLAYER 1 WINS
		else if (!fighter[1].isAlive()) {
			winner = 0;
			loser = 1;
			hasWinningPokemon = true;
		}
		
		return hasWinningPokemon;
	}
	public void getWinningPokemon() throws InterruptedException {
		
		// TRAINER 1 WINNER
		if (winner == 0) {		
			
			if (playerMove != null) playerMove.resetMoveTurns();
			playerMove = null;
			fighter[0].clearProtection();
					
			gp.playSE(gp.faint_SE, fighter[1].toString());
			typeDialogue(fighter[1].getName() + " fainted!");	
			
			gainXP();
		}
		// TRAINER 2 WINNER
		else if (winner == 1) {		
			
			if (cpuMove != null) cpuMove.resetMoveTurns();
			cpuMove = null;
			fighter[1].clearProtection();
			
			gp.playSE(gp.faint_SE, fighter[0].toString());			
			typeDialogue(fighter[0].getName() + " fainted!");	
		}
		// TIE GAME
		else if (winner == 2) {
			
			playerMove = null;
			cpuMove = null;
			
			gp.playSE(gp.faint_SE, fighter[0].toString());
			typeDialogue(fighter[0].getName() + " fainted!");
			
			gp.playSE(gp.faint_SE, fighter[1].toString());
			typeDialogue(fighter[1].getName() + " fainted!");
		}
	}		
	private void gainXP() throws InterruptedException {
		
		int gainedXP = BattleUtility.calculateEXPGain(fighter[loser], battleMode == trainerBattle);
				
		if (otherFighters.size() > 0) {
			
			gainedXP = (int) Math.ceil(gainedXP / (otherFighters.size() + 1));			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2800.0 / (double) gainedXP);
			
			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			increaseXP(fighter[0], xp, expTimer);	
			
			for (Pokemon p : otherFighters) {
								
				typeDialogue(p.getName() + " gained\n" + gainedXP + " Exp. Points!");
				
				xp = p.getXP() + gainedXP;				
				increaseXP(p, xp, 0);
			}
			
			otherFighters.clear();
		}
		else {
			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2500.0 / (double) gainedXP);

			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			increaseXP(fighter[0], xp, expTimer);	
		}
		
		getOtherFighters();	
		pause(500);
	}
	private void increaseXP(Pokemon p, int xp, int timer) throws InterruptedException {
		
		while (p.getXP() < xp) {
			
			p.setXP(p.getXP() + 1);
			
			pause(timer);
												
			// FIGHTER LEVELED UP
			if (p.getXP() >= p.getBXP() + p.getNextXP()) {			
				
				gp.pauseMusic();
				gp.playSE(gp.battle_SE, "level-up");
				
				p.levelUp();					
				gp.ui.battleState = gp.ui.battle_LevelUp;
			
				gp.playSE(gp.battle_SE, "upgrade");
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
				
				gp.playSE(gp.battle_SE, "upgrade");
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
								
				gp.gameState = gp.pauseState;
				gp.ui.pauseState = gp.ui.pause_Party;
				gp.ui.partyState = gp.ui.party_MoveSwap;
				
				while (oldMove == null && !gp.keyH.bPressed) {
					pause(5);
				}
				
				if (oldMove != null) {				
					
					if (playerMove == oldMove) {
						playerMove = null;
						fighter[0].clearProtection();
					}
					
					typeDialogue("1, 2, and.. .. ..\nPoof!", true);
					typeDialogue(pokemon.getName() + " forgot\n" + oldMove.getName() + ".", true);	
					typeDialogue("And...", true);
					
					gp.pauseMusic();
					gp.playSE(gp.battle_SE, "upgrade");
					
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
		if (trainer == null) {
			
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
					
					gp.gameState = gp.pauseState;
					gp.ui.pauseState = gp.ui.pause_Party;
					gp.ui.partyState = gp.ui.party_Main_Select;
					
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

					newFighter[1] = BattleUtility.getCPUFighter(trainer, fighter[1], fighter[0], weather);	
					
					if (gp.player.getAvailablePokemon() > 1 && set) {
						
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
					
					gp.gameState = gp.pauseState;
					gp.ui.pauseState = gp.ui.pause_Party;
					gp.ui.partyState = gp.ui.party_Main_Select;
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
					
					newFighter[1] = BattleUtility.getCPUFighter(trainer, fighter[1], fighter[0], weather);					
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.gameState = gp.pauseState;
					gp.ui.pauseState = gp.ui.pause_Party;
					gp.ui.partyState = gp.ui.party_Main_Select;
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
				
				gp.gameState = gp.pauseState;
				gp.ui.pauseState = gp.ui.pause_Party;
				gp.ui.partyState = gp.ui.party_Main_Select;	
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
			
			int moneyEarned = BattleUtility.calculateMoneyEarned(trainer);
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
		
	// DELAYED TURN USED
	private void getDelayedTurn() {
		
		// RESET NON-DELAYED MOVES
		int delay = BattleUtility.getDelay(playerMove, cpuMove);	
				
		if (delay == 0) { 
			
			if (playerMove != null) playerMove.resetMoveTurns();
			playerMove = null; 
			
			if (cpuMove != null) cpuMove.resetMoveTurns();			
			cpuMove = null; 
		}
		else if (delay == 1) {
			
			if (cpuMove != null) cpuMove.resetMoveTurns();
			cpuMove = null;	
		}
		else if (delay == 2) {
			
			if (playerMove != null) playerMove.resetMoveTurns();
			playerMove = null;
		}
		
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
		
		if (trainer == null) {			
			
			typeDialogue(gp.player.name + " used a\n" + ballUsed.name + "!", false);
			playSE(gp.battle_SE, "ball-throw");
			gp.ui.isFighterCaptured = true;
			playSE(gp.battle_SE, "ball-open");
			playSE(gp.battle_SE, "ball-bounce");
			
			for (int i = 0; i < 3; i++) {
				playSE(gp.battle_SE, "ball-shake");
				Thread.sleep(800);	
			}	
			
			if (BattleUtility.isCaptured(fighter[1], ballUsed)) {				
				capturePokemon();
			}
			else {
				gp.playSE(gp.battle_SE, "ball-open");
				gp.ui.isFighterCaptured = false;		
				gp.playSE(gp.battle_SE, "ball-open");
				typeDialogue("Oh no!\n" + fighter[1].getName() + " broke free!", true);
				fightStage = fight_Start;			
			}	
		}
		else {
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
			fighter[1].resetMoves();
			fighter[1].setBall(ballUsed);
			gp.player.pokeParty.add(fighter[1]);
			typeDialogue(fighter[1].getName() + " was added\nto your party!", true);
		}
		else {
			
		}
		
		endBattle();
	}
	
	// RUN AWAY METHOD
	public void escapeBattle() throws InterruptedException {
		/** ESCAPE FORMULA REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Escape **/
		
		boolean escape = false;
		
		if (trainer == null) {
			
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
				gp.playSE(gp.battle_SE, "run");
				typeDialogue("Got away safely!", true);
				endBattle();
			}
			else {				
				typeDialogue("Oh no!\nYou can't escape!", true);
				fightStage = fight_Start;
			}
		}
		else {
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
				
				gp.playSE(gp.cry_SE, gp.se.getFile(3, oldEvolve.toString()));
				
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
		
	// BATTLE END METHODS
 	public void endBattle() {
		gp.stopMusic();
		gp.setupMusic();
		
		for (Pokemon p : gp.player.pokeParty) {
			p.resetStats();
			p.resetStatStages();
			p.resetMoveTurns();
			p.clearActiveMoves();
		}
		
		if (trainer != null) {
			for (Pokemon p : trainer.pokeParty) {
				p.resetMoveTurns();
				p.resetStats();
				p.resetStatStages();
				p.clearActiveMoves();
			}	
		}		
		
		gp.ui.isFighterCaptured = false;
		gp.ui.commandNum = 0;
		
		fightStage = fight_Encounter;
		resetValues();
		
		gp.particleList.clear();
		gp.gameState = gp.playState;
	}	 	
 	private void resetValues() {
		battleQueue.clear();	
		
		active = false; running = false;
								
		trainer = null;
		fighter[0] = null; fighter[1] = null;
		newFighter[0] = null; newFighter[1] = null;
		otherFighters.clear();
		
		playerMove = null; cpuMove = null;		
		newMove = null; ballUsed = null;		
				
		weather = Weather.CLEAR;
		weatherDays = -1;
		
		winner = -1;
		loser = -1;
		
		escapeAttempts = 0;
 	}
	
	// MISC METHODS	
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
	private void increaseHP(Pokemon pkm, int gainedHP) throws InterruptedException {
				
		int hpTimer = getHPTimer(gainedHP);
		
		int newHP = pkm.getHP() + gainedHP;
		while (pkm.getHP() < newHP) {			
			pkm.setHP(pkm.getHP() + 1);
			pause(hpTimer);
		}	 
	}
	private void decreaseHP(Pokemon pkm, int lostHP) throws InterruptedException {
		
		int hpTimer = getHPTimer(lostHP);
		
		int newHP = pkm.getHP() - lostHP;
		while (newHP < pkm.getHP()) {			
			pkm.setHP(pkm.getHP() - 1);
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