import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ButtonExample {
    public static void main(String[] args) {
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
        Label label = new Label("Считывание...");
        label.setBounds(140, 170, 100, 100);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String s1 = tf.getText();
                String s2 = tf2.getText();
                int a = Integer.parseInt(s1);
                int b = Integer.parseInt(s2);
                if (cb.getItemAt(cb.getSelectedIndex()).equals("Плюс")){
                    label.setText("Итог: " + (a + b));
                }
                if (cb.getItemAt(cb.getSelectedIndex()).equals("Минус")){
                    label.setText("Итог: " + (a - b));
                }
                if (cb.getItemAt(cb.getSelectedIndex()).equals("Умножить")){
                    label.setText("Итог: " + (a * b));
                }
                if (cb.getItemAt(cb.getSelectedIndex()).equals("Делить")){
                    label.setText("Итог: " + (a / b));
                }
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
    }
}  