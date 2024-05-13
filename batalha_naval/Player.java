package batalha_naval;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<String> destroyedShips;

    public Player(String name) {
        this.name = name.toUpperCase();
        this.score = 0;
        this.destroyedShips = new ArrayList<>();
    }

    // Aumenta a pontuação do jogador
    public void increaseScore() {
        score++;
    }

    // Retorna a pontuação do jogador
    public int getScore() {
        return score;
    }

    // Armazena os navios destruídos pelo jogador
    public void recordDestroyedShip(String ship) {
        destroyedShips.add(ship);
    }

    // Imprime os navios destruídos pelo jogador
    public void displayDestroyedShips() {
        System.out.println("Navios destruídos pelo(a) " + name + ":");
        for (String ship : destroyedShips) {
            System.out.println(ship);
        }
    }

    public List<String> getdestroyedShips() {
        return this.destroyedShips;
    }

    // Retorna o nome do jogador
    public String getName() {
        return this.name;
    }
}
