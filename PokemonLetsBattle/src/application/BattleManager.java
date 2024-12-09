package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	
	private static List<Moves> soundMoves = Arrays.asList(
			Moves.GROWL, 
			Moves.HOWL,
			Moves.HYPERBEAM, 
			Moves.HYPERVOICE,
			Moves.PERISHSONG,
			Moves.SCREECH,
			Moves.SNORE,
			Moves.SUPERSONIC
	);
	private static final Map<Integer, Integer> magnitudeTable = Map.ofEntries(
			Map.entry(4, 5), 
			Map.entry(5, 10), 
			Map.entry(6, 20), 
			Map.entry(7, 30), 
			Map.entry(8, 20), 
			Map.entry(9, 10), 
			Map.entry(10, 5)
	);
	
	// GENERAL VALUES	
	private GamePanel gp;
	public boolean set = true;
	public boolean active = false;
	public boolean running = false;
	private int textSpeed = 30;
	
	// BATTLE INFORMATION			
	public boolean cpu = false;
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
	private final int queue_PlayerSwap = 1;
	private final int queue_CPUSwap = 2;
	private final int queue_GetCPUMove = 3;
	private final int queue_Rotation = 4;
	private final int queue_PlayerMove = 5;
	private final int queue_CPUMove = 6;
	private final int queue_ActiveMoves = 7;
	private final int queue_StatusDamage = 8;
	private final int queue_WeatherDamage = 9;
	private final int queue_TurnReset = 10;
	
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
	public final int fight_Start = 2;
	public final int fight_Capture = 3;
	public final int fight_Run = 4;
	public final int fight_Evolve = 5;
	
	/** CONSTRUCTOR **/
	public BattleManager(GamePanel gp) {
		this.gp = gp;
	}
	
	/** SETUP METHOD **/
	public void setup(int currentBattle, Entity trainer, Pokemon pokemon, String condition, boolean cpu) {
		
		if (gp.ui.textSpeed == 2) textSpeed = 30;
		else if (gp.ui.textSpeed == 3) textSpeed = 40;
		else if (gp.ui.textSpeed == 4) textSpeed = 50;
						
		battleMode = currentBattle;
						
		if (trainer != null) this.trainer = trainer;	
		else if (pokemon != null) fighter[1] = pokemon;
		
		this.cpu = cpu;
		
		if (condition == null) weather = Weather.CLEAR;
		else weather = Weather.valueOf(condition);

		weatherDays = -1;					

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
		
	/** SETUP BATTLE METHODS **/
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
		
		for (Pokemon p : gp.player.pokeParty) {
			if (p.isAlive()) { fighter[0] = p; break; }
		}
		
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
		
		gp.player.trackSeenPokemon(fighter[1]);
		
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
			if (p != fighter[0] && 		
					!otherFighters.contains(p) &&
					p.getItem() != null && 
					p.getItem().name.equals(ITM_EXP_Share.colName)) {				
				otherFighters.add(p);
			}
		}
	}

	/** END SETUP BATTLE METHODS **/
	
	/** SWAP FIGHTERS 
	 * @throws InterruptedException **/
	private void swapPlayerFighter() throws InterruptedException {
								
		if (!fighter[0].isAlive()) fighter[0] = null;
		
		gp.ui.battleState = gp.ui.battle_Dialogue;
		
		fighter[0] = newFighter[0];		
		if (otherFighters.contains(fighter[0])) {
			otherFighters.remove(fighter[0]);
		}		
		
		gp.playSE(gp.cry_SE, fighter[0].toString());	
		typeDialogue("Go, " + fighter[0].getName() + "!");					
		pause(100);
		
		newFighter[0] = null;
		
		getFighterAbility();
	}
	private void swapCPUFighter() throws InterruptedException {
		
		if (!fighter[1].isAlive()) fighter[1] = null;
		
		gp.ui.battleState = gp.ui.battle_Dialogue;
		
		fighter[1] = newFighter[1];		
		gp.player.trackSeenPokemon(fighter[1]);
		
		gp.playSE(gp.cry_SE, fighter[1].toString());	
		typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
		pause(100);		
		
		newFighter[1] = null;	
		
		getFighterAbility();
	}	
	
	/*
	private void swapFighters() throws InterruptedException {
			
		if (!fighter[0].isAlive()) fighter[0] = null;
		if (!fighter[1].isAlive()) fighter[1] = null;
		
		gp.ui.battleState = gp.ui.battle_Dialogue;
		
		// CPU SWAP OUT
		if (newFighter[1] != null) {
									
			fighter[1] = newFighter[1];			
			gp.player.trackSeenPokemon(fighter[1]);
			
			gp.playSE(gp.cry_SE, fighter[1].toString());	
			typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
			pause(100);
			
			// PLAYER SWAP OUT
			if (newFighter[0] != null) {
				
				if (otherFighters.contains(newFighter[0])) {
					otherFighters.remove(newFighter[0]);
				}
				
				fighter[0] = newFighter[0];		
				
				gp.playSE(gp.cry_SE, fighter[0].toString());	
				typeDialogue("Go, " + fighter[0].getName() + "!");					
				pause(100);
											
				running = false;								
				gp.ui.battleState = gp.ui.battle_Options;	
			}
			else {
				fightStage = fight_Start;
			}
			
			newFighter[0] = null;
			newFighter[1] = null;		
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
			
			if (fighter[0].getAbility() == Ability.NATURALCURE) {
				fighter[0].removeStatus();
			}

			fighter[0] = newFighter[0];		
			
			gp.playSE(gp.cry_SE, fighter[0].toString());
			typeDialogue("Go, " + fighter[0].getName() + "!");	
			pause(100);
			
			newFighter[0] = null;
			newFighter[1] = null;
			
			if (cpu) {
				fightStage = fight_Start;	
			}
			else {
				running = false;				
				gp.ui.player = 1;
				gp.ui.battleState = gp.ui.battle_Options;	
			}
		}		
		
		getFighterAbility();
	}
	*/
	public boolean swapPokemon(int partySlot) {
		
		Entity player = gp.player;
		if (gp.ui.player == 1) {
			player = trainer;
			battleQueue.add(queue_CPUSwap);
		}
		else {
			battleQueue.add(queue_PlayerSwap);
		}
		if (fighter[gp.ui.player] == player.pokeParty.get(partySlot)) {
			return false;
		}
		
		if (player.pokeParty.get(partySlot).isAlive()) {
						
			fighter[gp.ui.player].resetStats();
			fighter[gp.ui.player].resetStatStages();
			fighter[gp.ui.player].resetMoveTurns();
			fighter[gp.ui.player].clearActiveMoves();
			
			newFighter[gp.ui.player] = player.pokeParty.get(partySlot);
			
			if (gp.player.pokeParty.size() > 1) {
				Collections.swap(player.pokeParty, 0, partySlot);	
			}
						
			return true;
		}
		else {
			return false;
		}		
	}
						
	/** RUN BATTLE METHOD **/
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
			
			System.out.println(action);
			
			switch (action) {
			
				case queue_PlayerSwap:
					swapPlayerFighter();
					break;
				case queue_CPUSwap:
					swapCPUFighter();
					break;
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
					checkActiveMoves(fighter[0], fighter[1]);
					checkActiveMoves(fighter[1], fighter[0]);
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
	
	/** GET MOVE METHODS **/
	public Move getPlayerMove(int selection, int player) {
		
		Move selectedMove = fighter[player].getMoveSet().get(selection);
		
		boolean struggle = true;
		for (Move m : fighter[player].getMoveSet()) {
			if (m.getPP() > 0) {
				struggle = false;
				break;
			}
		}
		
		if (struggle) {
			selectedMove = new Move(Moves.STRUGGLE);
		}	
		
		if (player == 0) {
			playerMove = selectedMove;
		}
		else {
			cpuMove = selectedMove;
		}
		
		return selectedMove;
	}
	private void getCPUMove() throws InterruptedException {
		
		if (cpu) {
			
			// GET CPU MOVE IF NO CPU DELAY
			int delay = getDelay();
			
			if (delay == 0 || delay == 1) {						
				cpuMove = chooseCPUMove();		
			}	
		}		
	}
	private int getDelay() {		
		
		int delay = 0;
		
		// BOTH MOVES ARE ACTIVE
		if (playerMove != null && cpuMove != null) {
			
			// both fighters are waiting
			if (playerMove.isWaiting() && cpuMove.isWaiting()) {
				delay = 3;	
			}
			// fighter 2 is waiting;
			else if (cpuMove.isWaiting()) {
				delay = 2;	
			}
			// fighter 1 is waiting
			else if (playerMove.isWaiting()) {
				delay = 1;
			}	
		}
		// CPU MOVE IS ACTIVE AND WAITING
		else if (cpuMove != null && cpuMove.isWaiting()) {
			delay = 2;
		}
		// PLAYER MOVE IS ACTIVE AND WAITING
		else if (playerMove != null && playerMove.isWaiting()) {
			delay = 1;
		}
				
		return delay;
	}	
	public Move chooseCPUMove() {
		/** SWITCH OUT LOGIC REFERENCE: https://www.youtube.com/watch?v=apuO7pvmGUo **/
		
		Move bestMove = null;
		
		if (trainer == null || trainer.skillLevel  == trainer.skill_rookie) {
			bestMove = chooseCPUMove_Random();
		}
		else if (trainer.skillLevel == trainer.skill_smart) {
			bestMove = chooseCPUMove_Power();
		}
		else if (trainer.skillLevel == trainer.skill_elite) {
			bestMove = chooseCPUMove_Best();
		}		
		
		return bestMove;
	}
 	private Move chooseCPUMove_Random() {
		
		Move bestMove = null;
		
		int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
		bestMove = fighter[1].getMoveSet().get(ranMove);
				
		return bestMove;
	}
	private Move chooseCPUMove_Power() {
		
		Move bestMove = null;
		
		Map<Move, Integer> damageMoves = new HashMap<>();
		
		for (Move move : fighter[1].getMoveSet()) {
			
			if (move.getPower() > 0 && move.getPP() != 0) {
				
				double power = getPower(move, fighter[1], fighter[0]);				
				double type = getEffectiveness(fighter[0], move.getType());				
				
				// CALCULATE POWER OF EACH MOVE
				int result = (int) (power * type);
				
				damageMoves.put(move, result);	
			}		
		}
				
		if (damageMoves.isEmpty()) {		
			
			for (Move move : fighter[1].getMoveSet()) {
				
				if (move.getMType() == MoveType.STATUS) {
					bestMove = move;
					return bestMove;
				}		
			}
			
			int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
			bestMove = fighter[1].getMoveSet().get(ranMove);				
		}
		else {

			bestMove = Collections.max(damageMoves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 

			int val = 1 + (int)(Math.random() * 4);
			if (val == 1) {				
				int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
				bestMove = fighter[1].getMoveSet().get(ranMove);
			}	
		}
		
		return bestMove;
	}
	private Move chooseCPUMove_Best() {
		
		Move bestMove = null;
		
		Map<Move, Integer> damageMoves = new HashMap<>();
		Map<Move, Integer> koMoves = new HashMap<>();
		
		for (Move move : fighter[1].getMoveSet()) {
			
			if (move.getPower() > 0 && move.getPP() != 0) {
				
				int damage = calculateDamage(fighter[1], fighter[0], move);				
				damageMoves.put(move, damage);	
				
				if (damage >= fighter[0].getHP() && move.getPriority() > 0) {
					int accuracy = (int) getAccuracy(fighter[1], move);
					koMoves.put(move, accuracy);
				}
			}		
		}
		
		if (koMoves.isEmpty()) {
			
			if (damageMoves.isEmpty()) {		
				
				for (Move move : fighter[1].getMoveSet()) {
					
					if (move.getMType() == MoveType.STATUS) {
						bestMove = move;
						return bestMove;
					}		
				}
				
				int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
				bestMove = fighter[1].getMoveSet().get(ranMove);				
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
	/** END GET MOVE METHODS **/
	
	/** SET ROTATION **/
	private void setRotation() {		
		// 0 if neither	goes first
		// 1 if player moves first
		// 2 if cpu moves first
		// 3 if only cpu moves
		// 4 if only player	moves	
		
		if (fighter[0].isAlive() && fighter[1].isAlive()) {
							
			int firstTurn = getFirstTurn();	
			
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
	private int getFirstTurn() {
		
		int first = 1;
		
		if (playerMove == null && cpuMove == null) {
			first = 0;
		}
		else if (playerMove == null) {
			first = 3;
		}
		else if (cpuMove == null) {
			first = 4;
		}
		else {				
			if (playerMove.getPriority() > cpuMove.getPriority()) {
				first = 1;
			}
			else if (playerMove.getPriority() < cpuMove.getPriority()) {
				first = 2;
			}
			else {
				if (fighter[0].getSpeed() > fighter[1].getSpeed()) {
					first = 1;
				}
				else if (fighter[0].getSpeed() < fighter[1].getSpeed()) {
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
		
	/** PLAYER MOVE **/
	private void playerMove() throws InterruptedException {
		
		if (canMove(fighter[0], playerMove)) {
			move(fighter[0], fighter[1], playerMove);	
		}	
		else {
			playerMove.resetMoveTurns();
		}
	}		
	
	/** CPU MOVE **/
	private void cpuMove() throws InterruptedException {
		
		if (canMove(fighter[1], cpuMove)) {
			move(fighter[1], fighter[0], cpuMove);
		}		
		else {
			cpuMove.resetMoveTurns();
		}
	}	
	
	/** CAN MOVE METHODS **/
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
			
			int damage = getConfusionDamage(pkm);		
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
	private int getConfusionDamage(Pokemon pkm) {
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
	/** END CAN MOVE METHODS **/
		
	/** MOVE METHODS **/
	private void move(Pokemon atk, Pokemon trg, Move atkMove) throws InterruptedException {		

		getWeatherMoveDelay(atkMove);
		
		if (atk.hasActiveMove(Moves.WRAP)) {
			
			gp.playSE(gp.battle_SE, "hit-normal");
			atk.setHit(true);
			
			int damage = (int) Math.ceil(atk.getHP() * 0.0625);
			if (damage >= atk.getHP()) damage = atk.getHP();	
			decreaseHP(atk, damage);		
			
			typeDialogue(atk.getName() + " is\nhurt by Wrap!");
		}
		else if (atkMove.isReady()) {				
			
			typeDialogue(atk.getName() + " used\n" + atkMove.toString() + "!", false); 
			
			atk.setProtection(Protection.NONE);			
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
			atk.setProtection(atkMove.getProtection());				
			typeDialogue(atkMove.getDelay(atk.getName()));	
			
			atkMove.setTurnCount(atkMove.getTurnCount() - 1);
		}
	}		
	private void getWeatherMoveDelay(Move move) {
		
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
 	private boolean validMove(Pokemon atk, Pokemon trg, Move move) {
		
		if ((move.getMove() == Moves.SNORE && atk.hasStatus(Status.SLEEP)) ||
			(move.getMove() == Moves.DREAMEATER && trg.hasStatus(Status.SLEEP)) ||
			(move.getMove() == Moves.SUCKERPUNCH && battleQueue.peek() == queue_ActiveMoves) ||
			(move.getMove() == Moves.FEINT && (!trg.hasActiveMove(Moves.PROTECT) && !trg.hasActiveMove(Moves.DETECT)))) {
			return false;
		}
		else {
			return true;
		}
	}
	/** END MOVE METHODS **/
 	
 	/** ATTACK METHOD **/
	public void attack(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
			
		if (hit(atk, trg, move)) {
			
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
			
			if (move.getMove() == Moves.JUMPKICK || move.getMove() == Moves.HIGHJUMPKICK) {
				
				int damage = (int) Math.floor(atk.getHP() * 0.125);
				if (damage >= atk.getHP()) damage = atk.getHP();					
										
				decreaseHP(atk, damage);		
				typeDialogue(atk.getName() + " hurt\n" + damage + " itself!");		
			}
		}			
	}		
	private boolean hit(Pokemon atk, Pokemon trg, Move move) {
		/** PROBABILITY FORMULA REFERENCE: https://monster-master.fandom.com/wiki/Evasion **/
		/** PROTECTED MOVES REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Semi-invulnerable_turn **/
		
		boolean hit = false;
		
		switch (trg.getProtection()) {
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
					accuracy = getAccuracy(atk, move) * atk.getAccuracy();
				}
				else {
					accuracy = getAccuracy(atk, move) * (atk.getAccuracy() / trg.getEvasion());	
				}				
								
				Random r = new Random();
				float chance = r.nextFloat();
				
				// chance of missing is accuracy / 100
				hit = (chance <= ((float) accuracy / 100)) ? true : false;	
			}
		}
		
		return hit;
	}
	private double getAccuracy(Pokemon pkm, Move move) {
		
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
		
		if (pkm.getAbility() == Ability.COMPOUNDEYES) {
			accuracy *= 1.3;
		}
		
		return accuracy;
	}
	/** END ATTACK METHODS **/
	
	/** STATUS MOVE METHODS **/	
	private void statusMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {		
		
		if (trg.getStatus() == null) {		
			
			if (trg.hasActiveMove(Moves.SAFEGUARD)) {
				typeDialogue("It had no effect!");
			}
			else {
				setStatus(atk, trg, move.getEffect());		
			}			
		}
		else {				
			typeDialogue(trg.getName() + " is\nalready " + 
					trg.getStatus().getStatus() + "!");
		}			
	}
	private void setStatus(Pokemon atk, Pokemon trg, Status status) throws InterruptedException {
		
		if (trg.getHP() > 0) {
			
			if (trg.getStatus() == null) {
				
				if (trg.getAbility() == Ability.LIMBER && status == Status.PARALYZE) {
					typeDialogue ("It had no effect!");
				}
				else {
					trg.setStatus(status);		
					
					gp.playSE(gp.battle_SE, status.getStatus());
					typeDialogue(trg.getName() + status.printCondition());
					
					if (trg.getAbility() == Ability.QUICKFEET && !trg.getAbility().isActive()) {
						trg.getAbility().setActive(true);
						setAttribute(trg, Arrays.asList("speed"), 2);
					}
					else if (trg.getAbility() == Ability.GUTS && !trg.getAbility().isActive()) {
						trg.getAbility().setActive(true);
						setAttribute(trg, Arrays.asList("attack"), 2);
					}
					else if ((trg.getAbility() == Ability.SYNCHRONIZE) && 
							(status == Status.BURN || status == Status.PARALYZE || status == Status.POISON)) {						
						setStatus(trg, atk, status);
					}	
				}
			}		
		}
	}
	private void removeStatus(Pokemon pkm) throws InterruptedException {
		Status status = pkm.getStatus();
		pkm.removeStatus();	
		typeDialogue(pkm.getName() + status.printRecover());	
	}
	/** END STATUS MOVE METHODS **/

	/** ATTRIBUTE MOVE METHODS **/
	private void attributeMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			int level = move.getLevel();
							
			if (weather == Weather.SUNLIGHT && move.getMove() == Moves.GROWTH) {
				level++;
			}
			
			setAttribute(atk, move.getStats(), level);				
		}
		// if move changes target attributes
		else {			
			if (trg.hasActiveMove(Moves.MIST) || trg.getAbility() == Ability.CLEARBODY) {
				typeDialogue("It had no effect!");
			}
			else {				
				if (move.getMove() == Moves.CAPTIVATE && trg.getSex() == atk.getSex()) {
					typeDialogue("It had no effect!");					
				}
				else {
					setAttribute(trg, move.getStats(), move.getLevel());
					
					if (move.getMove() == Moves.SWAGGER) {
						setStatus(atk, trg, Status.CONFUSE);
					}		
				}
			}
		}			
	}
	private void setAttribute(Pokemon pkm, List<String> stats, int level) throws InterruptedException {
		
		// loop through each specified attribute to be changed
		for (String stat : stats) {	
									
			if (level > 0) {
				gp.playSE(gp.battle_SE, "stat-up");
				typeDialogue(pkm.changeStat(stat, level));	
			}
			else {				
				if (pkm.getAbility() == Ability.KEENEYE && stat.equals("accuracy")) {
					typeDialogue("It had no effect!");
				}
				else {
					gp.playSE(gp.battle_SE, "stat-down");		
					typeDialogue(pkm.changeStat(stat, level));	
				}
			}
		}			
	}
	/** END ATTRIBUTE MOVE METHODS **/
	
	/** WEATHER MOVE METHODS **/
	private void weatherMove(Move move) throws InterruptedException {			
		weather = move.getWeather();
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
	/** END WEATHER MOVE METHODS **/
	
	/** OTHER MOVE METHODS **/
	private void otherMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		switch (move.getMove()) {
			case DETECT, PROTECT:
				atk.addActiveMove(Moves.PROTECT);
				typeDialogue(atk.getName() + " protected\nitself!");
				break;
			case ENDURE:
				typeDialogue(atk.getName() + " braced\nitself!");
				break;
			case FLING:				
				Entity item = atk.getItem();
				if (item != null && item.damage != 0) {
					typeDialogue(atk.getName() +" flung\nits " + item.name + "!");
					damageMove(atk, trg, move);
				}
				else {
					typeDialogue("It had no effect!");
				}
				
				break;
			case FUTURESIGHT:
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(trg.getName() + " has\nforeseen an attack!");	
				}
				break;
			case HAZE:				
				atk.resetStats();
				atk.resetStatStages();		
				
				trg.resetStats();
				trg.resetStatStages();		
				
				typeDialogue("All stat changes were\neliminated!");
				break;
			case HEALBELL:				
				atk.removeStatus();
				
				if (atk == fighter[0]) {
					for (Pokemon p : gp.player.pokeParty) {
						p.removeStatus();
					}
				}
				else if (atk == fighter[1] && trainer != null) {
					for (Pokemon p : trainer.pokeParty) {
						p.removeStatus();
					}	
				}
				
				typeDialogue("A bell chimed!");
				break;
			case LEECHSEED:				
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + " planted\na seed on " + trg.getName() + "!");	
				}
				break;
			case LIGHTSCREEN, REFLECT:
				if (atk.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					atk.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + "'s " + move.toString() + "\nraised DEFENSE!");					
				}			
				break;	
			case MIST:
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + " became\nshrouded in MIST!");	
				}
				break;
			case METRONOME:				
				do {					
					int randomIndex = new Random().nextInt(Moves.values().length);
					move = new Move(Moves.values()[randomIndex]);	
				}
				while (move.getMove() == Moves.METRONOME || move.getMove() == Moves.SLEEPTALK);
								
				move(atk, trg, move);
				
				break;
			case ODORSLEUTH, MIRACLEEYE, FORESIGHT:
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + " identified\n" + trg.getName() + "!");					
				}	
				break;					
			case PERISHSONG:				
				if (trg.hasActiveMove(move.getMove()) && atk.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else if (trg.getAbility() == Ability.SOUNDPROOF && atk.getAbility() == Ability.SOUNDPROOF) {
					typeDialogue("It had no effect!");
				}
				else {
					if (!trg.hasActiveMove(move.getMove()) && trg.getAbility() != Ability.SOUNDPROOF) {
						trg.addActiveMove(move.getMove());
					}
					if (!atk.hasActiveMove(move.getMove()) && atk.getAbility() != Ability.SOUNDPROOF) {
						atk.addActiveMove(move.getMove());
					}	
					
					typeDialogue("All Pokémon hearing the song\nwill faint in three turns!");					
				}	
				break;
			case PSYCHOSHIFT:				
				if (atk.getStatus() != null) {
					setStatus(atk, trg, atk.getStatus());
					atk.removeStatus();
				}
				else {
					typeDialogue("It had no effect!");
				}
				
				break;
			case PSYCHUP:
				atk.resetStats();
				atk.resetStatStages();			
				
				atk.changeStat("defense", trg.getDefenseStg());		
				atk.changeStat("sp. attack", trg.getSpAttackStg());		
				atk.changeStat("sp. defense", trg.getSpDefenseStg());		
				atk.changeStat("speed", trg.getSpeedStg());
				atk.changeStat("evasion", trg.getAccuracyStg());
				atk.changeStat("evasion", trg.getEvasionStg());
				
				typeDialogue(atk.getName() + " copied\n" + trg.getName() + "'s stats!");
				break;
			case RECOVER, ROOST:
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
			case REFRESH:
				
				if (atk.hasStatus(Status.BURN) || atk.hasStatus(Status.PARALYZE) || atk.hasStatus(Status.POISON)) {
					typeDialogue(atk.getName() + " status\nreturned to normal!");
				}
				else {
					typeDialogue("It had no effect!");
				}
				
				break;
			case REST:
				int gainedHP = atk.getBHP() - atk.getHP();
				increaseHP(atk, gainedHP);				
				typeDialogue(atk.getName() + "\nregained health!");
				setStatus(trg, atk, Status.SLEEP);		
				break;	
			case SLEEPTALK:				
				if (atk.hasStatus(Status.SLEEP)) {	
										
					do { move = atk.getMoveSet().get(new Random().nextInt(atk.getMoveSet().size())); }
					while (move.getMove() == Moves.SLEEPTALK);	
					
					move(atk, trg, move);
				}
				else {				
					typeDialogue("It had no effect!");
				}
				break;
			case SPITE:
				Move trgMove = move == playerMove ? cpuMove : playerMove;
				
				if (trgMove.getPP() < 1) {
					typeDialogue("It had no effect!");
				}
				else {					
					int PP = 4;
					if (trgMove.getPP() < PP) {
						PP = trgMove.getPP();					
					}
					trgMove.setPP(trgMove.getPP() - PP);					
					typeDialogue("It reduced the PP of\n" + trg.getName() + "'s " + 
							trgMove.getName() + " by " + PP + "!");
				}				
				
				break;
			case SPLASH:
				typeDialogue("It had no effect!");
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
			case WISH:
				if (atk.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					atk.addActiveMove(move.getMove());
					typeDialogue(atk.getName() + " made\na wish!");					
				}	
				break;
			case WRAP:
				if (trg.hasActiveMove(move.getMove())) {
					typeDialogue("It had no effect!");
				}
				else {
					trg.addActiveMove(move.getMove());
					typeDialogue(trg.getName() + " was\nwrapped by " + atk.getName() + "!");					
				}		
			default:
				break;		
		}		
	}
	/** END OTHER MOVE METHODS **/
	
	/** DAMAGE MOVE METHODS **/
	private void damageMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		getDamage(atk, trg, move);
		
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
	private void getDamage(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
				
		double crit = getCritical(atk, trg, move);	
		int damage = 0;
		
		switch (move.getMove()) {
		
			case COMETPUNCH, FURYATTACK, FURYSWIPES:
				int turns = new Random().nextInt(5 - 2 + 1) + 2;	
				int i = 1;
				while (i <= turns) {				
					damage += (int) (dealDamage(atk, trg, move) * crit);				
					pause(800);				
					if (trg.getHP() <= 0) break;
					else i++;
				}				
				typeDialogue("Hit " + i + " times!");
				break;
			default:
				damage = (int) (dealDamage(atk, trg, move) * crit);
				break;		
		}
		
		if (damage <= 0) {
			typeDialogue("It had no effect!");
		}
		else {			
			if (crit >= 1.5) typeDialogue("A critical hit!");
			
			String hitSE = getHitSE(getEffectiveness(trg, move.getType()));		
			if (hitSE.equals("hit-super")) typeDialogue("It's super effective!");			
			else if (hitSE.equals("hit-weak")) typeDialogue("It's not very effective...");			
									
			typeDialogue(trg.getName() + " took\n" + damage + " damage!");	
			
			if (move.getMove() == Moves.FEINT) {
				trg.removeActiveMove(Moves.DETECT);
				trg.removeActiveMove(Moves.PROTECT);
				typeDialogue(trg.getName() + " fell for\nthe feint!");
			}
		}
		
		switch (move.getMove()) {
			case ABSORB, DREAMEATER, GIGADRAIN, LEECHLIFE, MEGADRAIN:
				absorbHP(atk, damage);
				break;
			default: 
				break;
		}		
		
		getRecoil(atk, move, damage);		
	}
	private int dealDamage(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		double effectiveness = getEffectiveness(trg, move.getType());
		String hitSE = getHitSE(effectiveness);		
		gp.playSE(gp.battle_SE, hitSE);
		trg.setHit(true);
					
		int damage = calculateDamage(atk, trg, move);	
		
		if (trg.hasActiveMove(Moves.REFLECT) && move.getMType() == MoveType.PHYSICAL) {
			damage /= 2;			
		}		
				
		if (trg.getAbility() == Ability.FLASHFIRE && move.getType() == Type.FIRE &&
				!trg.getAbility().isActive()) {
			trg.getAbility().setActive(true);
		}
		else if (trg.getAbility() == Ability.WONDERGUARD && effectiveness <= 1.0) {
			damage = 0;
		}
		
		if (damage >= trg.getHP()) {					
			
			damage = trg.getHP();			
			
			Move otherMove = atk == fighter[1] ? playerMove : cpuMove;
			if (move.getMove() == Moves.FALSESWIPE || otherMove.getMove() == Moves.ENDURE) {
				damage--;				
			}
		}
								
		decreaseHP(trg, damage);	
		
		return damage;
	}	
	private int calculateDamage(Pokemon atk, Pokemon trg, Move move) {
		/** DAMAGE FORMULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Damage **/
		
		int damage = 0;
		
		double level = atk.getLevel();		
		double power = getPower(move, atk, trg);
		double A = getAttack(move, atk, trg);
		double D = getDefense(move, atk, trg);
		double STAB = atk.checkType(move.getType()) ? 1.5 : 1.0;
		double type = getEffectiveness(trg, move.getType());
								
		Random r = new Random();
		double random = (double) (r.nextInt(100 - 85 + 1) + 85) / 100.0;
				
		damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * STAB * type * random);
										
		switch (move.getMove()) {
			case DRAGONRAGE:
				damage = 40;
				break;
			case ENDEAVOR: 		
				if (trg.getHP() < atk.getHP()) damage = 0;			
				else damage = trg.getHP() - atk.getHP();	
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
	private double getPower(Move move, Pokemon atk, Pokemon trg) {
		/** FLAIL POWER FORMULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Flail_(move) **/
				
		double power = 1.0; 
		
		switch (move.getMove()) {
			case ERUPTION:
				power = (atk.getHP() * 150.0) / atk.getBHP();
				break;
			case FLAIL, REVERSAL: 			
				double remainHP = atk.getHP() / atk.getBHP();
				
				if (remainHP >= 0.672) power = 20;
				else if (0.672 > remainHP && remainHP >= 0.344) power = 40;
				else if (0.344 > remainHP && remainHP >= 0.203) power = 80;
				else if (0.203 > remainHP && remainHP >= 0.094) power = 100;
				else if (0.094 > remainHP && remainHP >= 0.031) power = 150;
				else if (0.031 > remainHP) power = 200;		
				
				break;
				
			case FLING:				
				power = atk.getItem().power;	
				atk.giveItem(null);
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
			case PUNISHMENT: 
				
				power = 60.0;
								
				if (trg.getAttackStg() > 0) power += 20.0 * trg.getAttackStg();
				if (trg.getDefenseStg() > 0) power += 20.0 * trg.getDefenseStg();
				if (trg.getSpAttackStg() > 0) power += 20.0 * trg.getSpAttackStg();
				if (trg.getSpDefenseStg() > 0) power += 20.0 * trg.getSpDefenseStg();
				if (trg.getAccuracyStg() > 0) power += 20.0 * trg.getAccuracyStg();
				if (trg.getEvasionStg() > 0) power += 20.0 * trg.getEvasionStg();
				if (trg.getSpeedStg() > 0) power += 20.0 * trg.getSpeedStg();
				
				if (power > 200.0) power = 200.0;				
				
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
	private double getAttack(Move move, Pokemon atk, Pokemon trg) {
		
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
	private double getDefense(Move move, Pokemon atk, Pokemon trg) {
		
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
				if (trg.checkType(Type.ROCK) && move.getMType().equals(MoveType.SPECIAL)) {
					defense *= 1.5;
				}
				break;
		}			
		
		return defense;
	}
	public double getEffectiveness(Pokemon pkm, Type type) {
		
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
	/** END DAMAGE MOVE METHODS **/
	
	/** POST MOVE METHODS **/
	private void absorbHP(Pokemon pkm, int damage) throws InterruptedException {
								
		int gainedHP = (damage / 2);
			
		if (pkm.getHP() != pkm.getBHP()) {
			
			if (gainedHP + pkm.getHP() > pkm.getBHP()) {								
				gainedHP = pkm.getBHP() - pkm.getHP();							
				increaseHP(pkm, gainedHP);					
			}
			else {		
				increaseHP(pkm, gainedHP);
			}
			
			typeDialogue(pkm.getName() + "\nabsorbed " + gainedHP + " HP!");			
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

		switch (move.getMove()) {
			case BRICKBREAK:
				if (trg.hasActiveMove(Moves.REFLECT)) {
					trg.removeActiveMove(Moves.REFLECT);
					typeDialogue(atk.getName() + " broke\nthe foe's shield!");
				}
				else if (trg.hasActiveMove(Moves.LIGHTSCREEN)) {
					trg.removeActiveMove(Moves.LIGHTSCREEN);
					typeDialogue(atk.getName() + " broke\nthe foe's shield!");
				}				
				break;
			case OUTRAGE, PETALDANCE, THRASH:
				if (!move.isWaiting()) {
					setStatus(trg, atk, Status.CONFUSE);				
				}
				return;
			case RAPIDSPIN:
				if (trg.hasActiveMove(Moves.LEECHSEED)) {									
					trg.removeActiveMove(Moves.LEECHSEED);
					typeDialogue(trg.getName() + " broke free\nof LEECH SEED!");	
				}
				return;
			case WAKEUPSLAP:
				if (trg.hasStatus(Status.SLEEP)) {
					removeStatus(trg);
				}
				return;
			default:
				break;
		}
		
		// move causes attribute or status effect
		double probability = move.getProbability();
		if (probability != 0.0) {			
			
			if (atk.getAbility() == Ability.SERENEGRACE) {
				probability *= 2.0;
			}
			
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
					setStatus(atk, trg, move.getEffect());						
				}
			}							
		}
		
		if (atk.getStatus() == null && trg.getAbility() == Ability.STATIC && 
				move.getMType() == MoveType.PHYSICAL && Math.random() < 0.30) {
			setStatus(trg, atk, Status.PARALYZE);						
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
	/** END POST MOVE METHODS **/
	
	/** ACTIVE MOVE METHODS **/
	private void checkActiveMoves(Pokemon trg, Pokemon atk) throws InterruptedException {
			
		Iterator<Move> iterator = trg.getActiveMoves().iterator();
		while (iterator.hasNext()) {
			
			Move move = iterator.next();
			
			switch (move.getMove()) {				
				case FUTURESIGHT:					
					move.setTurnCount(move.getTurnCount() - 1);					
					if (move.getTurnCount() <= 0) {		
						iterator.remove();		
						futureSight(trg, atk);
					}
					break;
				case LEECHSEED:
					leechSeed(trg, atk);
					break;
				case MIST, REFLECT, SAFEGUARD, WRAP:
					move.setTurnCount(move.getTurnCount() - 1);					
					if (move.getTurnCount() <= 0) {		
						iterator.remove();
						typeDialogue(move.getDelay(move.getName()));												
					}
					break;
				case PERISHSONG:
					move.setTurnCount(move.getTurnCount() - 1);					
					if (move.getTurnCount() <= 0) {		
						iterator.remove();
						trg.setHP(0);							
					}					
					break;
				case WISH:
					move.setTurnCount(move.getTurnCount() - 1);					
					if (move.getTurnCount() <= 0) {		
						iterator.remove();
						typeDialogue(trg.getName() + "'s wish\ncame true!");
						
						if (trg.getHP() == trg.getBHP()) {
							typeDialogue("It had no effect!");
						}
						else {							
							int gainedHP = trg.getBHP() - trg.getHP();
							int halfHP = (int) Math.floor(trg.getBHP() / 2.0);
							if (gainedHP > halfHP) gainedHP = halfHP;
							
							increaseHP(trg, gainedHP);				
							typeDialogue(trg.getName() + "\nregained health!");	
						}										
					}		
				default: 
					break;
			}
		}
		
		if (trg.getStatus() != null && trg.getAbility() == Ability.SHEDSKIN &&
				Math.random() > 0.66) {
			removeStatus(trg);			
		}
		
		if (trg.getAbility() == Ability.SPEEDBOOST) {
			setAttribute(trg, Arrays.asList("speed"), 1);
		}
	}	
	private void futureSight(Pokemon trg, Pokemon atk) throws InterruptedException {
		
		Move move = new Move(Moves.FUTURESIGHT);
		
		int damage = dealDamage(atk, trg, move);
			
		String hitSE = getHitSE(getEffectiveness(trg, move.getType()));	
		
		if (hitSE.equals("hit-super")) typeDialogue("It's super effective!");			
		else if (hitSE.equals("hit-weak")) typeDialogue("It's not very effective...");			
			
		typeDialogue(trg.getName() + " took\n" + damage + " by Future Sight!");		
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
	/** END ACTIVE MOVE METHODS **/
		
	/** STATUS DAMAGE METHODS **/
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
	/** END STATUS MOVE METHODS **/
	
	/** WEATHER DAMAGE METHODS **/
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
	/** END WEATHER DAMAGE METHODS **/
	
	/** DELAYED TURN **/	
	private void getDelayedTurn() {
		
		// RESET NON-DELAYED MOVES
		int delay = getDelay();	
				
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
			gp.ui.player = 0;
			gp.ui.battleState = gp.ui.battle_Options;
		}
	}

	/** CHECK WINNER METHODS **/
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
			
			gainEXP();
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
	
	/** EXP METHODS **/
	private void gainEXP() throws InterruptedException {
		
		int gainedXP = calculateEXPGain();
				
		if (otherFighters.size() > 0) {
			
			gainedXP = (int) Math.ceil(gainedXP / (otherFighters.size() + 1));			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2800.0 / (double) gainedXP);
			
			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			increaseEXP(fighter[0], xp, expTimer);	
			
			for (Pokemon p : otherFighters) {
								
				typeDialogue(p.getName() + " gained\n" + gainedXP + " Exp. Points!");
				
				xp = p.getXP() + gainedXP;				
				increaseEXP(p, xp, 0);
			}
			
			otherFighters.clear();
		}
		else {
			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2500.0 / (double) gainedXP);

			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			increaseEXP(fighter[0], xp, expTimer);	
		}
		
		getOtherFighters();	
		pause(500);
	}
	private int calculateEXPGain() {
		// EXP FORMULA REFERENCE (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		
		int exp = 0;
		
		double b = fighter[loser].getXPYield();
		double L = fighter[loser].getLevel();
		double s = 1.0;
		double e = 1.0;
		double a = battleMode == trainerBattle ? 1.5 : 1.0;
		double t = 1.0;
		
		exp = (int) (Math.floor( (b * L) / 7 ) * Math.floor(1 / s) * e * a * t);
		
		return exp;
	}
	private void increaseEXP(Pokemon p, int xp, int timer) throws InterruptedException {
		
		while (p.getXP() < xp) {
			
			p.setXP(p.getXP() + 1);
			
			pause(timer);
												
			// FIGHTER LEVELED UP
			if (p.getXP() >= p.getBXP() + p.getNXP()) {			
				
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
//				fightStage = fight_Swap;	
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
//				fightStage = fight_Swap;	
				fightStage = fight_Start;				
				gp.ui.battleState = gp.ui.battle_Dialogue;	
			}
			
			typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
			
			newMove = null;
			gp.ui.commandNum = 0;	
		}
	}
	/** END GAIN EXP METHODS **/

	/** GET WINNING TRAINER **/
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
//					fightStage = fight_Swap;
					fightStage = fight_Start;
					
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

					newFighter[1] = getCPUFighter();	
					
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
//					fightStage = fight_Swap;
					fightStage = fight_Start;
					
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
					
					newFighter[1] = getCPUFighter();					
					
					winner = -1;							
					running = false;
//					fightStage = fight_Swap;
					fightStage = fight_Start;
					
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
	
	/** GET NEXT CPU FIGHTER METHODS **/
	private Pokemon getCPUFighter() {
		
		Pokemon nextFighter = null;
		
		if (trainer == null || trainer.skillLevel == trainer.skill_rookie) {
			nextFighter = getCPUFighter_Next();
		}
		else if (trainer.skillLevel == trainer.skill_smart) {
			nextFighter = getCPUFighter_Power();
		}
		else if (trainer.skillLevel == trainer.skill_elite) {
			nextFighter = getCPUFighter_Best();
		}
		
		return nextFighter;		
	}	
 	private Pokemon getCPUFighter_Next() {
		
 		Pokemon nextFighter = null;
 		
 		if (fighter == null) { 
 			nextFighter = trainer.pokeParty.get(0); 
 		}
 		else {
 			int index = trainer.pokeParty.indexOf(fighter[1]);
 			if (index < 0 || index + 1 == trainer.pokeParty.size()) {
 				nextFighter = null;
 			}
 			else {
 				nextFighter = trainer.pokeParty.get(index + 1);
 			}	
 		}	
 		
 		return nextFighter;
	}
 	private Pokemon getCPUFighter_Power() {
 		
 		Pokemon bestFighter = null;
 		
 		Map<Pokemon, Integer> fighters = new HashMap<>();
 		Map<Move, Integer> powerMoves = new HashMap<>();
 		
 		for (Pokemon p : trainer.pokeParty) {
 			
 			if (p.isAlive()) {
 				
 				for (Move m : p.getMoveSet()) {
 					
 					if (m.getPower() > 0 && m.getPP() != 0) {
 						
 						double power = getPower(m, p, fighter[0]);				
 						double type = getEffectiveness(fighter[0], m.getType());				
 						
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
 	private Pokemon getCPUFighter_Best() {
		
		Pokemon bestFighter = null;
 		
 		Map<Pokemon, Integer> fighters = new HashMap<>();
 		Map<Move, Integer> damageMoves = new HashMap<>();
 		
 		for (Pokemon p : trainer.pokeParty) {
 			
 			if (p.isAlive()) {
 				
 				for (Move m : p.getMoveSet()) {
 					
 					if (m.getPower() > 0 && m.getPP() != 0) {
 						
 						int damage = calculateDamage(p, fighter[0], m);				
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
 	/** END GET NEXT CPU FIGHTER METHODS **/
 	
	private void getSwapAnswer() {
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			gp.ui.resetFighterPositions();
			
//			fightStage = fight_Swap;
			fightStage = fight_Start;
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
	
	/** ANNOUNCE WINNER **/
	private void announceWinner() throws InterruptedException {
		
		// TRAINER 1 VICTORY
		if (winner == 0) {
			
			gp.ui.fighter_two_X = gp.screenWidth + gp.tileSize;
			
			gp.ui.battleState = gp.ui.battle_End;
			
			gp.stopMusic();
			gp.startMusic(1, 5);					
			
			typeDialogue("Player defeated\nTrainer " + trainer.name + "!", true);
			
			trainer.battleIconTimer = 0;
			trainer.speed = trainer.defaultSpeed;
			trainer.isDefeated = true;
			trainer.hasBattle = false;
						
			int dialogueSet = trainer.dialogueSet + 1;
			typeDialogue(trainer.dialogues[dialogueSet][0], true);
			
			int moneyEarned = calculateMoneyEarned();
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
	
	/** CALCULATE MONEY **/
	private int calculateMoneyEarned() {
		/** MONEY EARNED FORMULA REFERENCE (ALL GEN): https://bulbapedia.bulbagarden.net/wiki/Prize_money **/
				
		int payout = 0;
		
		int level = trainer.pokeParty.get(trainer.pokeParty.size() - 1).getLevel();
		int base = trainer.trainerClass;		
		payout = base * level;
		
		return payout;
	}
	
	/** CAPTURE METHODS **/
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
			
			if (isCaptured()) {				
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
	private void capturePokemon() throws InterruptedException {
		
		gp.stopMusic();
		gp.startMusic(1, 3);										
		typeDialogue("Gotcha!\n" + fighter[1].getName() + " was caught!", true);
						
		if (!gp.player.ownsPokemon(fighter[1].getIndex())) {
			gp.player.dexOwn++;
		}
		
		if (gp.player.pokeParty.size() < 6) {
			fighter[1].resetMoves();
			fighter[1].setBall(ballUsed);
			gp.player.pokeParty.add(fighter[1]);
			typeDialogue(fighter[1].getName() + " was added\nto your party!", true);
		}
		else {			
			boolean found = false;
			for (int i = 0; i < 10; i++) {
				for (int c = 0; c < 30; c++) {
					if (gp.player.pcParty[i][c] == null) {
						gp.player.pcParty[i][c] = fighter[1];
						found = true;
						break;
					}
				}
			}
			
			if (found) {
				typeDialogue(fighter[1].getName() + " was sent\nto your PC!", true);
			}
			else {
				typeDialogue("There is no more room in your PC!\n" + fighter[1].getName() + " was released!", true);
			}
		}
		
		endBattle();
	}
	/** END CAPTURE METHODS **/
	
	/** ESCAPE BATTLE **/
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
	
	/** EVOLVE METHODS **/
	private void checkEvolve() throws InterruptedException {
		
		gp.stopMusic();
		
		for (int i = 0; i < gp.player.pokeParty.size(); i++) {					
			if (gp.player.pokeParty.get(i).canEvolve()) {
				
				Pokemon oldEvolve = gp.player.pokeParty.get(i);
				Pokemon newEvolve = oldEvolve.evolve();
				
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
		
		gp.player.trackSeenPokemon(newEvolve);
		gp.player.trackOwnPokemon(newEvolve.getIndex());
				
		checkNewMove(newEvolve);
		
		return;
	}
	/** END EVOLVE METHODS **/
		
	/** END BATTLE METHODS **/
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
		
		gp.player.canMove = true;
 	}
 	/** END END BATTLE METHODS **/
	
 	/** MISC METHODS **/
 	public void typeDialogue(String dialogue) throws InterruptedException {
		
		gp.ui.battleDialogue = "";
		
		for (char letter : dialogue.toCharArray()) {
			gp.ui.battleDialogue += letter;
			pause(textSpeed);
		}		
		
		pause(1000);
		
		gp.ui.battleDialogue = "";
	}
	public void typeDialogue(String dialogue, boolean canSkip) throws InterruptedException {
		
		gp.ui.battleDialogue = "";
		
		for (char letter : dialogue.toCharArray()) {
			gp.ui.battleDialogue += letter;
			pause(textSpeed);
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
	/** END MISC METHODS **/
}