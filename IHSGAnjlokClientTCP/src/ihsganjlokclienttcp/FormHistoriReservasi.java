/*
 * Decompiled with CFR 0.152.
 */
package ihsganjlokclienttcp;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormHistoriReservasi
        extends JFrame {

    private static final Logger logger = Logger.getLogger(FormHistoriReservasi.class.getName());
    private int userId;
    private String nama;
    private String hakAkses;

    public FormHistoriReservasi(int userId, String nama, String hakAkses) {
        this.userId = userId;
        this.nama = nama;
        this.hakAkses = hakAkses;
        this.initComponents();
        if (!this.hakAkses.equalsIgnoreCase("Administrator")) {
            cmbUpdateStatusDetail.setVisible(false);
            btnUpdateStatusDetail.setVisible(false);
            cmbUpdateStatusRes.setVisible(false);
            btnUpdateStatusRes.setVisible(false);
            lblUpdateStatusMenu.setVisible(false);
            lblUpdateStatusReservasi.setVisible(false);
        }
        this.jTextFieldDariTanggal.setText("2026-07-01");
        this.jTextFieldSampaiTanggal.setText("2026-07-31");
        this.viewHistory();
        this.tampilDetailBarisPertama();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Navigasi");
        JMenuItem itemKembali = new JMenuItem("Kembali ke Dashboard");
        itemKembali.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                new FormDashboard(FormHistoriReservasi.this.userId, FormHistoriReservasi.this.nama, FormHistoriReservasi.this.hakAkses).setVisible(true);
                FormHistoriReservasi.this.dispose();
            }
        });
        menu.add(itemKembali);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    public void viewHistory() {
        DefaultTableModel model = (DefaultTableModel) this.tblHistory.getModel();
        model.setRowCount(0);
        String dariTanggal = this.jTextFieldDariTanggal.getText();
        String sampaiTanggal = this.jTextFieldSampaiTanggal.getText();
        String status = this.jComboBoxStatusReservasi.getSelectedItem().toString();
        try {
            List<String> dataHistory = this.kirimKeServerTCP("HISTORY|" + this.userId + "|" + this.hakAkses + "|" + dariTanggal + "|" + sampaiTanggal + "|" + status);
            for (String data : dataHistory) {
                String[] row = data.split("\\|");
                if (row.length >= 7) {
                    model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], row[6]});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan history: " + ex.getMessage());
        }
    }

    public void viewDetailPesanan(int reservasiID) {
        DefaultTableModel model = (DefaultTableModel) this.tblDetail.getModel();
        model.setRowCount(0);
        int total = 0;
        try {
            List<String> dataDetail = this.kirimKeServerTCP("DETAIL_HISTORY|" + reservasiID);
            for (String data : dataDetail) {
                String[] row = data.split("\\|");
                if (row.length >= 6) {
                    int subtotal = Integer.parseInt(row[4]);
                    total += subtotal;
                    model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5]});
                }
            }
            this.lblTotal.setText("Total Pesanan: Rp " + total);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan detail pesanan: " + ex.getMessage());
        }
    }

    public void clearDetail() {
        DefaultTableModel model = (DefaultTableModel) this.tblDetail.getModel();
        model.setRowCount(0);
        this.lblTotal.setText("Total Pesanan: Rp 0");
    }

    public void refreshHistory() {
        this.jTextFieldDariTanggal.setText("2026-07-01");
        this.jTextFieldSampaiTanggal.setText("2026-07-31");
        this.jComboBoxStatusReservasi.setSelectedItem("All");
        this.viewHistory();
        this.clearDetail();
    }

    public List<String> kirimKeServerTCP(String pesan) {
        ArrayList<String> hasil = new ArrayList<String>();
        try (Socket socket = new Socket("localhost", 6000); PrintWriter out = new PrintWriter(socket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(pesan);
            String response = in.readLine();
            if (response != null && !response.equals("")) {
                String[] rows;
                for (String row : rows = response.split("#")) {
                    hasil.add(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke TCP Server: " + ex.getMessage());
        }
        return hasil;
    }

    public void tampilDetailBarisPertama() {
        if (this.tblHistory.getRowCount() > 0) {
            this.tblHistory.setRowSelectionInterval(0, 0);
            int reservasiID = Integer.parseInt(this.tblHistory.getValueAt(0, 0).toString());
            this.viewDetailPesanan(reservasiID);
        } else {
            this.clearDetail();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblJudul = new javax.swing.JLabel();
        scrollDetail = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        jButtonRefrase = new javax.swing.JButton();
        jButtonCari = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        jTextFieldDariTanggal = new javax.swing.JTextField();
        jTextFieldSampaiTanggal = new javax.swing.JTextField();
        scrollHistory = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        jComboBoxStatusReservasi = new javax.swing.JComboBox<>();
        btnCancelled = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        lblUpdateStatusMenu = new javax.swing.JLabel();
        cmbUpdateStatusDetail = new javax.swing.JComboBox<>();
        btnUpdateStatusDetail = new javax.swing.JButton();
        lblUpdateStatusReservasi = new javax.swing.JLabel();
        cmbUpdateStatusRes = new javax.swing.JComboBox<>();
        btnUpdateStatusRes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblJudul.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        lblJudul.setText("HISTORY RESERVASI");

        scrollDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail Pesanan"));

        tblDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Menu", "Kategori", "Jumlah", "Harga", "SubTotal", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDetail.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollDetail.setViewportView(tblDetail);

        jButtonRefrase.setBackground(new java.awt.Color(204, 204, 204));
        jButtonRefrase.setText("Refresh");
        jButtonRefrase.addActionListener(this::jButtonRefraseActionPerformed);

        jButtonCari.setBackground(new java.awt.Color(204, 204, 204));
        jButtonCari.setText("Cari");
        jButtonCari.addActionListener(this::jButtonCariActionPerformed);

        lblTotal.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblTotal.setText("Total Pesanan: Rp 0");

        label1.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        label1.setText(" Dari Tanggal :");

        label2.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        label2.setText("Status Reservasi :");

        label3.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        label3.setText("Sampai Tanggal :");

        jTextFieldDariTanggal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextFieldDariTanggal.addActionListener(this::jTextFieldDariTanggalActionPerformed);

        jTextFieldSampaiTanggal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        scrollHistory.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel History Reservasi Final"));

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Reservasi", "Customer", "Nomor Meja", "Tanggal", "Jumlah Tamu", "Status Reservasi", "Total Pesanan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHistory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHistoryMouseClicked(evt);
            }
        });
        scrollHistory.setViewportView(tblHistory);

        jComboBoxStatusReservasi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBoxStatusReservasi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pending", "Confirmed", "Completed", "Cancelled" }));

        btnCancelled.setBackground(new java.awt.Color(204, 204, 204));
        btnCancelled.setText("Cancel Reservation");
        btnCancelled.addActionListener(this::btnCancelledActionPerformed);

        btnKembali.setBackground(new java.awt.Color(204, 204, 204));
        btnKembali.setText("Kembali");
        btnKembali.addActionListener(this::btnKembaliActionPerformed);

        lblUpdateStatusMenu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUpdateStatusMenu.setText("Update Status Menu:");

        cmbUpdateStatusDetail.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Preparing", "Ready", "Served", "Cancelled" }));
        cmbUpdateStatusDetail.setToolTipText("");

        btnUpdateStatusDetail.setText("UPDATE");
        btnUpdateStatusDetail.addActionListener(this::btnUpdateStatusDetailActionPerformed);

        lblUpdateStatusReservasi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUpdateStatusReservasi.setText("Update Status Reservasi:");

        cmbUpdateStatusRes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Confirmed", "Completed", "Cancelled" }));

        btnUpdateStatusRes.setText("UPDATE");
        btnUpdateStatusRes.addActionListener(this::btnUpdateStatusResActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(425, 425, 425)
                        .addComponent(lblJudul))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(16, 16, 16)
                                    .addComponent(jComboBoxStatusReservasi, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(71, 71, 71)
                                    .addComponent(jButtonCari)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButtonRefrase)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnCancelled)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(20, 20, 20)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldDariTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldSampaiTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addComponent(scrollDetail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblUpdateStatusReservasi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbUpdateStatusRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(btnUpdateStatusRes))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblUpdateStatusMenu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbUpdateStatusDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdateStatusDetail)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblJudul)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDariTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSampaiTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxStatusReservasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCari)
                            .addComponent(jButtonRefrase)
                            .addComponent(btnCancelled)
                            .addComponent(btnKembali))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(scrollDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUpdateStatusMenu)
                    .addComponent(cmbUpdateStatusDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateStatusDetail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUpdateStatusReservasi)
                    .addComponent(cmbUpdateStatusRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateStatusRes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(lblTotal)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelledActionPerformed
        int baris = this.tblHistory.getSelectedRow();
        if (baris >= 0) {
            String status = this.tblHistory.getValueAt(baris, 5).toString();
            if (status.equalsIgnoreCase("Cancelled")) {
                JOptionPane.showMessageDialog(this, "Reservasi sudah dibatalkan sebelumnya.");
                return;
            }
            if (status.equalsIgnoreCase("Completed")) {
                JOptionPane.showMessageDialog(this, "Reservasi yang sudah selesai (Completed) tidak dapat dibatalkan.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin membatalkan reservasi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int reservasiID = Integer.parseInt(this.tblHistory.getValueAt(baris, 0).toString());
                try {
                    List<String> response = this.kirimKeServerTCP("BATAL_RESERVASI~" + reservasiID);
                    if (!response.isEmpty()) {
                        JOptionPane.showMessageDialog(this, response.get(0));
                        this.viewHistory();
                        this.tampilDetailBarisPertama();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal membatalkan reservasi: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih reservasi yang ingin dibatalkan terlebih dahulu.");
        }
    }//GEN-LAST:event_btnCancelledActionPerformed

    private void btnUpdateStatusResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateStatusResActionPerformed
        // TODO add your handling code here:
        int baris = this.tblHistory.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih reservasi yang mau diupdate!");
            return;
        }
        String idRes = this.tblHistory.getValueAt(baris, 0).toString();
        String statusBaru = cmbUpdateStatusRes.getSelectedItem().toString();

        try {
            List<String> resp = kirimKeServerTCP("UPDATE_STATUS_RESERVASI|" + idRes + "|" + statusBaru);
            if (!resp.isEmpty()) {
                JOptionPane.showMessageDialog(this, resp.get(0));
                viewHistory(); 
                tampilDetailBarisPertama();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal update: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnUpdateStatusResActionPerformed

    private void btnUpdateStatusDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateStatusDetailActionPerformed
        // TODO add your handling code here:
        int barisRes = this.tblHistory.getSelectedRow();
        int barisDet = this.tblDetail.getSelectedRow();

        if (barisRes < 0 || barisDet < 0) {
            JOptionPane.showMessageDialog(this, "Pilih Reservasi dan Detail Menu yang mau diupdate!");
            return;
        }

        String idRes = this.tblHistory.getValueAt(barisRes, 0).toString();
        String namaMenu = this.tblDetail.getValueAt(barisDet, 0).toString(); 
        String statusBaru = cmbUpdateStatusDetail.getSelectedItem().toString();

        try {
            List<String> resp = kirimKeServerTCP("UPDATE_STATUS_DETAIL|" + idRes + "|" + namaMenu + "|" + statusBaru);
            if (!resp.isEmpty()) {
                JOptionPane.showMessageDialog(this, resp.get(0));
                viewDetailPesanan(Integer.parseInt(idRes)); 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal update: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnUpdateStatusDetailActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {
        new FormDashboard(this.userId, this.nama, this.hakAkses).setVisible(true);
        this.dispose();
    }

    private void jButtonCariActionPerformed(ActionEvent evt) {
        this.viewHistory();
        this.tampilDetailBarisPertama();
    }

    private void jTextFieldDariTanggalActionPerformed(ActionEvent evt) {
    }

    private void tblHistoryMouseClicked(MouseEvent evt) {
        int baris = this.tblHistory.getSelectedRow();
        if (baris >= 0) {
            int reservasiID = Integer.parseInt(this.tblHistory.getValueAt(baris, 0).toString());
            this.viewDetailPesanan(reservasiID);
        }
    }

    private void jButtonRefraseActionPerformed(ActionEvent evt) {
        this.jTextFieldDariTanggal.setText("2026-07-01");
        this.jTextFieldSampaiTanggal.setText("2026-07-31");
        this.jComboBoxStatusReservasi.setSelectedItem("All");
        this.viewHistory();
        this.tampilDetailBarisPertama();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (!"Nimbus".equals(info.getName())) {
                    continue;
                }
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(() -> new FormHistoriReservasi(0, "", "").setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelled;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnUpdateStatusDetail;
    private javax.swing.JButton btnUpdateStatusRes;
    private javax.swing.JComboBox<String> cmbUpdateStatusDetail;
    private javax.swing.JComboBox<String> cmbUpdateStatusRes;
    private javax.swing.JButton jButtonCari;
    private javax.swing.JButton jButtonRefrase;
    private javax.swing.JComboBox<String> jComboBoxStatusReservasi;
    private javax.swing.JTextField jTextFieldDariTanggal;
    private javax.swing.JTextField jTextFieldSampaiTanggal;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUpdateStatusMenu;
    private javax.swing.JLabel lblUpdateStatusReservasi;
    private javax.swing.JScrollPane scrollDetail;
    private javax.swing.JScrollPane scrollHistory;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTable tblHistory;
    // End of variables declaration//GEN-END:variables
}
