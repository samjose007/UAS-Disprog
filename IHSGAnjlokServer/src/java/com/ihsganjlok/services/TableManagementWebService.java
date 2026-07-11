/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package com.ihsganjlok.services;
import com.ihsganjlok.model.Meja;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
/**
 *
 * @author Jose
 */
@WebService(serviceName = "TableManagementWebService")
public class TableManagementWebService {
    
    @WebMethod(operationName = "getAllTables")
    public ArrayList<String> getAllTables() {
        Meja modelMeja = new Meja();
        ArrayList<Object> listDariModel = modelMeja.viewListData();
        ArrayList<String> dataYangDikirim = new ArrayList<>();
        for (Object obj : listDariModel) {
            Meja m = (Meja) obj;
            String baris = m.getId() + "|" + m.getNomorMeja() + "|" + m.getKapasitas() + "|" + m.getStatus();
            dataYangDikirim.add(baris);
        }
        
        return dataYangDikirim;
    }
    
    @WebMethod(operationName = "addTable")
    public String addTable(@WebParam(name = "nomorMeja") int nomorMeja, 
                           @WebParam(name = "kapasitas") int kapasitas, 
                           @WebParam(name = "status") String status) {
        try {
            Meja meja = new Meja();
            meja.setNomorMeja(nomorMeja);
            meja.setKapasitas(kapasitas);
            meja.setStatus(status);
            meja.insertData();
            return "SUCCESS: Meja nomor " + nomorMeja + " berhasil ditambahkan.";
        } catch (Exception e) {
            return "FAILED: Gagal menambah meja. Error: " + e.getMessage();
        }
    }
    
    @WebMethod(operationName = "updateTableStatus")
    public String updateTableStatus(@WebParam(name = "id") int id, 
                                    @WebParam(name = "nomorMeja") int nomorMeja,
                                    @WebParam(name = "kapasitas") int kapasitas,
                                    @WebParam(name = "status") String status) {
        try {
            Meja meja = new Meja();
            meja.setId(id);
            meja.setNomorMeja(nomorMeja);
            meja.setKapasitas(kapasitas);
            meja.setStatus(status);
            meja.updateData();
            return "SUCCESS: Status meja berhasil di-update menjadi " + status;
        } catch (Exception e) {
            return "FAILED: Gagal meng-update meja. Error: " + e.getMessage();
        }
    }
    
    @WebMethod(operationName = "deleteTable")
    public String deleteTable(@WebParam(name = "id") int id) {
        try {
            Meja meja = new Meja();
            meja.setId(id);
            meja.deleteData();
            return "SUCCESS: Meja berhasil dihapus dari sistem.";
        } catch (Exception e) {
            return "FAILED: Gagal menghapus meja. Error: " + e.getMessage();
        }
    }
}