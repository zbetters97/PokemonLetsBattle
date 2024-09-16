package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
	
	private GamePanel gp;
	
	public ConfigManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void saveConfig() {
		
		try {
			// IMPORT FILE
			BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
			
			// FULLSCREEN
			bw.write("FULLSCREEN\n" + gp.fullScreenOn);
			bw.newLine();
			
			// MUSIC VOLUME
			bw.write("MUSIC VOLUME\n" + String.valueOf(gp.music.volumeScale));			
			bw.newLine();
			
			// SOUND EFFECTS VOLUME
			bw.write("SE VOLUME\n" + String.valueOf(gp.se.volumeScale));			
			bw.newLine();
			
			// SOUND EFFECTS VOLUME
			bw.write("TEXT SPEED\n" + String.valueOf(gp.ui.textSpeed));	
			bw.newLine();
			
			// CLOSE FILE
			bw.close();			
		} 
		catch (IOException e) {			
			
		}
	}
	public void loadConfig() {
		
		try {
			// IMPORT FILE
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));
			
			br.readLine(); 
			
			// FULL SCREEN
			String s = br.readLine();
			gp.fullScreenOn = Boolean.valueOf(s);
			br.readLine();
			
			// MUSIC VOLUME
			s = br.readLine();
			gp.music.volumeScale = Integer.parseInt(s);
			br.readLine();
			
			// SOUND EFFECTS VOLUME
			s = br.readLine();
			gp.se.volumeScale = Integer.parseInt(s);
			br.readLine();
			
			// TEXT SPEED
			s = br.readLine();
			gp.ui.textSpeed = Integer.parseInt(s);
			
			br.close();			
		} 
		catch (Exception e) {
			
			// FULL SCREEN
			gp.fullScreenOn = false;
			
			// MUSIC VOLUME
			gp.music.volumeScale = 3;
			
			// SOUND EFFECTS VOLUME
			gp.se.volumeScale = 3;
			
			// TEXT SPEED
			gp.ui.textSpeed = 1;
		}
	}
}