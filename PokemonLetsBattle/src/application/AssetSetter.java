package application;

import person.NPC_Rival;

public class AssetSetter {
	
	private GamePanel gp;
	
	protected AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	protected void setNPC() {
		
		int mapNum = 0;
		int i = 0;
						
		gp.npc[mapNum][i] = new NPC_Rival(gp, 23, 12); i++;	
	}	
	protected void setObject() {		
		
		int mapNum = 0;
		int i = 0;	
	}	
	protected void setInteractiveObjects() {
				
		int mapNum = 0;
		int i = 0;		
	}
	protected void setInteractiveTiles(boolean reset) {
				
		int mapNum = 0;
		int i = 0;
	}
}