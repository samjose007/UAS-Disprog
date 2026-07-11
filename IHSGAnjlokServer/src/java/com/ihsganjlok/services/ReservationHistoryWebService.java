package com.ihsganjlok.services;

import com.ihsganjlok.model.ReservationHistory;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="ReservationHistoryWebService")
public class ReservationHistoryWebService {
    ReservationHistory history = new ReservationHistory();

    @WebMethod(operationName="showHistoryReservasi")
    public ArrayList<String> showHistoryReservasi(@WebParam(name="dariTanggal") String dariTanggal, @WebParam(name="sampaiTanggal") String sampaiTanggal, @WebParam(name="statusReservasi") String statusReservasi) {
        return this.history.viewHistory(dariTanggal, sampaiTanggal, statusReservasi);
    }

    @WebMethod(operationName="showDetailPesanan")
    public ArrayList<String> showDetailPesanan(@WebParam(name="reservasiID") int reservasiID) {
        return this.history.viewDetailPesanan(reservasiID);
    }
}

