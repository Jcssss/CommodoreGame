
/* PlayerEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: represents the player
 */

public class PlayerEntity extends Entity {

	private long lastTimeHurt; // the time at which a life was lost, used to calculate immunity
	private boolean isHurt = false; // did the player recently lose a life
	public final int MAX_AMMO = 24; // the max ammo the player can have
	private int ammo = MAX_AMMO; // the ammo the player has
	private final double SHOOTING_RANGE = 500; // the range the player's bullets travel
	private final double MELEE_RANGE = 50; // the range the player's punches travel
	private boolean isPunching = false;

	public PlayerEntity(Game g, String r, int newX, int newY, double d) {
		super(g, r, newX, newY, d, 5, 3.5); // calls the constructor in Entity

		// initialize variables
		this.direction = d;
		this.moveSpeed = 200;
		this.moving = false;
		this.lives = 3;
		this.radius = this.getHeight() / 3;
	} // PlayerEntity

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move ship
	 */
	public void move(long delta) {
		// determines the x and y distances needed to move
		this.setMovement();

		// ensures entity doesn't go off screen
		this.stopAtBounds();

		// moves the entity
		super.move(delta);
	} // move

	/*
	 * collidedWith input: other - the entity with which the ship has collided
	 * purpose: notification that the player's ship has collided with something
	 */
	public void collidedWith(Entity other) {

		// if the player collides with an enemy
		if (other instanceof EnemyEntity && !this.isHurt) {
			this.takeDamage();

			// if collides with enemy shot
		} else if (other instanceof EnemyShotEntity && !this.isHurt) {
			this.takeDamage();
			this.game.removeEntity(other);

			// if collides with ammo
		} else if (other instanceof AmmoEntity) {

			// adds ammo, caps at max ammo
			this.ammo = Math.min(ammo + ((AmmoEntity) other).MAG_SIZE, MAX_AMMO);
			this.game.removeBackgroundEntity(other);

			// if collides with med kit
		} else if (other instanceof MedKit) {

			// lives reset
			this.lives = 3;
			this.game.removeBackgroundEntity(other);

			// initiates enemy spawn
			this.game.justBeatLevel = false;
		} // if
	} // collidedWith

	// sets variables and takes lives
	private void takeDamage() {
		this.isHurt = true;
		this.lastTimeHurt = System.currentTimeMillis();
		this.lives--;

		// if the player has no lives, loses
		if (this.lives == 0) {
			this.game.notifyDeath();
		} // if
	} // isHurt

	// get and set methods

	public boolean getIsHurt() {
		return this.isHurt;
	} // getIsHurt

	public void setIsHurt(boolean b) {
		this.isHurt = b;
	} // setIsHurt

	public long getLastTimeHurt() {
		return this.lastTimeHurt;
	} // getLastTimeHurt

	public void useAmmo() {
		this.ammo--;
	} // useAmmo

	public int getAmmo() {
		return this.ammo;
	} // getAmmo

	public double getShootingRange() {
		return this.SHOOTING_RANGE;
	} // getShootingRange

	public double getMeleeRange() {
		return this.MELEE_RANGE;
	} // getMeleeRange

	public boolean getIsPunching() {
		return this.isPunching;
	} // getIsPunching

	public void setIsPunching(boolean b) {
		this.isPunching = b;
	} // setIsPunching
} // PlayerEntity