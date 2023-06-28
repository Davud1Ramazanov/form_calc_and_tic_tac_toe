package Client.Calc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Label;

public class ClientCalc {
    public static void main (String[] arg){
        SwingUtilities.invokeLater(() ->{

            JFrame f = new JFrame();
            JTextField tf = new JTextField();
            tf.setBounds(50,50, 100,30);
            JTextField tf2 = new JTextField();
            tf2.setBounds(200, 50, 100, 30);
            String operation[] = {"Плюс", "Минус", "Умножить", "Делить"};
            JComboBox cb = new JComboBox(operation);
            cb.setBounds(100, 100, 100, 30);
            JButton b=new JButton("Продолжить.");
            b.setBounds(100,150,155,40);
            JLabel label = new JLabel("Считывание...");
            label.setBounds(140, 170, 100, 100);

            b.addActionListener(e -> {
                try{
                    String serverName = "127.0.0.1";
                    int port = 4444;
                    Socket socket = new Socket(serverName, port);

                    OutputStream outToServer = socket.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outToServer);
                    String s1 = tf.getText();
                    String s2 = tf2.getText();
                    int a = Integer.parseInt(s1);
                    int b1 = Integer.parseInt(s2);
                    try {
                        out.writeInt(a);
                        out.writeInt(b1);
                        out.writeUTF(cb.getItemAt(cb.getSelectedIndex()).toString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    InputStream inFromServer = socket.getInputStream();
                    DataInputStream in = new DataInputStream(inFromServer);
                    label.setText("Итог: " + String.valueOf(in.readInt()));

                }catch (IOException ex){
                    ex.printStackTrace();
                }
            });

            f.add(b);
            f.add(tf);
            f.add(tf2);
            f.add(label);
            f.add(cb);
            f.setSize(400,400);
            f.setLayout(null);
            f.setVisible(true);

        });
    }
}
