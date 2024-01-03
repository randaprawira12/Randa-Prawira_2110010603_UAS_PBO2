
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
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Makul;
import util.FrameSetting;
import util.JamDigital;

/**
 *
 * @author Randa
 */
public class MakulFrame extends javax.swing.JFrame {

    private Makul makul;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Statement st;
    private String qry;

    /**
     * Method untuk mengambil seluruh makul
     * @param keyword
     * @return makulList
     */
    public ArrayList<Makul> getMakulList(String keyword) {
        ArrayList<Makul> makulList = new ArrayList<>();
        con = Koneksi.getKoneksi();
        qry = "SELECT * FROM makul" + keyword;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(qry);
            while (rs.next()) {                
                makul = new Makul(
                        rs.getString("kode_makul"), 
                        rs.getString("makul"),
                        rs.getInt("sks"),
                        rs.getString("semester"));
                makulList.add(makul);
            }
        } catch (SQLException | NullPointerException ex) {
            System.err.println("Error getMakulList(): " + ex.getMessage());
        }
        return makulList;
    }
    
    /**
     * Method untuk menampilkan data makul ke tabel makul
     */
    public void selectMakul(String keyword) {
        ArrayList<Makul> list = getMakulList(keyword);
        DefaultTableModel model = (DefaultTableModel) tMakul.getModel();
        Object[] row = new Object[5];
        int no = 1;
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = no++;
            row[1] = list.get(i).getKodeMakul();
            row[2] = list.get(i).getMakul();
            row[3] = list.get(i).getSks();
            row[4] = list.get(i).getSemester();
            model.addRow(row);
        }
    }
    
    /**
     * Method untuk mengatur ulang / refresh tabel makul
     */
    public final void resetTable(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tMakul.getModel();
        model.setRowCount(0);
        selectMakul(keyword);
    }
    
    /**
     * Method untuk membersihkan field
     */
    public void clearField() {
        eKdMakul.setText("");
        eMakul.setText("");
        eSks.setText("");
        cbSemester.setSelectedIndex(0);
        bKode.setEnabled(true);
        eMakul.requestFocus();
    }
    
    /**
     * Method untuk membuat 2 huruf random
     * @return twoChar
     */
    public String randomChar() {
        char[] charValue = new char[2];
        String twoChar;
        String alphabet = "ABCDEFGHIJKLMNOVQRSTU";
        int N = alphabet.length();
        Random r = new Random();
        
        for (int i = 0; i < 2; i++) {
            charValue[i] = alphabet.charAt(r.nextInt(N));
        }
        
        twoChar = String.copyValueOf(charValue);
        
        return twoChar;
    }
    
    /**
     * Method untuk membuat kode makul
     * @return kode
     */
    public String makeKode() {
        String kode = null;
        String lastKode = null;
        String kodeChar = randomChar();
        
        kode = "TI" + kodeChar + "01";
        try {
            con = Koneksi.getKoneksi();
            qry = "SELECT kode_makul FROM makul WHERE kode_makul LIKE ? ORDER BY kode_makul DESC";
            ps = con.prepareStatement(qry);
            ps.setString(1, "TI" + kodeChar + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                lastKode = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            System.err.println("Error makeId() : " + e.getMessage());
        }
        
        if (lastKode != null) {
            int angka = Integer.parseInt(lastKode.substring(4, 6));
            angka++;
            kode = "TI" + kodeChar + String.format("%02d", angka);
        }
        return kode;
    }
    
    /**
     * Method untuk mengatur lebar kolom tabel makul
     */
    public void lebarKolom(){ 
        TableColumn column;
        tMakul.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        column = tMakul.getColumnModel().getColumn(0); 
        column.setPreferredWidth(45);
        column = tMakul.getColumnModel().getColumn(1); 
        column.setPreferredWidth(160); 
        column = tMakul.getColumnModel().getColumn(2); 
        column.setPreferredWidth(544); 
        column = tMakul.getColumnModel().getColumn(3); 
        column.setPreferredWidth(120); 
        column = tMakul.getColumnModel().getColumn(4); 
        column.setPreferredWidth(140);
    }
    
    /**
     * Creates new form MakulFrame
     */
    public MakulFrame() {
        initComponents();
        FrameSetting.setFrame(this);
        JamDigital.getJam(lbl_jam);
        lebarKolom();
        resetTable("");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tMakul = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        eKdMakul = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        eMakul = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        eSks = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cbCari = new javax.swing.JComboBox<>();
        eCari = new javax.swing.JTextField();
        bKembali = new javax.swing.JLabel();
        bLogout = new javax.swing.JLabel();
        bExit = new javax.swing.JLabel();
        cbSemester = new javax.swing.JComboBox<>();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        bKode = new javax.swing.JButton();
        lbl_jam = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tMakul.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        tMakul.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Kode Matakuliah", "Matakuliah", "Bobot SKS", "Semester"
            }
        ));
        tMakul.setGridColor(new java.awt.Color(0, 0, 0));
        tMakul.setMinimumSize(new java.awt.Dimension(100, 0));
        tMakul.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tMakulMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tMakul);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 610, 1030, 180));

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel2.setText("Kode Matakuliah");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, -1, -1));

        eKdMakul.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eKdMakul.setEnabled(false);
        getContentPane().add(eKdMakul, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 190, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel3.setText("Matakuliah");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, -1));

        eMakul.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        getContentPane().add(eMakul, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, 300, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel5.setText("Semester");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 220, -1, -1));

        jLabel9.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel9.setText("Bobot SKS");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 280, -1, -1));

        eSks.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        getContentPane().add(eSks, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 280, 330, -1));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel15.setText("Cari Matakuliah");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 560, -1, -1));

        cbCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Matakuliah", "Kode Matakuliah", "Semester", " " }));
        cbCari.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 560, 220, 30));

        eCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eCariKeyPressed(evt);
            }
        });
        getContentPane().add(eCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 560, 300, -1));

        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/back.png"))); // NOI18N
        bKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bKembaliMouseClicked(evt);
            }
        });
        getContentPane().add(bKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 20, 70, 90));

        bLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/logout.png"))); // NOI18N
        bLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(bLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 20, 70, 90));

        bExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/exit.png"))); // NOI18N
        bExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bExitMouseClicked(evt);
            }
        });
        getContentPane().add(bExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 20, 90, 90));

        cbSemester.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8" }));
        cbSemester.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 220, 330, 30));

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
        getContentPane().add(bSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, 180, 40));

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
        getContentPane().add(bUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 180, 40));

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
        getContentPane().add(bHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 380, 180, 40));

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
        getContentPane().add(bBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 380, 180, 40));

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
        getContentPane().add(bKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 221, 110, 30));

        lbl_jam.setFont(new java.awt.Font("Microsoft YaHei", 1, 20)); // NOI18N
        lbl_jam.setForeground(new java.awt.Color(255, 255, 255));
        lbl_jam.setText("Jam");
        getContentPane().add(lbl_jam, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 1160, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("DATA MATA KULIAH");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1270, 130));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 860));

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
            case "Kode Matakuliah" :
                pilihan = "kode_makul";
                break;
            case "Matakuliah" :
                pilihan = "makul";
                break;
            case "Semester" :
                pilihan = "semester";
                break;
        }
        resetTable(" WHERE " + pilihan + " like '%" + eCari.getText() + "%'");
    }//GEN-LAST:event_eCariKeyPressed

    private void tMakulMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tMakulMouseClicked
        // TODO add your handling code here:
        eKdMakul.setText(tMakul.getValueAt(tMakul.getSelectedRow(), 1).toString());
        eMakul.setText(tMakul.getValueAt(tMakul.getSelectedRow(), 2).toString());
        eSks.setText(tMakul.getValueAt(tMakul.getSelectedRow(), 3).toString());
        cbSemester.setSelectedItem(tMakul.getValueAt(tMakul.getSelectedRow(), 4).toString());
        eMakul.requestFocus();
        bKode.setEnabled(false);
    }//GEN-LAST:event_tMakulMouseClicked

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        makul = new Makul();
        makul.setKodeMakul(eKdMakul.getText());
        makul.setMakul(eMakul.getText());
        makul.setSks(Integer.parseInt(eSks.getText()));
        makul.setSemester(cbSemester.getSelectedItem().toString());
        
        if (eMakul.getText().equals("") ||
            eSks.getText().equals("") ||
            eKdMakul.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Lengkapi data");
        } else {
            try {   
                con = Koneksi.getKoneksi();
                qry = "INSERT INTO makul VALUES (?, ?, ?, ?)";

                ps = con.prepareStatement(qry);
                ps.setString(1, makul.getKodeMakul());
                ps.setString(2, makul.getMakul());
                ps.setInt(3, makul.getSks());
                ps.setString(4, makul.getSemester());
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

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        if (eMakul.getText().equals("") ||
            eSks.getText().equals("")) 
        { 
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diubah");
        }else {
            qry = "UPDATE makul SET makul=?, sks=?, semester=? WHERE kode_makul=?";
            
            try {
                ps = con.prepareStatement(qry);
                ps.setString(1, eMakul.getText());
                ps.setString(2, eSks.getText());
                ps.setString(3, cbSemester.getSelectedItem().toString());
                ps.setString(4, eKdMakul.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil mengubah data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                resetTable("");
                clearField();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal mengubah data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                System.err.println("gagal ubah data: " + e.getMessage());
            }
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
            if (eMakul.getText().equals("") ||
                eSks.getText().equals("")) 
            {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus");
            }else {
                try {
                    con = Koneksi.getKoneksi();
                    qry = "DELETE FROM makul WHERE kode_makul = ?";
                    PreparedStatement ps = con.prepareStatement(qry);
                    ps.setString(1, eKdMakul.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Berhasil menghapus data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                    resetTable("");
                    clearField();
                } catch (SQLException e) {
                   JOptionPane.showMessageDialog(null, "Gagal menghapus data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                   System.err.println("gagal hapus data: " + e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        clearField();
    }//GEN-LAST:event_bBatalActionPerformed

    private void bKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKodeActionPerformed
        // TODO add your handling code here:
        eKdMakul.setText(makeKode());
    }//GEN-LAST:event_bKodeActionPerformed

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
            java.util.logging.Logger.getLogger(MakulFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MakulFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MakulFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MakulFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MakulFrame().setVisible(true);
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
    private javax.swing.JTextField eKdMakul;
    private javax.swing.JTextField eMakul;
    private javax.swing.JTextField eSks;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_jam;
    private javax.swing.JTable tMakul;
    // End of variables declaration//GEN-END:variables

}
