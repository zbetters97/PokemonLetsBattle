package moves;

import java.util.List;
import java.util.Random;

import properties.Status;
import properties.Type;

/*** MOVE CLASS ***/
public class Move {

	public enum MoveType {
		STATUS, ATTRIBUTE, PHYSICAL, SPECIAL, WEATHER, OTHER;
	}
	
	/** INITIALIZE VALUES FOR UNIQUE MOVES **/
	private Moves move;
	private int pp;
	private int bpp;
	private int turnCount;
	/** END INITIALIZE VALUES **/
	
	/** CONSTRUCTORS **/
	public Move (Moves move) {		
		this.move = move;
		this.pp = move.getpp();
		this.bpp = pp;			
		this.turnCount = getTurns();		
	}	
	public Move (Moves move, int pp) {
		this.move = move;
		this.bpp = move.getpp();	
		this.pp = pp;				
		this.turnCount = move.getTurns();	
	}
	/** END CONSTRUCTORS **/
	
	/** GETTERS AND SETTERS **/	
	public String toString() {
		return move.getName().toUpperCase();
	}
	public Moves getMove() { return move; }
	public String getName() { return move.getName(); }
	
	public int getPP() { return pp; }
	public void setPP(int pp) {	
		this.pp = pp; 
		if (this.pp < 0) {
			this.pp = 0; 
		}
	}
	
	public int getBPP() { return bpp; }
	public void setBPP(int bpp) { 
		this.bpp = bpp;  
		if (this.bpp < 0) {
			this.bpp = 0; 
		}
	}		
	public void resetPP() { pp = bpp;}
	
	public int getTurnCount() {	return turnCount; }
	public void setTurnCount(int turnCount) { this.turnCount = turnCount; }
	
	public int getTurns() {	
		
		if (move == Moves.OUTRAGE || move == Moves.PETALDANCE) {
			return new Random().nextInt(3 - 2 + 1) + 2;		
		}
		else if (move == Moves.ROLLOUT || move == Moves.ROCKBLAST) {
			return new Random().nextInt(5 - 2 + 1) + 2;
		}
		else {		
			return move.getTurns(); 
		}
	}	
	
	public boolean isReady() { 
		
		if (move == Moves.ROLLOUT || 
				move == Moves.PETALDANCE || 
				move == Moves.OUTRAGE || 
				move == Moves.REFLECT || 
				move == Moves.ROCKBLAST) {
			return true;
		}
		else if (move.getRecharge()) {
			if (turnCount == move.getTurns()) return true;				
			else return false;			
			
		}
		else {
			return turnCount <= 0;
		}
	}
	
	public void resetMoveTurns() {	
		
		if (move == Moves.ROLLOUT || 
				move == Moves.PETALDANCE || 
				move == Moves.OUTRAGE || 
				move == Moves.ROCKBLAST) {
			if (turnCount > 0) turnCount--;			
			else turnCount = getTurns();			
		}
		else if (move.getRecharge()) {
			turnCount--;		
		}
		else {
			turnCount = getTurns();		
		}
	}
	
	public boolean isWaiting() {	
		if (getMType() == MoveType.OTHER) {
			return false;
		}
		else {
			return turnCount < getTurns();			
		}		
	}
	/** END GETTERS AND SETTERS **/
	
	/** GETTERS **/
	public MoveType getMType() { return move.getMType(); }	
	public Type getType() { return move.getType(); }
	public Status getEffect() { return move.getEffect(); }	
	public double getSelfInflict() { return move.getSelfInflict(); }	
	public double getProbability() { return move.getProbability(); }	
	public double getFlinch() { return move.getFlinch(); }	
	public boolean isToSelf() { return move.isToSelf(); }	
	public int getAccuracy() { 
		if (move.getAccuracy() == -1) return 100;
		else return move.getAccuracy(); 
	}
	public int getPower() {	return move.getPower(); }	
	public boolean getGoFirst() { return move.getGoFirst(); }	
	public boolean getProtected() { return move.getProtected(); }	
	public boolean getRecharge() { return move.getRecharge(); }
	
	public String getWeather() { return move.getWeather(); }
	public String getDelay(String name) { return move.getDelay(name); }	
	public String getInfo() {	return move.getInfo(); }	
	public int getCrit() { return move.getCrit(); }	
	public int getLevel() { return move.getLevel(); }	
	public List<String> getStats() { return move.getStats(); }
	/** END GETTERS **/
}
/*** END MOVE CLASS ***/