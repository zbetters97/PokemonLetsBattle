package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import application.GamePanel;

public class TileManager {
	
	private GamePanel gp;
	public Tile[] tile;
		
	// [MAP NUMBER][ROW][COL]
	public int mapTileNum[][][];
	
	ArrayList<String> fileNames = new ArrayList<>();
	ArrayList<String> collisionStatus = new ArrayList<>();
	
	public TileManager(GamePanel gp) {		
		this.gp = gp;
		
		loadTileData();
		
		gp.maxWorldCol = 100;
		gp.maxWorldRow = 100;
		gp.worldWidth = gp.tileSize * 100;
		gp.worldHeight = gp.tileSize * 100;
		
		mapTileNum = new int[gp.maxMap][100][100];
	}
	
	public void loadMap() {
		
		String currentMap = "res/maps/" + gp.mapFiles[gp.currentMap];
		int mapLength = 0;
		
		try {			
			
			Scanner sc = new Scanner(new File(currentMap));
			
			for (int row = 0; sc.hasNextLine(); row++) {
				
				String line = sc.nextLine();
				String numbers[] = line.split(" ");
				mapLength = numbers.length;
				
				for (int col = 0; col < numbers.length; col++) {										
					int tileNum = Integer.parseInt(numbers[col]);					
					mapTileNum[gp.currentMap][col][row] = tileNum;
				}								
			}
			
			sc.close();
			
			// ASSIGN NEW WORLD DIMENSIONS
			gp.maxWorldCol = mapLength;
			gp.maxWorldRow = mapLength;
			gp.worldWidth = gp.tileSize * mapLength;
			gp.worldHeight = gp.tileSize * mapLength;
		} 
		catch(Exception e) {
			e.printStackTrace();
		}		
	}	
	
	private void loadTileData() {
		
		// IMPORT TILE DATA		
		InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		// ADD TILE DATA TO ARRAYS
		try {
			String line;
			while ((line = br.readLine()) != null) {
				fileNames.add(line);
				collisionStatus.add(br.readLine());	
			}						
			br.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// ASSIGN TILE COLLISION
		tile = new Tile[fileNames.size()];
		getTileImage(); 
	}
	
	public void getTileImage() {		
		
		// loop through all tile data in fileNames
		for (int i = 0; i< fileNames.size(); i++) {
			
			String fileName;
			boolean collision;
			
			// assign each name to fileName
			fileName = fileNames.get(i);
			
			// assign tile status
			if (collisionStatus.get(i).equals("true")) 
				collision = true;
			else
				collision = false;
						
			setup(i, fileName, collision);
		}
	}
	
	public void setup(int index, String imageName, boolean collision) {
		
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName));
			tile[index].image = GamePanel.utility.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {				
				
		int worldCol = 0;
		int worldRow = 0;
		boolean offCenter = false;
				
		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			// TILE NUMBERS FROM MAP TXT
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
			// WORLD X,Y
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			
			// PLAYER SCREEN POSITION X,Y OFFSET TO CENTER
			int screenX = worldX - gp.player.worldX + gp.player.screenX; 
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			// STOP CAMERA MOVEMENT AT WORLD BOUNDARY
			if (gp.player.screenX > gp.player.worldX) {
				screenX = worldX;
				offCenter = true;
			}
			if (gp.player.screenY > gp.player.worldY) {
				screenY = worldY;
				offCenter = true;
			}
			
			// FROM PLAYER TO RIGHT-EDGE OF SCREEN
			int rightOffset = gp.screenWidth - gp.player.screenX;		
			
			// FROM PLAYER TO RIGHT-EDGE OF WORLD
			if (rightOffset > gp.worldWidth - gp.player.worldX) {
				screenX = gp.screenWidth - (gp.worldWidth - worldX);
				offCenter = true;
			}			
			
			// FROM PLAYER TO BOTTOM-EDGE OF SCREEN
			int bottomOffSet = gp.screenHeight - gp.player.screenY;
			
			// FROM PLAYER TO BOTTOM-EDGE OF WORLD
			if (bottomOffSet > gp.worldHeight - gp.player.worldY) {
				screenY = gp.screenHeight - (gp.worldHeight - worldY);
				offCenter = true;
			}
			
			// DRAW TILES WITHIN PLAYER BOUNDARY
			if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
				worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {			
								
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			else if (offCenter) {					
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
										
			// TO NEXT COLUMN
			worldCol++;
			
			// TO NEXT ROW
			if (worldCol == gp.maxWorldCol) { 
				worldCol = 0;
				worldRow++; 
			}
		}
	}
}