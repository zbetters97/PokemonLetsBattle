package properties;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/*** TYPE CLASS ***/
public enum Type {
	
	NORMAL ("Normal", 0.5), 
	FIRE ("Fire", 0.5), 
	WATER ("Water", 0.5), 
	ELECTRIC ("Electr", 0.5), 
	GRASS ("Grass", 0.5), 
	ICE ("Ice", 0.5), 
	FIGHTING ("Fight", 0.5),
	POISON ("Poison", 0.5), 
	GROUND ("Ground", 0.5),
	FLYING ("Flying", 0.5),
	PSYCHIC ("Psychic", 0.5), 
	BUG ("Bug", 0.5),
	ROCK ("Rock", 0.5),
	GHOST ("Ghost", 0.5),
	DRAGON ("Dragon", 0.5),
	DARK ("Dark", 0.5),
	STEEL ("Steel", 0.5),
	FAIRY ("Fairy", 0.5);
			
 	private Hashtable<Type, Double> resistance, vulnerability;
    
    private String name;
    private Double strength;
    
    private static List<Type> TYPES = Arrays.asList(Type.values());

    /** CONSTRUCTOR **/
    Type(String name, Double strength) {
    	
        this.name = name;
        this.strength = strength;
        
        resistance = new Hashtable<Type, Double>();
        vulnerability = new Hashtable<Type, Double>();
    }
    /** END CONSTRUCTOR **/

