/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ihsganjloktcpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Jose
 */
public class IHSGAnjlokTCPServer {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(6000);
            System.out.println("TCP Server Reservasi berjalan di port 6000...");
            System.out.println("Menunggu client...");

            while (true) {
                Socket client = server.accept();
                System.out.println("Client terhubung: " + client.getInetAddress());

                HandleClient handler = new HandleClient(client);
                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (Exception ex) {
            System.out.println("Error TCP Server: " + ex);
        }
    }

    static class HandleClient implements Runnable {

        private Socket client;

        public HandleClient(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String pesan = in.readLine();
                System.out.println("Menerima Perintah dari Client: " + pesan);

                String hasil = prosesPesan(pesan);
                out.println(hasil); // Kembalikan jawaban ke Client

                in.close();
                out.close();
                client.close();

            } catch (Exception ex) {
                System.out.println("Error handle client: " + ex);
            }
        }

        public String prosesPesan(String pesan) {
            String hasil = "";
            try {
                // Memecah pesan rahasia berdasarkan simbol pipa |
                String[] pecah = pesan.split("\\|");
                String perintah = pecah[0];

                if (perintah.equals("HISTORY")) {
                    hasil = getHistoryReservasi(pecah[1], pecah[2], pecah[3]);

                } else if (perintah.equals("DETAIL_HISTORY")) {
                    hasil = getDetailPesanan(Integer.parseInt(pecah[1]));

                } else if (perintah.equals("CARI_MEJA")) {
                    // pecah = [CARI_MEJA, tanggal, jam, jumlahTamu]
                    hasil = cariMejaKosong(pecah[1], pecah[2], Integer.parseInt(pecah[3]));

                } else if (perintah.equals("BOOKING_MEJA")) {
                    // pecah = [BOOKING_MEJA, userID, idMeja, tanggal, jam, jumlahTamu]
                    String tanggalLengkap = pecah[3] + " " + pecah[4] + ":00"; // YYYY-MM-DD HH:MM:SS
                    hasil = bookingMeja(Integer.parseInt(pecah[1]), Integer.parseInt(pecah[2]), tanggalLengkap, Integer.parseInt(pecah[5]));
                }

            } catch (Exception ex) {
                System.out.println("Error prosesPesan: " + ex);
            }
            return hasil;
        }

