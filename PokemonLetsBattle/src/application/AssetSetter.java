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
						
		gp.npc[mapNum][i] = new NPC_Rival(gp, 8, 12); i++;	
		
		gp.npc[mapNum][i] = new NPC_Sign(gp, 10, 14, 0, "MAY'S HOUSE"); i++;
		gp.npc[mapNum][i] = new NPC_Sign(gp, 19, 14, 1, "PETALBURG GYM"); i++;	
		gp.npc[mapNum][i] = new NPC_Sign(gp, 19, 20, 0, "WELCOME TO:\nPetalburg Town"); i++;	
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