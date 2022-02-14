/* Sprayer.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: boss that shoots a spiral of shots
 */

public class Sprayer extends Boss {

	public final long TIME_BETWEEN_BULLETS = 30; // the time between each bullet in a given spiral
	private long lastBulletFired = 0; // the time that the last bullet was fired
	private int bulletsFired = 0; // the amount of bullets fired in a spiral
	private boolean shooting = false; // is the boss shooting

	public Sprayer(Game g, String r) {
		super(g, r, 5000, 10, 150.0);
	} // Sprayer

	// get and set methods

	public long getLastBulletFired() {
		return this.lastBulletFired;
	} // getLastBulletFired

	public void updateLastBulletFired() {
		this.lastBulletFired = System.currentTimeMillis();
	} // updateLastBulletFired

	public boolean getShooting() {
		return this.shooting;
	} // getShooting

	// increments the amount of shots
	public void incrementShots() {
		this.bulletsFired++;

		// if 45 bullets have been shot, the boss begins moving again
		if (this.bulletsFired == 45) {
			this.shooting = false;
			this.moving = true;
			this.updateLastTimeShot();
		} // if
	} // incrementShots

	// begins the shooting process
	public void startShooting() {
		this.shooting = true;
		this.moving = false;
		this.bulletsFired = 0;
	} // startShooting

	// moves the boss if it's not shooting
	public void move(long delta) {
		if (!this.shooting) {
			this.seePlayer();
			super.move(delta);
		} // if
	} // move
} // Sprayer
