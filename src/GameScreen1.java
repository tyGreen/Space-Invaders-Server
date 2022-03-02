//===================================================================================================
// SERVER-SIDE
//===================================================================================================

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//public class GameScreen1 extends JFrame implements KeyListener{
public class GameScreen1 extends JFrame {

	private static final long serialVersionUID = -8824959153681940829L;
	
	final int CLIENT_PORT = 5555;

	private GameHandler gameHandler;
	
	//Storage classes for game sprites:
	private Player player;
	private ProjectilePlayer playerProjectile;
	private Enemy[][] enemies;
	private UFO ufo;
	
	//JLabels to display sprites:
//	private JLabel lbl_player, lbl_prjct_player, lbl_score, lbl_currentScore, lbl_UFO;
//	private ImageIcon img_player, img_enemy, img_prjct_player, img_prjct_enemy, img_UFO;
//	private JLabel[] lbl_playerLives;
		
	//Graphics container:
	private Container container1;
	
	//GUI set-up (constructor):
	public GameScreen1() {
		//Window title:
		super("Space Invaders");
		//Screen size:
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		//Centers game window on screen upon launching:
		setLocationRelativeTo(null);
		
//		lbl_player = new JLabel();
//		myPlayer = new Player(lbl_player);
		player = new Player();
//		img_player = new ImageIcon(getClass().getResource(myPlayer.getFileName()));
//		lbl_player.setIcon(img_player);
//		lbl_player.setSize(myPlayer.getWidth(), myPlayer.getHeight());
		
		//SCOREBOARD:
		//"SCORE" label:
//		lbl_score = new JLabel("SCORE");
//		lbl_score.setFont(new Font("Serif", Font.BOLD, GameProperties.SCORE_TXT_SIZE));
//		lbl_score.setForeground(Color.white);
//		lbl_score.setSize(70, GameProperties.SCORE_TXT_SIZE);
		//Displays current score:
//		lbl_currentScore = new JLabel(String.valueOf(myPlayer.getPlayerScore()));
//		lbl_currentScore.setFont(new Font("Serif", Font.ITALIC, GameProperties.SCORE_TXT_SIZE));
//		lbl_currentScore.setForeground(Color.white);
//		lbl_currentScore.setSize(70, GameProperties.SCORE_TXT_SIZE);
		
//		lbl_playerLives = new JLabel[3];
//		for(int i = 0; i < 3; i++) {
//			lbl_playerLives[i] = new JLabel();
//			lbl_playerLives[i].setIcon(new ImageIcon(getClass().getResource("img_playerLifeIcon.png")));
//			lbl_playerLives[i].setSize(GameProperties.PLAYER_LIFE_ICON_WIDTH, GameProperties.PLAYER_LIFE_ICON_HEIGHT);
//		}
		
//		lbl_player = new JLabel();
//		myPlayer = new Player(lbl_player);
		player = new Player();
//		img_player = new ImageIcon(getClass().getResource(myPlayer.getFileName()));
//		lbl_player.setIcon(img_player);
//		lbl_player.setSize(myPlayer.getWidth(), myPlayer.getHeight());
		
//		lbl_UFO = new JLabel();
//		myUFO = new UFO(lbl_UFO);
		ufo = new UFO();
//		img_UFO = new ImageIcon(getClass().getResource(myUFO.getFileName()));
//		lbl_UFO.setIcon(img_UFO);
//		lbl_UFO.setSize(myUFO.getWidth(), myUFO.getHeight());
		
		enemies = new Enemy[GameProperties.ENEMY_ROWS][GameProperties.ENEMY_COLS];
		int enemyOffsetX = 0;
		int enemyOffsetY = 0;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			enemyOffsetX = 0;
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
//				enemies[i][j] = new Enemy((0 + enemyOffsetX), (GameProperties.ENEMY_HEIGHT + enemyOffsetY), new JLabel(), myPlayer, enemies, lbl_playerLives);
				enemies[i][j] = new Enemy((0 + enemyOffsetX), (GameProperties.ENEMY_HEIGHT + enemyOffsetY), player, enemies);
//				enemies[i][j].getLbl_enemy().setSize(enemies[i][j].getWidth(), enemies[i][j].getHeight());			
				enemyOffsetX += (enemies[i][j].getWidth() + GameProperties.ENEMY_SPACING);
				
				// Set icon according to row:
				if(i == 0) {
//					img_enemy = new ImageIcon(getClass().getResource("img_enemy1.gif"));
					enemies[i][j].setEnemyID(1);
				}
				else if (i == 1 || i == 2) {
//					img_enemy = new ImageIcon(getClass().getResource("img_enemy2.gif"));
					enemies[i][j].setEnemyID(2);
				}
				else {
//					img_enemy = new ImageIcon(getClass().getResource("img_enemy3.gif"));
					enemies[i][j].setEnemyID(3);
				}
				
//				enemies[i][j].getLbl_enemy().setIcon(img_enemy);
				
				// Set the "bumpers" (enemies to reach walls first):
				// If this is the first enemy in the first row (top left corner):
				if((i == 0) && (j == 0)) {
					// It is the left bumper:
					enemies[i][j].setIsLeftBumper(true);
				}
				// Else, if this is the last enemy in the first row (top right corner):
				else if((i == 0) && (j == GameProperties.ENEMY_COLS - 1)) {
					// It is the right bumper:
					enemies[i][j].setIsRightBumper(true);
					// Get focus first because enemies move to right first:
					enemies[i][j].setHasFocus(true);
					
				}
				// Else, if this enemy is the first in the last row (bottom left corner):
				else if((i == (GameProperties.ENEMY_ROWS - 1)) && (j == 0)) {
					// Set "bumper" flag to true:
					enemies[i][j].setIsBottomBumper(true);
				}
				
				// Set "can shoot" flag (allows enemies to launch projectiles):
				// If enemy is in bottom row:
				if(i == GameProperties.ENEMY_ROWS - 1) {
					// Set "can shoot" flag to true:
					enemies[i][j].setCanShoot(true);
				}
				
				// If this enemy is the last in its row:
				if(j == GameProperties.ENEMY_COLS - 1) {
					// Start positioning enemies on next row:
					enemyOffsetY += enemies[i][j].getHeight();
				}
				
				// ENEMY'S PROJECTILE:
				enemies[i][j].getEnemyProjectile().setX(enemies[i][j].getX() + 12); // Enemy projectile sprite half width of enemy sprite, so to be centered behind enemy, sprite needs to be offset by half width of enemy
				enemies[i][j].getEnemyProjectile().setY(enemies[i][j].getY());
				// Set size of enemy projectile label to match size of enemy projectile sprite:
//				enemies[i][j].getLbl_enemy().setSize(enemies[i][j].getWidth(), enemies[i][j].getHeight());			

//				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setSize(GameProperties.PRJCT_ENEMY_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT);
				// Set image icon of enemy projectile:
//				img_prjct_enemy = new ImageIcon(getClass().getResource(enemies[i][j].getEnemyProjectile().getFileName()));
//				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setIcon(img_prjct_enemy);
			}	
		}
		
