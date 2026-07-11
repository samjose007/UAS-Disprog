/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ihsganjlok.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author Jose
 */
public abstract class MyModel {
    protected Connection connection;
    protected Statement statement;
    protected ResultSet result;
    
    public MyModel(){
       try {
        this.connection = this.getConnection();
    } catch (Exception ex) {
        System.out.println("Error saat inisialisasi koneksi di konstruktor: " + ex.getMessage());
        this.connection = null; 
    }
    this.statement = null;
    this.result = null;
    }
    
    public Connection getConnection() throws SQLException{
        if (this.connection == null || this.connection.isClosed()){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/db_food_reservation?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "");
                System.out.println("Koneksi Database Berhasil!");
                return conn;
            } catch (Exception ex) {
                System.out.println("ERROR KONEKSI DB: " + ex.getMessage());
                return null;
            }
        } 
        return this.connection;
    }
    
    public abstract void insertData();
    public abstract void updateData();
    public abstract void deleteData();
    public abstract ArrayList<Object> viewListData();
}
