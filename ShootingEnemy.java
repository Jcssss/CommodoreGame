/* ShootingEnemy.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: enemies that shoot at the player
 */

public class ShootingEnemy extends EnemyEntity {

	private final double BULLET_RANGE = 500; // the range that the enemies bullets can travel
	private final double SHOOTING_RANGE = 300; // the range at which the enemy stops moving towards the player
	private boolean isMovingBack; // the enemy will move back if the player gets to close

	public ShootingEnemy(Game g, String r, int newX, int newY) {

		super(g, r, newX, newY, 1500, 4); // calls the constructor in Entity
		this.lastTimeShot = (long) (Math.random() * 4000);
	} // ShootingEnemy

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move alien
	 */
	public void move(long delta) {

		// points at player
		this.seePlayer();

		// if the enemy is too close to player, moves back
		if (displacement() <= this.SHOOTING_RANGE - 10) {
			this.isMovingBack = true;
			this.moving = true;

			// if the enemy is too far away, moves closer
		} else if (displacement() >= this.SHOOTING_RANGE) {
			this.isMovingBack = false;
			this.moving = true;

			// if the enemy is at the perfect range, stops moving
		} else {
			this.moving = false;
		} // if

		// if the enemy is moving back, flip the angle
		if (this.isMovingBack) {
			this.direction += 180;
		} // if

		// proceed with normal move
		super.move(delta);
	} // move

	// get and set methods

	public double getShootingRange() {
		return this.BULLET_RANGE;
	} // getShootingRange

	public boolean getIsMovingBack() {
		return this.isMovingBack;
	} // getIsMovingBack

	public double getDirection() {
		return (this.isMovingBack) ? this.direction + 180 : this.direction;
	} // getDirection
} // ShootingEnemy
