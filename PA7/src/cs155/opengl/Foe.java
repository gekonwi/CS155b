package cs155.opengl;

import java.util.Random;

/**
 * a foe is a box that moves around a room it has a current velocity which is
 * randomly changed by small increments without changing its speed.
 * 
 * @author tim
 * 
 */
public class Foe {

	public float[] vel = { 0f, 0f, 1f };
	public float[] pos = { 0f, 0f, 0f };

	public float speed;

	private long lastUpdateMillis = System.currentTimeMillis();

	private Random rand = new Random();
	private GameModel07 game;
	private boolean freeWill;
	
	private final float startAngle;

	public Foe(boolean freeWill, float speed, float xpos, float zpos,
			GameModel07 game, float startAngle) {
		this.startAngle = startAngle;
		
		this.game = game;
		
		this.speed = speed;
		this.freeWill = freeWill;
		this.pos[0] = xpos;
		this.pos[2] = zpos;

		double angleDif = rand.nextDouble() * 2 * Math.PI;
		rotateBy(angleDif);
	}

	public float getStartAngle() {
		return startAngle;
	}

	/**
	 * change the velocity slightly and uses it to update the position
	 */
	public void update() {
		long currentTimeMillis = System.currentTimeMillis();
		long dt = currentTimeMillis - lastUpdateMillis;
		lastUpdateMillis = currentTimeMillis;
		float velFactor = ((float) dt) / 1000f * speed;

		if (freeWill) {
			// generate angle (in radians)
			double angleDif = rand.nextGaussian() * velFactor / 5f;
			rotateBy(angleDif);
		}
			
		pos[0] += vel[0] * velFactor;
		pos[2] += vel[2] * velFactor;

		keepOnBoard();
	}

	/**
	 * 
	 * @param angleDif
	 *            in radians
	 */
	public void rotateBy(double angleDif) {
		float c1 = (float) Math.cos(angleDif);
		float s1 = (float) Math.sin(angleDif);
		float a = c1 * vel[0] - s1 * vel[2];
		float b = s1 * vel[0] + c1 * vel[2];

		vel[0] = a;
		vel[2] = b;
	}

	private void keepOnBoard() {
		if (pos[0] < 0) {
			vel[0] *= -1;
			pos[0] = 0;
		} else if (pos[0] > game.width - 1) {
			vel[0] *= -1;
			pos[0] = game.width - 1;
		}

		if (pos[2] < 0) {
			vel[2] *= -1;
			pos[2] = 0;
		} else if (pos[2] > game.height - 1) {
			vel[2] *= -1;
			pos[2] = game.height - 1;
		}
	}
}
