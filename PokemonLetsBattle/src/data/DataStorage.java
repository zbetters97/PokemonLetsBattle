package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pokemon.Pokemon.Pokedex;

public class DataStorage implements Serializable {
	
	private static final long serialVersionUID = -5842400749008672573L;
	
	// FILE INFO
	String file_date;
	boolean gameCompleted;
	
	// DAY STATE
	int dayState, dayCounter;
	float filterAlpha;
	
	// PROGRESS
	
	// PLAYER DATA
	String pName, pDirection;	
	int cMap, cArea, pWorldX, pWorldY;
	int pMoney, pDexSeen, pDexOwn;	
	
	// PLAYER BAG
	ArrayList<String> pKeyItemNames = new ArrayList<>();
	ArrayList<String> pItemNames = new ArrayList<>();
	ArrayList<Integer> pItemAmounts = new ArrayList<>();
	
	ArrayList<String> pPokeballNames = new ArrayList<>();
	ArrayList<Integer> pPokeballAmounts = new ArrayList<>();
	
	// PLAYER ITEM
	String pKeyItem;
	
	// PLAYER POKEPARTY
	ArrayList<String> pPokePartyUID = new ArrayList<>();
	ArrayList<Pokedex> pPokePartyID = new ArrayList<>();
	ArrayList<Character> pPokePartySex = new ArrayList<>();
	ArrayList<int[]> pPokePartyStats = new ArrayList<>();
	ArrayList<String> pPokePartyNature = new ArrayList<>();
	ArrayList<List<String>> pPokePartyMoveNames = new ArrayList<>();
	ArrayList<List<Integer>> pPokePartyMovePP = new ArrayList<>();
	ArrayList<String> pPokePartyStatus = new ArrayList<>();
	ArrayList<String> pPokePartyItem = new ArrayList<>();
	ArrayList<String> pPokePartyBall = new ArrayList<>();
	ArrayList<Boolean> pPokePartyAlive = new ArrayList<>();
	
	// PLAYER PC
	ArrayList<String> pPCPartyUID = new ArrayList<>();
	ArrayList<Pokedex> pPCPartyID = new ArrayList<>();
	ArrayList<Character> pPCPartySex = new ArrayList<>();
	ArrayList<int[]> pPCPartyStats = new ArrayList<>();
	ArrayList<String> pPCPartyNature = new ArrayList<>();
	ArrayList<List<String>> pPCPartyMoveNames = new ArrayList<>();
	ArrayList<List<Integer>> pPCPartyMovePP = new ArrayList<>();
	ArrayList<String> pPCPartyStatus = new ArrayList<>();
	ArrayList<String> pPCPartyItem = new ArrayList<>();
	ArrayList<String> pPCPartyBall = new ArrayList<>();	
	ArrayList<Boolean> pPCPartyAlive = new ArrayList<>();
	ArrayList<Integer> pPCPartyIndexBox = new ArrayList<>();
	ArrayList<Integer> pPCPartyIndexSlot = new ArrayList<>();
	
	// NPC DATA
	String npcNames[][];
	int npcWorldX[][];
	int npcWorldY[][];
	int npcDialogueSet[][];
	boolean npcHasBattle[][];
	
	// MAP OBJECTS
	String mapObjectNames[][];
	int mapObjectWorldX[][];
	int mapObjectWorldY[][];
	String mapObjectDirections[][];
	int mapObjectDoors[][];
	int mapObjectLedges[][];
	
	// MAP iTILES
	String iTileNames[][];
	int iTileWorldX[][];
	int iTileWorldY[][];
	String iTileDirections[][];
	
	public String toString() {
		return "[" + pName + "]  " + file_date;
	}
}