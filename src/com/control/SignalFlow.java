package com.control;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignalFlow {
   // private JButton Button;
    private JPanel panel1;
    private JButton button1;

    private void createUIComponents(){

    }
    public SignalFlow() {

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JOptionPane.showMessageDialog(null,"Hello, Mayoyyy <3");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SignalFlow");
        frame.setContentPane(new SignalFlow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
