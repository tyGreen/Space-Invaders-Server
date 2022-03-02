//===================================================================================================
// SERVER-SIDE
//===================================================================================================

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// Command-processing Service on the Server-side
 // (Processes commands from Client)

public class ServerService implements Runnable{

	private GameHandler gameHandler;
	
	// Server
	final int CLIENT_PORT = 5555;
	private Socket s;
	private Scanner in;
	
	// Sprites
	private Player player;
	private ProjectilePlayer playerProjectile;
	private Enemy[][] enemies;
	private ProjectileEnemy projectileEnemy;
	private UFO ufo;
	
	// Constructor
	public ServerService(Socket s, Player player, ProjectilePlayer playerProjectile, Enemy[][] enemies, UFO ufo, GameHandler gameHandler) {
		this.s = s;
		this.player = player;
		this.playerProjectile = playerProjectile;
		this.enemies = enemies;
		this.ufo = ufo;
		this.gameHandler = gameHandler;
	}
	
	@Override
	public void run() {
		try {
			in = new Scanner(s.getInputStream());
			processRequest();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				s.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	private void processRequest() throws IOException {
		while(true) {
			if(!in.hasNext()) {
				return;
			}
			
			String command = in.next(); // Gets first word in string and stores in var "command"
			
			if(command.equals("QUIT")) {
				return;
			}
			else {
				executeCommand(command);
			}
		}
	}
	
	// Bulk of work done here
	public void executeCommand(String command) throws IOException {
		if(command.equals("UPDATE_PLAYER")) {
			// Get player x,y 
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_PLAYER_LBL " + player.getX() + " " + player.getY() + " " + player.getCanMove() +"\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s2.close();
		}
		else if(command.equals("UPDATE_PLAYER_PROJECTILE")) {
			// Get player x,y 
			Socket s3 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s3.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_PLAYER_PROJECTILE_LBL " + playerProjectile.getX() + " " + playerProjectile.getY() +  " " + playerProjectile.getInMotion() + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s3.close();
		}
		else if(command.equals("UPDATE_ENEMIES")) {
			
			// Get player x,y 
			Socket s4 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s4.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
			
			/*
			String commandOut = "UPDATE_ENEMY_LBLS";
			for (int i=0; i<5; i++) {
				for (int j=0; j<11; j++) {
					commandOut += " "+enemies[i][j].getX() + " " + enemies[i][j].getY() + " " + enemies[i][j].getInMotion();
				}
			}
			commandOut += "\n";
			*/

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_ENEMY_LBLS " 
								// Row 1
								+ enemies[0][0].getX() + " " + enemies[0][0].getY() + " " + enemies[0][0].getInMotion() + " "
								+ enemies[0][1].getX() + " " + enemies[0][1].getY() + " " + enemies[0][1].getInMotion() + " "
								+ enemies[0][2].getX() + " " + enemies[0][2].getY() + " " + enemies[0][2].getInMotion() + " "
								+ enemies[0][3].getX() + " " + enemies[0][3].getY() + " " + enemies[0][3].getInMotion() + " "
								+ enemies[0][4].getX() + " " + enemies[0][4].getY() + " " + enemies[0][4].getInMotion() + " "
								+ enemies[0][5].getX() + " " + enemies[0][5].getY() + " " + enemies[0][5].getInMotion() + " "
								+ enemies[0][6].getX() + " " + enemies[0][6].getY() + " " + enemies[0][6].getInMotion() + " "
								+ enemies[0][7].getX() + " " + enemies[0][7].getY() + " " + enemies[0][7].getInMotion() + " "
								+ enemies[0][8].getX() + " " + enemies[0][8].getY() + " " + enemies[0][8].getInMotion() + " "
								+ enemies[0][9].getX() + " " + enemies[0][9].getY() + " " + enemies[0][9].getInMotion() + " "
								+ enemies[0][10].getX() + " " + enemies[0][10].getY() + " " + enemies[0][10].getInMotion() + " "
								// Row 2
								+ enemies[1][0].getX() + " " + enemies[1][0].getY() + " " + enemies[1][0].getInMotion() + " "
								+ enemies[1][1].getX() + " " + enemies[1][1].getY() + " " + enemies[1][1].getInMotion() + " "
								+ enemies[1][2].getX() + " " + enemies[1][2].getY() + " " + enemies[1][2].getInMotion() + " "
								+ enemies[1][3].getX() + " " + enemies[1][3].getY() + " " + enemies[1][3].getInMotion() + " "
								+ enemies[1][4].getX() + " " + enemies[1][4].getY() + " " + enemies[1][4].getInMotion() + " "
								+ enemies[1][5].getX() + " " + enemies[1][5].getY() + " " + enemies[1][5].getInMotion() + " "
								+ enemies[1][6].getX() + " " + enemies[1][6].getY() + " " + enemies[1][6].getInMotion() + " "
								+ enemies[1][7].getX() + " " + enemies[1][7].getY() + " " + enemies[1][7].getInMotion() + " "
								+ enemies[1][8].getX() + " " + enemies[1][8].getY() + " " + enemies[1][8].getInMotion() + " "
								+ enemies[1][9].getX() + " " + enemies[1][9].getY() + " " + enemies[1][9].getInMotion() + " "
								+ enemies[1][10].getX() + " " + enemies[1][10].getY() + " " + enemies[1][10].getInMotion() + " "
								// Row 3
								+ enemies[2][0].getX() + " " + enemies[2][0].getY() + " " + enemies[2][0].getInMotion() + " "
								+ enemies[2][1].getX() + " " + enemies[2][1].getY() + " " + enemies[2][1].getInMotion() + " "
								+ enemies[2][2].getX() + " " + enemies[2][2].getY() + " " + enemies[2][2].getInMotion() + " "
								+ enemies[2][3].getX() + " " + enemies[2][3].getY() + " " + enemies[2][3].getInMotion() + " "
								+ enemies[2][4].getX() + " " + enemies[2][4].getY() + " " + enemies[2][4].getInMotion() + " "
								+ enemies[2][5].getX() + " " + enemies[2][5].getY() + " " + enemies[2][5].getInMotion() + " "
								+ enemies[2][6].getX() + " " + enemies[2][6].getY() + " " + enemies[2][6].getInMotion() + " "
								+ enemies[2][7].getX() + " " + enemies[2][7].getY() + " " + enemies[2][7].getInMotion() + " "
								+ enemies[2][8].getX() + " " + enemies[2][8].getY() + " " + enemies[2][8].getInMotion() + " "
								+ enemies[2][9].getX() + " " + enemies[2][9].getY() + " " + enemies[2][9].getInMotion() + " "
								+ enemies[2][10].getX() + " " + enemies[2][10].getY() + " " + enemies[2][10].getInMotion() + " "
								// Row 4
								+ enemies[3][0].getX() + " " + enemies[3][0].getY() + " " + enemies[3][0].getInMotion() + " "
								+ enemies[3][1].getX() + " " + enemies[3][1].getY() + " " + enemies[3][1].getInMotion() + " "
								+ enemies[3][2].getX() + " " + enemies[3][2].getY() + " " + enemies[3][2].getInMotion() + " "
								+ enemies[3][3].getX() + " " + enemies[3][3].getY() + " " + enemies[3][3].getInMotion() + " "
								+ enemies[3][4].getX() + " " + enemies[3][4].getY() + " " + enemies[3][4].getInMotion() + " "
								+ enemies[3][5].getX() + " " + enemies[3][5].getY() + " " + enemies[3][5].getInMotion() + " "
								+ enemies[3][6].getX() + " " + enemies[3][6].getY() + " " + enemies[3][6].getInMotion() + " "
								+ enemies[3][7].getX() + " " + enemies[3][7].getY() + " " + enemies[3][7].getInMotion() + " "
								+ enemies[3][8].getX() + " " + enemies[3][8].getY() + " " + enemies[3][8].getInMotion() + " "
								+ enemies[3][9].getX() + " " + enemies[3][9].getY() + " " + enemies[3][9].getInMotion() + " "
								+ enemies[3][10].getX() + " " + enemies[3][10].getY() + " " + enemies[3][10].getInMotion() + " "
								// Row 5
								+ enemies[4][0].getX() + " " + enemies[4][0].getY() + " " + enemies[4][0].getInMotion() + " "
								+ enemies[4][1].getX() + " " + enemies[4][1].getY() + " " + enemies[4][1].getInMotion() + " "
								+ enemies[4][2].getX() + " " + enemies[4][2].getY() + " " + enemies[4][2].getInMotion() + " "
								+ enemies[4][3].getX() + " " + enemies[4][3].getY() + " " + enemies[4][3].getInMotion() + " "
								+ enemies[4][4].getX() + " " + enemies[4][4].getY() + " " + enemies[4][4].getInMotion() + " "
								+ enemies[4][5].getX() + " " + enemies[4][5].getY() + " " + enemies[4][5].getInMotion() + " "
								+ enemies[4][6].getX() + " " + enemies[4][6].getY() + " " + enemies[4][6].getInMotion() + " "
								+ enemies[4][7].getX() + " " + enemies[4][7].getY() + " " + enemies[4][7].getInMotion() + " "
								+ enemies[4][8].getX() + " " + enemies[4][8].getY() + " " + enemies[4][8].getInMotion() + " "
								+ enemies[4][9].getX() + " " + enemies[4][9].getY() + " " + enemies[4][9].getInMotion() + " "
								+ enemies[4][10].getX() + " " + enemies[4][10].getY() + " " + enemies[4][10].getInMotion() + " "
								+ "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s4.close();	
		}
		else if(command.equals("UPDATE_ENEMY_PROJECTILES")) {
			// Get player x,y 
			Socket s5 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s5.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_ENEMY_PROJECTILE_LBLS "
								// Row 1
								+ enemies[0][0].getEnemyProjectile().getX() + " " + enemies[0][0].getEnemyProjectile().getY() + " " + enemies[0][0].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][1].getEnemyProjectile().getX() + " " + enemies[0][1].getEnemyProjectile().getY() + " " + enemies[0][1].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][2].getEnemyProjectile().getX() + " " + enemies[0][2].getEnemyProjectile().getY() + " " + enemies[0][2].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][3].getEnemyProjectile().getX() + " " + enemies[0][3].getEnemyProjectile().getY() + " " + enemies[0][3].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][4].getEnemyProjectile().getX() + " " + enemies[0][4].getEnemyProjectile().getY() + " " + enemies[0][4].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][5].getEnemyProjectile().getX() + " " + enemies[0][5].getEnemyProjectile().getY() + " " + enemies[0][5].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][6].getEnemyProjectile().getX() + " " + enemies[0][6].getEnemyProjectile().getY() + " " + enemies[0][6].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][7].getEnemyProjectile().getX() + " " + enemies[0][7].getEnemyProjectile().getY() + " " + enemies[0][7].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][8].getEnemyProjectile().getX() + " " + enemies[0][8].getEnemyProjectile().getY() + " " + enemies[0][8].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][9].getEnemyProjectile().getX() + " " + enemies[0][9].getEnemyProjectile().getY() + " " + enemies[0][9].getEnemyProjectile().getInMotion() + " "
								+ enemies[0][10].getEnemyProjectile().getX() + " " + enemies[0][10].getEnemyProjectile().getY() + " " + enemies[0][10].getEnemyProjectile().getInMotion() + " "
								// Row 2
								+ enemies[1][0].getEnemyProjectile().getX() + " " + enemies[1][0].getEnemyProjectile().getY() + " " + enemies[1][0].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][1].getEnemyProjectile().getX() + " " + enemies[1][1].getEnemyProjectile().getY() + " " + enemies[1][1].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][2].getEnemyProjectile().getX() + " " + enemies[1][2].getEnemyProjectile().getY() + " " + enemies[1][2].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][3].getEnemyProjectile().getX() + " " + enemies[1][3].getEnemyProjectile().getY() + " " + enemies[1][3].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][4].getEnemyProjectile().getX() + " " + enemies[1][4].getEnemyProjectile().getY() + " " + enemies[1][4].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][5].getEnemyProjectile().getX() + " " + enemies[1][5].getEnemyProjectile().getY() + " " + enemies[1][5].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][6].getEnemyProjectile().getX() + " " + enemies[1][6].getEnemyProjectile().getY() + " " + enemies[1][6].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][7].getEnemyProjectile().getX() + " " + enemies[1][7].getEnemyProjectile().getY() + " " + enemies[1][7].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][8].getEnemyProjectile().getX() + " " + enemies[1][8].getEnemyProjectile().getY() + " " + enemies[1][8].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][9].getEnemyProjectile().getX() + " " + enemies[1][9].getEnemyProjectile().getY() + " " + enemies[1][9].getEnemyProjectile().getInMotion() + " "
								+ enemies[1][10].getEnemyProjectile().getX() + " " + enemies[1][10].getEnemyProjectile().getY() + " " + enemies[1][10].getEnemyProjectile().getInMotion() + " "
								// Row 3
								+ enemies[2][0].getEnemyProjectile().getX() + " " + enemies[2][0].getEnemyProjectile().getY() + " " + enemies[2][0].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][1].getEnemyProjectile().getX() + " " + enemies[2][1].getEnemyProjectile().getY() + " " + enemies[2][1].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][2].getEnemyProjectile().getX() + " " + enemies[2][2].getEnemyProjectile().getY() + " " + enemies[2][2].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][3].getEnemyProjectile().getX() + " " + enemies[2][3].getEnemyProjectile().getY() + " " + enemies[2][3].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][4].getEnemyProjectile().getX() + " " + enemies[2][4].getEnemyProjectile().getY() + " " + enemies[2][4].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][5].getEnemyProjectile().getX() + " " + enemies[2][5].getEnemyProjectile().getY() + " " + enemies[2][5].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][6].getEnemyProjectile().getX() + " " + enemies[2][6].getEnemyProjectile().getY() + " " + enemies[2][6].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][7].getEnemyProjectile().getX() + " " + enemies[2][7].getEnemyProjectile().getY() + " " + enemies[2][7].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][8].getEnemyProjectile().getX() + " " + enemies[2][8].getEnemyProjectile().getY() + " " + enemies[2][8].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][9].getEnemyProjectile().getX() + " " + enemies[2][9].getEnemyProjectile().getY() + " " + enemies[2][9].getEnemyProjectile().getInMotion() + " "
								+ enemies[2][10].getEnemyProjectile().getX() + " " + enemies[2][10].getEnemyProjectile().getY() + " " + enemies[2][10].getEnemyProjectile().getInMotion() + " "
								// Row 4
								+ enemies[3][0].getEnemyProjectile().getX() + " " + enemies[3][0].getEnemyProjectile().getY() + " " + enemies[3][0].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][1].getEnemyProjectile().getX() + " " + enemies[3][1].getEnemyProjectile().getY() + " " + enemies[3][1].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][2].getEnemyProjectile().getX() + " " + enemies[3][2].getEnemyProjectile().getY() + " " + enemies[3][2].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][3].getEnemyProjectile().getX() + " " + enemies[3][3].getEnemyProjectile().getY() + " " + enemies[3][3].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][4].getEnemyProjectile().getX() + " " + enemies[3][4].getEnemyProjectile().getY() + " " + enemies[3][4].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][5].getEnemyProjectile().getX() + " " + enemies[3][5].getEnemyProjectile().getY() + " " + enemies[3][5].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][6].getEnemyProjectile().getX() + " " + enemies[3][6].getEnemyProjectile().getY() + " " + enemies[3][6].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][7].getEnemyProjectile().getX() + " " + enemies[3][7].getEnemyProjectile().getY() + " " + enemies[3][7].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][8].getEnemyProjectile().getX() + " " + enemies[3][8].getEnemyProjectile().getY() + " " + enemies[3][8].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][9].getEnemyProjectile().getX() + " " + enemies[3][9].getEnemyProjectile().getY() + " " + enemies[3][9].getEnemyProjectile().getInMotion() + " "
								+ enemies[3][10].getEnemyProjectile().getX() + " " + enemies[3][10].getEnemyProjectile().getY() + " " + enemies[3][10].getEnemyProjectile().getInMotion() + " "
								// Row 5
								+ enemies[4][0].getEnemyProjectile().getX() + " " + enemies[4][0].getEnemyProjectile().getY() + " " + enemies[4][0].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][1].getEnemyProjectile().getX() + " " + enemies[4][1].getEnemyProjectile().getY() + " " + enemies[4][1].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][2].getEnemyProjectile().getX() + " " + enemies[4][2].getEnemyProjectile().getY() + " " + enemies[4][2].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][3].getEnemyProjectile().getX() + " " + enemies[4][3].getEnemyProjectile().getY() + " " + enemies[4][3].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][4].getEnemyProjectile().getX() + " " + enemies[4][4].getEnemyProjectile().getY() + " " + enemies[4][4].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][5].getEnemyProjectile().getX() + " " + enemies[4][5].getEnemyProjectile().getY() + " " + enemies[4][5].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][6].getEnemyProjectile().getX() + " " + enemies[4][6].getEnemyProjectile().getY() + " " + enemies[4][6].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][7].getEnemyProjectile().getX() + " " + enemies[4][7].getEnemyProjectile().getY() + " " + enemies[4][7].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][8].getEnemyProjectile().getX() + " " + enemies[4][8].getEnemyProjectile().getY() + " " + enemies[4][8].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][9].getEnemyProjectile().getX() + " " + enemies[4][9].getEnemyProjectile().getY() + " " + enemies[4][9].getEnemyProjectile().getInMotion() + " "
								+ enemies[4][10].getEnemyProjectile().getX() + " " + enemies[4][10].getEnemyProjectile().getY() + " " + enemies[4][10].getEnemyProjectile().getInMotion() + " "
								+"\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s5.close();	
		}
		else if(command.equals("UPDATE_UFO")) {
			// Open socket to client
			Socket s6 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s6.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_UFO_LBL " + ufo.getX() + " " + ufo.getY() + " " + ufo.getInMotion() + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s6.close();	
		}
		else if(command.equals("MOVE_PLAYER_LEFT")) {
			// Update player's x coordinate in response to client command
			player.setX(player.getX() - GameProperties.PLAYER_STEP);
			
			// Send a response
				// In response to client's command to move player left, send command back to
				// client service to update coordinates of player label
			Socket s7 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s7.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_PLAYER_LBL " + player.getX() + " " + player.getY() + " " + player.getCanMove() + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s7.close();
		}
		else if(command.equals("MOVE_PLAYER_RIGHT")) {
			// Update player's x coordinate in response to client command
			player.setX(player.getX() + GameProperties.PLAYER_STEP);
			// Update player projectile's coordinates to match (if not in motion)
			if(!playerProjectile.getInMotion()) {
				playerProjectile.setX(player.getX());
				playerProjectile.setY(player.getY());
			}
			
			// Send a response
			Socket s8 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s8.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_PLAYER_LBL " + player.getX() + " " + player.getY() + " " + player.getCanMove() + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s8.close();
		}
		else if(command.equals("LAUNCH_PLAYER_PROJECTILE")) {
			// Update player's x coordinate in response to client command
			playerProjectile.launchPlayerProjectile();
			
			// Send a response
			Socket s9 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s9.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_PLAYER_PROJECTILE_LBL " + playerProjectile.getX() + " " + playerProjectile.getY() + " " + playerProjectile.getInMotion() + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s9.close();
		}
		else if(command.equals("UPDATE_SCORE")) {
			Socket s10 = new Socket("localhost", CLIENT_PORT);
			
			// Initialize data stream to send data out
			OutputStream outstream = s10.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			// Reply with following command to ClientService:
			String commandOut = "UPDATE_SCORE_LBL " + player.getPlayerScore() + "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s10.close();
		}
		
	}
	
} // End Class