        // ==========================================
        // FUNGSI KONEKSI DATABASE (PORT 3307)
        // ==========================================
        public Connection getConnection() {
            Connection conn = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Menyesuaikan port (3307) dan nama DB (db_food_reservation)
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/db_food_reservation?useSSL=false&serverTimezone=UTC",
                        "root",
                        ""
                );
            } catch (Exception ex) {
                System.out.println("Error koneksi database: " + ex);
            }
            return conn;
        }

        // ==========================================
        // FUNGSI LOGIKA (BACK-END)
        // ==========================================
        public String cariMejaKosong(String tanggal, String jam, int jumlahTamu) {
            String hasil = "";
            try {
                Connection conn = getConnection();
                // Hanya memunculkan meja yang kapasitasnya cukup dan statusnya Tersedia
                String query = "SELECT id, nomorMeja, kapasitas, status FROM meja WHERE status = 'Tersedia' AND kapasitas >= ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, jumlahTamu);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String row = rs.getInt("id") + "|" + rs.getInt("nomorMeja") + "|" + rs.getInt("kapasitas") + "|" + rs.getString("status");
                    if (hasil.equals("")) {
                        hasil = row;
                    } else {
                        hasil = hasil + "#" + row;
                    }
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Error cariMejaKosong: " + e);
            }
            return hasil;
        }

        /**
         * FUNGSI ANTI DOUBLE BOOKING (SYNCHRONIZED)
         */
        public synchronized String bookingMeja(int userID, int idMeja, String tanggalLengkap, int jumlahTamu) {
            try {
                Connection conn = getConnection();

                // 1. Cek Ulang: Pastikan meja belum disambar orang sedetik yang lalu
                String cekQuery = "SELECT status FROM meja WHERE id = ?";
                PreparedStatement psCek = conn.prepareStatement(cekQuery);
                psCek.setInt(1, idMeja);
                ResultSet rs = psCek.executeQuery();

                boolean tersedia = false;
                if (rs.next() && rs.getString("status").equalsIgnoreCase("Tersedia")) {
                    tersedia = true;
                }
                rs.close();
                psCek.close();

                if (!tersedia) {
                    conn.close();
                    return "Gagal: Maaf, meja tersebut baru saja dipesan oleh pelanggan lain.";
                }

                // 2. Kunci Meja (Ganti status jadi 'Dipesan')
                String updateQuery = "UPDATE meja SET status = 'Dipesan' WHERE id = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateQuery);
                psUpdate.setInt(1, idMeja);
                psUpdate.executeUpdate();
                psUpdate.close();

                // 3. Masukkan data ke tabel Reservasi
                String insertQuery = "INSERT INTO reservasi (userID, mejaID, statusReservasi, tanggal, jumlahTamu) VALUES (?, ?, 'Pending', ?, ?)";
                PreparedStatement psInsert = conn.prepareStatement(insertQuery);
                psInsert.setInt(1, userID);
                psInsert.setInt(2, idMeja);
                psInsert.setTimestamp(3, java.sql.Timestamp.valueOf(tanggalLengkap));
                psInsert.setInt(4, jumlahTamu);
                psInsert.executeUpdate();
                psInsert.close();

                conn.close();
                return "SUKSES: Reservasi meja nomor " + idMeja + " berhasil! Selamat menikmati.";

            } catch (Exception e) {
                return "Error sistem: " + e.getMessage();
            }
        }

        public String getHistoryReservasi(String dariTanggal, String sampaiTanggal, String statusReservasi) {
            String hasil = "";
            try {
                Connection conn = getConnection();
                String query
                        = "SELECT r.id, u.nama AS namaCustomer, m.nomorMeja, "
                        + "DATE_FORMAT(r.tanggal, '%Y-%m-%d %H:%i:%s') AS tanggalReservasi, "
                        + "r.jumlahTamu, r.statusReservasi, "
                        + "IFNULL(SUM(dp.jumlahItem * dp.hargaSatuan), 0) AS totalPesanan "
                        + "FROM reservasi r "
                        + "JOIN users u ON r.userID = u.id "
                        + "JOIN meja m ON r.mejaID = m.id "
                        + "LEFT JOIN detailpesanan dp ON dp.reservasiID = r.id "
                        + "WHERE DATE(r.tanggal) BETWEEN ? AND ? ";

                if (!statusReservasi.equals("Semua")) {
                    query = query + "AND r.statusReservasi = ? ";
                }

                query = query
                        + "GROUP BY r.id, u.nama, m.nomorMeja, r.tanggal, r.jumlahTamu, r.statusReservasi "
                        + "ORDER BY r.tanggal DESC";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, dariTanggal);
                ps.setString(2, sampaiTanggal);

                if (!statusReservasi.equals("Semua")) {
                    ps.setString(3, statusReservasi);
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String row = rs.getInt("id") + "|" + rs.getString("namaCustomer") + "|"
                            + rs.getInt("nomorMeja") + "|" + rs.getString("tanggalReservasi") + "|"
                            + rs.getInt("jumlahTamu") + "|" + rs.getString("statusReservasi") + "|" + rs.getInt("totalPesanan");

                    if (hasil.equals("")) {
                        hasil = row;
                    } else {
                        hasil = hasil + "#" + row;
                    }
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (Exception ex) {
                System.out.println("Error getHistoryReservasi: " + ex);
            }
            return hasil;
        }

        public String getDetailPesanan(int reservasiID) {
            String hasil = "";
            try {
                Connection conn = getConnection();
                String query
                        = "SELECT mn.nama AS namaMenu, "
                        + "mn.kategori, "
                        + "dp.jumlahItem, dp.hargaSatuan, "
                        + "(dp.jumlahItem * dp.hargaSatuan) AS subtotal, dp.statusPesanan "
                        + "FROM detailpesanan dp "
                        + "JOIN menu mn ON dp.menuID = mn.id "
                        + "WHERE dp.reservasiID = ? "
                        + "ORDER BY dp.id ASC";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, reservasiID);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String row = rs.getString("namaMenu") + "|" + rs.getString("kategori") + "|"
                            + rs.getInt("jumlahItem") + "|" + rs.getInt("hargaSatuan") + "|"
                            + rs.getInt("subtotal") + "|" + rs.getString("statusPesanan");

                    if (hasil.equals("")) {
                        hasil = row;
                    } else {
                        hasil = hasil + "#" + row;
                    }
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (Exception ex) {
                System.out.println("Error getDetailPesanan: " + ex);
            }
            return hasil;
        }
    }
}
