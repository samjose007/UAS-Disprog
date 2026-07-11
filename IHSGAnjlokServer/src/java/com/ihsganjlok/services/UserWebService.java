package com.ihsganjlok.services;

import com.ihsganjlok.model.Users;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="UserWebService")
public class UserWebService {
    @WebMethod(operationName="login")
    public String login(@WebParam(name="username") String username, @WebParam(name="password") String password) {
        System.out.println("Server menerima: " + username + " dan " + password);
        Users modelUser = new Users();
        if (modelUser.checkLogin(username, password)) {
            System.out.println("Login berhasil di sisi server");
            modelUser.updateLoginTime();
            return "SUCCESS|" + modelUser.getId() + "|" + modelUser.getNama() + "|" + modelUser.getHakAkses();
        }
        System.out.println("Login gagal di sisi server");
        return "FAILED|Username atau password salah!";
    }

    @WebMethod(operationName="register")
    public String register(@WebParam(name="nama") String nama, @WebParam(name="alamat") String alamat, @WebParam(name="email") String email, @WebParam(name="username") String username, @WebParam(name="password") String password) {
        try {
            Users user = new Users();
            user.setNama(nama);
            user.setAlamat(alamat);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setHakAkses("Customer");
            user.insertData();
            return "SUCCESS|Registrasi atas nama " + nama + " berhasil dilakukan.";
        }
        catch (Exception e) {
            return "FAILED|Gagal mendaftar: " + e.getMessage();
        }
    }

    @WebMethod(operationName="getAllUsers")
    public ArrayList<String> getAllUsers() {
        Users modelUser = new Users();
        ArrayList<Object> listUsers = modelUser.viewListData();
        ArrayList<String> dataYangDikirim = new ArrayList<String>();
        for (Object obj : listUsers) {
            Users u = (Users)obj;
            String baris = u.getId() + "|" + u.getNama() + "|" + u.getAlamat() + "|" + u.getEmail() + "|" + u.getUsername() + "|" + u.getHakAkses();
            dataYangDikirim.add(baris);
        }
        return dataYangDikirim;
    }

    @WebMethod(operationName="getUserById")
    public String getUserById(@WebParam(name="id") int id) {
        Users modelUser = new Users();
        Users u = modelUser.getUserById(id);
        if (u != null) {
            return "SUCCESS|" + u.getId() + "|" + u.getNama() + "|" + u.getAlamat() + "|" + u.getEmail() + "|" + u.getUsername() + "|" + u.getPassword() + "|" + u.getHakAkses();
        }
        return "FAILED|User tidak ditemukan";
    }

    @WebMethod(operationName="updateProfile")
    public String updateProfile(@WebParam(name="id") int id, @WebParam(name="nama") String nama, @WebParam(name="alamat") String alamat, @WebParam(name="email") String email, @WebParam(name="username") String username, @WebParam(name="password") String password, @WebParam(name="hakAkses") String hakAkses) {
        try {
            Users user = new Users();
            user.setId(id);
            user.setNama(nama);
            user.setAlamat(alamat);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setHakAkses(hakAkses);
            user.updateData();
            return "SUCCESS|Profil berhasil diperbarui!";
        }
        catch (Exception e) {
            return "FAILED|Gagal update profil: " + e.getMessage();
        }
    }
}

