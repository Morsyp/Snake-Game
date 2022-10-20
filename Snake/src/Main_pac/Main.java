package Main_pac;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Main extends JComponent implements ActionListener{

	public static int gameSpeed = 3; // Iteration per second
	
	public static final int WIDTH = 600, HEIGHT = 400;
	private static int[] snakeX = new int[(WIDTH * HEIGHT)/2], snakeY = new int[(WIDTH * HEIGHT)/2];
	private static int appleX, appleY;
	private static char snakeDir = 'R'; // R -- right | L -- left | U -- up | D -- down
	private static int snakeLength = 2;
	private static boolean gameOver = false;
	private static int dotScale = 25;
	private static int score = 0;
	
	Timer t = new Timer(1000 / gameSpeed, this);
	Random r = new Random(); // For new apple pos
	static JFrame frame;
	Font font = new Font("Dialog", Font.BOLD, 23);
	Font smallFont = new Font("Dialog", Font.ROMAN_BASELINE, 13);
	
	public static void main(String[] args) {
		frame = new JFrame("Snake");

		Main mainc = new Main();
		frame.setVisible(true);
		frame.setSize(WIDTH+dotScale/2, HEIGHT+dotScale * 3/2);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mainc);
		frame.addKeyListener(new MyKeyAdapter());
		
		setStartSnakePos();
	}

	public void paint(Graphics g) {
		// Background
		g.setColor(new Color(0, 128, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Snake
		for (int i = 0; i < snakeLength; i++) {
			if (i != 0)
				g.setColor(new Color(0, 198, 0));
			else
				g.setColor(new Color(0, 233, 0));
			g.fillRect(snakeX[i], snakeY[i], dotScale, dotScale);
		}

		// Apple
		g.setColor(new Color(244, 0, 0));
		g.fillOval(appleX, appleY, dotScale, dotScale);

		// Score
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("Score: " + score, 0, 23);
		
		if (gameOver)
			gameOver(g);
	
		t.start();
	}

	// Main loop
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		checkColisions();
		checkEatApple();
		snakeMove();
	}

	public static void setStartSnakePos() {
		snakeLength = 2;

		// Start snake pos
		snakeX[0] = 8 * dotScale;
		snakeX[1] = snakeX[0] - dotScale;
		snakeY[0] = 7 * dotScale;
		snakeY[1] = 7 * dotScale;

		// Start apple pos
		appleX = 14 * dotScale;
		appleY = 7 * dotScale;
	}

	public void checkColisions() {
		// Check snake colide with walls
		if (snakeX[0] < 0 | snakeX[0] >= WIDTH | snakeY[0] < 0 | snakeY[0] >= HEIGHT)
			gameOver = true;
		// Check snake colide with self
		for (int si = 1; si < snakeLength; si++)
			if (snakeX[0] == snakeX[si] & snakeY[0] == snakeY[si])
				gameOver = true;
	}

	public void checkEatApple() {
		// Cheak apple
		if (snakeX[0] == appleX & snakeY[0] == appleY) {
			snakeLength++;
			score++;

			// New apple pos
			appleX = r.nextInt(WIDTH / dotScale) * dotScale;
			appleY = r.nextInt(HEIGHT / dotScale) * dotScale;
		}
	}

	public void snakeMove() {
		// Snake move
		for (int si = snakeLength; si > 0; si--) {
			snakeX[si] = snakeX[si - 1];
			snakeY[si] = snakeY[si - 1];
		}

		// Snake head(move)
		switch (snakeDir) {
		case 'R':
			snakeX[0] += dotScale;
			break;
		case 'L':
			snakeX[0] -= dotScale;
			break;
		case 'U':
			snakeY[0] -= dotScale;
			break;
		case 'D':
			snakeY[0] += dotScale;
			break;
		}
	}

	public void gameOver(Graphics g) {
		// Black bg
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//game over title
		g.setFont(font);
		g.setColor(Color.GREEN);
		g.drawString("GAME OVER", (WIDTH - getFontMetrics(g.getFont()).stringWidth("GAME OVER"))/2, HEIGHT/2);
		
		//End score title
		g.setFont(smallFont);
		g.drawString("End Score: " + score, (WIDTH - getFontMetrics(g.getFont()).stringWidth("End Score: " + score))/2, HEIGHT * 2/3);

		t.stop();
	}
	
	public static void restart() {
		setStartSnakePos();
		score = 0;
		snakeLength = 2;
		snakeDir = 'R';
		gameOver = false;
	}
	
	public static class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			//movement
			if (snakeDir != 'D') if (e.getKeyCode() == KeyEvent.VK_W | e.getKeyCode() == KeyEvent.VK_UP) snakeDir = 'U';
			if (snakeDir != 'U') if (e.getKeyCode() == KeyEvent.VK_S | e.getKeyCode() == KeyEvent.VK_DOWN) snakeDir = 'D';
			if (snakeDir != 'R') if (e.getKeyCode() == KeyEvent.VK_A | e.getKeyCode() == KeyEvent.VK_LEFT) snakeDir = 'L';
			if (snakeDir != 'L') if (e.getKeyCode() == KeyEvent.VK_D | e.getKeyCode() == KeyEvent.VK_RIGHT) snakeDir = 'R';
			
			//restart
			if (e.getKeyCode() == KeyEvent.VK_R) restart();
		}
	}
}
