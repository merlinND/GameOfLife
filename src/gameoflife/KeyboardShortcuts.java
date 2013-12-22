package gameoflife;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Shortcuts : 
 * R : restart and fill grid randomly 
 * E : restart and empty grid
 * T : restart and fill grid fully 
 * C : cycle through color modes 
 * G : display grid on / off 
 * S : display stats on / off 
 * Spacebar : play / pause 
 * Up arrow : increase speed 
 * Down arrow : decrease speed 
 * Right arrow (while paused) : go to next generation
 * 
 * @author Merlin
 */
public class KeyboardShortcuts implements KeyListener {
	Main m_game;
	Fenetre m_fenetre;
	MouseEvents m_mouse;

	public KeyboardShortcuts(Main game, MouseEvents mouse) {
		m_game = game;

		m_fenetre = m_game.fenetre;
		m_fenetre.canvas.addKeyListener(this);
		m_fenetre.canvas.setFocusTraversalKeysEnabled(false);

		m_mouse = mouse;
	}

	/** Handle the key-released event from the text field. */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) {
			Actions.restart(m_game, "random");
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
			Actions.restart(m_game, "empty");
		}
		if (e.getKeyCode() == KeyEvent.VK_T) {
			Actions.restart(m_game, "full");
		}
		if (e.getKeyCode() == KeyEvent.VK_C) {
			Actions.doColorModeChange(m_game, m_fenetre, 1);
		}
		if (e.getKeyCode() == KeyEvent.VK_G) {
			Actions.switchDisplayGrid(m_game);
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			Actions.switchDisplayStats(m_game);
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Actions.switchPause(m_game);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) { // Diminution de la vitesse de
													// défilement
			Actions.decreaseSpeed(m_game);
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) { // Augmentation de la vitesse de
												// défilement
			Actions.increaseSpeed(m_game);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Actions.goToNextGeneration(m_game);
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			Actions.switchFullscreen(m_fenetre);
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (m_fenetre.isFullscreen())
				Actions.switchFullscreen(m_fenetre);
			else
				Actions.exit(m_fenetre);
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

}

class MouseEvents implements MouseListener, MouseMotionListener, MouseWheelListener {
	private Main m_game;
	private Fenetre m_fenetre;

	public MouseEvents(Main game) {
		m_game = game;

		m_fenetre = m_game.fenetre;
		m_fenetre.canvas.addMouseListener(this);
		m_fenetre.canvas.addMouseMotionListener(this);
		m_fenetre.canvas.addMouseWheelListener(this);
	}

	public void mousePressed(MouseEvent e) {
		// Clic gauche : On rend cette case vivante
		if (e.getButton() == MouseEvent.BUTTON1)
			doClick(e, true);
		// Clic droit : On tue cette case
		else if (e.getButton() == MouseEvent.BUTTON3)
			doClick(e, false);
	}

	public void mouseDragged(MouseEvent e) {
		// Clic gauche : On rend cette case vivante
		if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
			doClick(e, true);
		// Clic droit : On tue cette case
		else if (e.getModifiers() == MouseEvent.BUTTON3_MASK)
			doClick(e, false);
	}

	private void doClick(MouseEvent e, Boolean makeLive) {
		int xClick = e.getPoint().x;
		int yClick = e.getPoint().y;

		// On calcule sur quelle case tombe le clic
		int xCase = (int) Math.ceil(xClick / m_game.fenetre.blockWidth);
		int yCase = (int) Math.ceil(yClick / m_game.fenetre.blockHeight);

		if (makeLive)
			m_game.world.makeCellAliveAt(xCase, yCase);
		else
			m_game.world.makeCellDieAt(xCase, yCase);

		// On actualise immédiatement l'affichage et les stats
		m_game.world.refreshStats();
		m_game.refresh(false);
	}

	// Scroll de la souris : change les couleurs des cases
	public void mouseWheelMoved(MouseWheelEvent e) {
		Actions.doColorModeChange(m_game, m_fenetre, e.getWheelRotation());
	}

	// INUTILES
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {

	}

}
