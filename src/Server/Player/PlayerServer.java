package Server.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerServer {
    private static final int port = 4444;

    private JFrame jframe;
    private JButton[][] buttons;
    private JButton currentPlayer;
    private boolean playerEnded;

    public PlayerServer() {
        jframe = new JFrame();
        buttons = new JButton[3][3];
        currentPlayer = new JButton("X");
        playerEnded = false;
        initializationBoard();
    }

    public void initializationBoard() {
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(300, 300);
        jframe.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                jframe.add(buttons[i][j]);
            }
        }

        jframe.setVisible(true);
    }

    public void start() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is on. Waiting for the client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client is connected to the server.");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            playGame(bufferedReader, printWriter);
            socket.close();
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void playGame(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
        while (!playerEnded) {
            if (currentPlayer.getText().equals("X")) {
                printBoard();
                System.out.println("Waiting for your move...");
            } else {
                System.out.println("Opponent's turn...");
            }

            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int row = -1;
            int col = -1;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j] == currentPlayer) {
                        row = i;
                        col = j;
                    }
                }
            }

            if (makeMove(row, col)) {
                printWriter.println(row);
                printWriter.println(col);
                printWriter.flush();
            } else {
                printWriter.println(-1);
                printWriter.flush();
                System.out.println("Invalid move. Please try again.");
            }

            if (checkWin()) {
                printBoard();
                JLabel label = new JLabel();
                label.setText("Congratulations! Player " + currentPlayer.getText() + " wins!");
                playerEnded = true;
            } else if (isBoardFull()) {
                printBoard();
                JLabel label = new JLabel();
                label.setText("It's a draw");
                playerEnded = true;
            } else {
                currentPlayer.setText(currentPlayer.getText().equals("X") ? "O" : "X");
            }
        }
    }

    private boolean makeMove(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && buttons[row][col].getText().equals("")) {
            buttons[row][col].setText(currentPlayer.getText());
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWin() {
        String currentSymbol = currentPlayer.getText();
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(currentSymbol) && buttons[i][1].getText().equals(currentSymbol) && buttons[i][2].getText().equals(currentSymbol)) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(currentSymbol) && buttons[1][j].getText().equals(currentSymbol) && buttons[2][j].getText().equals(currentSymbol)) {
                return true;
            }
        }
        if (buttons[0][0].getText().equals(currentSymbol) && buttons[1][1].getText().equals(currentSymbol) && buttons[2][2].getText().equals(currentSymbol)) {
            return true;
        }
        if (buttons[2][0].getText().equals(currentSymbol) && buttons[1][1].getText().equals(currentSymbol) && buttons[0][2].getText().equals(currentSymbol)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard() {
        System.out.println("Board:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(buttons[i][j].getText() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        PlayerServer server = new PlayerServer();
        server.start();
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            synchronized (PlayerServer.this) {
                if (buttons[row][col].getText().equals("") && currentPlayer.getText().equals("X")) {
                    buttons[row][col].setText(currentPlayer.getText());
                    currentPlayer.setText("O");
                    PlayerServer.this.notify();
                }
            }
        }
    }
}
