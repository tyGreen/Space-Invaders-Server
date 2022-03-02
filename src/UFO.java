//===================================================================================================
// SERVER-SIDE
//===================================================================================================

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;

public class UFO extends Sprite implements Runnable {
	private Thread thread;
	private Boolean stopThread, isAlive;
	
	// Getters & Setters:
	public Thread getThread() {return thread;}
	public void setThread(Thread thread) {this.thread = thread;}
	
	public Boolean getIsAlive() {return isAlive;}
	public void setIsAlive(Boolean isAlive) {this.isAlive = isAlive;}
	
	public Boolean getStopThread() {return stopThread;}
	public void setStopThread(Boolean stopThread) {this.stopThread = stopThread;}

	//Constructors:
	public UFO() {
		super(GameProperties.UFO_WIDTH, GameProperties.UFO_HEIGHT, "img_UFO.png", true, true, false);
		this.stopThread = false;
		this.isAlive = true;
	}
	
	public UFO(JLabel temp1) {
		super(GameProperties.UFO_WIDTH, GameProperties.UFO_HEIGHT, "img_UFO.png", true, true, false);
		this.stopThread = false;
		this.isAlive = true;
	}
	
	// UFO Methods:
	
	public boolean UFOWillSpawn() {
		// Generates two random integers between 1 & GameProperties.ENEMY_SHOT_FREQ:
		int randomInt1 = (int)(Math.random() * GameProperties.UFO_SPAWN_FREQ + 1);
		int randomInt2 = (int)(Math.random() * GameProperties.UFO_SPAWN_FREQ + 1);
		// If integers match:
		if(randomInt1 == randomInt2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void moveUFO() {
		this.setX(this.getX() + GameProperties.UFO_STEP);		
	}
	
	public void resetUFO() {
		this.setX(0 - GameProperties.UFO_WIDTH);
		// If UFO resetting due to being shot:
		if(!this.getIsAlive()) {
			// Reset "is alive" flag:
			this.setIsAlive(true);
		}
	}
	
	public void startUFOThread() {
		System.out.println("Starting UFO thread...");
		this.thread = new Thread(this, "UFO thread");
		this.thread.start();
	}
	
	@Override
	public void run() {
		
		// While the UFO's "stopThread" flag is false:
		while(!this.getStopThread()) {
			if(UFOWillSpawn()) {
				while(this.getIsAlive() && this.getX() <= GameProperties.SCREEN_WIDTH) {
					moveUFO();
					// If UFO has been shot:
					if(!getIsAlive()) {
						// break out of the loop:
						break;
					}
					try {
						Thread.sleep(300);
					}
					catch(Exception e) {
						
					}
				}
				resetUFO();
			}
			try {
				Thread.sleep(200);
			}
			catch(Exception e) {
				
			}
		}
	}
}
