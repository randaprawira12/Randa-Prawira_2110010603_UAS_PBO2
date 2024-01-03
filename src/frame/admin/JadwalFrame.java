
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
import model.Dosen;
import model.Jadwal;
import model.Kelas;
import model.Makul;
import util.FrameSetting;
import util.JamDigital;

/**
 *
 * @author Randa
 */
public class JadwalFrame extends javax.swing.JFrame {
    
    private Jadwal jadwal;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Statement st;
    private String qry;
    
    /**
     * Method untuk mengambil seluruh jadwal
     * @param keyword
     * @return jadwalList
     */
    public ArrayList<Jadwal> getJadwalList(String keyword) {
        ArrayList<Jadwal> jadwalList = new ArrayList<>();
        con = Koneksi.getKoneksi();
        qry = "SELECT * FROM jadwal "
                + "JOIN makul ON jadwal.kode_makul = makul.kode_makul "
                + "JOIN dosen ON jadwal.nip_dosen = dosen.nip "
                + "JOIN kelas ON jadwal.kode_kelas = kelas.kode_kelas ";
        
        String order = " ORDER BY jadwal.kode_jadwal";
        if (!keyword.equals("")) 
            qry = qry + " WHERE jadwal.semester_jadwal LIKE ?";
        
        qry = qry + order;
            
        try {
            ps = con.prepareStatement(qry);
            if (!keyword.equals("")) {
                ps.setString(1, "%" + eCari.getText() + "%");
            }
            rs = ps.executeQuery();
            while (rs.next()) {      
                jadwal = new Jadwal(
                        rs.getString("kode_jadwal"), 
                        rs.getString("makul.kode_makul"), 
                        rs.getString("makul.makul"),
                        rs.getString("dosen.nip"), 
                        rs.getString("dosen.nama_dosen"), 
                        rs.getString("kelas.kode_kelas"), 
                        rs.getString("kelas.nama_kelas"),
                        rs.getString("semester_jadwal"),
                        rs.getString("jam"), 
                        rs.getString("hari"));
                jadwalList.add(jadwal);
            }
        } catch (SQLException e) {
            System.err.println("Error getJadwalList : " + e.getMessage());
        }
        return jadwalList;
    }
    
