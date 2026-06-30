/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ihsganjlok.model;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Jose
 */
public class Reservasi extends MyModel {

    private int id;
    private int userID;
    private int mejaID;
    private String statusReservasi;
    private Date tanggal;
    private int jumlahTamu;
    private Date createdAt;

    public Reservasi() {
    }

    public Reservasi(int id, int userID, int mejaID, String statusReservasi, Date tanggal, int jumlahTamu, Date createdAt) {
        setId(id);
        setUserID(userID);
        setMejaID(mejaID);
        setStatusReservasi(statusReservasi);
        setTanggal(tanggal);
        setJumlahTamu(jumlahTamu);
        setCreatedAt(createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMejaID() {
        return mejaID;
    }

    public void setMejaID(int mejaID) {
        this.mejaID = mejaID;
    }

    public String getStatusReservasi() {
        return statusReservasi;
    }

    public void setStatusReservasi(String statusReservasi) {
        this.statusReservasi = statusReservasi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getJumlahTamu() {
        return jumlahTamu;
    }

    public void setJumlahTamu(int jumlahTamu) {
        this.jumlahTamu = jumlahTamu;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void insertData() {
        try {
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "INSERT INTO reservasi (userID, mejaID, statusReservasi, tanggal, jumlahTamu, createdAt) VALUES (?,?,?,?,?,?)");
            sql.setInt(1, this.userID);
            sql.setInt(2, this.mejaID);
            sql.setString(3, this.statusReservasi);
            sql.setTimestamp(4, new Timestamp(this.tanggal.getTime()));
            sql.setInt(5, this.jumlahTamu);
            // Mencegah error jika createdAt null (misal dari input baru)
            if (this.createdAt != null) {
                sql.setTimestamp(6, new Timestamp(this.createdAt.getTime()));
            } else {
                sql.setTimestamp(6, new Timestamp(new Date().getTime())); // set current time
            }
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di insert data reservasi: " + ex);
        }
    }

    @Override
    public void updateData() {
        try {
            // PERBAIKAN: Menyelesaikan logika UPDATE
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "UPDATE reservasi SET userID=?, mejaID=?, statusReservasi=?, tanggal=?, jumlahTamu=? WHERE id=?");
            sql.setInt(1, this.userID);
            sql.setInt(2, this.mejaID);
            sql.setString(3, this.statusReservasi);
            sql.setTimestamp(4, new Timestamp(this.tanggal.getTime()));
            sql.setInt(5, this.jumlahTamu);
            sql.setInt(6, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di update data reservasi: " + ex);
        }
    }

    @Override
    public void deleteData() {
        try {
            // PERBAIKAN: Menyelesaikan logika DELETE
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "DELETE FROM reservasi WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di delete data reservasi: " + ex);
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        // PERBAIKAN: Tipe diubah ke Object dan listData diisi
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = (Statement) MyModel.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM reservasi");
            while (this.result.next()) {
                Reservasi temp = new Reservasi(
                        this.result.getInt("id"),
                        this.result.getInt("userID"),
                        this.result.getInt("mejaID"),
                        this.result.getString("statusReservasi"),
                        this.result.getTimestamp("tanggal"),
                        this.result.getInt("jumlahTamu"),
                        this.result.getTimestamp("createdAt"));

                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error di view data reservasi: " + ex);
        }
        return listData;
    }
}
