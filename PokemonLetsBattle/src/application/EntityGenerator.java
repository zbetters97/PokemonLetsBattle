package application;
import entity.Entity;
import entity.collectables.balls.*;
import entity.collectables.items.*;
import entity.object.*;
import entity.object.object_interactive.*;

public class EntityGenerator {

	private GamePanel gp;
	
	protected EntityGenerator(GamePanel gp) {
		this.gp = gp;
	}
	
	public Entity getItem(String itemName) {
		
		Entity obj = null;
		
		switch (itemName) {				
			case COL_Ball_Great.colName: obj = new COL_Ball_Great(gp); break;
			case COL_Ball_Master.colName: obj = new COL_Ball_Master(gp); break;
			case COL_Ball_Poke.colName: obj = new COL_Ball_Poke(gp); break;
			case COL_Ball_Ultra.colName: obj = new COL_Ball_Ultra(gp); break;
			
			case ITM_EXP_Share.colName: obj = new ITM_EXP_Share(gp); break;
			case ITM_Full_Restore.colName: obj = new ITM_Full_Restore(gp); break;
			case ITM_Rod_Old.colName: obj = new ITM_Rod_Old(gp); break;
			case ITM_Rod_Good.colName: obj = new ITM_Rod_Good(gp); break;
			case ITM_Rod_Super.colName: obj = new ITM_Rod_Super(gp); break;
			
			case ITM_Heal_Antidote.colName: obj = new ITM_Heal_Antidote(gp); break;
			case ITM_Heal_Awakening.colName: obj = new ITM_Heal_Awakening(gp); break;
			case ITM_Heal_Burn.colName: obj = new ITM_Heal_Burn(gp); break;
			case ITM_Heal_Full.colName: obj = new ITM_Heal_Full(gp); break;
			case ITM_Heal_Ice.colName: obj = new ITM_Heal_Ice(gp); break;
			case ITM_Heal_Paralyze.colName: obj = new ITM_Heal_Paralyze(gp); break;
			
			case ITM_Potion_Hyper.colName: obj = new ITM_Potion_Hyper(gp); break;
			case ITM_Potion_Max.colName: obj = new ITM_Potion_Max(gp); break;
			case ITM_Potion_Super.colName: obj = new ITM_Potion_Super(gp); break;
			case ITM_Potion.colName: obj = new ITM_Potion(gp); break;
			
			case ITM_Repel_Max.colName: obj = new ITM_Repel_Max(gp); break;
			case ITM_Repel_Super.colName: obj = new ITM_Repel_Super(gp); break;
			case ITM_Repel.colName: obj = new ITM_Repel(gp); break;
			
			case ITM_Revive_Max.colName: obj = new ITM_Revive_Max(gp); break;
			case ITM_Revive.colName: obj = new ITM_Revive(gp); break;
			
			default: break;
		}
		
		return obj;		
	}	
	
	public Entity getObject(String objName, int worldX, int worldY) {
		
		Entity obj = null;
		
		switch (objName) {							
			case OBJ_Door.objName: obj = new OBJ_Door(gp, worldX, worldY); break;
			case OBJ_Grass.objName: obj = new OBJ_Grass(gp, worldX, worldY); break;
			case OBJ_Ledge.objName: obj = new OBJ_Ledge(gp, worldX, worldY); break;
			case OBJ_Boulder.objName: obj = new OBJ_Boulder(gp, worldX, worldY); break;
			case OBJ_Rock.objName: obj = new OBJ_Rock(gp, worldX, worldY); break;			
			case OBJ_Tree.objName: obj = new OBJ_Tree(gp, worldX, worldY); break;
			
			default: break;
		}
		
		return obj;		
	}	
	
	public Entity getDoor(int worldX, int worldY, int door) {
		return new OBJ_Door(gp, worldX, worldY, door);
	}
	public Entity getLedge(int worldX, int worldY, String direction, int tiles) {
		return new OBJ_Ledge(gp, worldX, worldY, direction, tiles);
	}
}