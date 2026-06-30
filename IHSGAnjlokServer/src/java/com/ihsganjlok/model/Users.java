package com.ihsganjlok.model;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Users extends MyModel {

    private int id;
    private String nama;
    private String alamat;
    private String email;
    private String username;
    private String password;
    private String hakAkses;
    private Date createdAt;
    private Date loginAt;

    public Users() {}

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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getHakAkses() { return hakAkses; }
    public void setHakAkses(String hakAkses) { this.hakAkses = hakAkses; }

    @Override
    public void insertData() {
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "INSERT INTO users (nama, alamat, email, username, password, hakAkses) VALUES (?,?,?,?,?,?)");
            sql.setString(1, this.nama);
            sql.setString(2, this.alamat);
            sql.setString(3, this.email);
            sql.setString(4, this.username);
            sql.setString(5, this.password);
            sql.setString(6, this.hakAkses);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error insert users: " + ex.getMessage());
        }
    }

    @Override
    public void updateData() {
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "UPDATE users SET nama=?, alamat=?, email=?, username=?, password=?, hakAkses=? WHERE id=?");
            sql.setString(1, this.nama);
            sql.setString(2, this.alamat);
            sql.setString(3, this.email);
            sql.setString(4, this.username);
            sql.setString(5, this.password);
            sql.setString(6, this.hakAkses);
            sql.setInt(7, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error update users: " + ex.getMessage());
        }
    }

    @Override
    public void deleteData() {
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement("DELETE FROM users WHERE id=?");
            sql.setInt(1, this.id);
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error delete users: " + ex.getMessage());
        }
    }

    @Override
    public ArrayList<Object> viewListData() {
        ArrayList<Object> listData = new ArrayList<>();
        try {
            this.statement = MyModel.connection.createStatement();
            this.result = this.statement.executeQuery("SELECT * FROM users");
            while (this.result.next()) {
                Users temp = new Users(
                        this.result.getInt("id"),
                        this.result.getString("nama"),
                        this.result.getString("alamat"),
                        this.result.getString("email"),
                        this.result.getString("username"),
                        this.result.getString("password"),
                        this.result.getString("hakAkses"),
                        this.result.getTimestamp("createdAt"),
                        this.result.getTimestamp("loginAt"));
                listData.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("Error view users: " + ex.getMessage());
        }
        return listData;
    }

    public boolean checkLogin(String checkUsername, String checkPassword) {
        boolean found = false;
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?");
            sql.setString(1, checkUsername);
            sql.setString(2, checkPassword);
            
            this.result = sql.executeQuery();
            if (this.result.next()) {
                this.setId(this.result.getInt("id"));
                this.setNama(this.result.getString("nama"));
                this.setHakAkses(this.result.getString("hakAkses"));
                found = true;
            }
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error checkLogin: " + ex.getMessage());
        }
        return found;
    }

    public void updateLoginTime() {
        try {
            PreparedStatement sql = MyModel.connection.prepareStatement(
                    "UPDATE users SET loginAt = CURRENT_TIMESTAMP WHERE id = ?");
            sql.setInt(1, this.getId());
            sql.executeUpdate();
            sql.close();
        } catch (Exception ex) {
            System.out.println("Error updateLoginTime: " + ex.getMessage());
        }
    }
}