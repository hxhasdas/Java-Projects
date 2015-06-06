package Sudoku;
/**
 * Klassen Sudoku som inneh�ller den rekursiva l�sningsmetodnen.
 * Sudoku hanterar ocks� set, get och check(sudokus regler) metoder.
 */
public class Sudoku {
	private int[][] board;

	/**
	 * Konstruktor, skapar spelbr�dan.
	 */
	public Sudoku() {
		board = new int[9][9];
	}

	/**
	 * L�ser sudokun.
	 * 
	 * @return true om det �r m�jligt, false annars.
	 */
	public boolean solve() {
		return solve(0, 0);
	}

	/**
	 * S�tter v�rdet p� positionen x, y till value.
	 * 
	 * @param x
	 *            x-koordinat
	 * @param y
	 *            y-koordinat
	 * @param value
	 *            v�rde
	 */
	public boolean set(int x, int y, int value) {
		if (check(x, y, value)) {
			board[x][y] = value;
			return true;
		}
		return false;
	}

	/**
	 * Returnerar v�rdet i koordinaterna x, y.
	 * 
	 * @param x x-koordinaten
	 * @param y y-koordinaten
	 * @return v�rdet i x, y
	 */
	public String get(int x, int y) {
		return String.valueOf(board[x][y]);
	}
	
	/**
	 * Nollst�ller sudokun.
	 */
	public void clear() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[1].length; j++) {
				board[i][j] = 0;
			}
		}
	}

	/**
	 * Unders�ker om man kan s�tta in v�rdet value i positionen x,y
	 * 
	 * @param x
	 *            x-koordinat
	 * @param y
	 *            y-koordinat
	 * @param value
	 *            v�rdet som ska s�ttas in
	 * @return true om det �r m�jligt, false annars.
	 */
	private boolean check(int x, int y, int value) {
		if(board[x][y] == value){
			return true;
		}
		else if (value <= 9 && value >= 1) {
			for (int i = 0; i < board.length; i++) {
				if (board[x][i] == value)
					return false;
			}
			for (int i = 0; i < board.length; i++) {
				if (board[i][y] == value)
					return false;
			}

			// r�knar ut vilken box value tillh�r.
			int xBox = (x / 3) * 3;
			int yBox = (y / 3) * 3;
			for (int i = xBox; i < xBox + 3; i++) {
				for (int j = yBox; j < yBox + 3; j++) {
					if (board[i][j] == value)
						return false;
				}
			}
			return true;
		} else {
			return false;
		}
			
	}
	
	/**
	 * L�ser sudokun genom en rekrusiv algoritm
	 * som anv�nder sig av backtracking
	 * 
	 * @return true om inga probem uppst�r
	 * @return false om det uppst�r ett hinder (backtracking)
	 */
	private boolean solve(int i, int j) {
		if (i == 9) {
			i = 0;
			j++;
		}
		if(j == 9)
			return true;
		if (board[i][j] == 0) {
			for (int n = 1; n < 10; ++n) {
				if (check(i, j, n)) {
					board[i][j] = n;
					if(solve(i+1, j))
						return true;
				}

			}
			board[i][j] = 0;
			return false;
		} else {
			return solve(i+1, j);
		}

	}
}