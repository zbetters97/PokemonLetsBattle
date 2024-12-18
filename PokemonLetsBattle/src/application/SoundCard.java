package application;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundCard {
	
	// CLIP HOLDERS
	public Clip clip;
	private String sounds[][] = new String[10][];
	
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
		sounds[7] = getSounds("world");	
		sounds[8] = getSounds("entity");
		sounds[9] = getSounds("musicbtl_multi");
	}	
	public String[] getSELibrary(int index) {
		return sounds[index];
	}
	
	private String[] getSounds(String library) { 
		
		if (Driver.class.getResource("Driver.class").toString().startsWith("jar")) {
			
			String jarPath;
			List<String> sounds = new ArrayList<String>();
			try {
				jarPath = new File(Driver.class
				         .getProtectionDomain()
				         .getCodeSource()
				         .getLocation()
				         .toURI()).getPath();
				
				JarFile jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> entries = jarFile.entries();
				
				while (entries.hasMoreElements()) {
					
				    JarEntry entry = entries.nextElement();
				    
				    if (entry.getName().startsWith("sound/" + library) && !entry.isDirectory()) {
				    	sounds.add("/" + entry.getName());
				    }
				}			
				
				jarFile.close();
				
				return sounds.stream().toArray(String[]::new);
			} 
			catch (URISyntaxException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
		}
		else {
			File folder = new File("res/sound/" + library);	
			List<String> sounds = new ArrayList<String>();
			
			for (File f : folder.listFiles()) {			
				String path = f.getName().toLowerCase();
				sounds.add("/sound/" + library + "/" + path);					
			}
			
			return sounds.stream().toArray(String[]::new);			
		}
		
		return null;		
	}
	
	public int getFile(int category, String file) {
		
		if (category > -1 && file != null) {
			for (int i = 0; i < sounds[category].length; i++) {
				if (sounds[category][i].toLowerCase().contains(file.toLowerCase())) {	
					return i;
				}			
			}	
		}
				
		return 0;		
	}
	
	public void setFile(int category, int record) {		
		
		try {			
			URL file = getClass().getResource(sounds[category][record]);
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			clip = AudioSystem.getClip();
			clip.open(ais);
			
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
			
			URL file = getClass().getResource(sounds[category][record]);
			
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