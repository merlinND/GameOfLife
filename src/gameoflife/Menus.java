package gameoflife;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 * @author Merlin
 */
class Menus {

	private Main m_game;
	private Fenetre m_fenetre;
	public JMenuBar menuBar;

	// Les items qui ont besoin d'etre mis à jours de l'extérieur
	public JMenuItem itemPlayPause, itemSpeed, itemFullscreen, itemGrid, itemStats;
	public JRadioButtonMenuItem[] itemsColorModes;

	public Menus(Main game, Fenetre fenetre) {

		// TODO
		// Faire correspondre l'état des menus à l'état du jeu (affichage,
		// pause, etc)

		m_game = game;
		m_fenetre = fenetre;

		// On crée tous les éléments du menu qui vont nous servir pendant la
		// déclaration
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JRadioButtonMenuItem menuRadioItem;

		// Initialisation de la barre de menu
		menuBar = new JMenuBar();

		// 1: Game of Life
		menu = new JMenu("Game of Life");
		menuBar.add(menu);

		menuItem = new JMenuItem("Play (Spacebar)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.switchPause(m_game);
			}
		});
		itemPlayPause = menuItem;
		menu.add(menuItem);
		menuItem = new JMenuItem("Go to next generation (Right Arrow)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.goToNextGeneration(m_game);
			}
		});
		menu.add(menuItem);

		// La vitesse
		menu.addSeparator();
		submenu = new JMenu("Speed");
		float currentFps = (float) (1 / (float) ((float) (m_game.getInterval()) / 1000));
		menuItem = new JMenuItem("Current speed : " + currentFps + " frames / second");
		menuItem.setEnabled(false);
		itemSpeed = menuItem;
		submenu.add(menuItem);
		menuItem = new JMenuItem("Increase (Up Arrow)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.increaseSpeed(m_game);
			}
		});
		submenu.add(menuItem);
		// Puis on donne quelques possibilités
		menuItem = new JMenuItem("0.5 frame / second");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setSpeedTo(m_game, 2000);
			}
		});
		submenu.add(menuItem);
		menuItem = new JMenuItem("1 frame / second");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setSpeedTo(m_game, 1000);
			}
		});
		submenu.add(menuItem);
		menuItem = new JMenuItem("10 frame / second");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setSpeedTo(m_game, 100);
			}
		});
		submenu.add(menuItem);
		menuItem = new JMenuItem("50 frames / second");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setSpeedTo(m_game, 20);
			}
		});
		submenu.add(menuItem);
		menuItem = new JMenuItem("100 frame / second");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setSpeedTo(m_game, 10);
			}
		});
		submenu.add(menuItem);
		menuItem = new JMenuItem("500 frame / second");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setSpeedTo(m_game, 2);
			}
		});
		submenu.add(menuItem);

		menuItem = new JMenuItem("Decrease (Down Arrow)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.decreaseSpeed(m_game);
			}
		});
		submenu.add(menuItem);
		menu.add(submenu);

		menu.addSeparator();
		menuItem = new JMenuItem("Restart game with empty grid (E)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.restart(m_game, "empty");
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Restart game with random grid (R)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.restart(m_game, "random");
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Restart game with full grid (T)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.restart(m_game, "full");
			}
		});
		menu.add(menuItem);

		menu.addSeparator();
		menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.exit(m_fenetre);
			}
		});
		menu.add(menuItem);
		// ------------

		// ------------
		// 2: Affichage
		menu = new JMenu("View");
		menuBar.add(menu);

		cbMenuItem = new JCheckBoxMenuItem("Enable fullscreen (F)");
		cbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.switchFullscreen(m_fenetre);
			}
		});
		itemFullscreen = cbMenuItem;
		menu.add(cbMenuItem);

		menu.addSeparator();

		cbMenuItem = new JCheckBoxMenuItem("Display grid (G)");
		cbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.switchDisplayGrid(m_game);
			}
		});
		itemGrid = cbMenuItem;
		menu.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Display statistics (S)");
		cbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.switchDisplayStats(m_game);
			}
		});
		itemStats = cbMenuItem;
		menu.add(cbMenuItem);

		// Les couleurs
		menu.addSeparator();
		submenu = new JMenu("Color modes");
		// On indique juste que l'on peut naviguer par le scroll ou C
		menuItem = new JMenuItem("Next color mode (C or Mouse Scroll)");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.doColorModeChange(m_game, m_fenetre, 1);
			}
		});
		submenu.add(menuItem);
		// On liste tous les color modes disponibles
		String[] colorModes = m_fenetre.colorModes;
		itemsColorModes = new JRadioButtonMenuItem[colorModes.length];

		String currentColorMode = m_fenetre.getColorMode();
		ButtonGroup buttonGroup = new ButtonGroup();
		for (int i = 0; i < colorModes.length; i++) {
			menuRadioItem = new JRadioButtonMenuItem(colorModes[i]);
			if (currentColorMode.equals(colorModes[i]))
				menuRadioItem.setSelected(true);

			menuRadioItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Actions.setColorModeTo(m_game, m_fenetre, ((JMenuItem) e.getSource()).getText());
				}
			});

			itemsColorModes[i] = menuRadioItem;
			buttonGroup.add(menuRadioItem);
			submenu.add(menuRadioItem);
		}
		menu.add(submenu);

		// Les tailles de block
		menu.addSeparator();
		submenu = new JMenu("Cell size");
		// On liste toutes les block sizes disponibles
		ButtonGroup sizeButtonGroup = new ButtonGroup();
		menuRadioItem = new JRadioButtonMenuItem("200x200");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 200, 200);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("100x100");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 100, 100);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("50x50");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 50, 50);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("25x25");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 25, 25);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("10x10");
		menuRadioItem.setSelected(true);
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 10, 10);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("5x5");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 5, 5);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("3x3");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 3, 3);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menuRadioItem = new JRadioButtonMenuItem("2x2");
		menuRadioItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Actions.setBlockSizeTo(m_fenetre, 2, 2);
			}
		});
		sizeButtonGroup.add(menuRadioItem);
		submenu.add(menuRadioItem);
		menu.add(submenu);
		// -------------

		m_fenetre.setJMenuBar(menuBar);
	}

}
