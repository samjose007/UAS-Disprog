package com.ihsganjlok.services;

import com.ihsganjlok.model.Menu;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "MenuManagementWebService")
public class MenuManagementWebService {

    @WebMethod(operationName = "getAllMenu")
    public ArrayList<String> getAllMenu() {
        Menu modelMenu = new Menu();
        ArrayList<Object> listMenu = modelMenu.viewListData();
        ArrayList<String> dataYangDikirim = new ArrayList<>();
        
        for (Object obj : listMenu) {
            Menu m = (Menu) obj;
            String baris = m.getId() + "|" + m.getNama() + "|" + m.getHarga() + "|" + m.getKategori() + "|" + m.getStatus();
            dataYangDikirim.add(baris);
        }
        return dataYangDikirim;
    }

    @WebMethod(operationName = "getAvailableMenu")
    public ArrayList<String> getAvailableMenu() {
        Menu modelMenu = new Menu();
        ArrayList<Object> listMenu = modelMenu.viewAvailableListData();
        ArrayList<String> dataYangDikirim = new ArrayList<>();
        
        for (Object obj : listMenu) {
            Menu m = (Menu) obj;
            String baris = m.getId() + "|" + m.getNama() + "|" + m.getHarga() + "|" + m.getKategori() + "|" + m.getStatus();
            dataYangDikirim.add(baris);
        }
        return dataYangDikirim;
    }

    @WebMethod(operationName = "searchMenu")
    public ArrayList<String> searchMenu(@WebParam(name = "keyword") String keyword) {
        Menu modelMenu = new Menu();
        ArrayList<Object> listMenu = modelMenu.searchData(keyword);
        ArrayList<String> dataYangDikirim = new ArrayList<>();
        
        for (Object obj : listMenu) {
            Menu m = (Menu) obj;
            String baris = m.getId() + "|" + m.getNama() + "|" + m.getHarga() + "|" + m.getKategori() + "|" + m.getStatus();
            dataYangDikirim.add(baris);
        }
        return dataYangDikirim;
    }

    @WebMethod(operationName = "addMenu")
    public String addMenu(@WebParam(name = "nama") String nama, 
                          @WebParam(name = "harga") int harga, 
                          @WebParam(name = "kategori") String kategori, 
                          @WebParam(name = "status") String status) {
        try {
            Menu menu = new Menu();
            menu.setNama(nama);
            menu.setHarga(harga);
            menu.setKategori(kategori);
            menu.setStatus(status); 
            menu.insertData();
            return "SUCCESS|Menu " + nama + " berhasil ditambahkan.";
        } catch (Exception e) {
            return "FAILED|Gagal menambah menu: " + e.getMessage();
        }
    }

    @WebMethod(operationName = "updateMenu")
    public String updateMenu(@WebParam(name = "id") int id, 
                             @WebParam(name = "nama") String nama, 
                             @WebParam(name = "harga") int harga, 
                             @WebParam(name = "kategori") String kategori, 
                             @WebParam(name = "status") String status) {
        try {
            Menu menu = new Menu();
            menu.setId(id);
            menu.setNama(nama);
            menu.setHarga(harga);
            menu.setKategori(kategori);
            menu.setStatus(status);
            menu.updateData();
            return "SUCCESS|Menu " + nama + " berhasil di-update.";
        } catch (Exception e) {
            return "FAILED|Gagal meng-update menu: " + e.getMessage();
        }
    }

    @WebMethod(operationName = "deleteMenu")
    public String deleteMenu(@WebParam(name = "id") int id) {
        try {
            Menu menu = new Menu();
            menu.setId(id);
            menu.deleteData();
            return "SUCCESS|Menu berhasil dihapus.";
        } catch (Exception e) {
            return "FAILED|Gagal menghapus menu: " + e.getMessage();
        }
    }
}