    /**
     * Method untuk menampilkan data jadwal ke tabel jadwal
     */
    public void selectJadwal(String keyword) {
        ArrayList<Jadwal> list = getJadwalList(keyword);
        DefaultTableModel model = (DefaultTableModel) tJadwal.getModel();
        Object[] row = new Object[8];
        int no = 1;
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = no++;
            row[1] = list.get(i).getKodeJadwal();
            row[2] = list.get(i).getMakul().getMakul();
            row[3] = list.get(i).getDosen().getNama();
            row[4] = list.get(i).getKelas().getKelas();
            row[5] = list.get(i).getSemester();
            row[6] = list.get(i).getJam();
            row[7] = list.get(i).getHari();
            model.addRow(row);
        }
    }
    
    /**
     * Method untuk mengatur ulang / refresh tabel jadwal
     */
    public final void resetTable(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tJadwal.getModel();
        model.setRowCount(0);
        selectJadwal(keyword);
    }
    
    /**
     * Method untuk membuat kode jadwal
     * @return kode
     */
    public String makeKode() {
        String kode = null;
        String date = null; 
        String lastKode = null;
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        date = df.format(now);
        kode = "TI0" + cbSemester.getSelectedItem().toString().substring(9, 10) + date + "01";
        try {
            con = Koneksi.getKoneksi();
            qry = "SELECT kode_jadwal FROM jadwal WHERE kode_jadwal LIKE ? ORDER BY kode_jadwal DESC";
            ps = con.prepareStatement(qry);
            ps.setString(1, "TI0" + cbSemester.getSelectedItem().toString().substring(9, 10) + date + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                lastKode = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            System.err.println("Error makeKode() : " + e.getMessage());
        }
        
        if (lastKode != null) {
            int angka = Integer.parseInt(lastKode.substring(8, 10));
            angka++;
            kode = "TI0" + cbSemester.getSelectedItem().toString().substring(9, 10) + date + String.format("%02d", angka);
        }
        return kode;
    }
    
    /**
     * Method untuk membersihkan field
     */
    public void clearField() {
        eKode.setText("");
        cbSemester.setSelectedIndex(0);
        cbDosen.setSelectedIndex(0);
        cbMakul.setSelectedIndex(0);
        cbKelas.setSelectedIndex(0);
        cbHari.setSelectedIndex(0);
        cbJam.setSelectedIndex(0);
        bKode.setEnabled(true);
    }
    
    /**
     * Method untuk mengambil daftar makul
     * @return ArrayList<Makul> makulList
     */
    public ArrayList<Makul> getMakulList() {
        ArrayList<Makul> makulList = new ArrayList<>();
        Makul makul;
        
        try {
        con = Koneksi.getKoneksi();
        st = con.createStatement();
            
            // query makul
            rs = st.executeQuery("SELECT * FROM makul");
            
            while (rs.next()){
                makul = new Makul(rs.getString("kode_makul"), rs.getString("makul"));
                makulList.add(makul);
            }
        } catch (SQLException e) {
            System.out.println("Error getMakulList(): " + e.getMessage());
        }
        
        return makulList;
    }
    
    /**
     * Method untuk mengambil daftar dosen
     * @return ArrayList<Dosen> dosenList
     */
    public ArrayList<Dosen> getDosenList() {
        ArrayList<Dosen> dosenList = new ArrayList<>();
        Dosen dosen;
        
        try {
        con = Koneksi.getKoneksi();
        st = con.createStatement();
            
            // query dosen
            rs = st.executeQuery("SELECT * FROM dosen");
            
            while (rs.next()){
                dosen = new Dosen(rs.getString("nip"), rs.getString("nama_dosen"));
                dosenList.add(dosen);
            }
        } catch (SQLException e) {
            System.out.println("Error getDosenList(): " + e.getMessage());
        }
        
        return dosenList;
    }
    
    /**
     * Method untuk mengambil daftar kelas
     * @return ArrayList<Kelas> kelasList
     */
    public ArrayList<Kelas> getKelasList() {
        ArrayList<Kelas> kelasList = new ArrayList<>();
        Kelas kelas;
        
        try {
        con = Koneksi.getKoneksi();
        st = con.createStatement();
            
            // query kelas
            rs = st.executeQuery("SELECT * FROM kelas");
            
            while (rs.next()){
                kelas = new Kelas(rs.getString("kode_kelas"), rs.getString("nama_kelas"));
                kelasList.add(kelas);
            }
        } catch (SQLException e) {
            System.out.println("Error getKelasList(): " + e.getMessage());
        }
        
        return kelasList;
    }

    /**
     * Method untuk mengatur lebar kolom tabel jadwal
     */
    public void lebarKolom(){ 
        TableColumn column;
        tJadwal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        column = tJadwal.getColumnModel().getColumn(0); 
        column.setPreferredWidth(35);
        column = tJadwal.getColumnModel().getColumn(1); 
        column.setPreferredWidth(90); 
        column = tJadwal.getColumnModel().getColumn(2); 
        column.setPreferredWidth(293); 
        column = tJadwal.getColumnModel().getColumn(3); 
        column.setPreferredWidth(305); 
        column = tJadwal.getColumnModel().getColumn(4); 
        column.setPreferredWidth(50); 
        column = tJadwal.getColumnModel().getColumn(5); 
        column.setPreferredWidth(90);
        column = tJadwal.getColumnModel().getColumn(6); 
        column.setPreferredWidth(90); 
        column = tJadwal.getColumnModel().getColumn(7); 
        column.setPreferredWidth(70);
    }
    
    /**
     * Method untuk mengambil daftar makul, dosen, kelas dan menampilkannya 
     * di comboBox sebagai pilihan
     */
    public void getListComboBox() {
        cbMakul.removeAllItems();
        cbDosen.removeAllItems();
        cbKelas.removeAllItems();
        
        try {
            con = Koneksi.getKoneksi();
            st = con.createStatement();
            String makul, kode_makul, nip, dosen, kode_kelas, kelas = "";
            
            // query makul
            rs = st.executeQuery("SELECT * FROM makul WHERE semester LIKE '%" + 
                    cbSemester.getSelectedItem().toString() + "%'");
            
            while (rs.next()){
                makul = rs.getString("makul");
                kode_makul = rs.getString("kode_makul");
                cbMakul.addItem("("+kode_makul+")" + " " + makul);
            }
            
            // query dosen
            rs = st.executeQuery("SELECT * FROM dosen");
            
            while (rs.next()){
                nip = rs.getString("nip");
                dosen = rs.getString("nama_dosen");
                cbDosen.addItem("("+nip+")" + " " + dosen);
            }
            
            // query kelas
            rs = st.executeQuery("SELECT * FROM kelas");
            
            while (rs.next()){
                kelas = rs.getString("nama_kelas");
                kode_kelas = rs.getString("kode_kelas");
                cbKelas.addItem("("+kode_kelas+")" + " " + kelas);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Terjadi Kesalahan: " + e.getMessage());
        }
    }
    
    /**
     * Creates new form JadwalFrame
     */
    public JadwalFrame() {
        initComponents();
        FrameSetting.setFrame(this);
        lebarKolom();
        JamDigital.getJam(lbl_jam);
        resetTable("");
        getListComboBox();
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
        eKode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        bKembali = new javax.swing.JLabel();
        bLogout = new javax.swing.JLabel();
        bExit = new javax.swing.JLabel();
        eCari = new javax.swing.JTextField();
        cbMakul = new javax.swing.JComboBox<>();
        bKode = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tJadwal = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbKelas = new javax.swing.JComboBox<>();
        cbJam = new javax.swing.JComboBox<>();
        cbHari = new javax.swing.JComboBox<>();
        cbDosen = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cbSemester = new javax.swing.JComboBox<>();
        lbl_jam = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel2.setText("Kode Jadwal");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 280, -1, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel3.setText("Matakuliah");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, -1, -1));

        eKode.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eKode.setEnabled(false);
        getContentPane().add(eKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 280, 130, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel5.setText("Hari");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 230, -1, -1));

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
        getContentPane().add(eCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 544, 300, -1));

        cbMakul.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbMakul.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "makul" }));
        cbMakul.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbMakul, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 300, 30));

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
        getContentPane().add(bKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 281, 110, 30));

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
        getContentPane().add(bSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 180, 40));

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
        getContentPane().add(bUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 400, 180, 40));

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
        getContentPane().add(bHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 180, 40));

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
        getContentPane().add(bBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 400, 180, 40));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel15.setText("Cari Jadwal Semester");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 545, -1, -1));

        tJadwal.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        tJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Kode Jadwal", "Matakuliah", "Dosen", "Kelas", "Semester", "Jam", "Hari"
            }
        ));
        tJadwal.setGridColor(new java.awt.Color(0, 0, 0));
        tJadwal.setMinimumSize(new java.awt.Dimension(100, 0));
        tJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tJadwalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tJadwal);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 590, 1030, 190));

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel4.setText("Dosen");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, -1, -1));

        jLabel6.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel6.setText("Jam");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 180, -1, -1));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel7.setText("Kelas");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, -1, -1));

        cbKelas.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kelas" }));
        cbKelas.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 300, 30));

        cbJam.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbJam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "07.30-09.00", "09.10-10.40", "10.50-12.20", "13.00-14.30", "14.00-15.30", "14.40-16.10", "15.40-17.10" }));
        cbJam.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbJam, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 180, 300, 30));

        cbHari.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbHari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Senin", "Selasa", "Rabu", "Kamis", "Jumat" }));
        cbHari.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbHari, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 230, 300, 30));

        cbDosen.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbDosen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "dosen" }));
        cbDosen.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbDosen, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 330, 720, 30));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel8.setText("Semester");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, -1, -1));

        cbSemester.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8" }));
        cbSemester.setPreferredSize(new java.awt.Dimension(64, 18));
        cbSemester.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSemesterItemStateChanged(evt);
            }
        });
        getContentPane().add(cbSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 300, 30));

        lbl_jam.setFont(new java.awt.Font("Microsoft YaHei", 1, 20)); // NOI18N
        lbl_jam.setForeground(new java.awt.Color(255, 255, 255));
        lbl_jam.setText("Jam");
        getContentPane().add(lbl_jam, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DATA JADWAL");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 110));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 860));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bExitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_bExitMouseClicked

    private void eCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eCariKeyPressed
        // TODO add your handling code here:
        resetTable(eCari.getText());
    }//GEN-LAST:event_eCariKeyPressed

    private void tJadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tJadwalMouseClicked
        // TODO add your handling code here:
        ArrayList<Makul> makulList = getMakulList();
        ArrayList<Dosen> dosenList = getDosenList();
        ArrayList<Kelas> kelasList = getKelasList();
        
        eKode.setText(tJadwal.getValueAt(tJadwal.getSelectedRow(), 1).toString());        
        cbSemester.setSelectedItem(tJadwal.getValueAt(tJadwal.getSelectedRow(), 5).toString());
        cbJam.setSelectedItem(tJadwal.getValueAt(tJadwal.getSelectedRow(), 6).toString());
        cbHari.setSelectedItem(tJadwal.getValueAt(tJadwal.getSelectedRow(), 7).toString());
        bKode.setEnabled(false);
        
        for (int i = 0; i < dosenList.size(); i++) {
            if (dosenList.get(i).getNama().equals(tJadwal.getValueAt(tJadwal.getSelectedRow(), 3).toString())) 
            {
                cbDosen.setSelectedItem("("+dosenList.get(i).getNip()+") "+dosenList.get(i).getNama());
            }
        }
        
        for (int i = 0; i < kelasList.size(); i++) {
            if (kelasList.get(i).getKelas().equals(tJadwal.getValueAt(tJadwal.getSelectedRow(), 4).toString())) 
            {
                cbKelas.setSelectedItem("("+kelasList.get(i).getKodeKelas()+") "+kelasList.get(i).getKelas());
            }
        }
        
        for (int i = 0; i < makulList.size(); i++) {
            if (makulList.get(i).getMakul().equals(tJadwal.getValueAt(tJadwal.getSelectedRow(), 2).toString())) 
            {
                cbMakul.setSelectedItem("("+makulList.get(i).getKodeMakul()+") "+makulList.get(i).getMakul());
            }
        }
    }//GEN-LAST:event_tJadwalMouseClicked

    private void cbSemesterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSemesterItemStateChanged
        // TODO add your handling code here:
        getListComboBox();
        ArrayList<Makul> list = getMakulList();
        
        try {
            if (tJadwal.getSelectedRow() == -1) {
                // batal
            }else {
               for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMakul().equals(tJadwal.getValueAt(tJadwal.getSelectedRow(), 2).toString())) 
                    {
                        cbMakul.setSelectedItem("("+list.get(i).getKodeMakul()+") "+list.get(i).getMakul());
                    }                       
                } 
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("error: " + e.getMessage());
        }
        
    }//GEN-LAST:event_cbSemesterItemStateChanged

    private void bKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bKembaliMouseClicked
        // TODO add your handling code here:
        new MenuUtama().setVisible(true);
        dispose();
    }//GEN-LAST:event_bKembaliMouseClicked

    private void bLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bLogoutMouseClicked
        // TODO add your handling code here:
        new frame.login.Login().setVisible(true);
        dispose();
    }//GEN-LAST:event_bLogoutMouseClicked

    private void bKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKodeActionPerformed
        // TODO add your handling code here:
        eKode.setText(makeKode());
        cbMakul.setEnabled(true);
    }//GEN-LAST:event_bKodeActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        jadwal = new Jadwal();
        jadwal.setKodeJadwal(eKode.getText());
        jadwal.setSemester(cbSemester.getSelectedItem().toString());
        jadwal.setMakul(
                new Makul(cbMakul.getSelectedItem().toString().substring(1, 7), 
                          cbMakul.getSelectedItem().toString().substring(9))
        );
        jadwal.setKelas(
                new Kelas(cbKelas.getSelectedItem().toString().substring(1, 10), 
                          cbKelas.getSelectedItem().toString().substring(12))
        );
        jadwal.setDosen(
                new Dosen(cbDosen.getSelectedItem().toString().substring(1, 19), 
                          cbDosen.getSelectedItem().toString().substring(21))
        );
        jadwal.setJam(cbJam.getSelectedItem().toString());
        jadwal.setHari(cbHari.getSelectedItem().toString());
        
        if (eKode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Lengkapi data");
        } else {
            try {   
                con = Koneksi.getKoneksi();
                qry = "INSERT INTO jadwal VALUES (?, ?, ?, ?, ?, ?, ?)";

                ps = con.prepareStatement(qry);
                ps.setString(1, jadwal.getKodeJadwal());
                ps.setString(2, jadwal.getMakul().getKodeMakul());
                ps.setString(3, jadwal.getDosen().getNip());
                ps.setString(4, jadwal.getKelas().getKodeKelas());
                ps.setString(5, jadwal.getSemester());
                ps.setString(6, jadwal.getJam());
                ps.setString(7, jadwal.getHari());
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
        if (eKode.getText().equals("")) { 
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diubah");
        }else {
           qry = "UPDATE jadwal SET kode_makul=?, nip_dosen=?, kode_kelas=?, "
                   + "semester_jadwal=?, jam=?, hari=? WHERE kode_jadwal=?";
            
            try {
                ps = con.prepareStatement(qry);
                ps.setString(1, cbMakul.getSelectedItem().toString().substring(1, 7));
                ps.setString(2, cbDosen.getSelectedItem().toString().substring(1, 19));
                ps.setString(3, cbKelas.getSelectedItem().toString().substring(1, 10));
                ps.setString(4, cbSemester.getSelectedItem().toString());
                ps.setString(5, cbJam.getSelectedItem().toString());
                ps.setString(6, cbHari.getSelectedItem().toString());
                ps.setString(7, eKode.getText());
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
            if (eKode.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus");
            }else {
                try {
                    con = Koneksi.getKoneksi();
                    qry = "DELETE FROM jadwal WHERE kode_jadwal = ?";
                    PreparedStatement ps = con.prepareStatement(qry);
                    ps.setString(1, eKode.getText());
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
            java.util.logging.Logger.getLogger(JadwalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JadwalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JadwalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JadwalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JadwalFrame().setVisible(true);
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
    private javax.swing.JComboBox<String> cbDosen;
    private javax.swing.JComboBox<String> cbHari;
    private javax.swing.JComboBox<String> cbJam;
    private javax.swing.JComboBox<String> cbKelas;
    private javax.swing.JComboBox<String> cbMakul;
    private javax.swing.JComboBox<String> cbSemester;
    private javax.swing.JTextField eCari;
    private javax.swing.JTextField eKode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_jam;
    private javax.swing.JTable tJadwal;
    // End of variables declaration//GEN-END:variables
}
