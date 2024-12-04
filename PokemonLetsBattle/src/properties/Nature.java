package properties;

import java.util.Arrays;
import java.util.List;

/*** NATURE CLASS ***/
public enum Nature {
	
	/*** NATURE EFFECTS REFERENCE: https://pokemondb.net/mechanics/natures ***/	
	/** ATTACK: 1, DEFENSE: 2, SP ATTACK: 3, SP DEFENSE: 4, SPEED: 5 **/
	
	ADAMANT ("Adamant", 1, 3),
	BASHFUL ("Bashful", 3, 3),
	BOLD ("Bold", 2, 2),
	BRAVE ("Brave", 1, 5),
	CALM ("Calm", 4, 1),
	CAREFUL ("Careful", 4, 3),
	DOCILE ("Docile", 2, 2),
	GENTLE ("Gentile", 4, 2),
	HARDY ("Hardy", 1, 1),
	HASTY ("Hasty", 5, 2),
	IMPISH ("Impish", 2, 3),
	JOLLY ("Jolly", 5, 3),
	LAX ("Lax", 2, 4),
	LONELY ("Lonely", 1, 2),
	MILD ("Mild", 3, 2),
	MODEST ("Modest", 3, 1),
	NAIVE ("Naive", 5, 4),
	NAUGHTY ("Naughty", 1, 4),
	QUIET ("Quiet", 3, 5),
	QUIRKY ("Quirky", 4, 4),
	RASH ("Rash", 3, 4),
	RELAXED ("Relaxed", 2, 5),
	SASSY ("Sassy", 4, 5),
	SERIOUS ("Serious", 5, 5),
	TIMID ("Timid", 5, 1);
		
    private String name; 
    private int increase, decrease;
    
    private static List<Nature> NATURES = Arrays.asList(Nature.values());
    
    /** CONSTRUCTOR **/
    Nature(String name, int increase, int decrease) {
    	this.increase = increase;  
    	this.decrease = decrease;
    	this.name = name;    	
    }
    /** END CONSTRUCTOR **/
	
	/** GETTERS **/	
	public String getName() { return name; }
	public int increase() { return increase; }
	public int decrease() { return decrease; }	
	public static List<Nature> getNatures() { return NATURES; }
	/** END GETTERS **/
}
/*** END NATURE CLASS ***/