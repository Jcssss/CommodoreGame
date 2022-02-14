/* EnemyShotEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: shots from enemies
 */
public class EnemyShotEntity extends ShotEntity {

	public EnemyShotEntity(Game g, String r, int newX, int newY, double d, double range, double speed) {
		super(g, r, newX, newY, d, range, speed, 2);
	} // EnemyShotEntity
	
	public void collidedWith(Entity other) {} // handled in player

} // EnemyShotEntity