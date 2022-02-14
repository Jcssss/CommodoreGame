/* NormalEnemy.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: enemies that melee
 */

public class NormalEnemy extends EnemyEntity {

	public NormalEnemy(Game g, String r, int newX, int newY) {
		super(g, r, newX, newY, 0, 4); // calls the constructor in Entity
	} // NormalEnemy

	public void move(long delta) {

		// faces player
		this.seePlayer();

		// if the entity is at a close range to the player, it stops moving
		this.moving = this.displacement() > 10;
		super.move(delta);
	} // move
} // AlienEntity
