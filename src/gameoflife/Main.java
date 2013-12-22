package gameoflife;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * @author Merlin
 */
public class Main implements ActionListener {
	public Fenetre fenetre;
	private KeyboardShortcuts keyboard;
	private MouseEvents mouse;

	public World world;
	public static final int BASE_WIDTH = 150;
	public static final int BASE_HEIGHT = 90;

	private int interval = 200;
	private Timer timer;

	private boolean isPaused;

	public static void main(String[] args) {
		Main gameOfLife = new Main();

		gameOfLife.world = new World(gameOfLife.BASE_WIDTH, gameOfLife.BASE_HEIGHT);
		gameOfLife.world.fillWithM();

		gameOfLife.fenetre = new Fenetre(gameOfLife, gameOfLife.BASE_WIDTH, gameOfLife.BASE_HEIGHT);
		gameOfLife.mouse = new MouseEvents(gameOfLife);
		gameOfLife.keyboard = new KeyboardShortcuts(gameOfLife, gameOfLife.mouse);

		// Le timer qui va faire passer les générations à interval de temps fixe
		gameOfLife.timer = new Timer(gameOfLife.interval, gameOfLife);
		gameOfLife.isPaused = true;

		gameOfLife.timer.start();
		// Le timer est maintenant démarré, mais le jeu est en pause
	}

	public void actionPerformed(ActionEvent e) // La fonction appelée par le
												// timer
	{
		if (!isPaused()) {
			world.iterate();
			refresh(true); // On affiche le monde dans la fenetre
		} else
			refresh(false);
	}

	// On actualise juste l'affichage
	public void refresh(Boolean moveOffset) {
		if (moveOffset)
			fenetre.paint(world.getGrid(), world.getWidth(), world.getHeight(), true); // On
																						// affiche
																						// le
																						// monde
																						// dans
																						// la
																						// fenetre
		else
			fenetre.paint(world.getGrid(), world.getWidth(), world.getHeight(), false); // On
																						// affiche
																						// le
																						// monde
																						// dans
																						// la
																						// fenetre
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int newInterval) {
		interval = newInterval;
		timer.setDelay(interval);

		// On met à jour le compteur de fps dans le menu
		float currentFps = (float) (1 / (float) ((float) (interval) / 1000));
		fenetre.menus.itemSpeed.setText("Current speed : " + currentFps + " frames / second");
	}

	public void play() {
		if (!timer.isRunning())
			timer.start();

		if (isPaused()) {
			isPaused = false;
		}
	}

	public void playTimer() {
		timer.start();
	}

	public void pause() {
		if (!isPaused()) {
			// timer.stop();
			isPaused = true;
		}
	}

	public void pauseTimer() {
		timer.stop();
	}

	void switchPause() {
		if (isPaused())
			play();
		else
			pause();
	}

	boolean isPaused() {
		return isPaused;
	}

	void restart(String fill) {
		// On crée un nouveau monde avec les dimensions du précédent
		world = new World(world.getWidth(), world.getHeight());
		if (fill.equals("random"))
			world.fillRandom();
		else if (fill.equals("full"))
			world.fillFull();

		fenetre.setWorld(world);
		// On affiche le monde dans la fenetre
		refresh(true);

		if (!isPaused())
			play();
	}

}