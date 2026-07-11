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
        PreparedStatement sql = null;
        try {
            sql = (PreparedStatement) this.connection.prepareStatement(
                    "INSERT INTO reservasi (userID, mejaID, statusReservasi, tanggal, jumlahTamu, createdAt) VALUES (?,?,?,?,?,?)");
            sql.setInt(1, this.userID);
            sql.setInt(2, this.mejaID);
            sql.setString(3, this.statusReservasi);
            sql.setTimestamp(4, new Timestamp(this.tanggal.getTime()));
            sql.setInt(5, this.jumlahTamu);
            if (this.createdAt != null) {
                sql.setTimestamp(6, new Timestamp(this.createdAt.getTime()));
            } else {
                sql.setTimestamp(6, new Timestamp(new Date().getTime()));
            }
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di insert data reservasi: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void updateData() {
        PreparedStatement sql = null;
        try {
            sql = (PreparedStatement) this.connection.prepareStatement(
                    "UPDATE reservasi SET userID=?, mejaID=?, statusReservasi=?, tanggal=?, jumlahTamu=? WHERE id=?");
            sql.setInt(1, this.userID);
            sql.setInt(2, this.mejaID);
            sql.setString(3, this.statusReservasi);
            sql.setTimestamp(4, new Timestamp(this.tanggal.getTime()));
            sql.setInt(5, this.jumlahTamu);
            sql.setInt(6, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di update data reservasi: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void deleteData() {
        PreparedStatement sql = null;
        try {
            sql = (PreparedStatement) this.connection.prepareStatement(
                    "DELETE FROM reservasi WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di delete data reservasi: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = (Statement) this.connection.createStatement();
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
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (this.statement != null) this.statement.close(); } catch (Exception e) {}
        }
        return listData;
    }
}
