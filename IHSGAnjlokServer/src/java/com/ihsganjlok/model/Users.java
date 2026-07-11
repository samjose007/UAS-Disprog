/*
 * Decompiled with CFR 0.152.
 */
package com.ihsganjlok.model;

import com.ihsganjlok.model.MyModel;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;

public class Users
extends MyModel {
    private int id;
    private String nama;
    private String alamat;
    private String email;
    private String username;
    private String password;
    private String hakAkses;
    private Date createdAt;
    private Date loginAt;

    public Users() {
    }

    public Users(int id, String nama, String alamat, String email, String username, String password, String hakAkses, Date createdAt, Date loginAt) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.username = username;
        this.password = password;
        this.hakAkses = hakAkses;
        this.createdAt = createdAt;
        this.loginAt = loginAt;
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

    public String getAlamat() {
        return this.alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHakAkses() {
        return this.hakAkses;
    }

    public void setHakAkses(String hakAkses) {
        this.hakAkses = hakAkses;
    }

    @Override
    public void insertData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("INSERT INTO users (nama, alamat, email, username, password, hakAkses) VALUES (?,?,?,?,?,?)");
            sql.setString(1, this.nama);
            sql.setString(2, this.alamat);
            sql.setString(3, this.email);
            sql.setString(4, this.username);
            sql.setString(5, this.password);
            sql.setString(6, this.hakAkses);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error insert users: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void updateData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("UPDATE users SET nama=?, alamat=?, email=?, username=?, password=?, hakAkses=? WHERE id=?");
            sql.setString(1, this.nama);
            sql.setString(2, this.alamat);
            sql.setString(3, this.email);
            sql.setString(4, this.username);
            sql.setString(5, this.password);
            sql.setString(6, this.hakAkses);
            sql.setInt(7, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error update users: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void deleteData() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("DELETE FROM users WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error delete users: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<Object>();
        try {
            this.statement = this.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM users");
            while (this.result.next()) {
                Users temp = new Users(this.result.getInt("id"), this.result.getString("nama"), this.result.getString("alamat"), this.result.getString("email"), this.result.getString("username"), this.result.getString("password"), this.result.getString("hakAkses"), this.result.getTimestamp("createdAt"), this.result.getTimestamp("loginAt"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error view users: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (this.statement != null) this.statement.close(); } catch (Exception e) {}
        }
        return listData;
    }

    public boolean checkLogin(String checkUsername, String checkPassword) {
        boolean found = false;
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            sql.setString(1, checkUsername);
            sql.setString(2, checkPassword);
            this.result = sql.executeQuery();
            if (this.result.next()) {
                this.setId(this.result.getInt("id"));
                this.setNama(this.result.getString("nama"));
                this.setHakAkses(this.result.getString("hakAkses"));
                found = true;
            }
        } catch (Exception ex) {
            System.out.println("Error checkLogin: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
        return found;
    }

    public void updateLoginTime() {
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("UPDATE users SET loginAt = CURRENT_TIMESTAMP WHERE id = ?");
            sql.setInt(1, this.getId());
            sql.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error updateLoginTime: " + ex.getMessage());
        } finally {
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
    }

    public Users getUserById(int userId) {
        Users u = null;
        PreparedStatement sql = null;
        try {
            sql = this.connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            sql.setInt(1, userId);
            this.result = sql.executeQuery();
            if (this.result.next()) {
                u = new Users(this.result.getInt("id"), this.result.getString("nama"), this.result.getString("alamat"), this.result.getString("email"), this.result.getString("username"), this.result.getString("password"), this.result.getString("hakAkses"), this.result.getTimestamp("createdAt"), this.result.getTimestamp("loginAt"));
            }
        } catch (Exception ex) {
            System.out.println("Error getUserById: " + ex.getMessage());
        } finally {
            try { if (this.result != null) this.result.close(); } catch (Exception e) {}
            try { if (sql != null) sql.close(); } catch (Exception e) {}
        }
        return u;
    }
}

