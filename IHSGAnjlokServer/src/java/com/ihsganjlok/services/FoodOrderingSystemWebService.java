/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package com.ihsganjlok.services;

import com.ihsganjlok.model.Reservasi;
import com.ihsganjlok.model.DetailPesanan;
import com.ihsganjlok.model.Meja;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Jose
 */
@WebService(serviceName = "FoodOrderingSystemWebService")
public class FoodOrderingSystemWebService {

    @WebMethod(operationName = "bookTable")
    public synchronized String bookTable(@WebParam(name = "userID") int userID,
            @WebParam(name = "mejaID") int mejaID,
            @WebParam(name = "tanggal") String tanggal,
            @WebParam(name = "jumlahTamu") int jumlahTamu) {
        try {
            Meja modelMeja = new Meja();
            Meja m = modelMeja.getMejaById(mejaID);
            boolean isTersedia = false;

            if (m != null && m.getStatus().equalsIgnoreCase("Tersedia")) {
                isTersedia = true;
                m.setStatus("Dipesan");
                m.updateData();
            }
            if (!isTersedia) {
                return "FAILED: Mohon maaf, meja tersebut tidak tersedia (atau tidak ditemukan). Silakan pilih meja yang lain.";
            }
            Reservasi reservasi = new Reservasi();
            reservasi.setUserID(userID);
            reservasi.setMejaID(mejaID);
            java.sql.Timestamp tanggalSQL = java.sql.Timestamp.valueOf(tanggal);
            reservasi.setTanggal(tanggalSQL);
            reservasi.setJumlahTamu(jumlahTamu);
            reservasi.setStatusReservasi("Pending");

            reservasi.insertData();
            return "SUCCESS: Reservasi meja nomor " + mejaID + " berhasil dibuat!";

        } catch (Exception e) {
            return "FAILED: Gagal melakukan booking. Error Sistem: " + e.getMessage();
        }
    }

    @WebMethod(operationName = "addOrder")
    public String addOrder(@WebParam(name = "reservasiID") int reservasiID,
            @WebParam(name = "menuID") int menuID,
            @WebParam(name = "jumlahItem") int jumlahItem,
            @WebParam(name = "hargaSatuan") int hargaSatuan) {
        try {
            DetailPesanan pesanan = new DetailPesanan();
            pesanan.setReservasiID(reservasiID);
            pesanan.setMenuID(menuID);
            pesanan.setJumlahItem(jumlahItem);
            pesanan.setHargaSatuan(hargaSatuan);
            pesanan.setStatusPesanan("Pending");

            pesanan.insertData();
            return "SUCCESS: Pesanan berhasil dikirim ke dapur!";
        } catch (Exception e) {
            return "FAILED: Gagal menambah pesanan. Error: " + e.getMessage();
        }
    }

    @WebMethod(operationName = "updateOrderStatus")
    public String updateOrderStatus(@WebParam(name = "pesananID") int pesananID,
            @WebParam(name = "reservasiID") int reservasiID,
            @WebParam(name = "menuID") int menuID,
            @WebParam(name = "jumlahItem") int jumlahItem,
            @WebParam(name = "hargaSatuan") int hargaSatuan,
            @WebParam(name = "statusBaru") String statusBaru) {
        try {
            DetailPesanan pesanan = new DetailPesanan();
            pesanan.setId(pesananID);
            pesanan.setReservasiID(reservasiID);
            pesanan.setMenuID(menuID);
            pesanan.setJumlahItem(jumlahItem);
            pesanan.setHargaSatuan(hargaSatuan);
            pesanan.setStatusPesanan(statusBaru);

            pesanan.updateData();
            return "SUCCESS: Status pesanan berhasil diperbarui menjadi " + statusBaru;
        } catch (Exception e) {
            return "FAILED: Gagal merubah status pesanan. Error: " + e.getMessage();
        }
    }
}
