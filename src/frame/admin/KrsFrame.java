
package frame.admin;

import frame.admin.*;
import connection.Koneksi;
import frame.mahasiswa.MenuUtamaMhs;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Dosen;
import model.Jadwal;
import model.Kelas;
import model.Krs;
import model.Makul;
import model.Mhs;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import util.FrameSetting;
import util.JamDigital;

/**
 *
 * @author Randa
 */
public class KrsFrame extends javax.swing.JFrame {
    
    private Krs krs;
    private Connection con;
    private ResultSet rs;
    private PreparedStatement ps;
    private Statement st;
    private String qry;
    private String idKrs;
    
    /**
     * Method untuk mengambil seluruh krs
     * @param keyword 
     * @return ArrayList<Krs> krsList
     */
    public ArrayList<Krs> getKrsList(String keyword) {
        ArrayList<Krs> krsList = new ArrayList<>();
        
        qry = "SELECT * FROM krs "
                + "JOIN mhs ON krs.id_mhs = mhs.id_mhs "
                + "JOIN jadwal ON krs.kode_jadwal = jadwal.kode_jadwal "
                + "JOIN makul ON jadwal.kode_makul = makul.kode_makul "
                + "JOIN dosen ON jadwal.nip_dosen = dosen.nip "
                + "JOIN kelas ON jadwal.kode_kelas = kelas.kode_kelas";
        
        if (!keyword.equals("")) 
            qry = qry + " WHERE mhs.npm LIKE ?";
            
        try {
            ps = con.prepareStatement(qry);
            if (!keyword.equals("")) {
                ps.setString(1, "%" + eCari.getText() + "%");
            }
            rs = ps.executeQuery();
            while (rs.next()) {   
                krs = new Krs(rs.getString("id_krs"),
                                rs.getInt("mhs.id_mhs"),
                                rs.getString("mhs.npm"),
                                rs.getString("mhs.nama"),
                                rs.getString("mhs.prog_studi"),
                                rs.getString("jadwal.kode_jadwal"),
                                rs.getString("makul.kode_makul"),
                                rs.getString("makul.makul"),
                                rs.getInt("makul.sks"),
                                rs.getString("makul.semester"),
                                rs.getString("kelas.kode_kelas"),
                                rs.getString("kelas.nama_kelas"),
                                rs.getString("dosen.nip"),
                                rs.getString("dosen.nama_dosen"),
                                rs.getString("tahun_ajar"));
                krsList.add(krs);
            }
        } catch (SQLException e) {
            System.err.println("Error getKrsList : " + e.getMessage());
        }
        return krsList;
    }
    
