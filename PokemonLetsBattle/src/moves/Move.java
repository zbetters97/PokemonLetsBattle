package moves;

import java.util.List;

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
		this.turnCount = move.getTurns();	
	}	
	/** END CONSTRUCTORS **/
	
	/** GETTERS AND SETTERS **/	
	public String toString() {
		return move.getName().toUpperCase();
	}
	public Moves getMove() { return move; }
	public String getName() { return move.getName(); }
	
	public int getpp() { return pp; }
	public void setpp(int pp) {	
		this.pp = pp; 
		if (this.pp < 0) {
			this.pp = 0; 
		}
	}
	
	public int getbpp() { return bpp; }
	public void setbpp(int bpp) { 
		this.bpp = bpp;  
		if (this.bpp < 0) {
			this.bpp = 0; 
		}
	}		
	public void resetpp() { pp = bpp;}
	
	public int getTurnCount() {	return turnCount; }
	public void setTurnCount(int turnCount) { this.turnCount = turnCount; }
	
	public int getTurns() {	return move.getTurns(); }	
	
	public boolean isReady() { 		
		return turnCount <= 0;	
	}
	public boolean isWaiting() {
		return turnCount < getTurns();
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
	
	public String getWeather() { return move.getWeather(); }
	public String getDelay(String name) { return move.getDelay(name); }	
	public String getInfo() {	return move.getInfo(); }	
	public int getCrit() { return move.getCrit(); }	
	public int getLevel() { return move.getLevel(); }	
	public List<String> getStats() { return move.getStats(); }
	/** END GETTERS **/
}
/*** END MOVE CLASS ***/