package properties.abilities;

import moves.Move;
import pokemon.Pokemon;
import properties.Status;

/*** NATURE CLASS ***/
public class Ability {		
	
	public enum Category {
		ATTACK, ATTRIBUTE, CRITICAL, DEFENSE, FLINCH, PP, RECOIL, STATUS, WEATHER;
	}
		
    protected String name, attribute, description;
    protected Category category;
    protected double factor;
    protected Status effect;   
    
    /** CONSTRUCTOR **/
    public Ability(String name, Category category, String description) {
    	this.name = name;  
    	this.category = category;
    	this.description = description;    	
    }    
    public Ability(String name, Category category, double factor, String description) {
    	this.name = name;  
    	this.category = category;
    	this.factor = factor;
    	this.description = description;    	
    }    
    public Ability(String name, Category category, String attribute, String description) {
    	this.name = name;  
    	this.category = category;
    	this.attribute = attribute;
    	this.description = description;    	
    } 
    public Ability(String name, Category category, String attribute, double factor, String description) {
    	this.name = name;  
    	this.category = category;
    	this.attribute = attribute;
    	this.factor = factor;
    	this.description = description;    	
    } 
    public Ability(String name, Category category, Status effect, String description) {
    	this.name = name;  
    	this.category = category;
    	this.effect = effect;
    	this.description = description;    	
    }    
    /** END CONSTRUCTOR **/
    
    public boolean isValid(Pokemon pokemon) { return false; }
    public boolean isValid(Pokemon attacker, Pokemon target, Move move) { return false; }
	
	/** GETTERS **/	
	public String getName() { return this.name; }
	public Category getCategory() { return this.category; }
	public double getFactor() { return this.factor; }
	public String getAttribute() { return this.attribute; }
	public Status getEffect() { return this.effect; }
	public String getDescription() { return this.description; }
	/** END GETTERS **/
}
/*** END NATURE CLASS ***/