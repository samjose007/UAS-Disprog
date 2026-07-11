/*
 * Decompiled with CFR 0.152.
 */
package com.ihsganjlok.model;

import com.ihsganjlok.model.MyModel;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Menu
extends MyModel {
    private int id;
    private String nama;
    private int harga;
    private String keterangan;
    private String kategori;
    private String status;

    public Menu() {
    }

    public Menu(int id, String nama, int harga, String keterangan, String kategori, String status) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.keterangan = keterangan;
        this.kategori = kategori;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return this.harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getKeterangan() {
        return this.keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKategori() {
        return this.kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void insertData() {
        if (this.connection == null) {
            System.out.println("DEBUG ERROR: Koneksi null, insert gagal!");
            return;
        }
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("INSERT INTO menu (nama, harga, keterangan, kategori, status) VALUES (?,?, ?, ?,?)");
            sql.setString(1, this.nama);
            sql.setInt(2, this.harga);
            sql.setString(3, this.keterangan);
            sql.setString(4, this.kategori);
            sql.setString(5, this.status);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("DEBUG ERROR: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void updateData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("UPDATE menu SET nama=?, harga=?, keterangan=?, kategori=?, status=? WHERE id=?");
            sql.setString(1, this.nama);
            sql.setInt(2, this.harga);
            sql.setString(3, this.keterangan);
            sql.setString(4, this.kategori);
            sql.setString(5, this.status);
            sql.setInt(6, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error update menu: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void deleteData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("DELETE FROM menu WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error delete menu: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = this.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM menu");
            while (this.result.next()) {
                Menu temp = new Menu(this.result.getInt("id"), this.result.getString("nama"), this.result.getInt("harga"), this.result.getString("keterangan"), this.result.getString("kategori"), this.result.getString("status"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error view menu: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (this.statement != null) this.statement.close(); } catch (Exception e) {}
        }
        return listData;
    }

    public ArrayList<Object> viewAvailableListData() {
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = this.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM menu WHERE status = 'Tersedia'");
            while (this.result.next()) {
                Menu temp = new Menu(this.result.getInt("id"), this.result.getString("nama"), this.result.getInt("harga"), this.result.getString("keterangan"), this.result.getString("kategori"), this.result.getString("status"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error view available menu: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (this.statement != null) this.statement.close(); } catch (Exception e) {}
        }
        return listData;
    }

    public ArrayList<Object> searchData(String keyword) {
        ArrayList<Object> listData = new ArrayList<Object>();
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("SELECT * FROM menu WHERE nama LIKE ? OR kategori LIKE ?");
            sql.setString(1, "%" + keyword + "%");
            sql.setString(2, "%" + keyword + "%");
            this.result = sql.executeQuery();
            while (this.result.next()) {
                Menu temp = new Menu(this.result.getInt("id"), this.result.getString("nama"), this.result.getInt("harga"), this.result.getString("keterangan"), this.result.getString("kategori"), this.result.getString("status"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error search menu: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
        return listData;
    }

    public boolean isNamaExist(String namaMenu, int currentId) {
        boolean exist = false;
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("SELECT id FROM menu WHERE nama = ? AND id != ?");
            sql.setString(1, namaMenu);
            sql.setString(2, String.valueOf(currentId));
            this.result = sql.executeQuery();
            if (this.result.next()) {
                exist = true;
            }
        } catch (Exception ex) {
            System.out.println("Error check nama: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
        return exist;
    }
}

