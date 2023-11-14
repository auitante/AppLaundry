package com.crud.applaundry;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class AplikasiLaundry extends JFrame {

    private JPanel inputPanel;

    private JTextField txtNama, txtAlamat, txtBerat, txtBiaya;
    private JComboBox<String> cmbJenisCucian;
    private JButton btnHitung, btnReset, btnSimpan, btnCetakNota, btnLogout;
    private JTextArea txtRiwayat;
    private List<Pelanggan> daftarPelanggan;
    private CetakNota cetakNotaFrame;

    private String[] jenisCucianOptions = {"Cuci Basah", "Cuci Kering", "Cuci Setrika"};
    private double[] biayaPerJenisCucian = {10000, 15000, 20000}; // Biaya per jenis cucian

    public AplikasiLaundry() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Aplikasi Laundry");
        setSize(600, 400);
        setLayout(new BorderLayout());

        daftarPelanggan = new ArrayList<>();

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNama = new JLabel("Nama Pelanggan:");
        txtNama = new JTextField();
        JLabel lblAlamat = new JLabel("Alamat Pelanggan:");
        txtAlamat = new JTextField();
        JLabel lblJenisCucian = new JLabel("Jenis Cucian:");
        cmbJenisCucian = new JComboBox<>(jenisCucianOptions);
        JLabel lblBerat = new JLabel("Berat Pakaian (kg):");
        txtBerat = new JTextField();
        JLabel lblBiaya = new JLabel("Biaya Laundry:");
        txtBiaya = new JTextField();

        btnHitung = new JButton("Hitung");
        btnReset = new JButton("Reset");
        btnSimpan = new JButton("Simpan Pesanan");
        btnLogout = new JButton("Log Out");
        btnCetakNota = new JButton("Cetak Nota");

        inputPanel.add(lblNama);
        inputPanel.add(txtNama);
        inputPanel.add(lblAlamat);
        inputPanel.add(txtAlamat);
        inputPanel.add(lblJenisCucian);
        inputPanel.add(cmbJenisCucian);
        inputPanel.add(lblBerat);
        inputPanel.add(txtBerat);
        inputPanel.add(lblBiaya);
        inputPanel.add(txtBiaya);



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnHitung);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnLogout);
        buttonPanel.add(btnCetakNota);

        txtRiwayat = new JTextArea();
        txtRiwayat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtRiwayat);

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        historyPanel.add(new JLabel("Riwayat Pesanan:"), BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.SOUTH);
        inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        btnHitung.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String namaPelanggan = txtNama.getText();
                String alamatPelanggan = txtAlamat.getText();
                String jenisCucian = (String) cmbJenisCucian.getSelectedItem();
                double beratPakaian = Double.parseDouble(txtBerat.getText());

                double biaya = hitungBiayaLaundry(jenisCucian, beratPakaian);

                txtBiaya.setText(String.valueOf(biaya));
            }
        });

        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtNama.setText("");
                txtAlamat.setText("");
                cmbJenisCucian.setSelectedIndex(0);
                txtBerat.setText("");
                txtBiaya.setText("");
            }
        });

        btnSimpan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String namaPelanggan = txtNama.getText();
                String alamatPelanggan = txtAlamat.getText();
                String jenisCucian = (String) cmbJenisCucian.getSelectedItem();
                double beratPakaian = Double.parseDouble(txtBerat.getText());
                double biaya = Double.parseDouble(txtBiaya.getText());
                Date tanggalPesan = new Date(); // Tanggal pesan saat ini
                Date tanggalSelesai = calculateTanggalSelesai(tanggalPesan);

                Pelanggan pelanggan = new Pelanggan(namaPelanggan, alamatPelanggan, jenisCucian, beratPakaian, biaya, tanggalPesan, tanggalSelesai);
                daftarPelanggan.add(pelanggan);

                // Simpan data ke database
                simpanDataPelanggan(namaPelanggan, alamatPelanggan, jenisCucian, beratPakaian, biaya, tanggalPesan, tanggalSelesai);

                JOptionPane.showMessageDialog(null, "Pesanan berhasil disimpan!");

                updateRiwayat();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(AplikasiLaundry.this, "Anda yakin ingin keluar?", "Konfirmasi Log Out", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Tutup jendela aplikasi
                    dispose();
                }
            }
        });

        btnCetakNota.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ambil riwayat dari txtRiwayat dan set di jendela CetakNota
                cetakNotaFrame.setCetakNotaText(txtRiwayat.getText());
                cetakNotaFrame.setVisible(true);
            }
        });

        cetakNotaFrame = new CetakNota();
    }

    private double hitungBiayaLaundry(String jenisCucian, double beratPakaian) {
        // Cari biaya berdasarkan jenis cucian
        double biayaPerKilogram = 0;
        for (int i = 0; i < jenisCucianOptions.length; i++) {
            if (jenisCucian.equals(jenisCucianOptions[i])) {
                biayaPerKilogram = biayaPerJenisCucian[i];
                break;
            }
        }

        // Hitung biaya
        return beratPakaian * biayaPerKilogram;
    }

    private Date calculateTanggalSelesai(Date tanggalPesan) {
        // Misalnya, hitung tanggal selesai 3 hari dari tanggal pesan
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggalPesan);
        cal.add(Calendar.DAY_OF_MONTH, 2);
        return cal.getTime();
    }

    private void updateRiwayat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        txtRiwayat.append("\n Nama: " + daftarPelanggan.get(daftarPelanggan.size() - 1).getNama() +
                "\n Alamat: " + daftarPelanggan.get(daftarPelanggan.size() - 1).getAlamat() +
                "\n Jenis Cucian: " + daftarPelanggan.get(daftarPelanggan.size() - 1).getJenisCucian() +
                "\n Berat: " + daftarPelanggan.get(daftarPelanggan.size() - 1).getBeratPakaian() + " kg" +
                "\n Biaya: " + daftarPelanggan.get(daftarPelanggan.size() - 1).getBiaya() +
                "\n Tanggal Pesan: " + dateFormat.format(daftarPelanggan.get(daftarPelanggan.size() - 1).getTanggalPesan()) +
                "\n Tanggal Selesai: " + dateFormat.format(daftarPelanggan.get(daftarPelanggan.size() - 1).getTanggalSelesai()) +
                "\n");
    }

    private void simpanDataPelanggan(String nama, String alamat, String jenisCucian, double berat, double biaya, Date tanggalPesan, Date tanggalSelesai) {
        try (Connection connection = Conn.connect()) {
            String query = "INSERT INTO laundry_orders (nama, alamat, jenis_cucian, berat, biaya, tanggal_pesan, tanggal_selesai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nama);
                preparedStatement.setString(2, alamat);
                preparedStatement.setString(3, jenisCucian);
                preparedStatement.setDouble(4, berat);
                preparedStatement.setDouble(5, biaya);
                preparedStatement.setDate(6, new java.sql.Date(tanggalPesan.getTime()));
                preparedStatement.setDate(7, new java.sql.Date(tanggalSelesai.getTime()));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data ke database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
    }
}

class Pelanggan {
    private String nama;
    private String alamat;
    private String jenisCucian;
    private double beratPakaian;
    private double biaya;
    private Date tanggalPesan;
    private Date tanggalSelesai;

    public Pelanggan(String nama, String alamat, String jenisCucian, double beratPakaian, double biaya, Date tanggalPesan, Date tanggalSelesai) {
        this.nama = nama;
        this.alamat = alamat;
        this.jenisCucian = jenisCucian;
        this.beratPakaian = beratPakaian;
        this.biaya = biaya;
        this.tanggalPesan = tanggalPesan;
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getJenisCucian() {
        return jenisCucian;
    }

    public double getBeratPakaian() {
        return beratPakaian;
    }

    public double getBiaya() {
        return biaya;
    }

    public Date getTanggalPesan() {
        return tanggalPesan;
    }

    public Date getTanggalSelesai() {
        return tanggalSelesai;
    }
}
