package batalha_naval;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Scanner scanner = new Scanner(System.in); // Lê as entradas do terminal
    private int rows, cols; // Tamanho do tabuleiro
    private Player player1, player2; // Jogadores
    private int maxScore = 2; // Até quanto vai o jogo
    private Board player1Board, player2Board; // Tabuleiro dos jogadores
    private Board initialBoard1, initialBoard2; // Guarda o tabuleiro inicial
    private Board visiblePlayer1Board, visiblePlayer2Board; // Tabuleiros que irão aparecer durante o jogo
    private boolean finished = false; // Encerra o jogo

    // Inicia o jogo
    public Game() {
        clear(); // Limpa a tela
        setPlayers(); // Cria os jogadores
        skipRows(1); // Pula uma linha
        setBoards(); // Cria os tabuleiros
        player1Board.showBoard();
        player2Board.showBoard();
        start(); // Começa e encerra o jogo
        skipRows(1); // Pula linha
        showInitialBoards();
        skipRows(1);
        showDestroyedBoards();
    }

    // Começa e encerra o jogo
    public void start() {
        while (!finished) {
            playRound(player1, player2, player2Board, visiblePlayer2Board);
            if (player2.getScore() >= maxScore) {
                finished = true;
                break;
            }
            playRound(player2, player1, player1Board, visiblePlayer1Board);
            if (player1.getScore() >= maxScore) {
                finished = true;
            }
        }

        clear();
        System.out.println("O JOGO ACABOU!");
        if (player1.getScore() >= player2.getScore()) {
            System.out.println(player1.getName() + " VENCEU!");
        } else if (player2.getScore() >= player1.getScore()){
            System.out.println(player2.getName() + " VENCEU!");
        } else System.out.println("EMPATE!");
        saveLog("player1Log", player1);
        saveLog("player2Log", player2);
    }

    // Mostra os tabuleiros como estavam no início
    public void showInitialBoards() {
        System.out.println("Onde estavam os barcos: ");
        initialBoard1.showBoard();
        initialBoard2.showBoard();
    }

    // Mostra os navios destruídos por cada usuário
    public void showDestroyedBoards() {
        player1.displayDestroyedShips();
        skipRows(1);
        player2.displayDestroyedShips();
    }

    // Uma rodada do jogo
    public void playRound(Player attacker, Player defender, Board defenderBoard, Board visibleBoard) {
        for (;;) {
            visibleBoard.showBoard();
            int row = 0;
            int col = 0;

            for (;;) {
                boolean ok = false;
                try {
                    System.out.println(attacker.getName()+", digite a coordenada y do ataque (linha): ");
                    row = scanner.nextInt();

                    System.out.println(attacker.getName()+", digite a coordenada x do ataque (coluna): ");
                    col = scanner.nextInt();
                    ok = true;
                } catch (InputMismatchException e) {
                    clear();
                    System.out.println("Digite um valor numérico");
                    skipRows(1);
                }
                if(ok) break;
            }

            char mark = defenderBoard.getPosition(row, col);
            System.out.println("Você escolheu a linha: "+ row);
            System.out.println("Você escolheu a coluna: "+ col + "\n");

            if (mark == 'X' || mark == 'Y') {
                clear();
                System.out.println("Você já atacou essa posição!\n");
            } else if (mark == 'O') {
                System.out.println("Errou!");
                defenderBoard.markPosition(row, col, 'Y');
                visibleBoard.markPosition(row, col, 'Y');
                break;
            } else {
                System.out.println("Acertou um barco!");
                String ship = getShip(defenderBoard, row, col);
                System.out.println(ship);
                defenderBoard.markPosition(row, col, 'X');
                visibleBoard.markPosition(row, col, 'X');
                attacker.increaseScore();
                defender.recordDestroyedShip(ship);
                break;
            }
        }
    }

    public String getShip(Board board, int row, int col) {
        char type = board.getPosition(row, col);
        System.out.println(type);
        if (type == 'F') {
            return "Fragata";
        } else if (type == 'B') {
            return "Bote";
        } else if (type == 'D') {
            return "Destroyer";
        } else if (type == 'A') {
            return "Porta aviões";
        } else if (type == 'S') {
            return "Submarino";
        } else return null;
    }

    // Guarda um log das embarcações abatidas em um arquivo
    public void saveLog(String fileName, Player player) {
        List<String> ships = player.getdestroyedShips(); // Pegar navios detruídos pelo usuário
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Barcos detruídos pelo jogador " + player.getName() + ":\n");
            for (int i = 0; i < ships.size(); i++) {
                String ship = ships.get(i);
                writer.write(ship + "\n");
            }
            System.out.println("Log do jogador(a) " + player.getName() + " salvado no arquivo: " + fileName);
        } catch (IOException e) {
            System.err.println("Erro escrevendo o arquivo: " + e.getMessage());
        }
    }
    
    // Cria os os tabuleiros e coloca os barcos
    public void setBoards() {
        boardSize(); // Define o tamanho dos tabuleiros
        this.player1Board = new Board(rows, cols, player1.getName()); // Cria o tabuleiro do jogador 1 
        this.player2Board = new Board(rows, cols, player2.getName()); // Cria o tabuleiro do jogador 2
        this.visiblePlayer1Board = new Board(rows, cols, player1.getName()); // Cria o tabuleiro visível do jogador 1
        this.visiblePlayer2Board = new Board(rows, cols, player2.getName()); // Cria o tabuleiro visível do jogador 2
        placeShips(player1, player1Board); // Coloca os barcos no tabuleiro do jogador 1
        placeShips(player2, player2Board); // Coloca os barcos no tabuleiro do jogador 2
        this.initialBoard1 = player1Board; // Guardar o tabuleiro inicial do jogador 1
        this.initialBoard2 = player2Board; // Guardar o tabuleiro inicial do jogador 2
    
    }
    
    // Cria os jogadores
    public void setPlayers() {
        for (;;) {
            boolean ok = false;
            try {
                System.out.println("Digite o nome do jogador 1: ");
                String name = scanner.nextLine();
                this.player1 = new Player(name);
                skipRows(1);

                System.out.println("Digite o nome do jogador 2: ");
                name = scanner.nextLine();
                this.player2 = new Player(name);

                ok = true;
            } catch (InputMismatchException e) {
                clear();
                System.out.println("O nome deve ser um texto");
                skipRows(1);
            }
            if(ok) break;
        }
    }
    
    // Define tamanho do tabuleiro
    public void boardSize() {
        for (;;) {
            boolean ok = false;
            try {
                for (;;) {
                    System.out.println("Digite a altura do tabuleiro: ");
                    this.rows = scanner.nextInt();
                    if (this.rows < 10) {
                        clear();
                        System.out.println("O tabuleiro deve ter pelo menos 10 linhas!\n");
                    } else break;
                }
                for (;;) {
                    System.out.println("Digite o comprimento do tabuleiro: ");
                    this.cols = scanner.nextInt();
                    if (this.cols < 10) {
                        clear();
                        System.out.println("O tabuleiro deve ter pelo menos 10 colunas!\n");
                    } else break;
                }
                ok = true;
            } catch (InputMismatchException e) {
                clear();
                System.out.println("Digite um valor numérico");
                skipRows(1);
            }
            if(ok) break;
        }
    }

    // Limpar a tela
    public void clear() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    // Pular uma linha
    public void skipRows(int rows) {
        for (int i = 0; i < rows; i++) {
            System.out.println("\n");
        }
    }

    // Define a posição dos navios no mapa
    private void placeShips(Player player, Board board) {
        // Cria todos os barcos e coloca eles em uma lista
        List<Ship> ships = Arrays.asList(
            new AircraftCarrier(), new AircraftCarrier(),
            new Destroyer(), new Destroyer(), new Destroyer(),
            new Submarine(), new Submarine(), new Submarine(), new Submarine(),
            new Frigate(), new Frigate(), new Frigate(), new Frigate(),
            new Boat(), new Boat(), new Boat(), new Boat(), new Boat(), new Boat()
        );

        Random random = new Random();
        for (Ship ship : ships) {
            int row, col; // Linhas e colunas geradas aleatoriamente
            boolean horizontal; // Define se o barco vai ser colocado na horizontal ou vetical

            do {
                row = random.nextInt(board.getRows());
                col = random.nextInt(board.getCols());
                horizontal = random.nextBoolean(); 
            } while (!isValidPosition(board, row, col, ship, horizontal));

            markPositionsOnBoard(board, row, col, ship, horizontal);
        }
    }

    // Retorna se a posição onde se está colocando o barco é válida
    private boolean isValidPosition(Board board, int row, int col, Ship ship, boolean horizontal) {
        if (horizontal) {
            if (col + ship.getSize() > board.getCols()) {
                return false;
            }

            for (int i = col; i < col + ship.getSize(); i++) {
                if (board.getPosition(row, i) != 'O') {
                    return false;
                }
            }
        } else {
            if (row + ship.getSize() > board.getRows()) {
                return false;
            }

            for (int i = row; i < row + ship.getSize(); i++) {
                if (board.getPosition(i, col) != 'O') {
                    return false;
                }
            }
        }

        return true;
    }

    // Coloca os barcos no tabuleiro
    private void markPositionsOnBoard(Board board, int row, int col, Ship ship, boolean horizontal) {
        if (horizontal) {
            for (int i = col; i < col + ship.getSize(); i++) {
                board.markPosition(row, i, ship.getType());
            }
        } else {
            for (int i = row; i < row + ship.getSize(); i++) {
                board.markPosition(i, col, ship.getType());
            }
        }
    }

}
