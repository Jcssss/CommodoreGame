/* PlayerShotEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: shots from the player
 */
public class PlayerShotEntity extends ShotEntity {

	public PlayerShotEntity(Game g, String r, int newX, int newY, double d, double range) {
		super(g, r, newX, newY, d, range, 300, 2); // calls the constructor in Entity
	} // constructor

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

			// notify the game that the enemy is dead
			game.notifyEnemyKilled(other.getCenterX(), other.getCenterY(), other);

			// remove affect entities from the Entity list
			game.removeEntity(this);
			game.removeEntity(other);

		} // if
	} // collidedWith

} // PlayerShotEntity class