package Client.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerClient {
    private static final int PORT = 4444;
    private static final String SERVER_IP = "127.0.0.1";

    public void start() {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            playGame(input, output);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGame(BufferedReader input, PrintWriter output) throws IOException {
        while (true) {
            System.out.println("Opponent's turn...");
            int row = Integer.parseInt(input.readLine());
            int col = Integer.parseInt(input.readLine());
            if (row == -1 && col == -1) {
                System.out.println("Invalid move. Please try again.");
            } else {
                makeMove(row, col);
            }

            if (checkWin()) {
                printBoard();
                System.out.println("Congratulations! Player O wins!");
                break;
            } else if (isBoardFull()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }

            printBoard();
            System.out.println("Your turn. Enter row and column (0-2): ");
            int yourRow = Integer.parseInt(input.readLine());
            int yourCol = Integer.parseInt(input.readLine());
            output.println(yourRow);
            output.println(yourCol);
            output.flush();

            if (checkWin()) {
                printBoard();
                System.out.println("Congratulations! Player X wins!");
                break;
            } else if (isBoardFull()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }
        }
    }

    private void makeMove(int row, int col) {
        System.out.println("Player O made a move at row " + row + ", column " + col);
    }

    private boolean checkWin() {
        return false;
    }

    private boolean isBoardFull() {
        return false;
    }

    private void printBoard() {

    }

    public static void main(String[] args) {
        PlayerClient client = new PlayerClient();
        client.start();
    }
}
