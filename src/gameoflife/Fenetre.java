package gameoflife;

/**
 * @author Merlin Nimier-David
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	private Main m_game;
	private World world;
	public Menus menus;
	private JFrame fullscreenFrame;
	public Canvas canvas;

	private int m_baseWidth;
	private int m_baseHeight;
	public int blockWidth = 10;
	public int blockHeight = 10;
	public Boolean showGrid = false;

	// private JFrame fullscreenWindow;
	private Boolean isFullscreen = false;
	private Boolean showStats = false;

	private Random random;

	private int offsetX = 0;
	private int offsetY = 0;

	public String[] colorModes = { "white", "gray", "sea", "emerald", "blood", "fire", "random", "random (dark)", "random (soft)", "colorful", "sky" };
	private String colorMode = "sky";
	private Color backgroundColor = new Color(30, 30, 35);

	public Fenetre(Main game, int baseWidth, int baseHeight) {
		m_game = game;
		world = game.world;
		menus = new Menus(m_game, this);
		canvas = new Canvas();

		this.random = new Random(System.currentTimeMillis());

		// Définit un titre pour notre fenetre
		this.setTitle("Game Of Life --- Merlin Nimier-David");

		// Terminer le processus lorsqu'on clique sur "Fermer"
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// On ajoute le contenu dans le conteneur approprié
		this.getContentPane().add(canvas);
		this.setVisible(true);

		// Définit une taille pour celle-ci
		m_baseWidth = baseWidth;
		m_baseHeight = baseHeight;
		this.setSize(new Dimension(this.getInsets().left + this.getInsets().right + baseWidth * blockWidth, this.getInsets().top + this.getInsets().bottom + this.menus.menuBar.getHeight() + baseHeight * blockHeight));
		// Et l'aligner au centre
		this.setLocationRelativeTo(null);

		// Evènement resize
		ComponentAdapter resizeHandler = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int canvasWidth = ((JFrame) e.getSource()).getWidth() - ((JFrame) e.getSource()).getInsets().left - ((JFrame) e.getSource()).getInsets().right;
				setWidth((int) Math.ceil((float) canvasWidth / (float) blockWidth));

				int canvasHeight = ((JFrame) e.getSource()).getHeight() - ((JFrame) e.getSource()).getInsets().top - ((JFrame) e.getSource()).getInsets().bottom - menus.menuBar.getHeight();
				setHeight((int) Math.ceil((float) canvasHeight / (float) blockHeight));

				canvas.setBounds(0, 0, canvasWidth, canvasHeight);
			}
		};
		this.addComponentListener(resizeHandler);
		// On trigger le resize immédiatement pour que le canvas se mette aux
		// bonnes dimensions
		resizeHandler.componentResized(new ComponentEvent(this, 0));

		// On crée le buffer
		canvas.createBufferStrategy(2);
		canvas.setBackground(backgroundColor);
		// Et on prend le focus pour recevoir les évènements clavier
		canvas.setFocusable(true);
		canvas.requestFocus();
	}

	// render : prend la grille en paramètre et l'affiche dans la fenetre
	void paint(int[][] grid, int width, int height, Boolean increaseOffset) {
		BufferStrategy buffer = canvas.getBufferStrategy();
		Graphics graphics = buffer.getDrawGraphics();

		if (increaseOffset) {
			offsetX += random.nextInt(10);
			offsetY += random.nextInt(10);
		}

		for (int y = 0; y <= height; y++) {
			for (int x = 0; x <= width; x++) {
				if (grid[x][y] == 1)
					graphics.setColor(getColorFromMode(x, y));
				else
					graphics.setColor(backgroundColor);

				graphics.fillRect(x * blockWidth, y * blockHeight, blockWidth, blockHeight);

				// Affichage de la grille si demandé
				if (showGrid) {
					graphics.setColor(new Color(50, 50, 50));
					graphics.drawRect(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
				}
			}
		}

		// STATISTIQUES
		if (showStats) {
			graphics.setColor(Color.WHITE);
			// Numéro de la génération
			graphics.drawString("Generation : " + world.getGenerationNumber(), 5, 15);
			// Nombre de cellules en vie
			graphics.drawString("Number of cells : " + world.getNumberOfCells(), 5, 30);
			// Densité actuelle
			graphics.drawString("Density : " + (float) world.getDensity() / 100, 5, 45);
			// Graphique de la densité au cours du temps
			int[] densityPoints = world.getDensityPoints();
			for (int i = 0; i < densityPoints.length; i++) {
				int value = densityPoints[i];
				if (value > 0 || (world.getGenerationNumber() - 100) >= 0) {
					if (value > 100)
						value = 100;
					graphics.drawRect(5 + i, 50 + 80 - value, 1, 1);
				}
			}
			// Petit indicateur en gris pour l'échelle du graphique
			graphics.setColor(Color.GRAY);
			graphics.drawString("]50% ", 110, 85);
		}

		// PAUSE
		if (m_game.isPaused()) {// Si l'on est en pause, on dessine un petit
								// signe dans le coin inférieur gauche
			graphics.setColor(new Color(255, 255, 255, 150));
			graphics.fillRect(20, height * blockHeight - 40, 10, 30);
			graphics.fillRect(35, height * blockHeight - 40, 10, 30);
		}

		// On en a fini avec nos graphics
		graphics.dispose();
		// On peint l'image que l'on a desssiné au fur et à mesure de la
		// fonction
		buffer.show();
		Toolkit.getDefaultToolkit().sync();
	}

	private Color getColorFromMode(int x, int y) {
		Color color = new Color(255, 255, 255);

		if (colorMode.equals("white"))
			color = Color.WHITE;
		else if (colorMode.equals("sea"))
			color = generateRandomColor(new Color(20, 40, 150), new Color(40, 60, 230));
		else if (colorMode.equals("blood"))
			color = generateRandomColor(new Color(150, 20, 40), new Color(230, 40, 60));
		else if (colorMode.equals("emerald"))
			color = generateRandomColor(new Color(50, 160, 30), new Color(70, 240, 50));
		else if (colorMode.equals("fire"))
			color = generateRandomColor(Color.RED);
		else if (colorMode.equals("random"))
			color = generateRandomColor(null);
		else if (colorMode.equals("random (dark)"))
			color = generateRandomColor(Color.BLACK);
		else if (colorMode.equals("random (soft)"))
			color = generateRandomColor(Color.WHITE);
		else if (colorMode.equals("colorful")) {
			float red = (float) Math.abs(Math.cos((x * 2 + offsetX) / 15));
			float green = (float) Math.abs(Math.sin((y * 2 + offsetY) / 17));
			float blue = (float) Math.abs(Math.sin((y + offsetX) / 15));
			color = generateColor(new Color(red, green, blue));
		} else if (colorMode.equals("sky")) {
			float red = (float) Math.abs(Math.cos((float) (x + offsetX) / blockWidth)) * 0.2f;
			float green = 0.1f + (float) Math.abs(Math.sin((float) (y + offsetY) / blockHeight)) * 0.3f;
			float blue = 0.8f + (float) Math.abs(Math.sin((float) (x + offsetX) * (y + offsetY) / blockWidth)) * 0.2f;
			color = new Color(red, green, blue);
		} else if (colorMode.equals("grey") || true) { // Par défaut, on choisit
														// la couleur grise
			color = new Color(150, 150, 150);
		}

		return color;
	}

	private Color generateRandomColor(Color mix) {
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

		// mix the color
		if (mix != null) {
			red = (red + mix.getRed()) / 2;
			green = (green + mix.getGreen()) / 2;
			blue = (blue + mix.getBlue()) / 2;
		}

		Color color = new Color(red, green, blue);
		return color;
	}

	private Color generateRandomColor(Color min, Color max) {
		int red = Math.abs(min.getRed() + random.nextInt(max.getRed() - min.getRed()));
		int green = Math.abs(min.getGreen() + random.nextInt(max.getGreen() - min.getGreen()));
		int blue = Math.abs(min.getBlue() + random.nextInt(max.getBlue() - min.getBlue()));

		Color color = new Color(red, green, blue);
		return color;
	}

	private Color generateColor(Color mix) {
		int red = 255;
		int green = 201;
		int blue = 14;

		// mix the color
		if (mix != null) {
			red = (red + mix.getRed()) / 2;
			green = (green + mix.getGreen()) / 2;
			blue = (blue + mix.getBlue()) / 2;
		}

		Color color = new Color(red, green, blue);
		return color;
	}

	public String getColorMode() {
		return colorMode;
	}

	public void setColorMode(String newColorMode) {
		colorMode = newColorMode;
	}

	public void setBlockSize(int newWidth, int newHeight) {
		blockWidth = newWidth;
		blockHeight = newHeight;

		int canvasWidth = this.getWidth() - this.getInsets().left - this.getInsets().right;
		setWidth((int) Math.ceil((float) canvasWidth / (float) blockWidth));

		int canvasHeight = this.getHeight() - this.getInsets().top - this.getInsets().bottom - menus.menuBar.getHeight();
		setHeight((int) Math.ceil((float) canvasHeight / (float) blockHeight));
	}

	public void setWorld(World theWorld) {
		world = theWorld;
	}

	public void setWidth(int newWidth) {
		world.setWidth(newWidth);
	}

	public void setHeight(int newHeight) {
		world.setHeight(newHeight);
	}

	public Boolean isFullscreen() {
		return isFullscreen;
	}

	public void switchFullscreen() {
		if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			if (!isFullscreen) { // ENTREE EN FULLSCREEN

				// On pause le timer pour etre sur qu'il n'y aura pas de
				// tentative de peinture faite pendant que l'on change de frame
				m_game.pauseTimer();

				// On crée la frame qui sera affichée en plein écran
				fullscreenFrame = new JFrame();
				fullscreenFrame.setUndecorated(true);
				fullscreenFrame.setIgnoreRepaint(true);
				fullscreenFrame.setResizable(false);

				// On se débarrasse de nos anciens buffers, qui étaient à la
				// mauvaise taille
				canvas.getBufferStrategy().dispose();

				// On passe notre canvas de l'ancienne frame à la frame qui sera
				// fullscreen
				this.getContentPane().remove(canvas);
				fullscreenFrame.getContentPane().add(canvas);

				// fullscreenFrame.setVisible(true);
				// On rend notre nouvelle frame en plein écran
				try {
					GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fullscreenFrame);
					// Workaround (see
					// http://stackoverflow.com/questions/13064607/fullscreen-swing-components-fail-to-receive-keyboard-input-on-java-7-on-mac-os-x)
					fullscreenFrame.setVisible(false);
					fullscreenFrame.setVisible(true);

				} catch (Exception e) {
					System.out.println("Exception à l'entrée du mode plein écran :");
					System.out.println(e);
				}

				// On cache l'ancienne fenetre au profit de la nouvelle
				this.setVisible(false);

				// On met notre canvas et notre grille de jeu à la bonne taille
				int canvasWidth = fullscreenFrame.getWidth();
				setWidth((int) Math.ceil((float) canvasWidth / (float) blockWidth));
				int canvasHeight = fullscreenFrame.getHeight();
				setHeight((int) Math.ceil((float) canvasHeight / (float) blockHeight));
				canvas.setBounds(0, 0, canvasWidth, canvasHeight);

				// On recrée les buffers pour notre canvas
				canvas.createBufferStrategy(2);

				canvas.requestFocus();
				m_game.playTimer();

				isFullscreen = true;
			} else { // SORTIE DU FULLSCREEN
						// On pause le timer pour etre sur qu'il n'y aura pas de
						// tentative de peinture faite pendant que l'on change
						// de frame
				m_game.pauseTimer();

				// Notre fullscreen frame n'est plus fullscreen
				try {
					GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
				} catch (Exception e) {
					System.out.println("Exception à la sortie du mode plein écran :");
					System.out.println(e);
				}

				// On passe notre canvas de la frame fullscreen à la frame
				// normale
				fullscreenFrame.getContentPane().remove(canvas);
				this.getContentPane().add(canvas, 0);

				// On se débarrasse de nos anciens buffers, qui étaient à la
				// taille fullscreen
				canvas.getBufferStrategy().dispose();

				// On remet notre canvas et grille de jeu à la taille de la
				// fenetre
				int canvasWidth = this.getWidth() - this.getInsets().left - this.getInsets().right;
				setWidth((int) Math.ceil((float) canvasWidth / (float) blockWidth));
				int canvasHeight = this.getHeight() - this.getInsets().top - this.getInsets().bottom - menus.menuBar.getHeight();
				setHeight((int) Math.ceil((float) canvasHeight / (float) blockHeight));
				canvas.setBounds(0, 0, canvasWidth, canvasHeight);
				// Et on l'aligne au centre
				this.setLocationRelativeTo(null);

				// On peut se débarasser de notre fullscreenFrame et rendre
				// visible l'ancienne à nouveau
				this.setVisible(true);
				fullscreenFrame.setVisible(false);
				fullscreenFrame.dispose();

				// On recrée les buffers pour notre canvas
				canvas.createBufferStrategy(2);

				canvas.requestFocus();
				m_game.playTimer();

				isFullscreen = false;
			}
		}
	}

	public Boolean areStatsDisplayed() {
		return showStats;
	}

	public void switchStatsDisplay() {
		if (showStats)
			showStats = false;
		else
			showStats = true;
	}

	public Boolean isGridDisplayed() {
		return showGrid;
	}

	public void switchGridDisplay() {
		if (showGrid)
			showGrid = false;
		else
			showGrid = true;
	}

}