    /**
     * Method untuk menampilkan data krs ke tabel krs
     * @param keyword 
     */
    public void selectKrs(String keyword) {
        ArrayList<Krs> list = getKrsList(keyword);
        DefaultTableModel model = (DefaultTableModel) tKrsMhs.getModel();
        Object[] row = new Object[12];
        int no = 1;
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = no++;
            row[1] = list.get(i).getIdKrs();
            row[2] = list.get(i).getMhs().getNpm();
            row[3] = list.get(i).getMhs().getNama();
            row[4] = list.get(i).getMhs().getProgStudi();
            row[5] = list.get(i).getKodeJadwal();
            row[6] = list.get(i).getDosen().getNama();
            row[7] = list.get(i).getMakul().getMakul();
            row[8] = list.get(i).getMakul().getSks();
            row[9] = list.get(i).getMakul().getSemester();
            row[10] = list.get(i).getKelas().getKelas();
            row[11] = list.get(i).getTahunAjar();
            model.addRow(row);
        }
    }
    
    /**
     * Method untuk mengatur ulang / refresh tabel krs
     * @param keyword 
     */
    public final void resetTable(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tKrsMhs.getModel();
        model.setRowCount(0);
        selectKrs(keyword);
    }

    /**
     * Method untuk menampilkan jadwal matakuliah pada tabel daftar matakuliah
     */
    public void setTableList() {
        Jadwal jadwal;
        ArrayList<Jadwal> jadwalList = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) tList.getModel();
        Object[] row = new Object[5];
        int no = 1;
        con = Koneksi.getKoneksi();
        qry = "SELECT * FROM jadwal "
                + "JOIN makul ON jadwal.kode_makul = makul.kode_makul "
                + "JOIN dosen ON jadwal.nip_dosen = dosen.nip "
                + "JOIN kelas ON jadwal.kode_kelas = kelas.kode_kelas "
                + "WHERE jadwal.semester_jadwal LIKE ? ORDER BY jadwal.kode_jadwal";

        try {
            ps = con.prepareStatement(qry);
            ps.setString(1, "%" + cbSemester.getSelectedItem().toString() + "%");

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
        
        for (int i = 0; i < jadwalList.size(); i++) {
            row[0] = no++;
            row[1] = jadwalList.get(i).getKodeJadwal();
            row[2] = jadwalList.get(i).getMakul().getMakul();
            row[3] = jadwalList.get(i).getDosen().getNama();
            row[4] = jadwalList.get(i).getKelas().getKelas();
            model.addRow(row);
        }
    }
    
    /**
     * Method untuk mengatur ulang / refresh tabel daftar makul
     */
    public final void resetTableList() {
        DefaultTableModel model = (DefaultTableModel) tList.getModel();
        model.setRowCount(0);
    }
    
    /**
     * Method untuk mengambil daftar jadwal pada tabel krs yang sudah diambil 
     * mahasiswa berdasarkan idMhs
     * @param idMhs
     * @return ArrayList<String> listKrsMhs
     */
    public ArrayList<String> getListKrsMhs(int id) {
        ArrayList<String> listKrsMhs = new ArrayList<>();
        String krsMhs;
        
        try {
            con = Koneksi.getKoneksi();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM krs WHERE id_mhs = " + id);

            while (rs.next()){
                krsMhs = rs.getString("kode_jadwal");
                listKrsMhs.add(krsMhs);
            }
        } catch (SQLException e) {
            System.err.println("Error getListKrsMhs(): " + e.getMessage());
        }
        return listKrsMhs;
    }
    
    /**
     * Method untuk membuat idKrs
     * @return id
     */
    public String makeId() {
        String id = null;
        String lastKode = null;
        id = "KS0001";
        try {
            con = Koneksi.getKoneksi();
            qry = "SELECT id_krs FROM krs WHERE id_krs LIKE ? ORDER BY id_krs DESC";
            ps = con.prepareStatement(qry);
            ps.setString(1, "KS%");
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                lastKode = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            System.err.println("Error makeId() : " + e.getMessage());
        }
        
        if (lastKode != null) {
            int angka = Integer.parseInt(lastKode.substring(2));
            angka++;
            id = "KS" + String.format("%04d", angka);
        }
        return id;
    }
    
    /**
     * Method untuk membersihkan field
     */
    public void clearField() {
        eNama.setText("");
        eNpm.setText("");
        eId.setText("");
        cbSemester.setSelectedIndex(0);
        eDosen.setText("");
        eKdJadwal.setText("");
        eMakul.setText("");
        eTahun.setText("");
        eCari.setText("ketikan npm...");
        eIdMhs.setText("ketikan npm...");
        setTahunAjar();
    }
    
    /**
     * Method untuk mengatur tahun ajar sesuai tahun sekarang
     */
    public void setTahunAjar() {
        String year, tahunAjar = "";
        int nextYear;
        int angka = 0;
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        year = df.format(now);
        
        nextYear = Integer.parseInt(year);
        tahunAjar = year + "/" + ++nextYear;
        eTahun.setText(tahunAjar);
    }
    
    /**
     * Method untuk mengatur label daftar makul sesuai dengan semester yang dipilih
     */
    public void setLabelDaftar() {
        lblDaftar.setText("Daftar Matakuliah " + cbSemester.getSelectedItem().toString());
    }
    
    /**
     * Method untuk mengatur lebar kolom tabel krs dan tabel daftar makul
     */
    public void lebarKolom(){ 
        TableColumn column;
        tList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        
        // tabel krs
        column = tKrsMhs.getColumnModel().getColumn(0); 
        column.setPreferredWidth(10);
        column = tKrsMhs.getColumnModel().getColumn(1); 
        column.setPreferredWidth(20);
        column = tKrsMhs.getColumnModel().getColumn(2); 
        column.setPreferredWidth(50);
        column = tKrsMhs.getColumnModel().getColumn(3); 
        column.setPreferredWidth(180);
        column = tKrsMhs.getColumnModel().getColumn(4); 
        column.setPreferredWidth(105);
        column = tKrsMhs.getColumnModel().getColumn(5); 
        column.setPreferredWidth(45);
        column = tKrsMhs.getColumnModel().getColumn(6); 
        column.setPreferredWidth(220);
        column = tKrsMhs.getColumnModel().getColumn(7); 
        column.setPreferredWidth(180);
        column = tKrsMhs.getColumnModel().getColumn(8); 
        column.setPreferredWidth(10);
        column = tKrsMhs.getColumnModel().getColumn(9); 
        column.setPreferredWidth(50);
        column = tKrsMhs.getColumnModel().getColumn(10); 
        column.setPreferredWidth(10);
        column = tKrsMhs.getColumnModel().getColumn(11); 
        column.setPreferredWidth(50);
        
        // tabel list
        column = tList.getColumnModel().getColumn(0); 
        column.setPreferredWidth(30);
        column = tList.getColumnModel().getColumn(1); 
        column.setPreferredWidth(100); 
        column = tList.getColumnModel().getColumn(2); 
        column.setPreferredWidth(244); 
        column = tList.getColumnModel().getColumn(3); 
        column.setPreferredWidth(340);
        column = tList.getColumnModel().getColumn(4); 
        column.setPreferredWidth(50);
    }
    
    /**
     * Creates new form MakulFrame
     */
    public KrsFrame() {
        initComponents();
        FrameSetting.setFrame(this);
        JamDigital.getJam(lbl_jam);
        lebarKolom();
        setTahunAjar();
        setTableList(); 
        setLabelDaftar();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        bKembali = new javax.swing.JLabel();
        bLogout = new javax.swing.JLabel();
        bExit = new javax.swing.JLabel();
        lblDaftar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbSemester = new javax.swing.JComboBox<>();
        eId = new javax.swing.JTextField();
        eNpm = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        eKdJadwal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        eIdMhs = new javax.swing.JTextField();
        eCari = new javax.swing.JTextField();
        eMakul = new javax.swing.JTextField();
        eDosen = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tList = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        eTahun = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tKrsMhs = new javax.swing.JTable();
        eNama = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        bSimpan = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        bCetak = new javax.swing.JButton();
        bNpm = new javax.swing.JButton();
        lbl_jam = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/back.png"))); // NOI18N
        bKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bKembaliMouseClicked(evt);
            }
        });
        getContentPane().add(bKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(1350, 10, 70, 90));

        bLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/logout.png"))); // NOI18N
        bLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(bLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1400, 10, 70, 90));

        bExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/button/exit.png"))); // NOI18N
        bExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bExitMouseClicked(evt);
            }
        });
        getContentPane().add(bExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1450, 10, 90, 90));

        lblDaftar.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        lblDaftar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDaftar.setText("Daftar Matakuliah ");
        getContentPane().add(lblDaftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 210, 770, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel3.setText("ID");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, -1, -1));

        jLabel6.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel6.setText("Matakuliah");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 470, -1, -1));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel7.setText("Kode Jadwal");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 420, -1, -1));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel8.setText("Semester");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, -1, -1));

        cbSemester.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8" }));
        cbSemester.setPreferredSize(new java.awt.Dimension(64, 18));
        cbSemester.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSemesterItemStateChanged(evt);
            }
        });
        getContentPane().add(cbSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 370, 300, 30));

        eId.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eId.setEnabled(false);
        getContentPane().add(eId, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 220, 300, -1));

        eNpm.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        getContentPane().add(eNpm, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 160, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel5.setText("Dosen");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 520, -1, -1));

        eKdJadwal.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eKdJadwal.setEnabled(false);
        getContentPane().add(eKdJadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 300, -1));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel15.setText("Cari KRS Mahasiswa");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 655, -1, -1));

        eIdMhs.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eIdMhs.setForeground(new java.awt.Color(102, 102, 102));
        eIdMhs.setText("ketikan npm...");
        eIdMhs.setPreferredSize(new java.awt.Dimension(118, 27));
        eIdMhs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eIdMhsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                eIdMhsFocusLost(evt);
            }
        });
        getContentPane().add(eIdMhs, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 655, 180, 30));

        eCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eCari.setForeground(new java.awt.Color(102, 102, 102));
        eCari.setText("ketikan npm...");
        eCari.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eCariFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                eCariFocusLost(evt);
            }
        });
        eCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eCariKeyPressed(evt);
            }
        });
        getContentPane().add(eCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 655, 300, -1));

        eMakul.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eMakul.setEnabled(false);
        getContentPane().add(eMakul, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 470, 300, -1));

        eDosen.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eDosen.setEnabled(false);
        getContentPane().add(eDosen, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, 300, -1));

        tList.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        tList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Kode", "Matakuliah", "Dosen", "Kelas"
            }
        ));
        tList.setGridColor(new java.awt.Color(0, 0, 0));
        tList.setMinimumSize(new java.awt.Dimension(100, 0));
        tList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 250, 770, 170));

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel4.setText("Tahun Ajaran");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, -1, -1));

        eTahun.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eTahun.setEnabled(false);
        getContentPane().add(eTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 320, 300, -1));

        jLabel9.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel9.setText("NPM");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, -1, -1));

        tKrsMhs.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        tKrsMhs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "ID KRS", "NPM", "Nama", "Program Studi", "Kode Jadwal", "Dosen", "Matakuliah", "SKS", "Semester", "Kelas", "Tahun Ajaran"
            }
        ));
        tKrsMhs.setGridColor(new java.awt.Color(0, 0, 0));
        tKrsMhs.setMinimumSize(new java.awt.Dimension(100, 0));
        tKrsMhs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tKrsMhsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tKrsMhs);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 700, 1430, 280));

        eNama.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eNama.setEnabled(false);
        eNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eNamaActionPerformed(evt);
            }
        });
        getContentPane().add(eNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 270, 300, -1));

        jLabel10.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel10.setText("Nama");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 270, -1, -1));

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
        getContentPane().add(bSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 450, 180, 40));

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
        getContentPane().add(bHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 450, 180, 40));

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
        getContentPane().add(bBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 450, 180, 40));

        bCetak.setBackground(new java.awt.Color(198, 210, 211));
        bCetak.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bCetak.setForeground(new java.awt.Color(59, 59, 59));
        bCetak.setText("Cetak");
        bCetak.setToolTipText("");
        bCetak.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCetakActionPerformed(evt);
            }
        });
        getContentPane().add(bCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 654, 100, 30));

        bNpm.setBackground(new java.awt.Color(198, 210, 211));
        bNpm.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bNpm.setForeground(new java.awt.Color(59, 59, 59));
        bNpm.setText("Cek Mahasiswa");
        bNpm.setToolTipText("");
        bNpm.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bNpm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNpmActionPerformed(evt);
            }
        });
        getContentPane().add(bNpm, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 171, 140, 30));

        lbl_jam.setFont(new java.awt.Font("Microsoft YaHei", 1, 20)); // NOI18N
        lbl_jam.setForeground(new java.awt.Color(255, 255, 255));
        lbl_jam.setText("Jam");
        getContentPane().add(lbl_jam, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1540, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("KARTU RENCANA STUDI");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1560, 110));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1550, 1040));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bExitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_bExitMouseClicked

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

    private void cbSemesterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSemesterItemStateChanged
        // TODO add your handling code here:
        resetTableList();
        setTableList();
        setLabelDaftar();
    }//GEN-LAST:event_cbSemesterItemStateChanged

    private void tListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tListMouseClicked
        // TODO add your handling code here:
        eKdJadwal.setText(tList.getValueAt(tList.getSelectedRow(), 1).toString());    
        eMakul.setText(tList.getValueAt(tList.getSelectedRow(), 2).toString());
        eDosen.setText(tList.getValueAt(tList.getSelectedRow(), 3).toString());
    }//GEN-LAST:event_tListMouseClicked

    private void tKrsMhsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tKrsMhsMouseClicked
        // TODO add your handling code here:
        idKrs = tKrsMhs.getValueAt(tKrsMhs.getSelectedRow(), 1).toString();
    }//GEN-LAST:event_tKrsMhsMouseClicked

    private void eNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eNamaActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        krs = new Krs();
        krs.setIdKrs(makeId());
        krs.setIdMhs(Integer.parseInt(eId.getText()));
        krs.setKodeJadwal(eKdJadwal.getText());
        krs.setTahunAjar(eTahun.getText());
        
        if (eNpm.getText().equals("") || eKdJadwal.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Lengkapi data");
        } else {   
            ArrayList<String> list = getListKrsMhs(krs.getIdMhs());
            qry = "INSERT INTO krs VALUES (?, ?, ?, ?)";
            int listSize = list.size();
            int gagal = 0;
            
            try { 
                for (int i = 0; i < listSize; i++) {
                    if (list.get(i).equals(krs.getKodeJadwal()))
                        gagal = 1;
                }
                
                if (gagal == 0) {
                    ps = con.prepareStatement(qry);
                    ps.setString(1, krs.getIdKrs());
                    ps.setInt(2, krs.getIdMhs());
                    ps.setString(3, krs.getKodeJadwal());
                    ps.setString(4, krs.getTahunAjar());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Berhasil menyimpan data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                    resetTable("");
                }else 
                   JOptionPane.showMessageDialog(null, "Gagal input, matakuliah ini sudah di input ke krs!", "Gagal input KRS", JOptionPane.ERROR_MESSAGE);  
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                System.err.println("gagal simpan data: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int pilihan = JOptionPane.showConfirmDialog(
                        null, 
                        "Yakin mau hapus", 
                        "Konfirmasi hapus", 
                        JOptionPane.YES_NO_OPTION);
        if (pilihan == 0) {
            if (idKrs == null) {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus");
            }else {
               try {
                    con = Koneksi.getKoneksi();
                    qry = "DELETE FROM krs WHERE id_krs = ?";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, idKrs);
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

    private void bCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCetakActionPerformed
        // TODO add your handling code here:
        int id = 0;
        
        try{
            con = Koneksi.getKoneksi();
            
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM mhs WHERE npm = " + eIdMhs.getText());
            
            while (rs.next()) {                
                id = rs.getInt("id_mhs");
            }
            
            if (id != 0) {
                HashMap param = new HashMap();
                //Mengambil parameter
                param.put("idMhs",id);

                JasperPrint JPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("../../report/krsReport.jasper"), param, con);
                JasperViewer.viewReport(JPrint, false);
            }else
                JOptionPane.showMessageDialog(null, "Mahasiswa dengan NPM: " + eIdMhs + ", tidak ada", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            
        }catch(Exception ex){
            System.out.println("Error print: " + ex.getMessage());
        }
    }//GEN-LAST:event_bCetakActionPerformed

    private void eCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eCariKeyPressed
        // TODO add your handling code here:
        resetTable(eCari.getText());
    }//GEN-LAST:event_eCariKeyPressed

    private void eCariFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eCariFocusLost
        // TODO add your handling code here:
        if (eCari.getText().equals("")) {
            eCari.setText("ketikan npm...");
        }
    }//GEN-LAST:event_eCariFocusLost

    private void eCariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eCariFocusGained
        // TODO add your handling code here:
        if (eCari.getText().equals("ketikan npm...")) {
            eCari.setText("");
        }
    }//GEN-LAST:event_eCariFocusGained

    private void bNpmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNpmActionPerformed
        // TODO add your handling code here:
        try {
            con = Koneksi.getKoneksi();
            st = con.createStatement();

            // query mhs
            rs = st.executeQuery("SELECT * FROM mhs WHERE npm = " + eNpm.getText());

            while (rs.next()){
                eId.setText(rs.getString("id_mhs"));
                eNama.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }//GEN-LAST:event_bNpmActionPerformed

    private void eIdMhsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eIdMhsFocusGained
        // TODO add your handling code here:
        if (eIdMhs.getText().equals("ketikan npm...")) {
            eIdMhs.setText("");
        }
    }//GEN-LAST:event_eIdMhsFocusGained

    private void eIdMhsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eIdMhsFocusLost
        // TODO add your handling code here:
        if (eIdMhs.getText().equals("")) {
            eIdMhs.setText("ketikan npm...");
        }
    }//GEN-LAST:event_eIdMhsFocusLost

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
            java.util.logging.Logger.getLogger(KrsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KrsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KrsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KrsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KrsFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBatal;
    private javax.swing.JButton bCetak;
    private javax.swing.JLabel bExit;
    private javax.swing.JButton bHapus;
    private javax.swing.JLabel bKembali;
    private javax.swing.JLabel bLogout;
    private javax.swing.JButton bNpm;
    private javax.swing.JButton bSimpan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbSemester;
    private javax.swing.JTextField eCari;
    private javax.swing.JTextField eDosen;
    private javax.swing.JTextField eId;
    private javax.swing.JTextField eIdMhs;
    private javax.swing.JTextField eKdJadwal;
    private javax.swing.JTextField eMakul;
    private javax.swing.JTextField eNama;
    private javax.swing.JTextField eNpm;
    private javax.swing.JTextField eTahun;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDaftar;
    private javax.swing.JLabel lbl_jam;
    private javax.swing.JTable tKrsMhs;
    private javax.swing.JTable tList;
    // End of variables declaration//GEN-END:variables
}
