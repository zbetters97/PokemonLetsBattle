package application;

import entity.npc.*;
import entity.object.*;
import entity.object.object_interactive.OBJ_Door_Gym;
import entity.object.object_interactive.OBJ_Door_Store;
import entity.object.object_interactive.OI_Boulder;

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
		
		gp.obj[mapNum][i] = new OBJ_Tree(gp, 22, 23); i++;	
		gp.obj[mapNum][i] = new OBJ_Rock(gp, 22, 22); i++;	
	}	
	protected void setInteractiveObjects() {
		
		int mapNum = 0;
		int i = 0;
				
		gp.obj_i[mapNum][i] = new OBJ_Door_Store(gp, 30, 25); i++;	
		gp.obj_i[mapNum][i] = new OBJ_Door_Store(gp, 35, 21); i++;	
		gp.obj_i[mapNum][i] = new OBJ_Door_Gym(gp, 25, 18); i++;	
		gp.obj_i[mapNum][i] = new OI_Boulder(gp, 21, 22); i++;	
	}
	protected void setInteractiveTiles(boolean reset) {
				
		int mapNum = 0;
		int i = 0;
	}
}