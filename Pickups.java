/* Pickups.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: background sprites
 * dropped by enemies and give benefits to the player
 */

public abstract class Pickups extends Entity {
	public Pickups(Game g, String r, int newX, int newY) {

		super(g, r, newX, newY, 0.0, 1, 5); // calls the constructor in Entity

		// initializes variables
		this.moveSpeed = 0;
		this.moving = false;
		this.radius = this.sprites[0].getHeight();
	} // Pickups

	public void collidedWith(Entity other) {

	} // collidedWith
} // Pickups
