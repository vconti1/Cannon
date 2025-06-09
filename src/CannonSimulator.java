
import java.awt.Color;
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
	
	// default cannon angle
	int cannonAngle = -45;
	final int maxAngle = -90;
	final int minAngle = 0;
	
	
	// simulator logic
	ArrayList<CannonBall> cannonBalls;
	Timer gameLoop;
	Timer runSimTimer;
	
	int velocity = 0;
	int acceleration = 2;
	double gravity = 0.3;
	
	
	
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
	}
	
	//transform with AffineTransformations
	public void draw(Graphics g) {
		
		Graphics2D cannon = (Graphics2D) g;
		Graphics2D cannonBase = (Graphics2D) g;
		//Graphics2D ball = (Graphics2D) g;
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform originalTransform = g2d.getTransform();
		
		// rotatable cannon 
		AffineTransform tx = new AffineTransform();
		tx.rotate(setCannonAngle(cannonAngle), pivotX, pivotY);
		cannon.setTransform(tx);
		cannon.setPaint(Color.gray);
		cannon.fillRect(0, boardHeight - cannonHeight, cannonWidth, cannonHeight);
		
		//this makes it so the base does not rotate
		g2d.setTransform(originalTransform);
		
		// cannon base
		cannonBase.setPaint(Color.ORANGE);
		cannonBase.fillRect(0, boardHeight - baseHeight, baseWidth, baseHeight);
		
		removeOutOfBounds();
		
		for (CannonBall cb : cannonBalls) {
		    cb.update();
		    cb.draw((Graphics2D) g);
		}
	   
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
			
			//System.out.println("X: "+x+" Y: "+ y);
			//System.out.println(isOutOfBounds);
		}
		public void checkCollision() {
			
			if(bottom >= verticalBoundBottom) {
				velocityY = -velocityY/1.5;
			}
			if(top>=verticalBoundTop) {
				// not used for now
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
	    //20 is good
	    double speed = 35;
	    
	    newCannonBall.velocityX = speed * Math.cos(angleRad);
	    newCannonBall.velocityY = speed * Math.sin(angleRad);

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
	        }
		
	        
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
