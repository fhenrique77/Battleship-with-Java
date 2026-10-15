
package battleship;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final int SIZE = 10;

    private static final String[] SHIP_NAMES = {
            "Aircraft Carrier",
            "Battleship",
            "Submarine",
            "Cruiser",
            "Destroyer"
    };

    private static final int[] SHIP_SIZES = {5, 4, 3, 3, 2};

    // Representa um navio
    static class Ship {
        int size;
        int hits;

        Ship(int size) {
            this.size = size;
        }
    }

    // Cada jogador possui seu próprio tabuleiro
    static class Player {
        char[][] board = new char[SIZE][SIZE];
        int[][] shipIds = new int[SIZE][SIZE];
        Ship[] ships = new Ship[SHIP_NAMES.length];

        int shipCount = 0;
        int remainingCells = 17;

        Player() {
            for (int i = 0; i < SIZE; i++) {
                Arrays.fill(board[i], '~');
                Arrays.fill(shipIds[i], -1);
            }
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Player[] players = {
                new Player(),
                new Player()
        };

        // ETAPAS 1 E 2: POSICIONAR OS NAVIOS
        for (int p = 0; p < 2; p++) {

            System.out.println(
                    "Player " + (p + 1) +
                            ", place your ships on the game field"
            );

            printBoard(players[p].board);

            for (int s = 0; s < SHIP_NAMES.length; s++) {

                placeShip(
                        players[p],
                        scanner,
                        SHIP_NAMES[s],
                        SHIP_SIZES[s]
                );
            }

            passTurn(scanner);
        }

        // ETAPAS 3 A 6: JOGO COMPLETO
        int currentPlayer = 0;

        while (true) {

            int opponent = 1 - currentPlayer;

            // Tabuleiro do adversário com fog of war
            printBoard(players[opponent].board, true);

            System.out.println("---------------------");

            // Tabuleiro do jogador atual
            printBoard(players[currentPlayer].board);

            System.out.println(
                    "Player " + (currentPlayer + 1) +
                            ", it's your turn:"
            );

            boolean won = takeShot(
                    scanner,
                    players[opponent]
            );

            if (won) {
                break;
            }

            passTurn(scanner);

            // Alterna entre os jogadores
            currentPlayer = opponent;
        }

        scanner.close();
    }

    // IMPRIME O TABULEIRO NORMAL
    public static void printBoard(char[][] board) {
        printBoard(board, false);
    }

    // IMPRIME O TABULEIRO COM OU SEM FOG OF WAR
    public static void printBoard(
            char[][] board,
            boolean fogOfWar
    ) {

        System.out.println("  1 2 3 4 5 6 7 8 9 10");

        for (int i = 0; i < SIZE; i++) {

            System.out.print((char) ('A' + i) + " ");

            for (int j = 0; j < SIZE; j++) {

                char cell = board[i][j];

                if (fogOfWar && cell == 'O') {
                    cell = '~';
                }

                System.out.print(cell + " ");
            }

            System.out.println();
        }
    }

    // CONVERTE COORDENADAS COMO A1, B5 OU J10
    private static int[] parseCoordinate(String input) {

        String coordinate = input.trim()
                .toUpperCase(Locale.ROOT);

        if (!coordinate.matches("[A-J](10|[1-9])")) {
            return null;
        }

        int row = coordinate.charAt(0) - 'A';

        int col = Integer.parseInt(
                coordinate.substring(1)
        ) - 1;

        return new int[]{row, col};
    }

    // POSICIONA QUALQUER UM DOS CINCO NAVIOS
    public static void placeShip(
            Player player,
            Scanner scanner,
            String name,
            int size
    ) {

        System.out.println(
                "Enter the coordinates of the " +
                        name + " (" + size + " cells):"
        );

        while (true) {

            String[] input = scanner.nextLine()
                    .trim()
                    .split("\\s+");

            if (input.length != 2) {
                System.out.println(
                        "Error! Wrong ship location! Try again:"
                );
                continue;
            }

            int[] from = parseCoordinate(input[0]);
            int[] to = parseCoordinate(input[1]);

            // Verifica coordenadas e alinhamento
            if (from == null || to == null ||
                    (from[0] != to[0] &&
                            from[1] != to[1])) {

                System.out.println(
                        "Error! Wrong ship location! Try again:"
                );
                continue;
            }

            // Calcula o tamanho do navio
            int length = Math.max(
                    Math.abs(to[0] - from[0]),
                    Math.abs(to[1] - from[1])
            ) + 1;

            if (length != size) {
                System.out.println(
                        "Error! Wrong length of the " +
                                name + "! Try again:"
                );
                continue;
            }

            // Define a direção do navio
            int rowDirection = Integer.compare(
                    to[0], from[0]
            );

            int colDirection = Integer.compare(
                    to[1], from[1]
            );

            int[][] cells = new int[size][2];

            boolean tooClose = false;

            // Verifica todas as posições do navio
            for (int i = 0; i < size; i++) {

                int row = from[0] + i * rowDirection;
                int col = from[1] + i * colDirection;

                cells[i][0] = row;
                cells[i][1] = col;

                // Verifica as 8 posições vizinhas
                for (int r = row - 1; r <= row + 1; r++) {

                    for (int c = col - 1; c <= col + 1; c++) {

                        if (r >= 0 && r < SIZE &&
                                c >= 0 && c < SIZE &&
                                player.board[r][c] == 'O') {

                            tooClose = true;
                        }
                    }
                }
            }

            // Impede navios sobrepostos ou encostados
            if (tooClose) {

                System.out.println(
                        "Error! You placed it too close " +
                                "to another one. Try again:"
                );

                continue;
            }

            // Registra o navio
            int shipId = player.shipCount++;

            player.ships[shipId] = new Ship(size);

            // Coloca o navio no tabuleiro
            for (int[] cell : cells) {

                player.board[cell[0]][cell[1]] = 'O';

                player.shipIds[cell[0]][cell[1]] = shipId;
            }

            printBoard(player.board);

            break;
        }
    }

    // REALIZA OS DISPAROS
    public static boolean takeShot(
            Scanner scanner,
            Player opponent
    ) {

        int[] target;

        // Valida a coordenada do disparo
        while (true) {

            target = parseCoordinate(
                    scanner.nextLine()
            );

            if (target != null) {
                break;
            }

            System.out.println(
                    "Error! You entered wrong coordinates! " +
                            "Try again:"
            );
        }

        int row = target[0];
        int col = target[1];

        char cell = opponent.board[row][col];

        // ACERTOU UM NAVIO
        if (cell == 'O') {

            opponent.board[row][col] = 'X';

            Ship ship = opponent.ships[
                    opponent.shipIds[row][col]
                    ];

            ship.hits++;

            opponent.remainingCells--;

            // Verifica se todos os navios afundaram
            if (opponent.remainingCells == 0) {

                System.out.println(
                        "You sank the last ship. " +
                                "You won. Congratulations!"
                );

                return true;
            }

            // Verifica se este navio foi afundado
            if (ship.hits == ship.size) {

                System.out.println("You sank a ship!");

            } else {

                System.out.println("You hit a ship!");
            }

        } else if (cell == 'X') {

            // Disparo repetido em uma posição atingida
            System.out.println("You hit a ship!");

        } else {

            // ERROU O DISPARO
            if (cell == '~') {
                opponent.board[row][col] = 'M';
            }

            System.out.println("You missed!");
        }

        return false;
    }

    // PASSA O TURNO PARA O OUTRO JOGADOR
    private static void passTurn(Scanner scanner) {

        System.out.println(
                "Press Enter and pass the move to another player"
        );

        scanner.nextLine();

        // Simula a limpeza da tela no console
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }
}
