package application;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundCard {
	
	// CLIP HOLDERS
	public Clip clip;
	private String sounds[][] = new String[6][];
	
	// VOLUME SLIDER
	private FloatControl fc;
	public int volumeScale = 3;
	public float volume;
	
	public SoundCard() {
		sounds[0] = getSounds("musicwrld");
		sounds[1] = getSounds("musicbtl");
		sounds[2] = getSounds("menu");
		sounds[3] = getSounds("moves");
		sounds[4] = getSounds("pdxcry");	
		sounds[5] = getSounds("pdxfaint");			
	}	
	
	private String[] getSounds(String library) {		
					
		File folder = new File(new File("").getAbsolutePath() + "/sound/" + library);
		File soundFiles[] = folder.listFiles();
		String sounds[] = new String[soundFiles.length];
		
		for (int i = 0; i < soundFiles.length; i++) {					
		
			String path = new File("").getAbsolutePath() + "/sound/" + 
					library + "/" + soundFiles[i].getName();
			
			sounds[i] = path;
		}	
		
		return sounds;					
	}
	
	public void setFile(int category, int record) {		
		try {			
			String filePath = sounds[category][record];
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath));
			clip = AudioSystem.getClip();
			clip.open(ais);
			
			// VOLUME
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			checkVolume();
		}
		catch (Exception e) {
			return;
		}
	}
	public void play() {		
		clip.start();
	}	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);		
	}	
	public void stop() {
		clip.stop();
	}
	public void checkVolume() {
		
		switch(volumeScale) {
			case 0: volume = -80f; break;
			case 1: volume = -20f; break;
			case 2: volume = -12f; break;
			case 3: volume = -5f; break;
			case 4: volume = 1f;break;
			case 5: volume = 6f; break;
		}
		fc.setValue(volume);
	}
}