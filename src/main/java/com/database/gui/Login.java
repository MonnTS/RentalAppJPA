package com.database.gui;

import org.postgresql.Driver;

import javax.swing.*;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public final class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField username;
    private JButton btnLogin;
    private JLabel lblPassword;
    private JLabel lblUsername;
    private JPasswordField txtPassword;
    private static JFrame frame;

    public Login() {
        btnLogin.addActionListener(e -> {
            try {
                logIn();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Rental App");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new Login().mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void logIn() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            DriverManager.registerDriver(new Driver());
            String url = "jdbc:postgresql://localhost/";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "Azure2022");
            connection = DriverManager.getConnection(url, props);
            String preparedQuery = "SELECT * FROM users WHERE username = ? AND password = crypt(?, password)";
            preparedStatement = connection.prepareStatement(preparedQuery);
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, new String(txtPassword.getPassword()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                preparedStatement.close();
                JOptionPane.showMessageDialog(null, "Welcome user " + username.getText());
                new CarGUI();
                frame.dispose();
                return;
            }

            JOptionPane.showMessageDialog(null, "Invalid credentials");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } finally {
            Objects.requireNonNull(preparedStatement).close();
            connection.close();
        }
    }
}
