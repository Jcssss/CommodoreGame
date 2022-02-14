
/* Game.java
 * Date: March 30th, 2020
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: A top down shooter, in which the player attempts to survive 12 waves of enemies.
 */

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {

	private static final long serialVersionUID = 4L;

	public final int WINDOW_WIDTH = 1280;
	public final int WINDOW_HEIGHT = 720;

	public static PlayerEntity player; // the player

	private BufferStrategy strategy; // take advantage of accelerated graphics
	private boolean leftPressed = false; // true if left arrow key currently pressed
	private boolean rightPressed = false; // true if right arrow key currently pressed
	private boolean upPressed = false; // true if up arrow key currently pressed
	private boolean downPressed = false; // true if down arrow key currently pressed
	private boolean firePressed = false; // true if firing
	private boolean meleePressed = false; // true if hitting

	private ArrayList<Entity> entities = new ArrayList<Entity>(); // list of entities that move

	private ArrayList<Entity> backgroundEntities = new ArrayList<Entity>(); // list of immovable entities

	private ArrayList<Entity> removeEntities = new ArrayList<Entity>(); // list of moving entities to remove from loop

	private ArrayList<Entity> removeBackgroundEntities = new ArrayList<Entity>(); // list of background entities to
																					// remove from loop

	private final long GRACE_PERIOD = 700; // the time before the player can be hurt again
	private long lastFire = 0; // time last shot fired
	private final long FIRING_INTERVAL = 300; // interval between shots (ms)
	private int enemyCount; // # of enemies left on screen
	private int level = 1; // the player's level

	// names of sprite images
	private String playerSprite = "playerGun";
	private String normalEnemySprite = "enemyFists";
	private String shootingEnemySprite = "enemyGun";
	private String playerShotSprite = "playerShot";
	private String enemyShotSprite = "enemyShot";
	private String ammoSprite = "ammo";
	private String medSprite = "meds";
	private String sniperSprite = "sniper";
	private String sprayerSprite = "sprayer";
	private String meleeSprite = "playerFist";
	private String bossShotSprite = "sniperShot";
	private String corpse = "enemyDead";
	private String carSprite = "car";
	private String sniperDead = "sniperDead";
	private String sprayerDead = "sprayerDead";
	private String carDead = "carDead";
	private String deadPlayerSprite = "playerDead";

	private boolean titleScreen = true; // on the title screen
	private boolean instructions = false; // on the instruction screen
	private boolean lore = false; // on the lore screen
	private boolean meetEnemies = false; // on the "meet the enemies" screen
	private long winTime; // the time at which a level is completed
	private long timeEnded; // time when game ends
	public long levelStart = 0; // the time at which the level starts, used for scoring
	private boolean needSpawn = true; // enemies need to be spawned
	public boolean justBeatLevel = false; // a level was just completed
	private boolean isWin = false; // did the player win
	private boolean isLose = false; // did the player lose
	private boolean gameOver = false; // is the game over

	private Sprite[] backgrounds = new Sprite[3]; // three different backgrounds for each level
	private Sprite titleArt = SpriteStore.get().getSprite("titlescreen.png", 1);
	private Sprite instructionArt = SpriteStore.get().getSprite("instructionscreen.png", 1);
	private Sprite loreArt = SpriteStore.get().getSprite("lorescreen.png", 1);
	private Sprite meetEnemyArt = SpriteStore.get().getSprite("galleryscreen.png", 1);
	private Sprite winArt = SpriteStore.get().getSprite("winscreen.png", 1);
	private Sprite loseArt = SpriteStore.get().getSprite("losescreen.png", 1);
	private Sprite hearts = SpriteStore.get().getSprite("life.gif", 5); // the player's lives, displayed in top
																				// // left corner
	private Sprite bossHearts = SpriteStore.get().getSprite("life0.gif", 20); // the boss's lives, displayed

	private int frameNum = 0; // the frame number, used for animation
	private int score = 0; // the player's current score
	private int oldScore = 0; // the player's score from the last game
	private int highScore = 0; // the highscore of the current sesson

	/*
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain game
		JFrame container = new JFrame("Mafia City: Reloaded");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(WINDOW_WIDTH - 10, WINDOW_HEIGHT - 10));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		panel.add(this);

		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		getBackgrounds();
		initEntities();
		titleScreen = true;

		// start the game
		gameLoop();
	} // constructor

	/*
	 * initEntities input: none output: none purpose: Initialize the starting state
	 * of the player and enemy entities. Each entity will be added to the array of
	 * entities in the game.
	 */
	private void initEntities() {

		// reset variables
		level = 12;
		needSpawn = true;
		winTime = 0;
		
		justBeatLevel = false;
		resetScreen();
		score = 0;

		// clear all entities
		entities.removeAll(entities);
		backgroundEntities.removeAll(backgroundEntities);

		// create the player and put in center of screen
		player = new PlayerEntity(this, playerSprite, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 90);
		entities.add(player);

	} // initEntities

	// loads the three backgrounds
	private void getBackgrounds() {
		for (int i = 0; i < backgrounds.length; i++) {
			backgrounds[i] = SpriteStore.get().getSprite("bg" + i + ".png", 0.5);
		} // for
	} // getBackgrounds

	// removes an entity from the game
	public void removeEntity(Entity entity) {
		removeEntities.add(entity);
	} // removeEntity

	// adds an entity to the game
	public void addEntity(Entity entity) {
		entities.add(entity);
	} // addEntity

	// removes a background entity from the game
	public void removeBackgroundEntity(Entity entity) {
		removeBackgroundEntities.add(entity);
	} // removeBackgroundEntity

	// adds a background entity to the game
	public void addBackgroundEntity(Entity entity) {
		backgroundEntities.add(entity);
	} // addBackgroundEntity

	// upon player death, returns to instruction screen
	public void notifyDeath() {
		isLose = true;
		addBackgroundEntity(
				new Corpse(this, deadPlayerSprite + (int) (Math.random() * 2 + 1), player.getCenterX(), player.getCenterY()));
		entities.remove(player);
		timeEnded = System.currentTimeMillis();
	} // notifyDeath

	// upon wave completion, increments level and spawns next wave
	public void notifyWin() {

		level++;
		winTime = System.currentTimeMillis();
		needSpawn = true;
		updateScore(Math.max(level * 6000 - (winTime - levelStart), 0) + 1000);

		// if the level was a boss fight
		if (level % 4 == 1) {
			justBeatLevel = true;
		} // if

		// if the player beat the game
		if (level == 13) {
			timeEnded = System.currentTimeMillis();
			isWin = true;
			level = 12;
		} // if
	} // notifyWin

	// spawns new enemies into the game
	public void spawnEnemies() {
		int width = SpriteStore.get().getSprite(normalEnemySprite + "0.gif", 4).getWidth();
		int height = SpriteStore.get().getSprite(normalEnemySprite + "0.gif", 4).getHeight();
		Entity enemy;
		int x = 0; // the x value of the enemies center coordinate
		int y = 0; // the y value of the enemies center coordinate

		// if the level is a boss fight, spawns a boss
		if (level % 4 == 0) {
			enemyCount = 1;
			switch (level % 12) {
			case 4:
				enemy = new Sniper(this, sniperSprite);
				break;
			case 8:
				enemy = new Sprayer(this, sprayerSprite);
				break;
			case 0:
				enemy = new Car(this, carSprite);
				break;
			default:
				enemy = new NormalEnemy(this, normalEnemySprite, 0, 0);
				break;
			} // switch
			addEntity(enemy);

			// if the level is not a boss fight, spawn normal enemies
		} else {
			enemyCount = level * 3;

			// randomly spawns enemies along the border of the screen
			for (int i = 0; i < enemyCount; i++) {

				// randomly chooses a side for the enemy to spawn along
				switch ((int) (Math.random() * 4)) {
				case 0: // N
					y = 0;
					x = (int) (Math.random() * (WINDOW_WIDTH - width - 20));
					break;
				case 1: // E
					x = WINDOW_WIDTH - width - 20;
					y = (int) (Math.random() * (WINDOW_HEIGHT - height - 20));
					break;
				case 2: // S
					y = WINDOW_HEIGHT - height - 20;
					x = (int) (Math.random() * (WINDOW_WIDTH - width - 20));
					break;
				case 3: // W
					x = 0;
					y = (int) (Math.random() * (WINDOW_HEIGHT - height - 20));
					break;
				} // switch
				x += 10;
				y += 10;

				// spawns 1/3 of the enemies as shooting enemies
				if (i < enemyCount / 3) {
					enemy = new ShootingEnemy(this, shootingEnemySprite, x, y);

					// spawns 2/3 of the enemies as normal enemies
				} else {
					enemy = new NormalEnemy(this, normalEnemySprite, x, y);
				} // if
				addEntity(enemy);
			} // for
		} // if
	} // spawnEnemies

	// notification that an enemy has been killed
	public void notifyEnemyKilled(int x, int y, Entity other) {
		enemyCount--;

		// if all enemies are killed
		if (enemyCount == 0) {
			notifyWin();
		} // if

		// spawns a corpse where the enemy died
		if (other instanceof Boss) {
			if (other instanceof Sniper) {
				addBackgroundEntity(new Corpse(this, sniperDead, x, y));
			} else if (other instanceof Sprayer) {
				addBackgroundEntity(new Corpse(this, sprayerDead, x, y));
			} else if (other instanceof Car) {
				addBackgroundEntity(new Corpse(this, carDead, x, y));
			} // if
		} else {
			addBackgroundEntity(new Corpse(this, corpse, x, y));
		} // if

		// increments score and drops items according to what kind of enemy was killed

		// if it's a shooting enemy
		if (other instanceof ShootingEnemy) {

			// 1/3 chance of dropping ammo
			if ((int) (Math.random() * 3) == 0) {
				addBackgroundEntity(new AmmoEntity(this, ammoSprite, x, y));
			} // if
			updateScore(100);

			// if it's a boss
		} else if (other instanceof Sniper || other instanceof Sprayer) {

			// drops a med kit
			addBackgroundEntity(new MedKit(this, medSprite, x, y));
			updateScore(1000);

			// if it's a normal enemy
		} else if (other instanceof NormalEnemy) {
			updateScore(50);
		} // if
	} // notifyEnemyKilled

	/* Attempt to fire. */
	public void tryToAttack() {
		int x = 0; // the x coordinate of the bullet's center
		int y = 0; // the y coordinate of the bullet's center
		ShotEntity shot = null;

		// initial position of shot
		// spawns at the center of the player
		x = player.getCenterX();
		y = player.getCenterY();

		// if the shot is a melee
		if (meleePressed) {
			shot = (ShotEntity) new PlayerMeleeEntity(this, meleeSprite, x, y, player.getDirection(),
					player.getMeleeRange());

			// used for animation purposes
			player.setIsPunching(true);

			// if the shot is a bullet
		} else if (firePressed) {

			// if the player is out of ammo, can't shoot
			if (player.getAmmo() == 0) {
				return;
			} // if

			// deduct one ammo
			player.useAmmo();

			shot = (ShotEntity) new PlayerShotEntity(this, playerShotSprite, x, y, player.getDirection(),
					player.getShootingRange());
		} // if
		addEntity(shot);
		lastFire = System.currentTimeMillis();
	} // tryToAttack

	/*
	 * gameLoop input: none output: none purpose: Main game loop. Runs throughout
	 * game play. Responsible for the following activities: - calculates speed of
	 * the game loop to update moves - moves the game entities - draws the screen
	 * contents (entities, text) - updates game events - checks input
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		String message = "";

		// keep loop running until game ends
		while (true) {

			frameNum++;

			// calc. time since last update, will be used to calculate
			// entities movement
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			// get graphics context for the accelerated surface and make it black
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

			// if the game has ended (win or loss)
			if (gameOver) {
				// draws a black background
				g.setColor(Color.black);
				g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
				g.setColor(Color.white);

				// if the player has won, displays win message
				if (isWin) {
					winArt.draw(g, 0, 0);
					g.drawString("Score: " + oldScore, 915, 268);

					// if the player has lost, displays lose image
				} else if (isLose) {
					loseArt.draw(g, 0, 0);
					g.drawString("Score: " + oldScore, 175, 470);

				} // if

				// if the player is on the title screen
			} else if (titleScreen) {

				// draws the title screen
				titleArt.draw(g, 0, 0);

				// if the player is on the instruction screen
			} else if (instructions) {

				// draws the instruction screen
				instructionArt.draw(g, 0, 0);

				// if the player is on the lore screen
			} else if (lore) {

				loreArt.draw(g, 0, 0);

				// if the player is on the "Meet the Enemies" screen
			} else if (meetEnemies) {

				meetEnemyArt.draw(g, 0, 0);

			} else {

				// draws background
				backgrounds[(level - 1) / 4].draw(g, 0, 0);
				g.setColor(Color.white);

				// if the level was just beat
				if (justBeatLevel) {

					// prints message
					message = (level == 12) ? "You Won!" : "Congratulations. You've completed Level " + (level / 4)
							+ ". Grab the med kit to continue.";
					drawMultiLineString(message, WINDOW_WIDTH / 2, 200, 25, true, g);
					winTime = System.currentTimeMillis();
				} // if

				// spawns new enemies
				if (needSpawn && !justBeatLevel) {

					// enemies take 4 seconds before they spawn
					if (lastLoopTime - winTime > 4000) {
						spawnEnemies();
						levelStart = System.currentTimeMillis();
						needSpawn = false;
					} // if

					// prints message
					g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
					drawMultiLineString("Next wave in : " + (4000 - (lastLoopTime - winTime)) / 1000, WINDOW_WIDTH / 2,
							200, 25, true, g);
					g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
				} // if

				// remove dead entities
				entities.removeAll(removeEntities);
				removeEntities.clear();

				backgroundEntities.removeAll(removeBackgroundEntities);
				removeBackgroundEntities.clear();

				// draws all of the background entities
				for (int i = 0; i < backgroundEntities.size(); i++) {
					Entity entity = (Entity) backgroundEntities.get(i);
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
					entity.draw(g);

					// corpses fade, when a corpse is completely faded it is removed
					if (entity instanceof Corpse && ((Corpse) entity).getOpacity() == 0) {
						removeBackgroundEntity(entity);
					} // if
				} // for

				// draws all of the moving entities
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

					// if the player is immune, it becomes less opaque
					if (entity instanceof PlayerEntity && player.getIsHurt()) {
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					} // if

					// if the entity is a shot or boss (only one frame) doesn't use frame number
					if (entity instanceof ShotEntity || entity instanceof Boss) {
						entity.draw(g);

						// if the entity is anything with animation, uses a frame number
					} else {
						entity.draw(g, (frameNum / 25) % 4);
					} // if
				} // for

				// moves all entities
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.move(delta);

					// if the entity is a shooting enemy, checks if it should shoot
					if (entity instanceof ShootingEnemy) {
						ShootingEnemy shooter = (ShootingEnemy) entity;

						// if the enemy has not shot recently
						if (System.currentTimeMillis() - shooter.getLastTimeShot() > shooter.getShotInterval()) {

							// shoots
							addEntity(new EnemyShotEntity(this, enemyShotSprite, entity.getCenterX(),
									entity.getCenterY(), entity.getDirection(),
									((ShootingEnemy) entity).getShootingRange(), (int) (200 + Math.random() * 50)));
							shooter.updateLastTimeShot();
						} // if
					} // if

					// if the entity is a boss
					if (entity instanceof Boss) {
						Boss boss = (Boss) entity;

						// if the boss hasn't changed it's movement direction recently, changes
						// (excludes car boss)
						if (System.currentTimeMillis() - boss.getLastTimeReset() > boss.RESET_INTERVAL && boss.moving
								&& !(boss instanceof Car)) {
							boss.setRandomDirection();
						} // if

						// if the boss hasn't dropped ammo recently, drops ammo
						if (System.currentTimeMillis() - boss.getLastDropAmmo() > boss.DROP_INTERVAL) {
							addBackgroundEntity(new AmmoEntity(this, ammoSprite, boss.getCenterX(), boss.getCenterY()));
							boss.updateLastDropAmmo();
						} // if

						// if the boss is a sniper
						if (boss instanceof Sniper) {

							// if the sniper has not shot recently, shoots
							if (System.currentTimeMillis() - boss.getLastTimeShot() > boss.getShotInterval()) {
								addEntity(new EnemyShotEntity(this, bossShotSprite, boss.getCenterX(),
										boss.getCenterY(), boss.getDirection(), boss.getRange(), 1000));
								boss.updateLastTimeShot();
							} // if

							// if the boss is a sprayer
						} else if (boss instanceof Sprayer) {
							Sprayer s = (Sprayer) boss;

							// if the sprayer has not shot recently, begins shooting
							if (!s.getShooting()
									&& System.currentTimeMillis() - s.getLastTimeShot() > s.getShotInterval()) {
								s.startShooting();
							} // if

							// increments bullets so that each bullet in a wave is staggered (forms a
							// spiral)
							if (System.currentTimeMillis() - s.getLastBulletFired() > s.TIME_BETWEEN_BULLETS
									&& !s.moving) {

								// increments the direction of the sniper by 16 degrees
								s.setDirection(s.getDirection() + 16);
								addEntity(new EnemyShotEntity(this, bossShotSprite, s.getCenterX(), s.getCenterY(),
										s.getDirection(), s.getRange(), 250));

								// updates variables
								s.updateLastBulletFired();
								s.incrementShots();
							} // if
						} // if

						g.setColor(Color.white);

						// draws the bosses lives
						// if the boss has more than 5 health
						if (boss.getLives() > 5) {

							// draws one heart and the number of lives next to it
							bossHearts.draw(g, boss.getCenterX() - bossHearts.getWidth() - 6,
									boss.getCenterY() - boss.getRadius() - bossHearts.getHeight() + 2);
							g.drawString(": " + boss.getLives() + "", boss.getCenterX(),
									boss.getCenterY() - boss.getRadius());

							// if the boss has less than 5 lives
						} else {

							// draws a heart for each life
							for (int j = 0; j < boss.getLives(); j++) {
								bossHearts.draw(g,
										boss.getCenterX() - ((boss.getLives() + 2) * bossHearts.getWidth() / 2)
												+ ((bossHearts.getWidth() + 2) * j),
										boss.getCenterY() - boss.getRadius() - bossHearts.getHeight() + 2);
							} // for
						} // if

					} // if
				} // for
				
					// draw player's lives
				for (int i = 0; i < player.getLives(); i++) {
					hearts.draw(g, 10 + 60 * i, 10);
				} // for

				// draw ammo
				drawMultiLineString("Ammo:\n" + player.getAmmo() + "/" + player.MAX_AMMO, WINDOW_WIDTH - 60, 25, 25,
						false, g);

				// draw score
				drawMultiLineString("Score: " + score + "\nHigh Score: " + highScore, WINDOW_WIDTH / 2, 25, 25, true,
						g);

				if ((isWin || isLose) && System.currentTimeMillis() - timeEnded > 3000) {
					resetGame();
					gameOver = true;
				} // if
				
				// brute force collisions, compare every entity
				// against every other entity. If any collisions
				// are detected notify both entities that it has
				// occurred

				// check that player has had grace period
				player.setIsHurt((System.currentTimeMillis() - player.getLastTimeHurt()) < GRACE_PERIOD);

				for (int i = 0; i < entities.size(); i++) {
					for (int j = 0; j < backgroundEntities.size(); j++) {
						Entity me = (Entity) entities.get(i);
						Entity him = (Entity) backgroundEntities.get(j);

						if (me.collidesWith(him)) {
							me.collidedWith(him);
							him.collidedWith(me);
						} // if
					} // inner for

					for (int j = i + 1; j < entities.size(); j++) {
						Entity me = (Entity) entities.get(i);
						Entity him = (Entity) entities.get(j);

						if (me.collidesWith(him)) {
							me.collidedWith(him);
							him.collidedWith(me);
						} // if
					} // inner for
				} // outer for
			} // while

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// player should not move without user input
			player.setIsMoving(!((leftPressed && rightPressed) || (!leftPressed && !rightPressed))
					|| !((upPressed && downPressed) || (!upPressed && !downPressed)));

			// move player
			if (player.moving) {
				if (upPressed && !downPressed && ((leftPressed && rightPressed) || (!leftPressed && !rightPressed))) {
					player.setDirection(90);
				} else if (upPressed && leftPressed && !rightPressed && !downPressed) {
					player.setDirection(135);
				} else if (leftPressed && !rightPressed
						&& ((upPressed && downPressed) || (!upPressed && !downPressed))) {
					player.setDirection(180);
				} else if (leftPressed && downPressed && !rightPressed && !upPressed) {
					player.setDirection(225);
				} else if (downPressed && !upPressed
						&& ((leftPressed && rightPressed) || (!leftPressed && !rightPressed))) {
					player.setDirection(270);
				} else if (downPressed && rightPressed && !leftPressed && !upPressed) {
					player.setDirection(315);
				} else if (!leftPressed && rightPressed
						&& ((upPressed && downPressed) || (!upPressed && !downPressed))) {
					player.setDirection(0);
				} else if (upPressed && rightPressed && !leftPressed && !downPressed) {
					player.setDirection(45);
				} // if
			} // if
			player.setMovement();

			// check that we've waited long enough to fire
			if (!((System.currentTimeMillis() - lastFire) < FIRING_INTERVAL)) {
				player.setIsPunching(false);
				if (firePressed || meleePressed) {
					tryToAttack();
				} // if
			} // if

			try {
				Thread.sleep(10);
			} catch (Exception e) {
			} // try

		} // while

	} // gameLoop

	// draws strings along multiple lines
	private void drawMultiLineString(String message, int x, int y, int lineSpacing, boolean center, Graphics2D g) {
		String line = "";

		// splits the message by "\n" and then draws each string on an individual line
		for (int i = 0; i < message.split("\n").length; i++) {
			line = message.split("\n")[i];

			// centers the lines if wanted
			g.drawString(line, (center) ? x - g.getFontMetrics().stringWidth(line) / 2 : x, y + i * lineSpacing);
		} // for
	} // drawMultiLineString

	private void updateScore(long i) {
		score += i;
		highScore = Math.max(score, highScore);
	} // updateScore

	private void resetScreen() {
		instructions = false;
		titleScreen = false;
		lore = false;
		meetEnemies = false;
		gameOver = false;
	} // resetScreen

	/*
	 * startGame input: none output: none purpose: start a fresh game, clear old
	 * data
	 */
	private void resetGame() {
		oldScore = score;
		// clear out any existing entities and initalize a new set
		entities.clear();
		backgroundEntities.clear();

		initEntities();

		// blank out any keyboard settings that might exist
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		firePressed = false;
		meleePressed = false;
	} // resetGame

	/*
	 * inner class KeyInputHandler handles keyboard input from the user
	 */
	private class KeyInputHandler extends KeyAdapter {

		// Keys
		private static final int LEFT = KeyEvent.VK_LEFT;
		private static final int RIGHT = KeyEvent.VK_RIGHT;
		private static final int UP = KeyEvent.VK_UP;
		private static final int DOWN = KeyEvent.VK_DOWN;
		private static final int FIRE = KeyEvent.VK_Z;
		private static final int MELEE = KeyEvent.VK_X;
		private static final int INSTRUCTIONS = KeyEvent.VK_SPACE;
		private static final int LORE = KeyEvent.VK_L;
		private static final int MEETENEMIES = KeyEvent.VK_M;
		private static final int ENTER = KeyEvent.VK_ENTER;
		private static final int EXIT = KeyEvent.VK_ESCAPE;

		/*
		 * The following methods are required for any class that extends the abstract
		 * class KeyAdapter. They handle keyPressed, keyReleased and keyTyped events.
		 */
		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == EXIT) {
				System.exit(0);
			} // if

			// if the game is on the title screen, instruction screen or end screen
			if (titleScreen || instructions || lore || meetEnemies || gameOver) {

				switch (e.getKeyCode()) {

				// toggles between instructions and title screen
				case INSTRUCTIONS:
					titleScreen = !titleScreen;
					instructions = !instructions;
					return;

				// toggles between lore and title screen
				case LORE:
					titleScreen = !titleScreen;
					lore = !lore;
					return;

				// toggles between "meet the enemies" and title screen
				case MEETENEMIES:
					titleScreen = !titleScreen;
					meetEnemies = !meetEnemies;
					return;

				case ENTER:
					if (titleScreen) {
						resetScreen();
						winTime = System.currentTimeMillis();
						isWin = false;
						isLose = false;
					} else {
						resetScreen();
						titleScreen = true;
					} // if
					return;
				} // switch
			} // if

			// once the game has started
			switch (e.getKeyCode()) {
			case LEFT:
				leftPressed = true;
				break;
			case RIGHT:
				rightPressed = true;
				break;
			case UP:
				upPressed = true;
				break;
			case DOWN:
				downPressed = true;
				break;
			case FIRE:
				firePressed = true;
				break;
			case MELEE:
				meleePressed = true;
			} // switch
		} // keyPressed

		public void keyReleased(KeyEvent e) {

			// respond to controls, toggle opposite if unstuck
			switch (e.getKeyCode()) {
			case LEFT:
				leftPressed = false;
				break;
			case RIGHT:
				rightPressed = false;
				break;
			case UP:
				upPressed = false;
				break;
			case DOWN:
				downPressed = false;
				break;
			case FIRE:
				firePressed = false;
				break;
			case MELEE:
				meleePressed = false;
			} // switch

		} // keyReleased

	} // class KeyInputHandler

	/**
	 * Main Program
	 */
	public static void main(String[] args) {
		// instantiate this object

		new Game();
	} // main
} // Game
