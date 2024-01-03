
package frame.admin;

import connection.Koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import model.Kelas;
import util.FrameSetting;
import util.JamDigital;

/**
 *
 * @author Randa
 */
public class KelasFrame extends javax.swing.JFrame {
    
    private Kelas kelas;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Statement st;
    private String qry;

    /**
     * Method untuk mengambil seluruh kelas
     * @param keyword
     * @return kelasList
     */
    public ArrayList<Kelas> getKelasList(String keyword) {
        ArrayList<Kelas> kelasList = new ArrayList<>();
        con = Koneksi.getKoneksi();
        qry = "SELECT * FROM kelas" + keyword;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(qry);
            while (rs.next()) {                
                kelas = new Kelas(
                        rs.getString("kode_kelas"), 
                        rs.getString("nama_kelas"), 
                        rs.getString("semester_kelas"));
                kelasList.add(kelas);
            }
        } catch (SQLException | NullPointerException ex) {
            System.err.println("Error getKelasList(): " + ex.getMessage());
        }
        return kelasList;
    }
    
    /**
     * Method untuk menampilkan data kelas ke tabel kelas
     */
    public void selectKelas(String keyword) {
        ArrayList<Kelas> list = getKelasList(keyword);
        DefaultTableModel model = (DefaultTableModel) tKelas.getModel();
        Object[] row = new Object[4];
        int no = 1;
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = no++;
            row[1] = list.get(i).getKodeKelas();
            row[2] = list.get(i).getKelas();
            row[3] = list.get(i).getSemester();
            model.addRow(row);
        }
    }
    
    /**
     * Method untuk mengatur ulang / refresh tabel kelas
     */
    public final void resetTable(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tKelas.getModel();
        model.setRowCount(0);
        selectKelas(keyword);
    }
    
    /**
     * Method untuk membuat kode kelas
     * @return kode
     */
    public String makeKode() {
        String kode = null;
        String date = null; 
        String lastKode = null;
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        date = df.format(now);
        kode = "TI" + date + cbSemester.getSelectedItem().toString().substring(9, 10) + "01";
        try {
            con = Koneksi.getKoneksi();
            qry = "SELECT kode_kelas FROM kelas WHERE kode_kelas LIKE ? ORDER BY kode_kelas DESC";
            ps = con.prepareStatement(qry);
            ps.setString(1, "TI" + date + cbSemester.getSelectedItem().toString().substring(9, 10) + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                lastKode = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            System.err.println("Error makeKode() : " + e.getMessage());
        }
        
        if (lastKode != null) {
            int angka = Integer.parseInt(lastKode.substring(7, 9));
            angka++;
            kode = "TI" + date + cbSemester.getSelectedItem().toString().substring(9, 10) + String.format("%02d", angka);
        }
        return kode;
    }
    
    /**
     * Method untuk membersihkan field
     */
    public void clearField() {
        eKode.setText("");
        eKelas.setText("");
        cbSemester.setSelectedIndex(0);
        eKelas.requestFocus();
        bKode.setEnabled(true);
    }
    
    /**
     * Method untuk mengatur lebar kolom tabel kelas
     */
    public void lebarKolom(){ 
        TableColumn column;
        tKelas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        column = tKelas.getColumnModel().getColumn(0); 
        column.setPreferredWidth(30);
        column = tKelas.getColumnModel().getColumn(1); 
        column.setPreferredWidth(380); 
        column = tKelas.getColumnModel().getColumn(2); 
        column.setPreferredWidth(215); 
        column = tKelas.getColumnModel().getColumn(3); 
        column.setPreferredWidth(399); 
    }
    
    /**
     * Creates new form MakulFrame
     */
    public KelasFrame() {
        initComponents();
        FrameSetting.setFrame(this);
        lebarKolom();
        JamDigital.getJam(lbl_jam);
        resetTable("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        eKelas = new javax.swing.JTextField();
        eKode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        bKembali = new javax.swing.JLabel();
        bLogout = new javax.swing.JLabel();
        bExit = new javax.swing.JLabel();
        eCari = new javax.swing.JTextField();
        cbSemester = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tKelas = new javax.swing.JTable();
        cbCari = new javax.swing.JComboBox<>();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        bKode = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        lbl_jam = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel2.setText("Kode");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, -1, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel3.setText("Kelas");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, -1, -1));

        eKelas.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        getContentPane().add(eKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, 270, -1));

        eKode.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eKode.setEnabled(false);
        getContentPane().add(eKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 190, 190, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel5.setText("Semester");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, -1, -1));

        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/back.png"))); // NOI18N
        bKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bKembaliMouseClicked(evt);
            }
        });
        getContentPane().add(bKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 70, 90));

        bLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/logout.png"))); // NOI18N
        bLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(bLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 10, 70, 90));

        bExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/exit.png"))); // NOI18N
        bExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bExitMouseClicked(evt);
            }
        });
        getContentPane().add(bExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 10, 90, 90));

        eCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eCariKeyPressed(evt);
            }
        });
        getContentPane().add(eCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 545, 210, -1));

        cbSemester.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8" }));
        cbSemester.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, 270, 30));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel15.setText("Cari Kelas");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 549, -1, -1));

        tKelas.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        tKelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Kode Kelas", "Kelas", "Semester"
            }
        ));
        tKelas.setGridColor(new java.awt.Color(0, 0, 0));
        tKelas.setMinimumSize(new java.awt.Dimension(100, 0));
        tKelas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tKelasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tKelas);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 590, 1030, 190));

        cbCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kelas", "Semester", "Kode Kelas" }));
        cbCari.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 545, 100, 30));

        bUbah.setBackground(new java.awt.Color(198, 210, 211));
        bUbah.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        bUbah.setForeground(new java.awt.Color(59, 59, 59));
        bUbah.setText("UBAH");
        bUbah.setToolTipText("");
        bUbah.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });
        getContentPane().add(bUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 350, 180, 40));

        bHapus.setBackground(new java.awt.Color(198, 210, 211));
        bHapus.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        bHapus.setForeground(new java.awt.Color(59, 59, 59));
        bHapus.setText("HAPUS");
        bHapus.setToolTipText("");
        bHapus.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });
        getContentPane().add(bHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 350, 180, 40));

        bBatal.setBackground(new java.awt.Color(198, 210, 211));
        bBatal.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        bBatal.setForeground(new java.awt.Color(59, 59, 59));
        bBatal.setText("BATAL");
        bBatal.setToolTipText("");
        bBatal.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalActionPerformed(evt);
            }
        });
        getContentPane().add(bBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 350, 180, 40));

        bKode.setBackground(new java.awt.Color(198, 210, 211));
        bKode.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bKode.setForeground(new java.awt.Color(59, 59, 59));
        bKode.setText("Buat Kode");
        bKode.setToolTipText("");
        bKode.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKodeActionPerformed(evt);
            }
        });
        getContentPane().add(bKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 191, 110, 30));

        bSimpan.setBackground(new java.awt.Color(198, 210, 211));
        bSimpan.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        bSimpan.setForeground(new java.awt.Color(59, 59, 59));
        bSimpan.setText("SIMPAN");
        bSimpan.setToolTipText("");
        bSimpan.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(bSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 180, 40));

        lbl_jam.setFont(new java.awt.Font("Microsoft YaHei", 1, 20)); // NOI18N
        lbl_jam.setForeground(new java.awt.Color(255, 255, 255));
        lbl_jam.setText("Jam");
        getContentPane().add(lbl_jam, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DATA KELAS");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 110));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 860));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bExitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_bExitMouseClicked

    private void bLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bLogoutMouseClicked
        // TODO add your handling code here:
        new frame.login.Login().setVisible(true);
        dispose();
    }//GEN-LAST:event_bLogoutMouseClicked

    private void bKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bKembaliMouseClicked
        // TODO add your handling code here:
        new MenuUtama().setVisible(true);
        dispose();
    }//GEN-LAST:event_bKembaliMouseClicked

    private void eCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eCariKeyPressed
        // TODO add your handling code here:
        String pilihCari = cbCari.getSelectedItem().toString();
        String pilihan = "";
        switch(pilihCari){
            case "Kelas" :
                pilihan = "nama_kelas";
                break;
            case "Kode Kelas" :
                pilihan = "kode_kelas";
                break;
            case "Semester" :
                pilihan = "semester_kelas";
                break;
        }
        resetTable(" WHERE " + pilihan + " like '%" + eCari.getText() + "%'");
    }//GEN-LAST:event_eCariKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        resetTable("");
    }//GEN-LAST:event_formWindowActivated

    private void tKelasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tKelasMouseClicked
        // TODO add your handling code here:
        eKode.setText(tKelas.getValueAt(tKelas.getSelectedRow(), 1).toString());
        eKelas.setText(tKelas.getValueAt(tKelas.getSelectedRow(), 2).toString());
        cbSemester.setSelectedItem(tKelas.getValueAt(tKelas.getSelectedRow(), 3).toString());
        eKelas.requestFocus();
        bKode.setEnabled(false);
    }//GEN-LAST:event_tKelasMouseClicked

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        if (!(eKelas.getText().equals(""))) { 
            qry = "UPDATE kelas SET nama_kelas=?, semester_kelas=? WHERE kode_kelas=?";
            
            try {
                ps = con.prepareStatement(qry);
                ps.setString(1, eKelas.getText());
                ps.setString(2, cbSemester.getSelectedItem().toString());
                ps.setString(3, eKode.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil mengubah data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                resetTable("");
                clearField();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal mengubah data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                System.err.println("gagal ubah data: " + e.getMessage());
            }
        }else {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diubah");
        }
    }//GEN-LAST:event_bUbahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int pilihan = JOptionPane.showConfirmDialog(
                        null, 
                        "Yakin mau hapus", 
                        "Konfirmasi hapus", 
                        JOptionPane.YES_NO_OPTION);
        if (pilihan == 0) {
            if (!(eKelas.getText().equals(""))) {
                try {
                    con = Koneksi.getKoneksi();
                    qry = "DELETE FROM kelas WHERE kode_kelas = ?";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, eKode.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Berhasil menghapus data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                    resetTable("");
                    clearField();
                } catch (SQLException e) {
                   JOptionPane.showMessageDialog(null, "Gagal menghapus data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                   System.err.println("gagal hapus data: " + e.getMessage());
                }
            }else {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus");
            }
        }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        clearField();
    }//GEN-LAST:event_bBatalActionPerformed

    private void bKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKodeActionPerformed
        // TODO add your handling code here:
        eKode.setText(makeKode());
    }//GEN-LAST:event_bKodeActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        kelas = new Kelas();
        kelas.setKodeKelas(eKode.getText());
        kelas.setKelas(eKelas.getText());
        kelas.setSemester(cbSemester.getSelectedItem().toString());
        
        if (kelas.getKelas().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, "Lengkapi data");
        } else {
            try {   
                con = Koneksi.getKoneksi();
                qry = "INSERT INTO kelas VALUES (?, ?, ?)";

                ps = con.prepareStatement(qry);
                ps.setString(1, kelas.getKodeKelas());
                ps.setString(2, kelas.getKelas());
                ps.setString(3, kelas.getSemester());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil menyimpan data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                resetTable("");
                clearField();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                System.err.println("gagal simpan data: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KelasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KelasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KelasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KelasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KelasFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBatal;
    private javax.swing.JLabel bExit;
    private javax.swing.JButton bHapus;
    private javax.swing.JLabel bKembali;
    private javax.swing.JButton bKode;
    private javax.swing.JLabel bLogout;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cbCari;
    private javax.swing.JComboBox<String> cbSemester;
    private javax.swing.JTextField eCari;
    private javax.swing.JTextField eKelas;
    private javax.swing.JTextField eKode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_jam;
    private javax.swing.JTable tKelas;
    // End of variables declaration//GEN-END:variables
}
