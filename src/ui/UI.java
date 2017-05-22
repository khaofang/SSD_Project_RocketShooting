package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Bullet;
import model.Game;

public class UI extends JFrame implements KeyListener, Observer {
	
	private static ImageIcon II_BULLET_ROCKET = new ImageIcon("src/res/rocket_bullet.png");
	private static ImageIcon II_ENEMY = new ImageIcon("src/res/enemy.png");
	private static ImageIcon II_ROCKET = new ImageIcon("src/res/rocket.png");
	private static ImageIcon II_OBSTACLE; // Not implements resource yet
	private static int UI_WIDTH = 840;
	private static int UI_HEIGHT = 480;
	
	private JPanel panel;
	
	private List<JLabel> lblRocketBullet;
	private JLabel lblRocket;
	private JLabel lblPressToStart;
	private JLabel lblScore;
	
	// TODO: Component
	
	private Game game;

	public UI(Game game) {
		this.game = game;
		panel = (JPanel) getContentPane();
		panel.setPreferredSize(new Dimension(UI_WIDTH, UI_HEIGHT));
		panel.setBackground(new Color(186, 205, 168));
		panel.setLayout(null);
		initComponent();
		this.addKeyListener(this);
		
		lblRocketBullet = new ArrayList<JLabel>();
		for (int i = 0; i < 15; i++) {
			lblRocketBullet.add(new JLabel(II_BULLET_ROCKET));
			panel.add(lblRocketBullet.get(i));
			lblRocketBullet.get(i).setVisible(false);
		}
		
		setTitle("Rocket Shooting v0.18");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
	}
	
	public void run() {
		setVisible(true);
	}
	
	public void initComponent() {
		lblRocket = new JLabel(II_ROCKET);
		panel.add(lblRocket);
		lblRocket.setBounds(16, 240, 64, 64);
		game.getRocket().setY(240);
		
		lblPressToStart = new JLabel("< SPACE TO START >");
		lblPressToStart.setFont(new Font(lblPressToStart.getFont().toString(), Font.BOLD, 32));
		panel.add(lblPressToStart);
		lblPressToStart.setBounds(480, 240, lblPressToStart.getPreferredSize().width, lblPressToStart.getPreferredSize().height);
		
		lblScore = new JLabel(String.format("%05d", game.getScore()));
		lblScore.setFont(new Font(lblPressToStart.getFont().toString(), Font.BOLD, 32));
		panel.add(lblScore);
		lblScore.setBounds(740, 0, lblScore.getPreferredSize().width, lblScore.getPreferredSize().height);
		
		// TODO: implement more components
		
	}

	@Override
	public void update(Observable o, Object arg) {
		for (int i = 0; i < 15; i++) {
			Bullet b = game.getRocket().getBulletPool().getBullets().get(i);
			JLabel lbl = lblRocketBullet.get(i);
			if (b.isActive()) {
				lbl.setVisible(true);
				int x = b.getX();
				int y = b.getY();
				lbl.setBounds(x, y, lbl.getPreferredSize().width,lbl.getPreferredSize().height);
			} else {
				lbl.setVisible(false);
			}
		}

		System.out.println("UPDATE!");
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyPressed(KeyEvent e) {	
		System.out.println("Key is typing : " + e.getKeyCode());
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			lblPressToStart.setVisible(false);
		}
		
		if (game.isPlaying()) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				game.moveRocketUp(64, 64);
				int y = game.getRocket().getY();
				lblRocket.setBounds(16, y, 64, 64);			
			} else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				game.moveRocketDown(64, 416);
				int y = game.getRocket().getY();
				lblRocket.setBounds(16, y, 64, 64);	
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				game.startGame();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		UI ui = new UI(g);
		g.addObserver(ui);
		ui.run();
	}
	
}