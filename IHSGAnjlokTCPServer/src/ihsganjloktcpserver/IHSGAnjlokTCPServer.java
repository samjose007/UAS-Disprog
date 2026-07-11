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

                String pesan;
                while ((pesan = in.readLine()) != null) {
                    System.out.println("Menerima Perintah dari Client: " + pesan);

                    String hasil = prosesPesan(pesan);
                    out.println(hasil);
                }

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
                String[] pecah = pesan.split("\\|");
                String perintah = pecah[0];

                if (perintah.equals("HISTORY")) {
                    hasil = getHistoryReservasi(pecah[1], pecah[2], pecah[3], pecah[4], pecah[5]);

                } else if (perintah.equals("DETAIL_HISTORY")) {
                    hasil = getDetailPesanan(Integer.parseInt(pecah[1]));

                } else if (perintah.equals("CARI_MEJA")) {
                    hasil = cariMejaKosong(pecah[1], pecah[2], Integer.parseInt(pecah[3]));

                } else if (perintah.equals("BOOKING_MEJA")) {
                    String tanggalLengkap = pecah[3] + " " + pecah[4] + ":00";                    
                    hasil = bookingMeja(Integer.parseInt(pecah[1]), Integer.parseInt(pecah[2]), tanggalLengkap, Integer.parseInt(pecah[5]), pecah[6]);
                } else if (pesan.startsWith("GET_ALL_MEJA")) {
                    hasil = getAllMeja();
                } else if (pesan.startsWith("TAMBAH_MEJA")) {
                    String[] data = pesan.split("~");
                    hasil = tambahMeja(data[1], data[2], data[3]);
                } else if (pesan.startsWith("UPDATE_MEJA")) {
                    String[] data = pesan.split("~");
                    hasil = updateMeja(data[1], data[2], data[3], data[4]);
                } else if (pesan.startsWith("HAPUS_MEJA")) {
                    String[] data = pesan.split("~");
                    hasil = hapusMeja(data[1]);
                } else if (pesan.startsWith("BATAL_RESERVASI")) {
                    String[] data = pesan.split("~");
                    hasil = batalReservasi(data[1]);
                }

            } catch (Exception ex) {
                System.out.println("Error prosesPesan: " + ex);
            }
            return hasil;
        }

        public String getAllMeja() {
            StringBuilder sb = new StringBuilder();
            try (Connection conn = new MyConnection().getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM meja"); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getInt("id")).append("~")
                            .append(rs.getInt("nomorMeja")).append("~")
                            .append(rs.getInt("kapasitas")).append("~")
                            .append(rs.getString("status")).append("\n");
                }
                sb.append("DONE\n");
            } catch (Exception e) {
                System.out.println("Error getAllMeja: " + e);
            }
            return sb.toString();
        }

        public String tambahMeja(String noMeja, String kapasitas, String status) {
            try (Connection conn = new MyConnection().getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO meja (nomorMeja, kapasitas, status) VALUES (?,?,?)")) {
                ps.setInt(1, Integer.parseInt(noMeja));
                ps.setInt(2, Integer.parseInt(kapasitas));
                ps.setString(3, status);
                ps.executeUpdate();
                return "Meja berhasil ditambahkan!";
            } catch (Exception e) {
                return "Error tambah meja: " + e.getMessage();
            }
        }

        public String updateMeja(String id, String noMeja, String kapasitas, String status) {
            try (Connection conn = new MyConnection().getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE meja SET nomorMeja=?, kapasitas=?, status=? WHERE id=?")) {
                ps.setInt(1, Integer.parseInt(noMeja));
                ps.setInt(2, Integer.parseInt(kapasitas));
                ps.setString(3, status);
                ps.setInt(4, Integer.parseInt(id));
                ps.executeUpdate();
                return "Meja berhasil diupdate!";
            } catch (Exception e) {
                return "Error update meja: " + e.getMessage();
            }
        }

        public String hapusMeja(String id) {
            try (Connection conn = new MyConnection().getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM meja WHERE id=?")) {
                ps.setInt(1, Integer.parseInt(id));
                ps.executeUpdate();
                return "Meja berhasil dihapus!";
            } catch (Exception e) {
                return "Error hapus meja: " + e.getMessage();
            }
        }

        public String batalReservasi(String idReservasi) {
            try (Connection conn = new MyConnection().getConnection();) {
                String getMeja = "SELECT mejaID FROM reservasi WHERE id = ?";
                int mejaID = -1;
                try (PreparedStatement ps = conn.prepareStatement(getMeja)) {
                    ps.setInt(1, Integer.parseInt(idReservasi));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        mejaID = rs.getInt("mejaID");
                    }
                }
                if (mejaID != -1) {
                    try (PreparedStatement psUpdateRes = conn.prepareStatement("UPDATE reservasi SET statusReservasi='Cancelled' WHERE id=?")) {
                        psUpdateRes.setInt(1, Integer.parseInt(idReservasi));
                        psUpdateRes.executeUpdate();
                    }
                    try (PreparedStatement psUpdateMeja = conn.prepareStatement("UPDATE meja SET status='Tersedia' WHERE id=?")) {
                        psUpdateMeja.setInt(1, mejaID);
                        psUpdateMeja.executeUpdate();
                    }
                    return "Reservasi berhasil dibatalkan!";
                } else {
                    return "Reservasi tidak ditemukan.";
                }
            } catch (Exception e) {
                return "Error batal reservasi: " + e.getMessage();
            }
        }

        public String cariMejaKosong(String tanggal, String jam, int jumlahTamu) {
            String hasil = "";
            try (Connection conn = new MyConnection().getConnection()) {
                if (conn == null) {
                    return "ERROR|Koneksi database TCP Server GAGAL (Mungkin driver belum ditambah di library/URL salah).";
                }
                
                String query = "SELECT id, nomorMeja, kapasitas, status FROM meja WHERE status = 'Tersedia' AND kapasitas >= ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, jumlahTamu);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String row = rs.getInt("id") + "|" + rs.getInt("nomorMeja") + "|" + rs.getInt("kapasitas") + "|" + rs.getString("status");
                            if (hasil.equals("")) {
                                hasil = row;
                            } else {
                                hasil = hasil + "#" + row;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error cariMejaKosong: " + e);
                return "ERROR|Terjadi error di fungsi cariMejaKosong: " + e.getMessage();
            }
            return hasil;
        }

        public String bookingMeja(int userID, int idMeja, String tanggalLengkap, int jumlahTamu, String cartPesanan) {
            try (Connection conn = new MyConnection().getConnection()) {
                if (conn == null) {
                     return "BOOKING_REPLY|GAGAL: Koneksi database TCP Server GAGAL.";
                }

                String updateQuery = "UPDATE meja SET status = 'Dipesan' WHERE id = ? AND status = 'Tersedia'";
                int rowAffected = 0;
                try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                    psUpdate.setInt(1, idMeja);
                    rowAffected = psUpdate.executeUpdate();
                }

                if (rowAffected == 0) {
                    return "BOOKING_REPLY|GAGAL: Maaf, meja tersebut baru saja dipesan oleh pelanggan lain.";
                }
                
                String insertQuery = "INSERT INTO reservasi (userID, mejaID, statusReservasi, tanggal, jumlahTamu) VALUES (?, ?, 'Pending', ?, ?)";
                int reservasiID = -1;
                try (PreparedStatement psInsert = conn.prepareStatement(insertQuery, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psInsert.setInt(1, userID);
                    psInsert.setInt(2, idMeja);
                    psInsert.setTimestamp(3, java.sql.Timestamp.valueOf(tanggalLengkap));
                    psInsert.setInt(4, jumlahTamu);
                    psInsert.executeUpdate();

                    try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            reservasiID = rsKeys.getInt(1);
                        }
                    }
                }
                
                if (reservasiID != -1 && !cartPesanan.equals("0,0")) {
                    String[] listPesanan = cartPesanan.split(";");
                    String foodQuery = "INSERT INTO detailpesanan (reservasiID, menuID, jumlahItem, hargaSatuan, statusPesanan) "
                            + "VALUES (?, ?, ?, (SELECT harga FROM menu WHERE id = ?), 'Pending')";
                    try (PreparedStatement psFood = conn.prepareStatement(foodQuery)) {
                        for (String item : listPesanan) {
                            if (!item.isEmpty()) {
                                String[] detail = item.split(",");
                                int mId = Integer.parseInt(detail[0]);
                                int mQty = Integer.parseInt(detail[1]);

                                psFood.setInt(1, reservasiID);
                                psFood.setInt(2, mId);
                                psFood.setInt(3, mQty);
                                psFood.setInt(4, mId);
                                psFood.addBatch();
                            }
                        }
                        psFood.executeBatch();
                    }
                }                            

                return "BOOKING_REPLY|SUKSES: Reservasi meja nomor " + idMeja + " beserta pesanan makanan berhasil!";

            } catch (Exception e) {
                return "BOOKING_REPLY|Error sistem:" + e.getMessage();
            }
        }

        public String getHistoryReservasi(String userId, String hakAkses, String dariTanggal, String sampaiTanggal, String statusReservasi) {
            String hasil = "";
            try (Connection conn = new MyConnection().getConnection()) {
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

                if (!statusReservasi.equals("All")) {
                    query = query + "AND r.statusReservasi = ? ";
                }

                if (hakAkses.equals("Customer")) {
                    query = query + "AND r.userID = ? ";
                }

                query = query
                        + "GROUP BY r.id, u.nama, m.nomorMeja, r.tanggal, r.jumlahTamu, r.statusReservasi "
                        + "ORDER BY r.tanggal DESC";

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, dariTanggal);
                    ps.setString(2, sampaiTanggal);

                    int paramIndex = 3;
                    if (!statusReservasi.equals("All")) {
                        ps.setString(paramIndex++, statusReservasi);
                    }

                    if (hakAkses.equals("Customer")) {
                        ps.setInt(paramIndex++, Integer.parseInt(userId));
                    }

                    try (ResultSet rs = ps.executeQuery()) {
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
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error getHistoryReservasi: " + ex);
            }
            return hasil;
        }

        public String getDetailPesanan(int reservasiID) {
            String hasil = "";
            try (Connection conn = new MyConnection().getConnection()) {
                String query
                        = "SELECT mn.nama AS namaMenu, "
                        + "mn.kategori, "
                        + "dp.jumlahItem, dp.hargaSatuan, "
                        + "(dp.jumlahItem * dp.hargaSatuan) AS subtotal, dp.statusPesanan "
                        + "FROM detailpesanan dp "
                        + "JOIN menu mn ON dp.menuID = mn.id "
                        + "WHERE dp.reservasiID = ? "
                        + "ORDER BY dp.id ASC";

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, reservasiID);
                    try (ResultSet rs = ps.executeQuery()) {
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
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error getDetailPesanan: " + ex);
            }
            return hasil;
        }
    }
}
