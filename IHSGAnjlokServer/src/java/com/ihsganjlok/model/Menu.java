package com.ihsganjlok.model;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Menu extends MyModel {

    private int id;
    private String nama;
    private int harga;
    private String kategori;
    private String status;

    public Menu() {}

    public Menu(int id, String nama, int harga, String kategori, String status) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.status = status;
    }

  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public void insertData() {
        if (MyModel.connection == null) {
            System.out.println("DEBUG ERROR: Koneksi null, insert gagal!");
            return;
        }
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "INSERT INTO menu (nama, harga, kategori, status) VALUES (?,?,?,?)");
            sql.setString(1, this.nama);
            sql.setInt(2, this.harga);
            sql.setString(3, this.kategori);
            sql.setString(4, this.status);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("DEBUG ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void updateData() {
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "UPDATE menu SET nama=?, harga=?, kategori=?, status=? WHERE id=?");
            sql.setString(1, this.nama);
            sql.setInt(2, this.harga);
            sql.setString(3, this.kategori);
            sql.setString(4, this.status);
            sql.setInt(5, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error update menu: " + ex.getMessage());
        }
    }

    @Override
    public void deleteData() {
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement("DELETE FROM menu WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error delete menu: " + ex.getMessage());
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<>();
        try {
            this.statement = MyModel.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM menu");
            while (this.result.next()) {
                Menu temp = new Menu(
                        this.result.getInt("id"),
                        this.result.getString("nama"),
                        this.result.getInt("harga"),
                        this.result.getString("kategori"),
                        this.result.getString("status"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error view menu: " + ex.getMessage());
        }
        return listData;
    }

    public ArrayList<Object> viewAvailableListData() {
        ArrayList<Object> listData = new ArrayList<>();
        try {
            this.statement = MyModel.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM menu WHERE status = 'Tersedia'");
            while (this.result.next()) {
                Menu temp = new Menu(
                        this.result.getInt("id"),
                        this.result.getString("nama"),
                        this.result.getInt("harga"),
                        this.result.getString("kategori"),
                        this.result.getString("status"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error view available menu: " + ex.getMessage());
        }
        return listData;
    }

    public ArrayList<Object> searchData(String keyword) {
        ArrayList<Object> listData = new ArrayList<>();
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "SELECT * FROM menu WHERE nama LIKE ? OR kategori LIKE ?");
            sql.setString(1, "%" + keyword + "%");
            sql.setString(2, "%" + keyword + "%");
            this.result = sql.executeQuery();
            while (this.result.next()) {
                Menu temp = new Menu(
                        this.result.getInt("id"),
                        this.result.getString("nama"),
                        this.result.getInt("harga"),
                        this.result.getString("kategori"),
                        this.result.getString("status"));
                listData.add(temp);
            }
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error search menu: " + ex.getMessage());
        }
        return listData;
    }
}