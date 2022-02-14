
/* Entity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: An entity is any object that appears in the game.
 * It is responsible for resolving collisions and movement.
 * Entities are drawn and moved based on their centers
 */

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public abstract class Entity {

	// Java Note: the visibility modifier "protected"
	// allows the variable to be seen by this class,
	// any classes in the same package, and any subclasses
	// "private" - this class only
	// "public" - any class can see it

	protected Game game;

	protected double x; // current x location
	protected double y; // current y location
	protected double centerX; // current center x location
	protected double centerY; // current center y location
	protected Sprite[] sprites = new Sprite[5];
	protected double dx; // horizontal speed (px/s) + -> right
	protected double dy; // vertical speed (px/s) + -> down
	protected double direction; // the direction the entity moves / faces
	protected double moveSpeed; // the speed of the entity
	protected boolean moving = true; // is the entity moving
	protected int lives = 0; // the lives of the entity
	protected int radius = 0; // the radius of the entity's circle

	private Circle me = new Circle(); // bounding circle of
										// this entity
	private Circle him = new Circle(); // bounding circle of other
										// entities

	/*
	 * Constructor input: reference to the image for this entity, initial x and y
	 * location to be drawn at
	 */
	public Entity(Game g, String r, int newX, int newY, double d, int frames, double scale) {
		this.game = g;

		// Enemy entities have set scales, bosses however, are bigger
		if (this instanceof Boss) {
			scale /= 2;
		} // if

		// if the entity has animation
		if (frames != 1) {

			// fetch all frames
			for (int i = 0; i < frames; i++) {
				this.sprites[i] = SpriteStore.get().getSprite(r + i + ".gif", scale);
			} // for

			// if the entity has no animation
		} else {
			this.sprites[0] = SpriteStore.get().getSprite(r + ".gif", scale);
		} // if

		// sets the coordinates and direction of the entity
		this.centerX = newX;
		this.centerY = newY;
		this.center();
		this.direction = d;
	} // constructor

	/*
	 * move input: delta - the amount of time passed in ms output: none purpose:
	 * after a certain amount of time has passed, update the location
	 */
	public void move(long delta) {

		// update location of entity based its move speeds
		this.centerX += (delta * dx) / 1000;
		this.centerY += (delta * dy) / 1000;
		this.center();
	} // move

	// stops entities from going off the screen
	protected void stopAtBounds() {

		// stop at left side of screen
		if ((dx < 0) && (x <= 0)) {
			dx = 0;

			// stop at right side of screen
		} else if ((dx > 0) && (x >= game.WINDOW_WIDTH - this.getWidth())) {
			dx = 0;
		} // if

		// stop at upper side of screen
		if ((dy < 0) && (y <= 0)) {
			dy = 0;

			// stop at bottom side of screen
		} else if ((dy > 0) && (y >= game.WINDOW_HEIGHT - this.getHeight())) {
			dy = 0;
		} // if
	} // stopAtBounds

	// calculates the coordinates of the top left corner given the center
	// coordinates
	public void center() {
		this.x = this.centerX - this.getWidth() / 2;
		this.y = this.centerY - this.getHeight() / 2;
	} // center

	protected void setMovement() {

		// if the entity is not moving, it does not move
		if (!this.moving) {
			dy = 0;
			dx = 0;
			return;
		} // if

		// calculates the x and y that an entity needs to move in order to move
		// a given distance in any direction
		dy = -this.moveSpeed * Math.sin(this.direction * Math.PI / 180);
		dx = this.moveSpeed * Math.cos(this.direction * Math.PI / 180);
	} // setMovement

	// get and set methods

	public double getDirection() {
		return this.direction;
	} // getDirection

	public void setDirection(double d) {
		this.direction = d;
	} // setDirection

	public int getX() {
		return (int) x;
	} // getX

	public int getY() {
		return (int) y;
	} // getY

	public int getCenterX() {
		return (int) centerX;
	} // getCenterX

	public int getCenterY() {
		return (int) centerY;
	} // getCenterY

	// get dimensions
	public int getWidth() {
		return this.sprites[0].getWidth();
	} // getWidth

	public int getHeight() {
		return this.sprites[0].getHeight();
	} // getHeight

	public int getRadius() {
		return this.radius;
	} // getRadius

	public int getLives() {
		return this.lives;
	} // getLives

	public void setIsMoving(boolean b) {
		this.moving = b;
	} // setIsMoving

	// draws the entity, if no frame number, automatically draws the first image in
	// the array
	public void draw(Graphics2D g) {
		AffineTransform orig = g.getTransform();
		AffineTransform newRot = (AffineTransform) (orig.clone());

		// rotates the screen, draws the image, rotates the screen back
		// results in a rotated image
		newRot.rotate(-Math.toRadians(this.getDirection()), this.centerX, this.centerY);
		g.setTransform(newRot);
		sprites[0].draw(g, this.getX(), this.getY());
		g.setTransform(orig);
	} // draw

	// overloaded method: if frame number, draws the specified frame
	public void draw(Graphics2D g, int num) {

		// if the player is punching, draws a specific frame
		if (this instanceof PlayerEntity && ((PlayerEntity) this).getIsPunching()) {
			num = 4;

			// if the entity is not moving, draws the first frame (stationary pose)
		} else if (!this.moving) {
			num = 0;
		} // if

		AffineTransform orig = g.getTransform();
		AffineTransform newRot = (AffineTransform) (orig.clone());

		// rotates the screen, draws the image, rotates the screen back
		// results in a rotated image
		newRot.rotate(-Math.toRadians(this.getDirection()), this.centerX, this.centerY);
		g.setTransform(newRot);
		sprites[num].draw(g, this.getX(), this.getY());
		g.setTransform(orig);
	} // draw

	/*
	 * collidesWith input: the other entity to check collision against output: true
	 * if entities collide purpose: check if this entity collides with the other.
	 */
	public boolean collidesWith(Entity other) {
		me.setBounds((int) this.getCenterX(), (int) this.getCenterY(), this.radius);
		him.setBounds((int) other.getCenterX(), (int) other.getCenterY(), other.getRadius());
		return me.intersects(him);
	} // collidesWith

	/*
	 * collidedWith input: the entity with which this has collided purpose:
	 * notification that this entity collided with another Note: abstract methods
	 * must be implemented by any class that extends this class
	 */
	public abstract void collidedWith(Entity other);

} // Entity