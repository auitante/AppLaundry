package com.crud.applaundry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RegisterForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;

    public RegisterForm() {
        setTitle("Register Form");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField();
        btnRegister = new JButton("Register");

        add(lblUsername);
        add(txtUsername);
        add(lblPassword);
        add(txtPassword);
        add(new JLabel());
        add(btnRegister);

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                char[] password = txtPassword.getPassword();

                // Simpan data pendaftaran ke tempat yang sesuai, misalnya database atau daftar pengguna
                // Di sini, kita hanya menampilkan pesan sebagai contoh
                JOptionPane.showMessageDialog(null, "Pendaftaran berhasil!");
                dispose(); // Tutup jendela pendaftaran setelah pendaftaran berhasil
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegisterForm registerForm = new RegisterForm();
                registerForm.setVisible(true);
            }
        });
    }
}


class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister; // Tambahkan tombol Register

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10)); // Tambahkan satu baris untuk tombol Register

        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register"); // Tambahkan tombol Register

        add(lblUsername);
        add(txtUsername);
        add(lblPassword);
        add(txtPassword);
        add(new JLabel());
        add(btnLogin);
        add(new JLabel());
        add(btnRegister); // Tambahkan tombol Register

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                char[] password = txtPassword.getPassword();

                if ("admin".equals(username) && "12345678".equals(new String(password))) {
                    JOptionPane.showMessageDialog(null, "Login berhasil!");
                    dispose(); // Tutup jendela login setelah login berhasil
                    AplikasiLaundry aplikasi = new AplikasiLaundry();
                    aplikasi.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Login gagal. Coba lagi.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterForm registerForm = new RegisterForm();
                registerForm.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
    }
}