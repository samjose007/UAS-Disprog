/*
 * Decompiled with CFR 0.152.
 */
package com.ihsganjlok.model;

import com.ihsganjlok.model.MyModel;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DetailPesanan
extends MyModel {
    private int id;
    private int reservasiID;
    private int menuID;
    private int jumlahItem;
    private int hargaSatuan;
    private String statusPesanan;

    public DetailPesanan() {
    }

    public DetailPesanan(int id, int reservasiID, int menuID, int jumlahItem, int hargaSatuan, String statusPesanan) {
        this.setId(id);
        this.setReservasiID(reservasiID);
        this.setMenuID(menuID);
        this.setJumlahItem(jumlahItem);
        this.setHargaSatuan(hargaSatuan);
        this.setStatusPesanan(statusPesanan);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservasiID() {
        return this.reservasiID;
    }

    public void setReservasiID(int reservasiID) {
        this.reservasiID = reservasiID;
    }

    public int getMenuID() {
        return this.menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public int getJumlahItem() {
        return this.jumlahItem;
    }

    public void setJumlahItem(int jumlahItem) {
        this.jumlahItem = jumlahItem;
    }

    public int getHargaSatuan() {
        return this.hargaSatuan;
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public String getStatusPesanan() {
        return this.statusPesanan;
    }

    public void setStatusPesanan(String statusPesanan) {
        this.statusPesanan = statusPesanan;
    }

    @Override
    public void insertData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("INSERT INTO detailpesanan (reservasiID, menuID, jumlahItem, hargaSatuan, statusPesanan) VALUES (?,?,?,?,?)");
            sql.setInt(1, this.reservasiID);
            sql.setInt(2, this.menuID);
            sql.setInt(3, this.jumlahItem);
            sql.setInt(4, this.hargaSatuan);
            sql.setString(5, this.statusPesanan);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di insert data detailpesanan: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void updateData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("UPDATE detailpesanan SET reservasiID=?, menuID=?, jumlahItem=?, hargaSatuan=?, statusPesanan=? WHERE id=?");
            sql.setInt(1, this.reservasiID);
            sql.setInt(2, this.menuID);
            sql.setInt(3, this.jumlahItem);
            sql.setInt(4, this.hargaSatuan);
            sql.setString(5, this.statusPesanan);
            sql.setInt(6, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di update data detailpesanan: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void deleteData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("DELETE FROM detailpesanan WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di delete data detailpesanan: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = this.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM detailpesanan");
            while (this.result.next()) {
                DetailPesanan temp = new DetailPesanan(this.result.getInt("id"), this.result.getInt("reservasiID"), this.result.getInt("menuID"), this.result.getInt("jumlahItem"), this.result.getInt("hargaSatuan"), this.result.getString("statusPesanan"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error di view data detailpesanan: " + ex);
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (this.statement != null) this.statement.close(); } catch (Exception e) {}
        }
        return listData;
    }
}