//		lbl_prjct_player = new JLabel();
//		prjct_player = new ProjectilePlayer(lbl_prjct_player, myPlayer, lbl_currentScore, myUFO);
		playerProjectile = new ProjectilePlayer(player, ufo);
//		img_prjct_player = new ImageIcon(getClass().getResource(prjct_player.getFileName()));
//		lbl_prjct_player.setIcon(img_prjct_player);
//		lbl_prjct_player.setSize(prjct_player.getWidth(), prjct_player.getHeight());
//		prjct_player.setLbl_prjct_player(lbl_prjct_player);
		playerProjectile.setEnemies(enemies);
		
		gameHandler = new GameHandler(player, playerProjectile, enemies, ufo);

		container1 = getContentPane();
		container1.setBackground(Color.black);
		setLayout(null);
		
		//Set object coordinates:
		player.setX((GameProperties.SCREEN_WIDTH/2) - player.getWidth());
		player.setY(GameProperties.SCREEN_HEIGHT - (player.getHeight() * 2));
		
		ufo.setX(0 - ufo.getWidth());
		ufo.setY(5);
		
		playerProjectile.setX(player.getX() + (playerProjectile.getWidth()/2));
		playerProjectile.setY(player.getY());
		
		//Update lbl positions to match stored values:
//		lbl_score.setLocation(15, 15);
//		lbl_currentScore.setLocation(15, (15 + GameProperties.SCORE_TXT_SIZE));
//		lbl_player.setLocation(myPlayer.getX(), myPlayer.getY());
//		lbl_UFO.setLocation(myUFO.getX(), myUFO.getY());
		
		int offset_playerLives = 0;
