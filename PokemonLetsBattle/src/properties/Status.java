package properties;

import java.awt.Color;

import application.GamePanel;

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
	    	case ("PAR"): color = new Color(253,174,16); break;
	    	case ("PSN"): color = new Color(188,82,231); break;
	    	case ("CNF"): color = new Color(226,196,116); break;
	    	case ("BRN"): color = new Color(249,78,0); break;
	    	case ("FRZ"): color = new Color(98,204,212); break;
	    	case ("SLP"): color = new Color(125,125,125); break;
		}		
		return color; 
	}
	/** END GETTERS **/
		
	public void printStatus(GamePanel gp, String fighter) {
	
		switch (this.abr) {  	
			case ("PAR"): 				
				gp.ui.addBattleDialogue(fighter + " is paralyzed\nand unable to move!");
				gp.playSE(6, condition);	
				break;
	    	case ("PSN"): 
	    		gp.ui.addBattleDialogue(fighter + " is hurt\nfrom the poison!"); 
	    		gp.playSE(6, condition);	
	    		break;
	    	case ("CNF"): 
	    		gp.ui.addBattleDialogue(fighter + " hurt\nitself in confusion!"); 
	    		gp.playSE(6, "hit-normal");	
	    		break;
	    	case ("BRN"): 
	    		gp.ui.addBattleDialogue(fighter + " is hurt\nfrom the burn!"); 
	    		gp.playSE(6, condition);	
	    		break;
	    	case ("FRZ"): 
	    		gp.ui.addBattleDialogue(fighter + " is frozen\nsolid!"); 
	    		gp.playSE(6, condition);	
	    		break;
	    	case ("SLP"): 
	    		gp.ui.addBattleDialogue(fighter + " is fast\nasleep!"); 
	    		gp.playSE(6, condition);	
	    		break;
		}				
	}
	
	public String printRecover() {
		String recover = "";
		
		switch (this.abr) {  	
			case ("PAR"): recover = " healed\nfrom paralysis!"; break;
			case ("PSN"): recover = " healed\nfrom the poison!"; break;
	    	case ("CNF"): recover = " snapped\nout of confusion!"; break;
	    	case ("BRN"): recover = " healed\nfrom the burn!"; break;
	    	case ("FRZ"): recover = " thawed\nfrom the ice!"; break;
	    	case ("SLP"): recover = " woke up!"; break;
		}		
		return recover; 
	}
}
/*** END STATUS CLASS ***/