/* EnemyEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: Represents one of the enemies
 */
public abstract class EnemyEntity extends Entity {

	protected double shootingRange = 0; // to be used when enemies shoot
	protected long shotInterval = 0; // the interval between shots
	protected long lastTimeShot = 0; // the time that a shot was last fired

	/*
	 * construct a ne w alien input: game - the game in which the alien is being
	 * created r - the image representing the alien x, y - initial location of alien
	 */
	public EnemyEntity(Game g, String r, int newX, int newY, long si, int frames) {
		super(g, r, newX, newY, 90, frames, 4); // calls the constructor in Entity

		// initiates variables
		this.moveSpeed = 65 + (int) (Math.random() * 40);
		this.moving = true;
		this.shotInterval = si;
		this.radius = Game.player.radius;
	} // EnemyEntity

	public void move(long delta) {

		// determines the x and y distances needed to move
		this.setMovement();

		// ensures entity doesn't go off screen
		this.stopAtBounds();

		// moves the entity
		super.move(delta);
	} // move

	/*
	 * collidedWith input: other - the entity with which the alien has collided
	 * purpose: notification that the alien has collided with something
	 */
	public void collidedWith(Entity other) {
		// collisions with aliens are handled in ShotEntity and ShipEntity
	} // collidedWith

	// calculates the angle the entity needs to turn in order to be facing the
	// player's center
	protected void seePlayer() {

		// tan(90) and tan(270) are undefined, if statement ensures that no error is
		// given
		if (this.x != Game.player.getX()) {

			// calculates angle
			this.direction = 180
					* -Math.atan((Game.player.getCenterY() - this.centerY) / (Game.player.getCenterX() - this.centerX))
					/ Math.PI;

			// if tan is undefined
		} else {

			// if the player is above the entity, faces up
			if (this.centerY > Game.player.getY()) {
				this.direction = 90;

				// if the player is below the entity, faces down
			} else {
				this.direction = 270;
			} // if
		} // if

		// accounts for co-tangent angles
		if ((this.centerX > Game.player.getCenterX() && this.centerY < Game.player.getCenterY())
				|| (this.centerX > Game.player.getCenterX() && this.centerY > Game.player.getCenterY())) {
			this.direction += 180;
		} // if
	} // seePlayer

	// get and set methods

	public double getRange() {
		return this.shootingRange;
	} // getRange

	public long getShotInterval() {
		return this.shotInterval;
	} // getShotInterval

	public long getLastTimeShot() {
		return this.lastTimeShot;
	} // getLastTimeShot

	public void updateLastTimeShot() {
		this.lastTimeShot = System.currentTimeMillis();
	} // updateLastTimeShot

	// calculates the distance between the center of the enemy and the center of the
	// player
	protected double displacement() {
		return Math.sqrt(Math.pow(this.x - Game.player.getX(), 2) + Math.pow(this.y - Game.player.getY(), 2));
	} // displacement
} // EnemyEntity
