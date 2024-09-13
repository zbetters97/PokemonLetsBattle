package moves;

import java.util.List;

import properties.Status;
import properties.Type;

/*** MOVE CLASS ***/
public class Move {

	public enum MoveType {
		STATUS, ATTRIBUTE, PHYSICAL, SPECIAL, OTHER;
	}
	
	/** INITIALIZE VALUES FOR UNIQUE MOVES **/
	private Moves move;
	private int pp;
	private int bpp;
	private int numTurns;
	private int turns;
	/** END INITIALIZE VALUES **/
	
	/** CONSTRUCTORS **/
	public Move (Moves move) {		
		this.move = move;
		this.pp = move.getpp();
		this.bpp = pp;
	}	
	public Move (Moves move, int numTurns) {
		this.move = move;
		this.pp = move.getpp();
		this.bpp = pp;
		this.numTurns = numTurns;
		this.turns = numTurns;
	}
	/** END CONSTRUCTORS **/
	
	/** GETTERS AND SETTERS **/	
	public String toString() {
		return move.getName().toUpperCase();
	}
	public Moves getMove() { return move; }
	public String getName() { return move.getName(); }
	
	public int getpp() { return pp; }
	public void setpp(int pp) {	this.pp = pp; }
	
	public int getbpp() { return bpp; }
	public void setbpp(int bpp) { this.bpp = bpp; }	
	
	public int getTurns() {	return turns; }
	public void setTurns(int turns) { this.turns = turns; }
	
	public int getNumTurns() {	return numTurns; }
	public void setNumTurns(int numTurns) {	this.numTurns = numTurns; }	
	/** END GETTERS AND SETTERS **/
	
	/** GETTERS **/
	public MoveType getMType() { return move.getMType(); }	
	public Type getType() { return move.getType(); }
	public Status getEffect() { return move.getEffect(); }	
	public Double getSelfInflict() { return move.getSelfInflict(); }	
	public Double getProbability() { return move.getProbability(); }	
	public boolean isToSelf() { return move.isToSelf(); }	
	public int getAccuracy() { 
		if (move.getAccuracy() == -1) return 100;
		else return move.getAccuracy(); 
	}
	public int getPower() {	return move.getPower(); }	
	public boolean getGoFirst() { return move.getGoFirst(); }	
	public boolean getCanHit() { return move.getCanHit(); }	
	public String getDelay(String name) { return move.getDelay(name); }	
	public String getInfo() {	return move.getInfo(); }	
	public int getCrit() { return move.getCrit(); }	
	public int getLevel() { return move.getLevel(); }	
	public List<String> getStats() { return move.getStats(); }
	/** END GETTERS **/
}
/*** END MOVE CLASS ***/