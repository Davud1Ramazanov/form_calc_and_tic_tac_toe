package Server.Calc;

import java.net.*;
import java.io.*;

public class ServerCalc extends Thread {
    private ServerSocket serverCalc;

    public ServerCalc(int port) throws IOException {
        serverCalc = new ServerSocket(port);
    }

    public void run() {
        while (true) {
            try {
                Socket server = serverCalc.accept();
                DataInputStream in = new DataInputStream(server.getInputStream());
                int a, b1;
                a = in.readInt();
                b1 = in.readInt();
                String operation = in.readUTF();
                int total = 0;
                if (operation.equals("Плюс")) {
                    total = a + b1;
                } else if (operation.equals("Минус")) {
                    total = a - b1;
                } else if (operation.equals("Умножить")) {
                    total = a * b1;
                } else if (operation.equals("Делить")) {
                    total = a / b1;
                }

                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeInt(total);
                out.flush();

                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Время сокета истекло!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        int port = 4444;
        try {
            Thread t = new ServerCalc(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
