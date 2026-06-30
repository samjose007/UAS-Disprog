/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ihsganjlok.model;

import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 *
 * @author aldo
 */
public class ReservationHistory extends MyModel {

    public ReservationHistory() {
        super();
    }

    public ArrayList<String> viewHistory(String dariTanggal, String sampaiTanggal, String statusReservasi) {
        ArrayList<String> dataHistory = new ArrayList<String>();

        try {
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

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, dariTanggal);
            ps.setString(2, sampaiTanggal);

            if (!statusReservasi.equals("Semua")) {
                ps.setString(3, statusReservasi);
            }

            result = ps.executeQuery();

            while (result.next()) {
                String row
                        = result.getInt("id") + "|"
                        + result.getString("namaCustomer") + "|"
                        + result.getInt("nomorMeja") + "|"
                        + result.getString("tanggalReservasi") + "|"
                        + result.getInt("jumlahTamu") + "|"
                        + result.getString("statusReservasi") + "|"
                        + result.getInt("totalPesanan");

                dataHistory.add(row);
            }

            result.close();
            ps.close();

        } catch (Exception ex) {
            System.out.println("Error viewHistory: " + ex);
        }

        return dataHistory;
    }

    public ArrayList<String> viewDetailPesanan(int reservasiID) {
        ArrayList<String> dataDetail = new ArrayList<String>();

        try {
            String query
                    = "SELECT mn.nama AS namaMenu, mn.kategori, dp.jumlahItem, dp.hargaSatuan, "
                    + "(dp.jumlahItem * dp.hargaSatuan) AS subtotal, dp.statusPesanan "
                    + "FROM detailpesanan dp "
                    + "JOIN menu mn ON dp.menuID = mn.id "
                    + "WHERE dp.reservasiID = ? "
                    + "ORDER BY dp.id ASC";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, reservasiID);

            result = ps.executeQuery();

            while (result.next()) {
                String row
                        = result.getString("namaMenu") + "|"
                        + result.getString("kategori") + "|"
                        + result.getInt("jumlahItem") + "|"
                        + result.getInt("hargaSatuan") + "|"
                        + result.getInt("subtotal") + "|"
                        + result.getString("statusPesanan");

                dataDetail.add(row);
            }

            result.close();
            ps.close();

        } catch (Exception ex) {
            System.out.println("Error viewDetailPesanan: " + ex);
        }

        return dataDetail;
    }

    @Override
    public void insertData() {
        // Biarkan kosong, karena class ini hanya untuk View / Reporting
    }

    @Override
    public void updateData() {
        // Biarkan kosong, karena class ini hanya untuk View / Reporting
    }

    @Override
    public void deleteData() {
        // Biarkan kosong, karena class ini hanya untuk View / Reporting
    }

    @Override
    public ArrayList<Object> viewListData() {
        // PERBAIKAN: Tipe diubah menjadi Object agar sinkron tanpa error
        return new ArrayList<Object>();
    }
}
