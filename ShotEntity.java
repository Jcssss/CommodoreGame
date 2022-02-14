/* ShotEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: Shots in the game
 */
public abstract class ShotEntity extends Entity {

	protected double range; // the range of the bullet
	protected double initX; // the starting x position of the bullet
	protected double initY; // the starting y position of the bullet
	private final double DISPLACEMENT = 30; // the distance between the middle of the shooting sprite and the end of its
											// gun

	// constructor
	public ShotEntity(Game g, String r, int newX, int newY, double d, double range, double speed, double scale) {
		super(g, r, newX, newY, d, 1, scale); // calls the constructor in Entity

		// moves the bullet so that it spawns at the end of the gun, not the center of
		// the entity
		this.calculateStartPos();

		// initializes variables
		this.initX = this.centerX;
		this.initY = this.centerY;
		this.center();
		this.range = range;
		this.moving = true;
		this.moveSpeed = speed;
		this.radius = 5;

		// calculates the x and y distance that the bullet must move
		// dx and dy are final, bullet moves in a straight line
		setMovement();
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move shot
	 */
	public void move(long delta) {
		super.move(delta); // calls the move method in Entity

		// if shot moves off screen, remove it from entity list
		if (this.y <= -this.getHeight() || this.y >= this.game.WINDOW_HEIGHT || x >= this.game.WINDOW_WIDTH
				|| x <= this.getWidth()) {
			this.game.removeEntity(this);

			// if shot reaches its range, removes it
		} else if (displacement() > range) {
			this.game.removeEntity(this);
		} // if
	} // move

	// sets the bullets spawn position to the end of the shooters gun
	private void calculateStartPos() {
		this.centerX += (Math.cos(Math.toRadians(this.direction)) * this.DISPLACEMENT);
		this.centerY -= (Math.sin(Math.toRadians(this.direction)) * this.DISPLACEMENT);
	} // calculateStartPos

	// determines the distance the bullet has travelled
	private double displacement() {
		return Math.sqrt(Math.pow(this.x - this.initX, 2) + Math.pow(this.y - this.initY, 2));
	} // displacement
} // ShotEntity
