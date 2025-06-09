import javax.swing.JFrame;
import java.awt.Graphics;

public class Main {
	public static void main(String[] args) {
		int boardWidth = 1750;
		int boardHeight = 720;
		
		JFrame frame = new JFrame("Cannon Simulator");
		

		frame.setSize(boardWidth, boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		CannonSimulator buildingBlocks = new CannonSimulator();
		frame.add(buildingBlocks);
		frame.pack();
		buildingBlocks.requestFocus();
		frame.setVisible(true);
	}
}
