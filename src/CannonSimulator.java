
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;


public class CannonSimulator extends JPanel implements ActionListener, KeyListener {

	final int boardWidth = 1750;
	final int boardHeight = 720;
	final int horizontalBoundRight = boardWidth;
	final int horizontalBoundLeft = 0;
	final int verticalBoundTop = 0;
	final int verticalBoundBottom = boardHeight;
	final int outOfBounds = -100;
	final int cannonWidth = 150;
	final int cannonHeight = 40;
	final int baseHeight = 60;
	final int baseWidth = 60;
	final int pivotX = 0;
	final int pivotY = (boardHeight - cannonHeight);
	
	// default cannon attributes
	int cannonAngle = -45;
	final int maxAngle = -90;
	final int minAngle = 0;
	int cannonSpeed = 20;
	final int minSpeed = 5;
	final int maxSpeed = 50;
	
	
	// simulator logic
	ArrayList<CannonBall> cannonBalls;
	Timer gameLoop;
	Timer runSimTimer;
	
	int velocity = 0;
	int acceleration = 2;
	double gravity = 0.3;
	
	// sound
	Sound sound = new Sound();
	
	
	
	CannonSimulator(){
		setPreferredSize(new Dimension(boardWidth,boardHeight));
		setBackground(Color.black);
		setFocusable(true);
		addKeyListener(this);
		cannonBalls = new ArrayList<>();
		
		runSimTimer = new Timer(1500,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		
		runSimTimer.start();
		
		//game timer
		gameLoop = new Timer(1000/60,this);
		gameLoop.start();
		playMusic(0);
		
	}
	
	//transform with AffineTransformations
	public void draw(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform originalTransform = g2d.getTransform();
		
		// rotatable cannon 
		AffineTransform tx = new AffineTransform();
		tx.rotate(setCannonAngle(cannonAngle), pivotX, pivotY);
		g2d.setTransform(tx);
		g2d.setPaint(Color.gray);
		g2d.fillRect(0, boardHeight - cannonHeight, cannonWidth, cannonHeight);
		
		//this makes it so the base does not rotate
		g2d.setTransform(originalTransform);
		
		// cannon base
		g2d.setPaint(Color.ORANGE);
		g2d.fillRect(0, boardHeight - baseHeight, baseWidth, baseHeight);
		
		
		
		removeOutOfBounds();
		
		for (CannonBall cb : cannonBalls) {
		    cb.update();
		    cb.draw((Graphics2D) g);
		}
		
	    // display cannon speed
	    g2d.setColor(Color.WHITE);
	    g2d.setFont(new Font("Arial", Font.BOLD, 25));
	    g2d.drawString("Cannon Speed: " + cannonSpeed, 10, 30);
	    
	    // display cannon angle
	    g2d.drawString("Cannon Angle: " + (-cannonAngle) + "°", 10, 60);
	    
	   
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		//System.out.println(cannonAngle);
	}
	
	public double setCannonAngle(int newAngle) {
		return Math.toRadians(newAngle);
	}
	
	class CannonBall{
		int x;
		int y;
		int radius = 30;
		int bottom;
		int top;
		int right;
		int left;
		boolean isOutOfBounds = false;
		double velocityX = velocity;
		double velocityY = velocity;
		
		
		CannonBall() {
		        this.x = boardWidth / 2;
		        this.y = 0;
		    }
		
		public void update() {
			
			checkOutOfBounds();
			checkCollision();
			
			x+=velocityX;
			y+=velocityY;
			
			// these track the positions of the edges of the cannonball for collision checking
			bottom = y + radius;
			top = y;
			left = x;
			right = x+radius;
			
			velocityY+=gravity;
			
			System.out.println("X: "+x+" Y: "+ y);
			//System.out.println(isOutOfBounds);
		}
		public void checkCollision() {
			
			if(bottom >= verticalBoundBottom) {
				velocityY = -velocityY/1.5;
			}
			
			if(top<verticalBoundTop) {
				// not used for now
				velocityY = -velocityY/1.5;
			}
			
			if(right >= horizontalBoundRight) {
				velocityX = -velocityX;
			}
			
			if(left <= horizontalBoundLeft) {
				//velocityX = velocityX/2;
			}
			
		}
		public void checkOutOfBounds() {
			if(x < outOfBounds) {
				isOutOfBounds = true;
			}
		}
		public void draw(Graphics2D g) {
	        g.setColor(Color.WHITE);
	        g.fillOval(x, y, radius, radius);
	    }
	}
	
	public void spawnCannonBall() {
		CannonBall newCannonBall = new CannonBall();
		double angleRad = setCannonAngle(cannonAngle);  
		
		newCannonBall.x = (int)(pivotX + Math.cos(angleRad) * cannonWidth);
	    newCannonBall.y = (int)(pivotY + Math.sin(angleRad) * cannonWidth);
	    
	    newCannonBall.velocityX = cannonSpeed * Math.cos(angleRad);
	    newCannonBall.velocityY = cannonSpeed * Math.sin(angleRad);

	    cannonBalls.add(newCannonBall);
	}
	
	public void removeOutOfBounds() {
		for (int i = cannonBalls.size() - 1; i >= 0; i--) {
	        if (cannonBalls.get(i).isOutOfBounds) {
	            cannonBalls.remove(i);
	            System.out.println("Cannon ball despawned!");
	        }
	    }
	}
	

	// check to see if any cannon ball is touching the edge of the screen or another cannonball
	/*
	public void checkCollision() {
		
		for(int i = 0; i < cannonBalls.size() - 1; i++) {
			if(cannonBalls.get(i).x==0) {
				cannonBalls.get(i).velocityY = (cannonBalls.get(i).velocityY)-((cannonBalls.get(i).velocityY)*2);
			}
		}
		
	}
	*/
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		
			// angle cannon up
			if ((!(cannonAngle == maxAngle))&&(key == KeyEvent.VK_W || key == KeyEvent.VK_UP)) {
	            // if up key is pressed then add 5 to the cannon angle
				cannonAngle = cannonAngle - 5; //subtract since cannon angle is already negative
				repaint();
	        }
	        // angle cannon down
	        if ((!(cannonAngle == minAngle))&&(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)) {
	            //if down key is pressed subtract 5 from cannon angle
	        	cannonAngle = cannonAngle + 5;
	        	repaint();
	        }
	        if(key == KeyEvent.VK_SPACE) {
	        	spawnCannonBall();
	        	playSoundEffect(1);
	        	
	        }
	        if ((!(cannonSpeed == maxSpeed))&&(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)) {
	           cannonSpeed = cannonSpeed + 5;
	        }
	        if ((!(cannonSpeed == minSpeed))&&(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)) {
		       cannonSpeed = cannonSpeed - 5;
		    }
	        
		
	        
	}
	
	public void playMusic(int i) {
		
		sound.setFile(i);
		sound.play();
		sound.loop();
		
	}
	public void playSoundEffect(int i) {
		sound.setFile(i);
		sound.play();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
