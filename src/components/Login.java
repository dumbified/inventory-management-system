package components;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    private JFrame frame;

    public Login() {
        // Main frame setup
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        placeComponents(panel);

        frame.setVisible(true);

        // set image icon
        ImageIcon icon = new ImageIcon("src/img/711.png");
        frame.setIconImage(icon.getImage());
    }

    //login form
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel headerLabel = new JLabel("Inventory Management System");
        headerLabel.setBounds(10, 10, 280, 25);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 16));
        panel.add(headerLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 50, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(110, 50, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 80, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(110, 80, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(110, 120, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());

                if (authenticate(user, password)) {
                    //success
                    ImageIcon successIcon = new ImageIcon("src/img/success.png");
                    String successMessage = "<html><font color='green'>Login successful!</font></html>";
                    JOptionPane.showMessageDialog(frame, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                    frame.dispose(); // Close the login window
                    
                    new App(); // Open the main inventory management system after successful login
                } else {
                    //failed
                    ImageIcon errorIcon = new ImageIcon("src/img/failed.png");
                    String errorMessage = "<html><font color='red'>做得好 social credit -100000 ↓</font></html>";
                    JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE, errorIcon);
                }
            }
        });
    }

    //user authentication
    private boolean authenticate(String user, String password) {
        return "admin".equals(user) && "123456".equals(password);
    }

    public static void main(String[] args) {
        new Login();
    }
}
