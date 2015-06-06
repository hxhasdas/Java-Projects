package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.io.*;

/**
 * Klassen SudokuGUI som skapar GUI't och h�ller reda p� anv�ndarens handlingar,
 * dvs knapptryck och ifyllda l�sningar.
 */

public class SudokuGUI {
	private Scanner scan = null;
	private boolean solved;
	private boolean solution = true;
	private boolean incorrectInput = false;
	private int count = 0;
	private Sudoku sudoku;
	private JFrame frame;
	private JPanel boardPanel;
	private JPanel buttonPanel;
	private JTextField[][] board;

	/**
	 * Konstruktor.
	 * Skapar GUI't med alla komponenter.
	 */
	public SudokuGUI() {
		sudoku = new Sudoku();

		frame = new JFrame("SudokuSolver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		boardPanel = new JPanel(new GridLayout(9, 9));
		frame.add(boardPanel, BorderLayout.NORTH);
		board = new JTextField[9][9];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[1].length; j++) {
				board[i][j] = new JTextField(1);
				board[i][j].setEditable(true);
				boardPanel.add(board[i][j]);
			}
		}
		boardColorer();

		buttonPanel = new JPanel(new FlowLayout());
		frame.add(buttonPanel);
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ClearListener());
		buttonPanel.add(clear);
		JButton generate = new JButton("Generate");
		generate.addActionListener(new GenerateListener());
		buttonPanel.add(generate);
		JButton solution = new JButton("Check Solution");
		solution.addActionListener(new SolutionListener());
		buttonPanel.add(solution);
		JButton solve = new JButton("Solve");
		solve.addActionListener(new SolveListener());
		buttonPanel.add(solve);
		frame.pack();
		frame.setSize(235, 270);
		frame.setVisible(true);
	}

	/**
	 * Nollst�ller spelbr�dan.
	 */
	private void clear() {
		solved = false;
		sudoku.clear();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j].setText("");
				board[i][j].setEditable(true);
			}
		}
	}

	/**
	 * F�rgkodar fyra 3x3 kvadrater f�r att anv�nderen l�ttare ska kunna skilja
	 * dem �t.
	 */
	private void boardColorer() {
		for (int i = 0; i < 3; i++) {
			for (int j = 3; j < 6; j++) {
				board[j][i].setBackground(Color.LIGHT_GRAY);
			}
		}
		for (int i = 3; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				board[j][i].setBackground(Color.LIGHT_GRAY);
			}
		}
		for (int i = 6; i < 9; i++) {
			for (int j = 3; j < 6; j++) {
				board[j][i].setBackground(Color.LIGHT_GRAY);
			}
		}
		for (int i = 3; i < 6; i++) {
			for (int j = 6; j < 9; j++) {
				board[j][i].setBackground(Color.LIGHT_GRAY);
			}
		}
	}

	/**
	 * H�mtar talen fr�n board och returnerar sant om det gick, falskt annars.
	 * 
	 * @return true om talen gick att h�mta, false annars
	 */
	private boolean get() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[1].length; j++) {
				if (board[i][j].getText().compareToIgnoreCase("") != 0) {
					try {
						if (!(sudoku.set(j, i, Integer.parseInt(board[i][j]
								.getText())))) {
							return false;
						}
					} catch (Exception e) {
						
						JOptionPane.showMessageDialog(null,
								"Endast siffror kan anv�ndas i sudoku.",
								"Felmeddelande", JOptionPane.ERROR_MESSAGE);
						incorrectInput = true;
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Skriver ut sudokun i GUI't.
	 */
	private void show() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[1].length; j++) {
				board[j][i].setText(sudoku.get(i, j));
				if (board[j][i].getText().compareTo("") != 0)
					board[j][i].setEditable(false);
			}
		}
	}

	/**
	 * Privat lyssnarklass till knappen Clear.
	 * Nollst�ller spelbr�dan samt sudokun.
	 */
	private class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clear();
		}
	}

	/**
	 * Privat lyssnarklass till knappen Solve. H�mtar talen i board och l�ser
	 * sudokun. Felmeddelande visas om den inte g�r att l�sa.
	 */
	private class SolveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sudoku.clear();
			if (solved)
				JOptionPane.showMessageDialog(null, "Sudokun �r redan l�st.",
						"Obs!", JOptionPane.INFORMATION_MESSAGE);
			else if (!get() || !sudoku.solve() || incorrectInput) {
				incorrectInput = false;
				solved = false;
				solution = false;
				JOptionPane.showMessageDialog(null,
						"Sudokun kunde inte l�sas.", "Felmeddelande",
						JOptionPane.ERROR_MESSAGE);
				solved = false;
			} 
			else if (sudoku.solve() && !incorrectInput) {
				solved = true;
				solution = false;
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board.length; j++) {
						board[i][j].setEditable(false);
					}
				}
				show();
			}
		}
	}

	/**
	 * Privat lyssnarklass till knappen Generate.
	 * �ppnar ett filv�ljarf�nster om ingen fil redan �r inl�st. L�ser in en fil med sudoku.
	 * Skriver ut sudokun i f�nstret en i taget.
	 * Pre: Filen inneh�ller sudoku. 
	 *
	 */
	private class GenerateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String line;
			Scanner lineScanner;
			clear();
			solved = false;
			if (count == 0 || !scan.hasNextLine()) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(chooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						scan = new Scanner(file);
					} catch (FileNotFoundException f) {
						System.out.println("Kan inte �ppna filen: Sudoku");
						System.exit(1);
					}
					for (int i = 0; i < 9; i++) {
						line = scan.nextLine();
						lineScanner = new Scanner(line);
						for (int j = 0; j < 9; j++) {
							int nbr = lineScanner.nextInt();
							if (nbr > 0) {
								board[i][j].setText(String.valueOf(nbr).trim());
								board[i][j].setEditable(false);
							} else {
								board[i][j].setEditable(true);
							}
						}
					}
					count++;
				} 
			}else if (count > 0 && scan.hasNextLine()) {
				line = scan.nextLine();
				for (int i = 0; i < 9; i++) {
					line = scan.nextLine();
					lineScanner = new Scanner(line);
					for (int j = 0; j < 9; j++) {
						int nbr = lineScanner.nextInt();
						if (nbr > 0) {
							board[i][j].setText(String.valueOf(nbr).trim());
							board[i][j].setEditable(false);
						} else {
							board[i][j].setEditable(true);
						}
					}
				}
				count++;
			}
		}
	}
	
	/**
	 * Privat lyssnarklass till knappen Solution. 
	 * Kollar om den inmatade l�sningen �r korrekt.
	 */
	private class SolutionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int i = 0;
			get();
			sudoku.solve();
			while (solution && i < board.length) {
				for (int j = 0; j < board.length; j++) {
					if (board[j][i].getText().compareToIgnoreCase(
							sudoku.get(i, j)) != 0) {
						solution = false;
						JOptionPane.showMessageDialog(null,
								"Ingen giltlig l�sning.", "Obs!",
								JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
				i++;
			}
			if (solution)
				JOptionPane.showMessageDialog(null, "Du har l�st sudokun.",
						"Grattis", JOptionPane.INFORMATION_MESSAGE);
			else if (solved && !solution) {
				JOptionPane.showMessageDialog(null,
						"Datorn har redan l�st sudokun.", "Obs!",
						JOptionPane.INFORMATION_MESSAGE);
			}
			solution = true;
		}
	}
}
