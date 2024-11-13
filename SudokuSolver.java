import javax.swing.*;
import java.awt.*;

public class SudokuSolver {
    private static final int SIZE = 9; // Size of the Sudoku grid (9x9)
    private JFrame frame;
    private JTextField[][] cells;
    private JButton solveButton, resetButton;

    public SudokuSolver() {
        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));
        cells = new JTextField[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                gridPanel.add(cells[row][col]);
            }
        }

        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        resetButton = new JButton("Reset");

        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);

        solveButton.addActionListener(e -> solveSudoku());
        resetButton.addActionListener(e -> resetGrid());

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Method to solve the Sudoku
    private void solveSudoku() {
        int[][] board = getBoard();

        // Check if the input board is valid before solving
        if (!isValidBoard(board)) {
            JOptionPane.showMessageDialog(frame, "Invalid Sudoku input. Please check the puzzle and try again.");
            return;
        }

        if (solveSudokuUtil(board)) {
            updateGrid(board);
            JOptionPane.showMessageDialog(frame, "Sudoku Solved!");
        } else {
            JOptionPane.showMessageDialog(frame, "No solution exists for the given Sudoku puzzle.");
        }
    }

    // Recursive backtracking algorithm to solve Sudoku
    private boolean solveSudokuUtil(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudokuUtil(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Validate if placing num at (row, col) is valid
    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    // Method to validate the initial board input by the user
    private boolean isValidBoard(int[][] board) {
        // Check rows and columns
        for (int i = 0; i < SIZE; i++) {
            boolean[] rowCheck = new boolean[SIZE + 1];
            boolean[] colCheck = new boolean[SIZE + 1];
            for (int j = 0; j < SIZE; j++) {
                // Validate row
                if (board[i][j] != 0) {
                    if (rowCheck[board[i][j]]) {
                        return false;
                    }
                    rowCheck[board[i][j]] = true;
                }

                // Validate column
                if (board[j][i] != 0) {
                    if (colCheck[board[j][i]]) {
                        return false;
                    }
                    colCheck[board[j][i]] = true;
                }
            }
        }

        // Check 3x3 subgrids
        for (int row = 0; row < SIZE; row += 3) {
            for (int col = 0; col < SIZE; col += 3) {
                boolean[] subgridCheck = new boolean[SIZE + 1];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int num = board[row + i][col + j];
                        if (num != 0) {
                            if (subgridCheck[num]) {
                                return false;
                            }
                            subgridCheck[num] = true;
                        }
                    }
                }
            }
        }

        return true; // The board is valid
    }

    // Method to reset the Sudoku grid
    private void resetGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText("");
            }
        }
    }

    // Method to get current board state from GUI
    private int[][] getBoard() {
        int[][] board = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText();
                board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
        return board;
    }

    // Method to update GUI grid with current board state
    private void updateGrid(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}