//		for(int i = 0; i < lbl_playerLives.length; i++) {
//			lbl_playerLives[i].setLocation(offset_playerLives, (GameProperties.SCREEN_HEIGHT - 70));
//			offset_playerLives += 30;
//		}
		
//		lbl_prjct_player.setLocation(prjct_player.getX(), prjct_player.getY());
		
		int offset_enemyX = 0;
		int offset_enemyY = 0;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			offset_enemyX = 0;
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
//				enemies[i][j].getLbl_enemy().setLocation(enemies[i][j].getX() + offset_enemyX, enemies[i][j].getY() + offset_enemyY);
//				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setLocation(enemies[i][j].getEnemyProjectile().getX(), enemies[i][j].getEnemyProjectile().getY());
				offset_enemyX += (enemies[i][j].getWidth() + GameProperties.ENEMY_SPACING);
				if(j == (GameProperties.ENEMY_COLS)) {
					offset_enemyY += enemies[i][j].getHeight();
				}
			}
		}
				
		//Add objects to screen:
//		add(lbl_score);
//		add(lbl_currentScore);
//		add(lbl_player);
//		add(lbl_UFO);
//		
//		for(int i = 0; i < lbl_playerLives.length; i++) {
//			this.add(lbl_playerLives[i]);
//		}
//		
//		add(lbl_prjct_player);
		
//		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
//			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
//				this.add(enemies[i][j].getLbl_enemy()).setVisible(enemies[i][j].getVisible());
//				this.add(enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy()).setVisible(false);
//			}
//		}
		
//		lbl_prjct_player.setVisible(false);
		
