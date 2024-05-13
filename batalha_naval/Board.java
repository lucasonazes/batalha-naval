package batalha_naval;

public class Board {
    private char[][] board;
    private int rows;
    private int cols;
    private String name;
    
    // Cria o tabuleiro
    public Board(int rows, int cols, String name) {
        this.rows = rows;
        this.cols = cols;
        this.name = name;
        this.board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = 'O';
            }
        }
    }

    // Altera o valor da posição do tabuleiro
    public void markPosition(int row, int col, char mark) {
        board[row][col] = mark;
    }


    // Mostra o tabuleiro
    public void showBoard() {
        System.out.println("---------------------------------");
        System.out.println("-----TABULEIRO DO(A) "+this.name+"----------");
        System.out.println("---------------------------------");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < board[i].length - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    // Retorna o tamanho do tabuleiro
    public int getLength() {
        return board.length;
    }

    // Retorna a posição do tabuleiro
    public char getPosition(int i, int j) {
        return this.board[i][j];
    }

    // Retorna o número de linhas do tabuleiro
    public int getRows() {
        return this.rows;
    }

    // Retorna o número de colunas do tabuleiro
    public int getCols() {
        return this.cols;
    }
}
