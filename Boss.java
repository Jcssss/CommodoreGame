/* Boss.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: bosses in the game
 */

public abstract class Boss extends EnemyEntity {

	protected long lastDropAmmo; // the last time ammo was dropped by the boss
	public final long DROP_INTERVAL = 10000; // the time between each ammo drop
	protected double moveDirection; // the direction the boss is moving, different from facing
	protected long lastTimeReset; // the last time the boss's move direction was reset
	public final long RESET_INTERVAL = 2000; // the time between each move direction reset

	public Boss(Game g, String r, long shotInt, int life, double speed) {
		super(g, r, 0, 10, shotInt, 1); // calls the constructor in Entity

		// initializes variables
		this.centerX = game.WINDOW_WIDTH / 2 - this.getWidth() / 2;
		this.shootingRange = g.WINDOW_WIDTH;
		this.moveSpeed = speed;
		this.lives = life;
		this.radius *= (this instanceof Car) ? 3 : 2;
		updateLastTimeShot();
	} // Boss

	// moves the entity, needed so that more specific movement could be made in Boss
	// subclasses
	public void move(long delta) {
		super.move(delta);
	} // move

	// decreases the bosses lives
	public void loseLife() {
		this.lives--;

		// if the boss dies, notifies death
		if (this.lives <= 0) {
			this.game.notifyEnemyKilled(this.getX(), this.getY(), (Entity) this);
			this.game.removeEntity(this);
		} // if
	} // lostLife

	protected void setMovement() {

		// if the entity is not moving, it does not move
		if (!this.moving) {
			dy = 0;
			dx = 0;
			return;
		} // if

		// calculates the x and y that an entity needs to move in order to move
		// a given distance in any direction
		dy = -this.moveSpeed * Math.sin(this.moveDirection * Math.PI / 180);
		dx = this.moveSpeed * Math.cos(this.moveDirection * Math.PI / 180);
	} // setMovement

	// get and set methods

	public void setRandomDirection() {
		this.moveDirection = this.direction + Math.random() * 90 - 45;
		this.lastTimeReset = System.currentTimeMillis();
	} // setRandomDirecton

	public long getLastTimeReset() {
		return this.lastTimeReset;
	} // getLastTimeReset

	public long getLastDropAmmo() {
		return this.lastDropAmmo;
	} // getLastDropAmmo

	public void updateLastDropAmmo() {
		this.lastDropAmmo = System.currentTimeMillis();
	} // updateLastDropAmmo
} // Boss
