/* PlayerMeleeEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: a type of shot thrown out when the player is punching
 */

public class PlayerMeleeEntity extends ShotEntity {

	public PlayerMeleeEntity(Game g, String r, int newX, int newY, double d, double range) {
		super(g, r, newX, newY, d, range, 600, 4); // calls the constructor in Entity
	} // PlayerMeleeEntity

	/*
	 * collidedWith input: other - the entity with which the shot has collided
	 * purpose: notification that the shot has collided with something
	 */
	public void collidedWith(Entity other) {

		// if it has hit an enemy, kill it!
		if (other instanceof Boss) {
			Boss boss = (Boss) other;
			game.removeEntity(this);
			boss.loseLife();
		} else if (other instanceof EnemyEntity) {

			// notify the game that the alien is dead
			game.notifyEnemyKilled(other.getCenterX(), other.getCenterY(), other);

			// remove affect entities from the Entity list
			game.removeEntity(this);
			game.removeEntity(other);

		} // if
	} // collidedWith

} // PlayerMeleeEntity
