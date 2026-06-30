/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ihsganjlok.model;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Jose
 */
public class DetailPesanan extends MyModel {
    private int id;
    private int reservasiID;
    private int menuID;
    private int jumlahItem;
    private int hargaSatuan;
    private String statusPesanan;

    public DetailPesanan() {
    }
    
    public DetailPesanan(int id, int reservasiID, int menuID, int jumlahItem, int hargaSatuan, String statusPesanan) {
        setId(id);
        setReservasiID(reservasiID);
        setMenuID(menuID);
        setJumlahItem(jumlahItem);
        setHargaSatuan(hargaSatuan);
        setStatusPesanan(statusPesanan);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservasiID() { return reservasiID; }
    public void setReservasiID(int reservasiID) { this.reservasiID = reservasiID; }

    public int getMenuID() { return menuID; }
    public void setMenuID(int menuID) { this.menuID = menuID; }

    public int getJumlahItem() { return jumlahItem; }
    public void setJumlahItem(int jumlahItem) { this.jumlahItem = jumlahItem; }

    public int getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(int hargaSatuan) { this.hargaSatuan = hargaSatuan; }

    public String getStatusPesanan() { return statusPesanan; }
    public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }
    
    @Override
    public void insertData() {
        try {
            // PERBAIKAN PENTING: Typo 'hargaStuan' diubah menjadi 'hargaSatuan'
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "INSERT INTO detailpesanan (reservasiID, menuID, jumlahItem, hargaSatuan, statusPesanan) VALUES (?,?,?,?,?)");
            sql.setInt(1, this.reservasiID);
            sql.setInt(2, this.menuID);
            sql.setInt(3, this.jumlahItem);
            sql.setInt(4, this.hargaSatuan);
            sql.setString(5, this.statusPesanan);            
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di insert data detailpesanan: " + ex);
        }
    }

    @Override
    public void updateData() {
        try {
            // PERBAIKAN: Menyelesaikan logika UPDATE detail pesanan
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "UPDATE detailpesanan SET reservasiID=?, menuID=?, jumlahItem=?, hargaSatuan=?, statusPesanan=? WHERE id=?");
            sql.setInt(1, this.reservasiID);
            sql.setInt(2, this.menuID);
            sql.setInt(3, this.jumlahItem);
            sql.setInt(4, this.hargaSatuan);
            sql.setString(5, this.statusPesanan);
            sql.setInt(6, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di update data detailpesanan: " + ex);
        }
    }

    @Override
    public void deleteData() {
        try {
             // PERBAIKAN: Menyelesaikan logika DELETE
             PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "DELETE FROM detailpesanan WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di delete data detailpesanan: " + ex);
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        // PERBAIKAN: Tipe diubah ke Object dan menambahkan data ke List
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = (Statement) MyModel.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM detailpesanan");
            while (this.result.next()) {
                DetailPesanan temp = new DetailPesanan(
                        this.result.getInt("id"),
                        this.result.getInt("reservasiID"),
                        this.result.getInt("menuID"),
                        this.result.getInt("jumlahItem"),
                        this.result.getInt("hargaSatuan"),
                        this.result.getString("statusPesanan"));    
                
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error di view data detailpesanan: " + ex);
        }
        return listData;
    }
}