//		container1.addKeyListener(this);
		container1.setFocusable(true);
		
		// Thread to check if game win or lose conditions met:
		Thread thread_main = new Thread (new Runnable() {
			public void run() {
				synchronized(this) {
					Boolean continueThread = true;
					while(continueThread) {
						for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
							for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
								if(enemies[i][j].getInMotion() && enemies[i][j].getGameOver()) {
									gameHandler.gameOverRoutine();
									try {
										Socket s1 = new Socket("localhost", CLIENT_PORT);
										
										// Initialize data stream to send data out
										OutputStream outstream = s1.getOutputStream();
										PrintWriter out = new PrintWriter(outstream);
	
										// Reply with following command to ClientService:
										String commandOut = "SEND_GAMEOVER " + gameHandler.getGameOver() + "\n";
										System.out.println("Sending: " + commandOut);
										out.println(commandOut);
										out.flush();
											
										s1.close();
									}
									catch(Exception e) {
										e.printStackTrace();
									}
									continueThread = false;
									break;
								}
								
								if(enemies[i][j].getInMotion() && enemies[i][j].getEnemyProjectile().getGameOver()) {
									gameHandler.gameOverRoutine();	
									try {
										Socket s1 = new Socket("localhost", CLIENT_PORT);
										
										// Initialize data stream to send data out
										OutputStream outstream = s1.getOutputStream();
										PrintWriter out = new PrintWriter(outstream);
	
										// Reply with following command to ClientService:
										String commandOut = "SEND_GAMEOVER\n";
										System.out.println("Sending: " + commandOut);
										out.println(commandOut);
										out.flush();
											
										s1.close();
									}
									catch(Exception e) {
										e.printStackTrace();
									}
									continueThread = false;
									break;
								}
								
								if(playerProjectile.getInvasionStopped()) {
									gameHandler.gameWonRoutine();
									try {
										Socket s1 = new Socket("localhost", CLIENT_PORT);
										
										// Initialize data stream to send data out
										OutputStream outstream = s1.getOutputStream();
										PrintWriter out = new PrintWriter(outstream);
	
										// Reply with following command to ClientService:
										String commandOut = "SEND_INVASION_STOPPED\n";
										System.out.println("Sending: " + commandOut);
										out.println(commandOut);
										out.flush();
											
										s1.close();
									}
									catch(Exception e) {
										e.printStackTrace();
									}
									continueThread = false;
									break;
								}
							}				
						}
					}
				}
			}
		});
		thread_main.start();
		
		// Thread #1 - Server to Listen for Commands from Client
		Thread thread_server = new Thread (new Runnable() {
			public void run() {
				synchronized(this) {
					try {
						final int SERVER_PORT = 5557;
						ServerSocket server = new ServerSocket(SERVER_PORT);
						System.out.println("Waiting for clients to connect...");
						while(true) {
							Socket s = server.accept();
							System.out.println("client connected");
							
							ServerService sService = new ServerService (s, player, playerProjectile, enemies, ufo, gameHandler);
							Thread t = new Thread(sService);
							t.start();
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread_server.start();
		
		
		
		// Object threads that originally ran on client-side:
		// Thread #2 - Player Projectile Thread
			// Starts when space key pressed (doesn't start automatically)

		// Thread #3 - Enemy (array) Thread
		// Start thread for each enemy & its projectile:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j].moveEnemy();
				// If enemy can shoot (in bottom row), start its projectile thread:
				if(enemies[i][j].getCanShoot()) {
					// Thread #4 - Enemy Projectile Thread
					enemies[i][j].getEnemyProjectile().startEnemyProjectileThread();
				}
			}
		}
		
		// Thread #5 - UFO Thread
		// Start UFO thread:
		ufo.startUFOThread();
		
		//Action upon hitting close button:
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
	} // End GameScreen constructor

	
//=====================================================================================================
//	PROGRAM MAIN
//=====================================================================================================
	
	public static void main(String args[]) { 
		GameScreen1 myGameScreen = new GameScreen1();
//		myGameScreen.setVisible(true); 
	}
	
//=====================================================================================================
//	KEYBOARD CONTROLS
//=====================================================================================================
	
//	@Override
//	public void keyTyped(KeyEvent e) {
//		//Key press & release treated as one
//	}
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		//Key press only
//		int player_x = myPlayer.getX();
//		int player_y = myPlayer.getY();
//		int enemy_x = 0;
//		int enemy_y = 0;
//		
//		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
//			if((myPlayer.getCanMove() == true) && (player_x - GameProperties.PLAYER_STEP) > 0) {
//				player_x -= GameProperties.PLAYER_STEP;
//			}
//		}
//		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
//			if((myPlayer.getCanMove() == true) && (player_x + myPlayer.getWidth() + GameProperties.PLAYER_STEP) < GameProperties.SCREEN_WIDTH) {
//				player_x += GameProperties.PLAYER_STEP;
//			}
//		}
//		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {//Launch player projectile
//				if((myPlayer.getCanMove() == true) && (prjct_player.getInMotion() == false)) {
//					//Shoot
//					prjct_player.launchPlayerProjectile();
//				}
//		}
//		myPlayer.setX(player_x);
////		lbl_player.setLocation(myPlayer.getX(), myPlayer.getY());
//		
//		//Update x-coordinate of player projectile ONLY IF not already in motion:
//		if(prjct_player.getInMotion() == false) {
//			prjct_player.setX(player_x + (prjct_player.getHeight()/2));
//			prjct_player.setY(player_y);
////			lbl_prjct_player.setLocation(prjct_player.getX(), prjct_player.getY());
//		}
//
//		//Update x-coordinate of enemy projectile ONLY IF not already in motion:
//		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
//			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
//				enemy_x = enemies[i][j].getX();
//				enemy_y = enemies[i][j].getY();
//
//				if(enemies[i][j].getEnemyProjectile().getInMotion() == false) {
//					enemies[i][j].getEnemyProjectile().setX(enemy_x);
//					enemies[i][j].getEnemyProjectile().setY(enemy_y);
//				}
//			}
//		}
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//		//Key release only	
//	}	
	
} // End GameScreen1 Class
