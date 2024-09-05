package properties;

import java.awt.Color;

/*** STATUS CLASS ***/
public enum Status {
	/*** STATUS CHART REFERENCE: https://pokemon.fandom.com/wiki/Status_Effects ***/
	
	PARALYZE ("Paralyze", "paralyzed", "PAR"),
	POISON ("Poison", "poisoned", "PSN"),
	CONFUSE ("Confuse", "confused", "CNF"),
	BURN ("Burn", "burned", "BRN"),
	FREEZE ("Freeze", "frozen", "FRZ"),
	SLEEP ("Sleep", "asleep", "SLP");

    private String name, condition, abr;
    
    /** CONSTRUCTOR **/
    Status(String name, String condition, String abr) {    
    	this.name = name;
    	this.condition = condition;        
    	this.abr = abr;    	
    }
    /** END CONSTRUCTOR **/
	
	/** GETTERS **/
    public String getName() { return this.name; }
    public String getCondition() { return this.condition; }
	public String getAbreviation() { return this.abr; }
	
	public Color getColor() {
		
		Color color = Color.BLACK;
		
		switch (this.abr) {  	
	    	case ("PAR"): color = Color.YELLOW; break;
	    	case ("PSN"): color = Color.PINK; break;
	    	case ("CNF"): color = Color.CYAN; break;
	    	case ("BRN"): color = Color.RED; break;
	    	case ("FRZ"): color = Color.BLUE; break;
	    	case ("SLP"): color = Color.GRAY; break;
		}		
		return color; 
	}
	/** END GETTERS **/
	
	public String printStatus() {
		String status = "";
		
		switch (this.abr) {  	
			case ("PAR"): status = " is paralyzed and unable to move!"; break;
	    	case ("PSN"): status = " is hurt from the poison!"; break;
	    	case ("CNF"): status = " hurt itself in confusion!"; break;
	    	case ("BRN"): status = " is hurt from the burn!"; break;
	    	case ("FRZ"): status = " is frozen solid!"; break;
	    	case ("SLP"): status = " is fast asleep!"; break;
		}			
		return status; 
	}
	
	public String printRecover() {
		String recover = "";
		
		switch (this.abr) {  	
			case ("PAR"): recover = " healed from paralysis!"; break;
			case ("PSN"): recover = " healed from the poison!"; break;
	    	case ("CNF"): recover = " snapped out of confusion!"; break;
	    	case ("BRN"): recover = " healed from the burn!"; break;
	    	case ("FRZ"): recover = " thawed from the ice!"; break;
	    	case ("SLP"): recover = " woke up!"; break;
		}		
		return recover; 
	}
}
/*** END STATUS CLASS ***/