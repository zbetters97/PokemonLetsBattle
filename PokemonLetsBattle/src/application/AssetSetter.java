package application;

import entity.npc.*;
import tile.tile_interactive.*;

public class AssetSetter {
	
	private GamePanel gp;
	
	protected AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	protected void setNPC() {
		
		int mapNum = 0;
		int i = 0;
						
		gp.npc[mapNum][i] = new NPC_Rival(gp, 15, 16); i++;	
		
		gp.npc[mapNum][i] = new NPC_Sign(gp, 18, 19, 0, "MAY'S HOUSE"); i++;
		gp.npc[mapNum][i] = new NPC_Sign(gp, 27, 19, 1, "PETALBURG GYM"); i++;	
		gp.npc[mapNum][i] = new NPC_Sign(gp, 27, 25, 0, "WELCOME TO:\nPetalburg Town"); i++;	
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