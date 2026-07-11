/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ihsganjloktcpserver;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Jose
 */
public class MyConnection {

    Connection connect;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3307/db_food_reservation?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "");
        } catch (Exception ex) {
            System.out.println("Error Get Connection : " + ex);
        }
        return connect;
    }
}
