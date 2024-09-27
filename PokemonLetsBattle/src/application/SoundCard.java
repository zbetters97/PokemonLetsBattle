package application;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundCard {
	
	// CLIP HOLDERS
	public Clip clip;
	private String sounds[][] = new String[7][];
	
	// VOLUME SLIDER
	private FloatControl fc;
	public int volumeScale = 3;
	public float volume;
	
	public SoundCard() {
		sounds[0] = getSounds("musicwrld");
		sounds[1] = getSounds("musicbtl");
		sounds[2] = getSounds("menu");
		sounds[3] = getSounds("pdxcry");	
		sounds[4] = getSounds("pdxfaint");	
		sounds[5] = getSounds("moves");
		sounds[6] = getSounds("battle");		
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
	
	public int getFile(int category, String file) {
		
		if (category > 0 && file != null) {
			for (int i = 0; i < sounds[category].length; i++) {	
				if (sounds[category][i].contains(file)) {
					return i;
				}			
			}	
		}
		
		return 0;		
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
				
	public int getSoundDuration(int category, int record) {
		
		int duration = 0;
		
		if (category >= 0 && record >= 0) {		
			String filePath = sounds[category][record];
			File file = new File(filePath);		
			AudioInputStream audioInputStream;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(file);
				AudioFormat format = audioInputStream.getFormat();
				long frames = audioInputStream.getFrameLength();
				double length = (double) ((frames + 0.0) / format.getFrameRate()); 
				duration = (int) Math.ceil(60.0 * length);
				if (duration == 0) duration = 60;
			} 
			catch (UnsupportedAudioFileException e) {			
				e.printStackTrace();
			} 
			catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		return duration;
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