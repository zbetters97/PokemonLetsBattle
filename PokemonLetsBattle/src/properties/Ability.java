package properties;

import application.GamePanel.Weather;

/*** TYPE CLASS ***/
public enum Ability {
	AIRLOCK ("Airlock", Weather.CLEAR, "All weather effects are\nnegated while the Pokémon\nis on the field."),
	BLAZE ("Blaze", "When HP is below 1/3rd,\nFire’s power increases to\n1.5 times"),
	DRIZZLE ("Drizzle", Weather.RAIN, "Weather changes to Heavy\nRain when the Pokémon enters\nthe battle."),
	DROUGHT ("Drought", Weather.SUNLIGHT, "Weather changes to Clear\nSkies when the Pokémon enters\nthe battle."),
	FLASHFIRE ("Flash Fire", false, "Activates when user is hit\nby a damaging Fire-type\nmove. Once activated, user’s\nFire-type moves deal 1.5\ntimes damage."),
	GUTS ("Guts", false, "Attack raises to 1.5 times\nwhen induced with a status."),
	INNERFOCUS ("Inner Focus", "This Pokémon will not\nflinch."), 
	INTIMIDATE ("Intimidate", "Upon entering battle, the\nopponent’s Attack lowers one\nstage."),
	LEVITATE ("Levitate", "Damage dealing Ground-type\nmoves have no effect."),
	OVERGROW ("Overgrow", "When HP is below 1/3rd,\nGrass's power increases\nto 1.5 times."),
	PRESSURE ("Pressure", "When this Pokémon is hit\nby a move, the opponent’s\nPP lowers by 2 rather than\n1."),
	QUICKFEET ("Quick Feet", false, "Speed raises to 1.5 times\nwhen induced with a status."),
	ROCKHEAD ("Rock Head", "Does not receive recoil\ndamage from recoil-causing\nmoves."),
	SHELLARMOR ("Shell Armor", "Opponent’s moves cannot\nCritical Hit."),
	SNIPER ("Sniper", "Power of critical-hit moves\nis tripled."),
	SOUNDPROOF ("Sound Proof", "Unaffected by sound moves."),
	STATIC ("Static", "The opponent has a 30%\nchance of being induced with\nPARALYZE when using a\nphysical attack."),
	SWIFTSWIM ("Swift Swim", false, "When rainy, The Pokémon’s\nSpeed doubles. However,\nSpeed will not double on the\nturn weather becomes Heavy\nRain."),
	SYNCHRONIZE ("Synchronize", false, "When this Pokémon becomes\nPOISON, PARALYZE, or BURN,\nso does the opponent."),
	THICKFAT ("Thick Fat", "Fire and Ice-type moves\ndeal 50% damage."),
	TORRENT ("Torrent", "When HP is below 1/3rd,\nWater's power increases to\n1.5 times");
			
    private String name, description;
    private Weather weather;
    private boolean active;

    /** CONSTRUCTOR **/
    Ability(String name, boolean active, Weather weather, String description) {    	
        this.name = name;
        this.active = active;
        this.weather = weather;
        this.description = description;
    }
    Ability(String name, String description) {    	
        this(name, true, null, description);
     }
    Ability(String name, boolean active, String description) {    	
       this(name, active, null, description);
    }
    Ability(String name, Weather weather, String description) {    	
    	this(name, true, weather, description);
    }
    /** END CONSTRUCTOR **/
    
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Weather getWeather() { return weather; }
    public String getDescription() { return description; }
}
/*** END TYPE CLASS ***/