import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.swing.Timer;


public class CannonSimulator extends JPanel implements ActionListener, KeyListener {

	final int boardWidth = 1080;
	final int boardHeight = 720;
	final int cannonWidth = 150;
	final int cannonHeight = 40;
	final int pivotX = 0;
	final int pivotY = (boardHeight - cannonHeight);
	
	//Hardcoded for now, but the user should be able to change angle of the cannon
	double angle = Math.toRadians(-45); 
	
	// simulator logic
	Timer gameLoop;
	Timer runSimTimer;
	
	
	
	CannonSimulator(){
		setPreferredSize(new Dimension(boardWidth,boardHeight));
		setBackground(Color.black);
		setFocusable(true);
		addKeyListener(this);
		
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
		AffineTransform tx = new AffineTransform();
		Graphics2D cannon = (Graphics2D) g;
		
		
		tx.rotate(angle, pivotX, pivotY);
		cannon.setTransform(tx);
		cannon.setPaint(Color.white);
		cannon.fillRect(0, boardHeight - cannonHeight, cannonWidth, cannonHeight);
		
		
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		System.out.println("running");
	}
	
	public double setCannonAngle(int newAngle) {
		return Math.toRadians(-newAngle);
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		
		// angle cannon up
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            // if up key is pressed then add 5 to the cannon angle
        }
        // angle cannon down
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            //if down key is pressed subtract 5 from cannon angle
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
