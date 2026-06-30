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
public class Meja extends MyModel {

    private int id;
    private int nomorMeja;
    private int kapasitas;
    private String status;

    public Meja() {
    }

    public Meja(int id, int nomorMeja, int kapasitas, String status) {
        setId(id);
        setNomorMeja(nomorMeja);
        setKapasitas(kapasitas);
        setStatus(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNomorMeja() {
        return nomorMeja;
    }

    public void setNomorMeja(int nomorMeja) {
        this.nomorMeja = nomorMeja;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void insertData() {
        try {
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "INSERT INTO meja (nomorMeja, kapasitas, status) VALUES (?,?,?)");
            sql.setInt(1, this.nomorMeja);
            sql.setInt(2, this.kapasitas);
            sql.setString(3, this.status);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di insert data meja: " + ex);
        }
    }

    @Override
    public void updateData() {
        try {
            // PERBAIKAN: Membuka comment dan menyempurnakan UPDATE meja
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "UPDATE meja SET nomorMeja=?, kapasitas=?, status=? WHERE id=?");
            sql.setInt(1, this.nomorMeja);
            sql.setInt(2, this.kapasitas);
            sql.setString(3, this.status);
            sql.setInt(4, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di update data meja: " + ex);
        }
    }

    @Override
    public void deleteData() {
        try {
            // PERBAIKAN: Membuka comment dan menyempurnakan DELETE meja
            PreparedStatement sql = (PreparedStatement) MyModel.connection.prepareStatement(
                    "DELETE FROM meja WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error di delete data meja: " + ex);
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        // PERBAIKAN: Tipe diubah jadi Object dan menambahkan data ke List
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = (Statement) MyModel.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM meja");
            while (this.result.next()) {
                Meja temp = new Meja(
                        this.result.getInt("id"),
                        this.result.getInt("nomorMeja"),
                        this.result.getInt("kapasitas"),
                        this.result.getString("status"));

                listData.add(temp); // Pastikan ini tidak terlewat
            }
        } catch (Exception ex) {
            System.out.println("Error di view data meja: " + ex);
        }
        return listData;
    }
}
