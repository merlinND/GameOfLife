package gameoflife;

import javax.swing.JOptionPane;

/**
 * @author Merlin Nimier-David
 */
class Actions {

	static void switchPause(Main game) {
		game.switchPause();
		if (game.isPaused())
			game.fenetre.menus.itemPlayPause.setText("Play (Spacebar)");
		else
			game.fenetre.menus.itemPlayPause.setText("Pause (Spacebar)");
	}

	static void goToNextGeneration(Main game) {
		if (game.isPaused())
			game.world.iterate();
	}

	// Vitesse
	static void increaseSpeed(Main game) {
		if (game.getInterval() - 100 > 0)
			game.setInterval(game.getInterval() - 100);
		else
			game.setInterval(10);
	}

	static void setSpeedTo(Main game, int newInterval) {
		game.setInterval(newInterval);
	}

	static void decreaseSpeed(Main game) {
		game.setInterval(game.getInterval() + 100);
	}

	static void exit(Fenetre fenetre) {
		// On demande confirmation avant de quitter
		int answer = JOptionPane.showConfirmDialog(fenetre, "Voulez vous vraiment quitter ?", "Quitter", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	// Restart
	static void restart(Main game, String fill) {
		game.restart(fill);
		game.pause();
	}

	// ------------
	// 2: Affichage
	static void switchFullscreen(Fenetre fenetre) {
		fenetre.switchFullscreen();
		if (fenetre.isFullscreen())
			fenetre.menus.itemFullscreen.setSelected(true);
		else
			fenetre.menus.itemFullscreen.setSelected(false);
	}

	static void switchDisplayGrid(Main game) {
		game.fenetre.switchGridDisplay();
		if (game.fenetre.isGridDisplayed())
			game.fenetre.menus.itemGrid.setSelected(true);
		else
			game.fenetre.menus.itemGrid.setSelected(false);
		game.refresh(false);
	}

	static void switchDisplayStats(Main game) {
		game.fenetre.switchStatsDisplay();
		if (game.fenetre.areStatsDisplayed())
			game.fenetre.menus.itemStats.setSelected(true);
		else
			game.fenetre.menus.itemStats.setSelected(false);
		game.refresh(false);
	}

	static void doColorModeChange(Main game, Fenetre fenetre, int r) {
		String[] colorModes = fenetre.colorModes;
		String currentColorMode = fenetre.getColorMode();

		// On parcourt tous les modes de couleurs disponibles
		int selectedIndex = 0;
		for (int i = 0; i < colorModes.length; i++) {
			if (currentColorMode.equals(colorModes[i])) {
				if (r == 1) { // Si on scroll vers l'avant
					if (i < colorModes.length - 1)
						selectedIndex = i + 1;
					else if (i == colorModes.length - 1)
						selectedIndex = 0;
				} else { // Sinon on scroll vers l'arrière
					if (i > 0)
						selectedIndex = i - 1;
					else if (i == 0)
						selectedIndex = colorModes.length - 1;
				}
				game.refresh(false);
			}
		}
		fenetre.setColorMode(colorModes[selectedIndex]);
		fenetre.menus.itemsColorModes[selectedIndex].setSelected(true);
	}

	static void setColorModeTo(Main game, Fenetre fenetre, String newColorMode) {
		fenetre.setColorMode(newColorMode);

		game.refresh(false);
	}

	static void setBlockSizeTo(Fenetre fenetre, int newWidth, int newHeight) {
		fenetre.setBlockSize(newWidth, newHeight);
	}
}
