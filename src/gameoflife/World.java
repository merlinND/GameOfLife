package gameoflife;

import java.util.ArrayList;

/**
 * @author Merlin
 */
public class World {
	// PROPRIETES
	private int xMax, yMax, activeX, activeY;
	private int[][] grid;
	private int[][] nextGrid;
	private int generationNumber;

	private int numberOfCells;
	private int density;
	private ArrayList<String> densityPoints;

	// METHODES

	// Constructeur
	public World() {
		xMax = 9;
		yMax = 9;
		init();
	}

	public World(int customXMax, int customYMax) {
		xMax = customXMax;
		yMax = customYMax;
		init();
	}

	// Méthode d'initialisation
	private void init() {
		generationNumber = 0;

		activeX = 0;
		activeY = 0;
		grid = new int[xMax + 1][yMax + 1];
		nextGrid = new int[xMax + 1][yMax + 1];

		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= xMax; x++) {
				// Remplissage vide
				grid[x][y] = 0;
			}
		}

		// Statistiques
		numberOfCells = 0;
		density = 0;
		densityPoints = new ArrayList<String>();
		densityPoints.add(String.valueOf(density));
	}

	// Remplit le monde de manière aléatoire
	void fillRandom() {
		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= xMax; x++) {
				// Remplissage aléatoire
				if ((int) Math.round(Math.random()) == 1) {
					grid[x][y] = 1;
				} else
					grid[x][y] = 0;
			}
		}
		refreshStats();
	}

	// Remplit le monde de manière en entier
	void fillFull() {
		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= xMax; x++) {
				// Remplissage aléatoire
				grid[x][y] = 1;
			}
		}
		refreshStats();
	}

	void fillWithM() {
		int xMiddle = Math.round(xMax / 2);
		int yMiddle = Math.round(yMax / 2);

		// La première branche du M
		makeCellAliveAt(xMiddle - 2, yMiddle - 2);
		makeCellAliveAt(xMiddle - 2, yMiddle - 1);
		makeCellAliveAt(xMiddle - 2, yMiddle);
		makeCellAliveAt(xMiddle - 2, yMiddle + 1);
		makeCellAliveAt(xMiddle - 2, yMiddle + 2);
		// Les pixels du milieu
		makeCellAliveAt(xMiddle - 1, yMiddle - 1);
		makeCellAliveAt(xMiddle, yMiddle);
		makeCellAliveAt(xMiddle + 1, yMiddle - 1);
		// La seconde branche
		makeCellAliveAt(xMiddle + 2, yMiddle - 2);
		makeCellAliveAt(xMiddle + 2, yMiddle - 1);
		makeCellAliveAt(xMiddle + 2, yMiddle);
		makeCellAliveAt(xMiddle + 2, yMiddle + 1);
		makeCellAliveAt(xMiddle + 2, yMiddle + 2);
		refreshStats();
	}

	// Affiche le monde dans son état actuel par la console
	public void displayWorld() {
		System.out.println();
		System.out.println();
		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= xMax; x++) {
				// System.out.print("[" + grid[x][y] + "]");

				if (grid[x][y] == 1)
					System.out.print(" ■ ");
				else
					System.out.print(" □ ");
			}
			System.out.print("\n");
		}
		// On affiche ensuite un séparateur
		// for(int i = 0; i <= xMax; i++) System.out.print("---");
		System.out.println();
		System.out.println();
	}

	// Passe à la génération suivante
	public void iterate() {
		numberOfCells = 0;
		// On parcourt toutes les cases pour appliquer les règles à chacune
		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= xMax; x++) {
				setActiveSquare(x, y);
				int sum = bool2int(n()) + bool2int(ne()) + bool2int(e()) + bool2int(se()) + bool2int(s()) + bool2int(sw()) + bool2int(w()) + bool2int(nw());

				if (sum == 3 && !isActiveSquareAlive()) {
					makeCellAliveAt(x, y, true);
					numberOfCells++;
				} else if ((sum == 2 || sum == 3) && isActiveSquareAlive()) {
					makeCellAliveAt(x, y, true);
					numberOfCells++;
				} else
					makeCellDieAt(x, y, true);
			}
		}

		// Après avoir parcouru toutes les cases, on effectue le passage à la
		// prochaine génération
		grid = nextGrid;
		nextGrid = new int[xMax + 1][yMax + 1];
		generationNumber++;

		// On calcule la densité pour cette génération
		density = Math.round(100 * (float) numberOfCells / (float) (getWidth() * getHeight()));
		if (density < 0)
			density = 0;
		if (density > 100)
			density = 100;

		// Et on enregistre ce point de densité dans le tableau qui en garde la
		// trace
		densityPoints.ensureCapacity(generationNumber + 5);
		densityPoints.add(String.valueOf(density));
	}

	public int[][] getGrid() {
		return grid;
	}

	// Gestion des dimensions du monde
	public int getWidth() {
		return xMax;
	}

	public void setWidth(int newWidth) {
		int[][] newGrid = new int[newWidth][yMax + 1];
		// On remplit la nouvelle grille par l'ancienne
		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= (newWidth - 1); x++) {
				if (x <= xMax)
					newGrid[x][y] = grid[x][y];
				else
					// Remplissage des cases qui n'existaient pas précédemment
					newGrid[x][y] = 0;
			}
		}
		// Puis on switch
		grid = newGrid;
		nextGrid = new int[newWidth][yMax + 1];
		xMax = newWidth - 1;
	}

	public int getHeight() {
		return yMax;
	}

	public void setHeight(int newHeight) {
		int[][] newGrid = new int[xMax + 1][newHeight];

		// On remplit la nouvelle grille par l'ancienne
		for (int y = 0; y <= (newHeight - 1); y++) {
			for (int x = 0; x <= xMax; x++) {
				if (y <= yMax)
					newGrid[x][y] = grid[x][y];
				else
					// Remplissage des cases qui n'existaient pas précédemment
					newGrid[x][y] = 0;
			}
		}
		// Puis on switch
		grid = newGrid;
		nextGrid = new int[xMax + 1][newHeight];
		yMax = newHeight - 1;
	}

	// Statistiques
	public void refreshStats() {
		numberOfCells = 0;
		// On recompte le nombre de cellules
		for (int y = 0; y <= yMax; y++) {
			for (int x = 0; x <= xMax; x++) {
				if (isCellAliveAt(x, y))
					numberOfCells++;
			}
		}

		// On recalcule la densité
		density = Math.round(100 * (float) numberOfCells / (float) (getWidth() * getHeight()));
		if (density < 0)
			density = 0;
		if (density > 100)
			density = 100;

		// Et on réajuste le point dans le tableau des densités
		densityPoints.set(generationNumber, String.valueOf(density));

	}

	public int getNumberOfCells() {
		return numberOfCells;
	}

	public int getDensity() {
		return density;
	}

	public int[] getDensityPoints() {
		int capacity = 100;
		int[] latestPoints = new int[capacity];
		for (int i = 0; i < capacity; i++) {
			if ((densityPoints.size() - capacity + i) < densityPoints.size() && (densityPoints.size() - capacity + i) >= 0) {
				latestPoints[i] = Integer.parseInt(densityPoints.get(densityPoints.size() - capacity + i));
			}
		}

		return latestPoints;
	}

	// Rend une cellule donnée vivante
	public void makeCellAliveAt(int x, int y) {
		if (x >= 0 && x <= xMax && y >= 0 && y <= yMax)
			grid[x][y] = 1;
	}

	// Surcharge : rend la cellule vivante, mais dans la prochaine génération
	// seulement
	public void makeCellAliveAt(int x, int y, boolean nextGeneration) {
		if (nextGeneration)
			nextGrid[x][y] = 1;
		else
			grid[x][y] = 1;
	}

	// Rend une cellule donnée morte
	public void makeCellDieAt(int x, int y) {
		if (x >= 0 && x <= xMax && y >= 0 && y <= yMax)
			grid[x][y] = 0;
	}

	// Surcharge : rend la cellule morte, mais dans la prochaine génération
	// seulement
	public void makeCellDieAt(int x, int y, boolean nextGeneration) {
		if (nextGeneration)
			nextGrid[x][y] = 0;
		else
			grid[x][y] = 0;
	}

	// Rend une case donnée active
	private void setActiveSquare(int x, int y) {
		activeX = x;
		activeY = y;
	}

	// Donne l'état de la case active
	public boolean isActiveSquareAlive() {
		if (grid[activeX][activeY] == 1)
			return true;
		else
			return false;
	}

	// Renvoie l'état de la case demandée
	public boolean isCellAliveAt(int x, int y) {
		if (grid[x][y] == 1)
			return true;
		else
			return false;
	}

	// Renvoie l'état de la case Nord, Nord-Est, Est, .. de la case active
	public boolean n() {
		if (grid[mod(activeX, xMax + 1)][mod(activeY - 1, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean ne() {
		if (grid[mod(activeX + 1, xMax + 1)][mod(activeY - 1, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean e() {
		if (grid[mod(activeX + 1, xMax + 1)][mod(activeY, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean se() {
		if (grid[mod(activeX + 1, xMax + 1)][mod(activeY + 1, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean s() {
		if (grid[mod(activeX, xMax + 1)][mod(activeY + 1, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean sw() {
		if (grid[mod(activeX - 1, xMax + 1)][mod(activeY + 1, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean w() {
		if (grid[mod(activeX - 1, xMax + 1)][mod(activeY, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	public boolean nw() {
		if (grid[mod(activeX - 1, xMax + 1)][mod(activeY - 1, yMax + 1)] == 1)
			return true;
		else
			return false;
	}

	// HELPERS
	// Convertit un boolean en int
	public static int bool2int(boolean toConvert) {
		if (toConvert)
			return 1;
		else
			return 0;
	}

	// Opération modulo
	public static int mod(int a, int b) {
		int result = a % b;
		if (result < 0)
			result += b;
		return result;
	}

	int getGenerationNumber() {
		return generationNumber;
	}
}
