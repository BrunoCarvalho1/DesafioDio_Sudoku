import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku {
   private static final int SIZE = 9;
   private JTextField[][] cells = new JTextField[SIZE][SIZE];
   private int[][] board = {
           {5, 3, 0, 0, 7, 0, 0, 0, 0},
           {6, 0, 0, 1, 9, 5, 0, 0, 0},
           {0, 9, 8, 0, 0, 0, 0, 6, 0},
           {8, 0, 0, 0, 6, 0, 0, 0, 3},
           {4, 0, 0, 8, 0, 3, 0, 0, 1},
           {7, 0, 0, 0, 2, 0, 0, 0, 6},
           {0, 6, 0, 0, 0, 0, 2, 8, 0},
           {0, 0, 0, 4, 1, 9, 0, 0, 5},
           {0, 0, 0, 0, 8, 0, 0, 7, 9}
   };

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> new Sudoku().createAndShowGUI());
   }

   private void createAndShowGUI() {
      JFrame frame = new JFrame("Sudoku");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());

      JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
      Font font = new Font("Monospaced", Font.BOLD, 20);

      for (int i = 0; i < SIZE; i++) {
         for (int j = 0; j < SIZE; j++) {
            JTextField cell = new JTextField();
            cell.setHorizontalAlignment(JTextField.CENTER);
            cell.setFont(font);

            if (board[i][j] != 0) {
               cell.setText(String.valueOf(board[i][j]));
               cell.setEditable(false);
               cell.setBackground(Color.LIGHT_GRAY);
            } else {
               final int row = i, col = j;
               cell.addKeyListener(new KeyAdapter() {
                  @Override
                  public void keyReleased(KeyEvent e) {
                     String text = cell.getText();
                     if (text.matches("[1-9]")) {
                        int num = Integer.parseInt(text);
                        board[row][col] = num;
                        if (!isValidMove(board, row, col, num)) {
                           JOptionPane.showMessageDialog(frame, "Número inválido nessa posição!", "Erro", JOptionPane.ERROR_MESSAGE);
                           cell.setBackground(Color.PINK);
                        } else {
                           cell.setBackground(Color.WHITE);
                           if( isBoardCompleteAndValid()){
                              JOptionPane.showMessageDialog(frame, "Parabéns! Sudoku resolvido com sucesso!");
                           }
                        }
                     } else if (text.isEmpty()) {
                        board[row][col] = 0;
                        cell.setBackground(Color.WHITE);
                     } else {
                        cell.setText("");
                     }
                  }
               });
            }

            cells[i][j] = cell;
            gridPanel.add(cell);
         }
      }

      frame.add(gridPanel, BorderLayout.CENTER);
      frame.setSize(500, 500);
      frame.setVisible(true);
   }

   private boolean isValidMove(int[][] board, int row, int col, int num){
      for(int i = 0; i < 9; i++){
         if(i != col && board[row][i] == num ) return false;
         if(i != row && board[i][col] == num ) return false;
      }

      int startRow = row - row % 3;
      int startCol = col - col % 3;

      for(int i = startRow; i < startRow + 3; i++){
         for(int j = startCol; j < startCol + 3; j++){
            if(i != row && j != col && board[i][j] == num) return false;
         }
      }

      return true;
   }

   private boolean isBoardCompleteAndValid() {
      for (int i = 0; i < SIZE; i++) {
         boolean[] rowCheck = new boolean[SIZE + 1];
         boolean[] colCheck = new boolean[SIZE + 1];

         for (int j = 0; j < SIZE; j++) {
            int rowVal = board[i][j];
            int colVal = board[j][i];

            if (rowVal == 0 || rowCheck[rowVal]) return false;
            if (colVal == 0 || colCheck[colVal]) return false;

            rowCheck[rowVal] = true;
            colCheck[colVal] = true;
         }
      }

      for (int blockRow = 0; blockRow < SIZE; blockRow += 3) {
         for (int blockCol = 0; blockCol < SIZE; blockCol += 3) {
            boolean[] blockCheck = new boolean[SIZE + 1];
            for (int i = 0; i < 3; i++) {
               for (int j = 0; j < 3; j++) {
                  int val = board[blockRow + i][blockCol + j];
                  if (val == 0 || blockCheck[val]) return false;
                  blockCheck[val] = true;
               }
            }
         }
      }

      return true;
   }
}
