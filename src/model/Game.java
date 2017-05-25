package model;

import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

public class Game extends Observable {

	private OpponentPool op;
	private Rocket rocket;
	private boolean playing;
	private int score;
	private long time;

	// TODO: implement other attributes later

	public Game() {
		rocket = Rocket.getInstance();
		op = OpponentPool.getInstance();
	}

	public OpponentPool getOp() {
		return op;
	}

	public Rocket getRocket() {
		return rocket;
	}

	public int getScore() {
		return score;
	}

	public long getTime() {
		return time;
	}
	
	public void end() {
		playing = false;
	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean moveRocketDown(int rocketSize, int upperBound) {
		if (rocket.getY() + rocketSize >= upperBound)
			return false;

		rocket.move(rocketSize);
		return true;
	}

	public boolean moveRocketUp(int rocketSize, int lowerBound) {
		if (rocket.getY() <= lowerBound)
			return false;

		rocket.move((-1) * rocketSize);
		return true;
	}

	public boolean isRocketHitOpponent() {
		for(Opponent opList: op.getOpponents()) {
			if( Math.abs(rocket.getX() - opList.getX()) < 64 && rocket.getY() == opList.getY() )
				return true;
		}

		return false;
	}

	public void startGame() {
		playing = true;
		Thread thread = new Thread() {
			@Override
			public void run() {
				long i = 0;
				time = 0;
				while (isPlaying()) {
					rocket.getBulletPool().move();
					if (i % 10 == 3)
						rocket.shoot();
					op.move();
					if (i % 30 == 3)
						op.launch();
					time += 40;
					
					if (isRocketHitOpponent()) {
						end();
						JOptionPane.showMessageDialog(null, "Game Over!");
						break;
					}

					// TODO: other tasks later

					i++;
					setChanged();
					notifyObservers();
					
					try {
						Thread.sleep(40);
					} catch (Exception e) {
					}
				}
			}
		};
		thread.start();
	}

	public class GameData {

		private int[][] rocketBulletPos;
		private int rocketY;
		private long currTime;

	}

	private class ReplayData {

		List<GameData> datas;

	}

}