	static {		
		/*** TYPE CHART REFERENCE: https://pokemondb.net/type ***/
		
		NORMAL.resistantTo(new ArrayList<Type>(Arrays.asList(
				ROCK, STEEL)), 0.5);		
		NORMAL.resistantTo(GHOST, 0.0);		
		NORMAL.vulnerableTo(FIGHTING, 1.5);
		
		FIRE.resistantTo(new ArrayList<Type>(Arrays.asList(
				FIRE, GRASS, ICE, 
				BUG, STEEL, FAIRY)), 0.5); 	
		FIRE.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				WATER, GROUND, ROCK)), 1.5);
		
		WATER.resistantTo(new ArrayList<Type>(Arrays.asList(
				FIRE, WATER, ICE, 
				STEEL)), 0.5);
		WATER.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				ELECTRIC, GRASS)), 1.5);
		
		ELECTRIC.resistantTo(new ArrayList<Type>(Arrays.asList(
				ELECTRIC, FLYING, STEEL)), 0.5);
		ELECTRIC.vulnerableTo(GROUND, 1.5);
		
		GRASS.resistantTo(new ArrayList<Type>(Arrays.asList(
				WATER, ELECTRIC, GRASS, 
				GROUND)), 0.5);
		GRASS.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				FIRE, ICE, POISON, 
				FLYING, BUG)), 1.5);
		
		ICE.resistantTo(ICE, 0.5);		
		ICE.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				FIRE, FIGHTING, ROCK, 
				STEEL)), 1.5);
		
		FIGHTING.resistantTo(new ArrayList<Type>(Arrays.asList(
				BUG, ROCK, DARK)), 0.5);
		FIGHTING.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				FLYING, PSYCHIC, FAIRY)), 1.5);
		
		POISON.resistantTo(new ArrayList<Type>(Arrays.asList(
				GRASS, FIGHTING, POISON, 
				BUG, FAIRY)), 0.5);
		POISON.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				GROUND, PSYCHIC)), 1.5);
		
		GROUND.resistantTo(new ArrayList<Type>(Arrays.asList(
				POISON, ROCK)), 0.5);
		GROUND.resistantTo(ELECTRIC, 0.0);
		GROUND.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				WATER, GRASS, ICE)), 1.5);
		
		FLYING.resistantTo(new ArrayList<Type>(Arrays.asList(
				GRASS, FIGHTING, BUG)), 0.5);
		FLYING.resistantTo(GROUND, 0.0);
		FLYING.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				ELECTRIC, ICE, ROCK)), 1.5);
		
		PSYCHIC.resistantTo(new ArrayList<Type>(Arrays.asList(
				FIGHTING, PSYCHIC)), 0.5);
		PSYCHIC.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				BUG, GHOST, DARK)), 1.5);
		
		BUG.resistantTo(new ArrayList<Type>(Arrays.asList(
				GRASS, FIGHTING, GROUND)), 0.5);
		BUG.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				FIRE, FLYING, ROCK)), 1.5);
		
		ROCK.resistantTo(new ArrayList<Type>(Arrays.asList(
				NORMAL, FIRE, POISON, 
				FLYING)), 0.5);		
		ROCK.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				WATER, GRASS, FIGHTING, 
				GROUND, STEEL)), 1.5);
		
		GHOST.resistantTo(new ArrayList<Type>(Arrays.asList(
				POISON, BUG)), 0.5);	
		GHOST.resistantTo(new ArrayList<Type>(Arrays.asList(
				NORMAL, FIGHTING)), 0.0);		
		GHOST.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				GHOST, DARK)), 1.5);
		
		DRAGON.resistantTo(new ArrayList<Type>(Arrays.asList(
				FIRE, WATER, ELECTRIC, 
				GRASS)), 0.5);		
		DRAGON.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				ICE, DRAGON, FAIRY)), 1.5);
		
		DARK.resistantTo(new ArrayList<Type>(Arrays.asList(
				GHOST, DARK)), 0.5);		
		DARK.resistantTo(PSYCHIC, 0.0);
		DARK.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				FIGHTING, BUG, FAIRY)), 1.5);
		
		STEEL.resistantTo(new ArrayList<Type>(Arrays.asList(
				NORMAL, GRASS, ICE, 
				FLYING, PSYCHIC, BUG,
				ROCK, DRAGON, STEEL, 
				FAIRY)), 0.5);	
		STEEL.resistantTo(POISON, 0.0);
		STEEL.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				FIRE, FIGHTING, GROUND)), 1.5);
		
		FAIRY.resistantTo(new ArrayList<Type>(Arrays.asList(
				FIGHTING, BUG, DARK)), 0.5);		
		FAIRY.resistantTo(DRAGON, 0.0);
		FAIRY.vulnerableTo(new ArrayList<Type>(Arrays.asList(
				POISON, STEEL)), 1.5);
	}
    
	public void resistantTo(Type type, Double strength) {
        resistance.put(type, strength);
    }
    public void resistantTo(ArrayList<Type> typeList, Double strength) {    	
    	for (Type type : typeList) {
    		 resistance.put(type, strength);
    	}       
    }
    
    public void vulnerableTo(Type type, Double strength) {
    	vulnerability.put(type, strength);
    }
    public void vulnerableTo(ArrayList<Type> typeList, Double strength) {    	
    	for (Type type : typeList) {
    		vulnerability.put(type, strength);
    	}       
    }    

    public Color getColor() {
    	
    	Color color = Color.BLACK;
    	
    	switch (this.name) {
	    	case ("Normal"): color = new Color(168,167,119); break;    	
	    	case ("Fire"): color = new Color(240,127,47); break;
	    	case ("Water"): color = new Color(101,142,244); break;
	    	case ("Electr"): color = new Color(245,209,43); break;
	    	case ("Grass"): color = new Color(119,198,82); break;
	    	case ("Ice"): color = new Color(152,216,216); break;
	    	case ("Fight"): color = new Color(192,46,36); break;
	    	case ("Poison"): color = new Color(159,64,162); break;
	    	case ("Ground"): color = new Color(224,191,96); break;
	    	case ("Flying"): color = new Color(167,144,239); break;
	    	case ("Psychic"): color = new Color(250,85,135); break;
	    	case ("Bug"): color = new Color(168,184,33); break;
	    	case ("Rock"): color = new Color(184,159,56); break;
	    	case ("Ghost"): color = new Color(113,87,152); break;
	    	case ("Dragon"): color = new Color(112,55,253); break;
	    	case ("Dark"): color = new Color(111,88,72); break;
	    	case ("Steel"): color = new Color(184,184,208); break;
	    	case ("Fairy"): color = new Color(217,163,215); break;
	    }
    	
    	return color;
    }
    
    public String printType() {    	 	
    	return name;
    }
    
    public List<Type> getPTypes() { 
    	return TYPES; 
    }
    
    public Hashtable<Type, Double> getResistance() { return this.resistance; }
    public Hashtable<Type, Double> getVulnerability() { return this.vulnerability; }  
 
    public String getName() { return this.name.toUpperCase(); }
    public Double getStrength() { return this.strength; }
}
/*** END TYPE CLASS ***/