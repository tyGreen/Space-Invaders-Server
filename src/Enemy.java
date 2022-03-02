//===================================================================================================
// SERVER-SIDE
//===================================================================================================

import javax.swing.JLabel;

public class Enemy extends Sprite implements Runnable {
	
	private Thread thread;
	private Player myPlayer;
	private ProjectileEnemy enemyProjectile;
	private Boolean isRightBumper, isLeftBumper, isBottomBumper, hasFocus, gameOver, canShoot;
		// Flag to track if enemy's position within array is located on the perimeter 
		// (i.e. will it be one of the first enemies to reach a wall or the ground?)
	private Enemy[][] enemies;
	private int enemyID;
	
	public ProjectileEnemy getEnemyProjectile() {return enemyProjectile;}
	public void setEnemyProjectile(ProjectileEnemy enemyProjectile) {this.enemyProjectile = enemyProjectile;}
	
	public Thread getThread() {return thread;}
	public void setThread(Thread thread) {this.thread = thread;}
	
	public Boolean getGameOver() {return gameOver;}
	public void setGameOver(Boolean gameOver) {this.gameOver = gameOver;}
	
	public Enemy[][] getEnemies() {return enemies;}
	public void setEnemies(Enemy[][] enemies) {this.enemies = enemies;}
	
	public Boolean getHasFocus() {return hasFocus;}
	public void setHasFocus(Boolean hasFocus) {this.hasFocus = hasFocus;}
	
	public Boolean getIsRightBumper() {return isRightBumper;}
	public void setIsRightBumper(Boolean isRightBumper) {this.isRightBumper = isRightBumper;}
	
	public Boolean getIsLeftBumper() {return isLeftBumper;}
	public void setIsLeftBumper(Boolean isLeftBumper) {this.isLeftBumper = isLeftBumper;}
	
	public Boolean getIsBottomBumper() {return isBottomBumper;}
	public void setIsBottomBumper(Boolean isBottomBumper) {this.isBottomBumper = isBottomBumper;}
	
	public Boolean getCanShoot() {return canShoot;}
	public void setCanShoot(Boolean canShoot) {this.canShoot = canShoot;}
	
	public int getEnemyID() {return enemyID;}
	public void setEnemyID(int enemyID) {this.enemyID = enemyID;}
	
	//Constructors:
	public Enemy() {
		super(GameProperties.ENEMY_WIDTH, GameProperties.ENEMY_HEIGHT, "img_invader1.gif", true, true, false);
		this.enemyProjectile = new ProjectileEnemy(this, this.myPlayer, this.enemies);
		this.isRightBumper = false;
		this.isLeftBumper = false;
		this.isBottomBumper = false;
		this.hasFocus = false;
		this.gameOver = false;
		this.canShoot = false;
		this.enemyID = 0;
		
	}
	
	public Enemy(int temp1, int temp2, Player temp4, Enemy[][] temp5) {
		super(GameProperties.ENEMY_WIDTH, GameProperties.ENEMY_HEIGHT, "img_invader1.gif", true, true, false);
		this.x = temp1;
		this.y = temp2;
		this.myPlayer = temp4;
		this.enemies = temp5;
		this.enemyProjectile = new ProjectileEnemy(this, this.myPlayer, this.enemies);
		this.isRightBumper = false;
		this.isLeftBumper = false;
		this.isBottomBumper = false;
		this.hasFocus = false;
		this.gameOver = false;
		this.canShoot = false;
		this.enemyID = 0;
	}
	
	//Other methods:
	public void display() {System.out.println("x,y / visible?: " + this.x + "," + this.y + " / " + this.visible);}
	
