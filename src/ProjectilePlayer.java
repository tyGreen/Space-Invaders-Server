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

public class ProjectilePlayer extends Sprite implements Runnable {
	
	//Attributes:
	private Thread thread;
	private Enemy[][] enemies;
	private Boolean collision, keyPressed, invasionStopped;
	private Player myPlayer;
	private int enemyCount;
	private UFO myUFO;
	final int CLIENT_PORT = 5555;
	
	public int getEnemyCount() {
		return enemyCount;
	}
	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}
	public Boolean getCollision() {return collision;}
	public void setCollision(Boolean collision) {this.collision = collision;}
	
	public Boolean getKeyPressed() {return keyPressed;}
	public void setKeyPressed(Boolean keyPressed) {this.keyPressed = keyPressed;}
	
	public Boolean getInvasionStopped() {return invasionStopped;}
	public void setInvasionStopped(Boolean boardCleared) {this.invasionStopped = boardCleared;}
	
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setEnemies(Enemy[][] temp) {this.enemies = temp;}
	
	//Constructors:
	//Default
	public ProjectilePlayer() {
		super(GameProperties.PRJCT_PLAYER_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT, "img_prjct_player.png", false, false, false);
		this.collision = false;
		this.inMotion = false;
		this.keyPressed = false;
		this.enemyCount = GameProperties.ENEMY_COUNT;
		this.invasionStopped = false;
	}
	
	//Secondary
	public ProjectilePlayer(Player temp2, UFO temp4) {
		super(GameProperties.PRJCT_PLAYER_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT, "img_prjct_player.png", false, false, false);
		this.myPlayer = temp2;
		this.myUFO = temp4;
		this.collision = false;
		this.inMotion = false;
		this.keyPressed = false;
		this.enemyCount = GameProperties.ENEMY_COUNT;
		this.invasionStopped = false;
	}
	
	//Other methods:
	
	private void resetPlayerProjectile() {
		//Resets x,y coordinates & label of player projectile:
		this.setX(myPlayer.getX());
		this.setY(myPlayer.getY());
		//Set motion flag to false:
		this.setInMotion(false);
		//Set collision flag to false:
		this.setCollision(false);
		//Hides the label:
//		this.hide();
		this.setKeyPressed(false);
	}
	
	public void updatePlayerScore(int id) {
		if(id == 1) {
			myPlayer.setPlayerScore(myPlayer.getPlayerScore() + GameProperties.PTS_PER_ENEMY1);
		}
		else if(id == 2) {
			myPlayer.setPlayerScore(myPlayer.getPlayerScore() + GameProperties.PTS_PER_ENEMY2);
		}
		else if(id == 3) {
			myPlayer.setPlayerScore(myPlayer.getPlayerScore() + GameProperties.PTS_PER_ENEMY3);
		}
		else {
			myPlayer.setPlayerScore(myPlayer.getPlayerScore() + GameProperties.PTS_PER_UFO);
		}
	}
	
	public void reassignRightBumper(Enemy enemy) {
		// Store enemy array in "enemies" variable:
		Enemy[][] enemies = enemy.getEnemies();
		int currentRightBumperCol;
		Boolean enemyHadFocus = false;
		// Get this enemy's position (row & column #):
		OUTER:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				
				// Find this enemy:
				if(enemies[i][j] == enemy) {
					currentRightBumperCol = j;
					if(enemies[i][j].getHasFocus()) {
						enemies[i][j].setHasFocus(false);
						enemyHadFocus = true;
					}
					
					// Find the next in-motion (not destroyed) enemy to left of old right bumper:
					for(j = currentRightBumperCol - 1; j >= 0; j--) {
						
						// If enemy in motion (not destroyed)						
						if(enemies[i][j].getInMotion()) {
							
							// Becomes the next right bumper:
							enemies[i][j].setIsRightBumper(true);
							if(enemyHadFocus) {
								enemies[i][j].setHasFocus(true);
							}
							break OUTER;
						}
					}
				}
			}
		}
	}
	
	public void reassignBottomBumper(Enemy enemy) {
		// Store enemy array in "enemies" variable:
		Enemy[][] enemies = enemy.getEnemies();
		int currentBottomBumperRow = 0;
		int currentBottomBumperCol = 0;
		Boolean enemyHadFocus = false;
		// Get this enemy's position (row & column #):
		OUTER:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				
				//Find this enemy:
				if(enemies[i][j] == enemy) {
					currentBottomBumperRow = i;
					currentBottomBumperCol = j;
				
					// Set right bumper flag to false:
					if(enemies[i][j].getHasFocus()) {
						enemies[i][j].setHasFocus(false);
						enemyHadFocus = true;
					}
					
					// Find the next in-motion (not destroyed) enemy to left of old right bumper:
					for(i = currentBottomBumperRow; i >= 0; i--) {
						for(j = currentBottomBumperCol; j < GameProperties.ENEMY_COLS; j++) {
							
							// If enemy in motion (not destroyed)						
							if(enemies[i][j].getInMotion()) {
								
								// Becomes the next right bumper:
								enemies[i][j].setIsBottomBumper(true);
								if(enemyHadFocus) {
									enemies[i][j].setHasFocus(true);
								}
								break OUTER;
							}
						}
						currentBottomBumperCol = 0;
					}
				}
			}
		}
	}
	
	public void reassignLeftBumper(Enemy enemy) {
		// Store enemy array in "enemies" variable:
		Enemy[][] enemies = enemy.getEnemies();
		//int currentRightBumperRow = 0;
		int currentLeftBumperCol;
		boolean enemyHadFocus = false;
		// Get this enemy's position (row & column #):
		OUTER:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				// If right bumper found:
				if(enemies[i][j] == enemy) {
					currentLeftBumperCol = j;
					if(enemies[i][j].getHasFocus()) {
						enemies[i][j].setHasFocus(false);
						enemyHadFocus = true;
					}
					// Find the next in-motion (not destroyed) enemy to left of old right bumper:
					for(j = currentLeftBumperCol + 1; j < GameProperties.ENEMY_COLS; j++) {
						// If enemy in motion (not destroyed)						
						if(enemies[i][j].getInMotion()) {
							// Becomes the next right bumper:
							enemies[i][j].setIsLeftBumper(true);
							if(enemyHadFocus) {
								enemies[i][j].setHasFocus(true);
							}
							break OUTER;
						}
					}
				}
			}
		}
	}
	
	private void increaseInvasionSpeed(int count) {
		switch(count) {
			case 40: 
				GameProperties.ENEMY_STEP *= 4; 
				break;
			case 25: 
				GameProperties.ENEMY_STEP *= 2; 
				break;
			case 10: 
				GameProperties.ENEMY_STEP *= 2; 
				break;
			case 1: 
				GameProperties.ENEMY_STEP *= 2; 
				break;
		}	
	}
	
	private void updateEnemyCount() {
		GameProperties.ENEMY_COUNT -= 1;
	}
	
	public void reassignCanShoot(Enemy enemy) {
		Enemy[][] enemies = enemy.getEnemies();
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j< GameProperties.ENEMY_COLS; j++) {
				if(enemies[i][j] == enemy) {
					enemy.setCanShoot(false);
					// If not first row (prevents flag reassignment going out of bounds):
					if((i - 1) >= 0) {
						// Sets "can shoot" flag of enemy directly behind destroyed enemy to true:
						enemies[i - 1][j].setCanShoot(true);
						enemies[i - 1][j].getEnemyProjectile().startEnemyProjectileThread();
					}
				}
			}
		}		
	}

	private void destroyEnemy(Enemy enemy) {
		enemy.stop();
		enemy.setCanShoot(false);
		enemy.getHitbox().setSize(0, 0);
		// Pass in ID of "1" for enemies:
		this.updatePlayerScore(enemy.getEnemyID());
		this.updateEnemyCount();
		
		if(GameProperties.ENEMY_COUNT == 0) {
			this.setInvasionStopped(true);
		}
		
		// Reassign "can shoot" flag:
		reassignCanShoot(enemy);
		
		// If enemy has bumper flag set, reassign it:
		if(enemy.getIsRightBumper()) {
			enemy.setIsRightBumper(false);
			reassignRightBumper(enemy);	
		}
		
		if(enemy.getIsBottomBumper()) {
			enemy.setIsBottomBumper(false);
			reassignBottomBumper(enemy);
		}
		
		if(enemy.getIsLeftBumper()) {
			enemy.setIsLeftBumper(false);
			reassignLeftBumper(enemy);
		}
		
		increaseInvasionSpeed(GameProperties.ENEMY_COUNT);
	}
	
	private Boolean detectEnemyCollision() {
		//Iterate through enemy array and test each enemy's hitbox for collision w/ player projectile hitbox:
		Boolean collisionDetected = false;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				//If player collision detected:
				if(this.hitbox.intersects(enemies[i][j].getHitbox().getX(), (enemies[i][j].getHitbox().getY() - enemies[i][j].getHeight() ), enemies[i][j].getHitbox().getWidth(), enemies[i][j].getHitbox().getHeight())) {
					collisionDetected = true;
					resetPlayerProjectile();
					myPlayer.setAlienFlag(i, j, false);
					destroyEnemy(enemies[i][j]);	
				}
			}
		}
		return collisionDetected;
	}
	
	public void destroyUFO(UFO ufo) {
		ufo.setIsAlive(false);
		// Pass in ID of "2" for UFOs:
		this.updatePlayerScore(4);
	}
	
	private Boolean detectUFOCollision() {
		Boolean collisionDetected = false;
		//If UFO collision detected:
		if(this.hitbox.intersects(myUFO.getHitbox())) {
			collisionDetected = true;
			resetPlayerProjectile();
			destroyUFO(myUFO);	
		}
		return collisionDetected;
	}
	
	
	public void launchPlayerProjectile() {
		this.setKeyPressed(true);
		this.thread = new Thread(this, "Enemy Projectile Thread");
		this.thread.start();
	}
	
	@Override
	public void run() {
		while(this.getKeyPressed() == true) {
			int thread_x = this.getX();
			int thread_y = this.getY();
			while(thread_y > 0 && this.getCollision() == false) {
				this.move();
				thread_y -= GameProperties.PRJCT_PLAYER_STEP;
				this.setX(thread_x);
				this.setY(thread_y);
				System.out.println("Player Projectile x,y: " + this.getX() + "," + this.getY() + "/" + this.getInMotion());
				if(this.detectEnemyCollision()) {
					try {
						Socket s99 = new Socket("localhost", CLIENT_PORT);
					
						// Initialize data stream to send data out
						OutputStream outstream = s99.getOutputStream();
						PrintWriter out = new PrintWriter(outstream);

						// Reply with following command to ClientService:
						String commandOut = "PLAY_ENEMY_EXPLOSION\n";
						System.out.println("Sending: " + commandOut);
						out.println(commandOut);
						out.flush();
							
						s99.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					break;
				}
				else if(this.detectUFOCollision() ) {
					break;
				}
				else if(thread_y <= 0) {
					resetPlayerProjectile();
					break;
				}
				try {
					Thread.sleep(25);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
}
