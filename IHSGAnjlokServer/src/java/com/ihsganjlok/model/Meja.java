/*
 * Decompiled with CFR 0.152.
 */
package com.ihsganjlok.model;

import com.ihsganjlok.model.MyModel;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Meja
extends MyModel {
    private int id;
    private int nomorMeja;
    private int kapasitas;
    private String status;

    public Meja() {
    }

    public Meja(int id, int nomorMeja, int kapasitas, String status) {
        this.setId(id);
        this.setNomorMeja(nomorMeja);
        this.setKapasitas(kapasitas);
        this.setStatus(status);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNomorMeja() {
        return this.nomorMeja;
    }

    public void setNomorMeja(int nomorMeja) {
        this.nomorMeja = nomorMeja;
    }

    public int getKapasitas() {
        return this.kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void insertData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("INSERT INTO meja (nomorMeja, kapasitas, status) VALUES (?,?,?)");
            sql.setInt(1, this.nomorMeja);
            sql.setInt(2, this.kapasitas);
            sql.setString(3, this.status);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di insert data meja: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void updateData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("UPDATE meja SET nomorMeja=?, kapasitas=?, status=? WHERE id=?");
            sql.setInt(1, this.nomorMeja);
            sql.setInt(2, this.kapasitas);
            sql.setString(3, this.status);
            sql.setInt(4, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di update data meja: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void deleteData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("DELETE FROM meja WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error di delete data meja: " + ex);
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = this.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM meja");
            while (this.result.next()) {
                Meja temp = new Meja(this.result.getInt("id"), this.result.getInt("nomorMeja"), this.result.getInt("kapasitas"), this.result.getString("status"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error di view data meja: " + ex);
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (this.statement != null) this.statement.close(); } catch (Exception e) {}
        }
        return listData;
    }

    public Meja getMejaById(int id) {
        Meja m = null;
        java.sql.PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("SELECT * FROM meja WHERE id = ?");
            sql.setInt(1, id);
            this.result = sql.executeQuery();
            if (this.result.next()) {
                m = new Meja(this.result.getInt("id"), this.result.getInt("nomorMeja"), this.result.getInt("kapasitas"), this.result.getString("status"));
            }
        } catch (Exception ex) {
            System.out.println("Error di getMejaById: " + ex);
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
        return m;
    }
}