	public void moveEnemiesRight(Enemy[][] array) {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				array[i][j].setX(array[i][j].getX() + GameProperties.ENEMY_STEP);
				// Enemey's projectile must move with it (if not in motion):
				if(!array[i][j].getEnemyProjectile().getInMotion()) {
					array[i][j].getEnemyProjectile().setX(array[i][j].getX());
				}
			}
		}
	}
	
	public void moveEnemiesDown(Enemy[][] array) {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				array[i][j].setY(array[i][j].getY() + array[i][j].getHeight());
				if(!array[i][j].getEnemyProjectile().getInMotion()) {
					array[i][j].getEnemyProjectile().setY(array[i][j].getY());
				}
			}
		}
	}
	
	public void moveEnemiesLeft(Enemy[][] array) {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				array[i][j].setX(array[i][j].getX() - GameProperties.ENEMY_STEP);
				if(!array[i][j].getEnemyProjectile().getInMotion()) {
					array[i][j].getEnemyProjectile().setX(array[i][j].getX());
				}
			}
		}
	}
	
	public void moveEnemy() {
		this.thread = new Thread(this, "Enemy Thread");
		this.thread.start(); //Triggers/starts the thread
	}
	
	@Override
	public void run() { 
		// Master loop - when this ends, thread dies:
		MOTION:
		while(this.getInMotion()) {
			// While enemy has focus:
				// This loop keeps all (non-destroyed) enemies moving by running four inner loops
				// in sequence depending on current flag set, & so long as at least one enemy has 
				// the focus (at least one enemy left on screen)
			while(this.getHasFocus()) { 
				 
				if(GameProperties.ENEMY_COUNT > 1) {
					
					// While enemy has RIGHT bumper flag set:
					if(this.getIsRightBumper()) {
						
						// While RIGHT bumper has room to move right:
						while( (this.getX() + this.getWidth() + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
							// Move all enemies right:
							moveEnemiesRight(this.enemies);
							if(!this.getInMotion()) {
								break MOTION;
							}
							try {
								Thread.sleep(200);
							}
							catch(Exception e) {
								
							}
						}
						// Focus shifts from this enemy to enemy with BOTTOM bumper flag set:
						this.setHasFocus(false);
						// Iterate through array to find enemy with BOTTOM bumper flag set:
						for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
							for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
								if(enemies[i][j].getIsBottomBumper()) {
									enemies[i][j].setHasFocus(true);
								}
							}
						}
					}
					
					// While enemy has BOTTOM bumper flag set:
					if(this.getIsBottomBumper()) {
						// While BOTTOM bumper still has room to move down:
						if((this.getY() + this.getHeight()) < myPlayer.getY()) {
							moveEnemiesDown(this.enemies);
							if(!this.getInMotion()) {
								break MOTION;
							}
							try {
								Thread.sleep(200);
							}
							catch(Exception e) {
								
							}
							this.setHasFocus(false);
							// Determine whether to shift focus to right bumper or left bumper:
							// Iterate through array to find right bumper:
							OUTER:
							for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
								for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
									if(enemies[i][j].getIsRightBumper()) {
										// Right bumper found. Test if right step would put it out of bounds:
										if( (enemies[i][j].getX() + enemies[i][j].getWidth() + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
											// If not, give right bumper the focus next:
											enemies[i][j].setHasFocus(true);
											break OUTER;
										}
										// Otherwise we must be at right edge of screen & must now move left:
										else {
											// Left bumper will get focus next. Iterate through array again to find it:
											for(i = 0; i < GameProperties.ENEMY_ROWS; i++) {
												for(j = 0; j < GameProperties.ENEMY_COLS; j++) {
													if(enemies[i][j].getIsLeftBumper()) {
														// Once found, give it focus:
														enemies[i][j].setHasFocus(true);
														break OUTER;
													}
												}
											}
										}
									}
								}
							}	
						}
						else {
							this.setGameOver(true);
						}
					}
					
					// While enemy has LEFT bumper flag set:
					if(this.getIsLeftBumper()) {
						// While LEFT bumper has room to move left:
						while(this.getX() - GameProperties.ENEMY_STEP > 0) {
							// Move all enemies down
							moveEnemiesLeft(this.enemies);
							if(!this.getInMotion()) {
								break MOTION;
							}
							try {
								Thread.sleep(200);
							}
							catch(Exception e) {
								
							}
						}
						// Focus shifts from this enemy to enemy with BOTTOM bumper flag set:
						this.setHasFocus(false);
						if(GameProperties.ENEMY_COUNT > 1) {
							// Iterate through array to find enemy with BOTTOM bumper flag set:
							for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
								for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
									if(enemies[i][j].getIsBottomBumper()) {
										enemies[i][j].setHasFocus(true);
									}
								}
							}
						}
					}
				}
				// Otherwise, this is the final enemy:
				else {
					// Move right while room to do so:
					while( (this.getX() + this.getWidth() + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
						// Move all enemies right:
						moveEnemiesRight(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					
					// Move down one line while room to do so:
					if((this.getY() + this.getHeight()) < myPlayer.getY()) {
						moveEnemiesDown(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					
					// While BOTTOM bumper still has room to move down:
					if((this.getY() + this.getHeight()) < myPlayer.getY()) {
						moveEnemiesDown(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					else {
						this.setGameOver(true);
						break;
					}
					
					// Move left while room to do so:
					while(this.getX() - GameProperties.ENEMY_STEP > 0) {
						// Move all enemies down
						moveEnemiesLeft(this.enemies);
						if(!this.getInMotion()) {
							break MOTION;
						}
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					
					// Move down one line while room to do so:
					if((this.getY() + this.getHeight()) < myPlayer.getY()) {
						moveEnemiesDown(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					else {
						this.setGameOver(true);
						break;
					}
					// REPEAT
				}	
			}
			
			// While this enemy DOESN'T have focus:
			while(!this.getHasFocus()) {
				try {
					Thread.sleep(200);
				}
				catch(Exception e) {
					
				}
			}	
		} 
	}
}