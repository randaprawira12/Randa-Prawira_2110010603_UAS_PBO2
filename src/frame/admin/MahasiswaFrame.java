
package frame.admin;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import connection.Koneksi;
import frame.login.Login;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Mhs;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.FrameSetting;
import util.JamDigital;

/**
 *
 * @author Randa
 */
public class MahasiswaFrame extends javax.swing.JFrame {

    private Mhs mhs;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Statement st;
    private String qry;
    private BufferedImage bImage;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Method untuk mengambil seluruh mahasiswa
     * @param keyword
     * @return mhsList
     */
    public ArrayList<Mhs> getMhsList(String keyword) {
        ArrayList<Mhs> mhsList = new ArrayList<>();
        con = Koneksi.getKoneksi();
        qry = "SELECT * FROM mhs" + keyword;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(qry);
            while (rs.next()) {                
                mhs = new Mhs(
                        rs.getInt("id_mhs"), 
                        rs.getString("npm"), 
                        rs.getString("nama"),
                        rs.getString("j_kelamin"),
                        rs.getString("prog_studi"),
                        rs.getString("tgl_lahir"),
                        rs.getString("tempat_lahir"),
                        rs.getString("password"),
                        rs.getBlob("foto"));
                mhsList.add(mhs);
            }
        } catch (SQLException | NullPointerException ex) {
            System.err.println("Error getMhsList(): " + ex.getMessage());
        }
        return mhsList;
    }
    
    /**
     * Method untuk menampilkan data mhs ke tabel mhs
     */
    public void selectMhs(String keyword) {
        ArrayList<Mhs> list = getMhsList(keyword);
        DefaultTableModel model = (DefaultTableModel) tMhs.getModel();
        Object[] row = new Object[9];
        int no = 1;
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = no++;
            row[1] = list.get(i).getIdMhs();
            row[2] = list.get(i).getNpm();
            row[3] = list.get(i).getNama();
            row[4] = list.get(i).getJk();
            row[5] = list.get(i).getProgStudi();
            row[6] = list.get(i).getTglLahir();
            row[7] = list.get(i).getTptLahir();
            row[8] = list.get(i).getFoto();
            model.addRow(row);
        }
    }
    
    /**
     * Method untuk mengatur ulang / refresh tabel mhs
     */
    public final void resetTable(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tMhs.getModel();
        model.setRowCount(0);
        selectMhs(keyword);
    }
    
    /**
     * Method untuk menentukan radio button yang dipilih
     * @param jenisKelamin 
     */
    public void rbJenisKelaminSetSelected(String jenisKelamin) {
        if (jenisKelamin.equals("Laki-laki")) {
            rbLaki.setSelected(true);
        }else {
            rbPerempuan.setSelected(true);
        }
    }
    
    /**
     * Method untuk mengambil data radio button yang dipilih
     * @return String jenisKelamin
     */
    public String rbJenisKelaminGetSelected() {
        if (rbLaki.isSelected()) {
            return "Laki-laki";
        }else if (rbPerempuan.isSelected()){
            return "Perempuan";
        }else {
            return "";
        }
    }
    
    /**
     * Method untuk membuat npm
     * @return npm
     */
    public String makeNpm() {
        String npm = null;
        String date = null; 
        String lastNpm = null;
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        date = df.format(now).substring(2);
        npm = date + "630001";

        try {
            con = Koneksi.getKoneksi();
            qry = "SELECT npm FROM mhs WHERE npm LIKE ? ORDER BY id_mhs DESC";
            ps = con.prepareStatement(qry);
            ps.setString(1, date + "63%");
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                lastNpm = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            System.err.println("Error makeNpm() : " + e.getMessage());
        }
        
        if (lastNpm != null) {
            int angka = Integer.parseInt(lastNpm.substring(4));
            angka++;
            npm = date + "63" + String.format("%04d", angka);
        }
        return npm;
    }
    
    /**
     * Method untuk membersihkan field
     */
    public void clearField() {
        eNama.setText("");
        eNpm.setText("");
        eTmptLahir.setText("");
        cbProgStudi.setSelectedIndex(0);
        dpTglLahir.setDate(new Date());
        rbLaki.setSelected(true);
        eNama.requestFocus();
        lFoto.setIcon(new javax.swing.ImageIcon("D:\\PBO5P\\KRS_JAVA_APP\\src\\asset\\img\\800px-No-Image-Placeholder.svg.png"));
        bNpm.setEnabled(true);
        bSimpan.setEnabled(true);
    }
    
    /**
     * Method format tanggal
     * @param tanggal
     * @return date
     */
    public Date getFormattedDate(String tanggal) {
        try {
            Date tanggalLahir = dateFormat.parse(tanggal);
            return tanggalLahir;
        } catch (ParseException e) {
            System.err.println("Error Tanggal : " + e);
            return new Date();
        }
    }
    
    /**
     * Method untuk mengambil foto dalam format blob
     * @param imageBlob
     * @return BufferedImage b
     */
    public BufferedImage getBufferedImage(Blob imageBlob) {
        InputStream binaryStream = null;
        BufferedImage b = null;
        try {
            binaryStream = imageBlob.getBinaryStream();
            b = ImageIO.read(binaryStream);
        } catch (SQLException | IOException e) {
            System.err.println("Error getBufferedImage : " + e);
        }
        return b;
    }
    
    /**
     * Method untuk mengambil foto untuk diubah menjadi format blob
     * @param bi
     * @return Blob blFile
     */
    public Blob getBlobImage(BufferedImage bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Blob blFile = null;
        try {
            ImageIO.write(bi, "png", baos);
            blFile = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
        } catch (SQLException | IOException e) {
            Logger.getLogger(MahasiswaFrame.class.getName()).log(Level.SEVERE, null, e);
        }
        return blFile;
    }
    
    /**
     * Method untuk mengubah ukuran foto agar sesuai dengan ukuran yang ditentukan
     * @param originalImage
     * @param type
     * @return BufferedImage resizedImage
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(lFoto.getWidth(), lFoto.getHeight(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, lFoto.getWidth(), lFoto.getHeight(), null);
        g.dispose();
        return resizedImage;
    }
    
    /**
     * Method untuk mengatur lebar kolom tabel mhs
     */
    public void lebarKolom(){ 
        TableColumn column;
        tMhs.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        column = tMhs.getColumnModel().getColumn(0); 
        column.setPreferredWidth(30);
        column = tMhs.getColumnModel().getColumn(1); 
        column.setPreferredWidth(54); 
        column = tMhs.getColumnModel().getColumn(2); 
        column.setPreferredWidth(110); 
        column = tMhs.getColumnModel().getColumn(3); 
        column.setPreferredWidth(350);
        column = tMhs.getColumnModel().getColumn(4); 
        column.setPreferredWidth(100); 
        column = tMhs.getColumnModel().getColumn(5); 
        column.setPreferredWidth(160); 
        column = tMhs.getColumnModel().getColumn(6); 
        column.setPreferredWidth(110);
        column = tMhs.getColumnModel().getColumn(7); 
        column.setPreferredWidth(160);
        column = tMhs.getColumnModel().getColumn(8); 
        column.setPreferredWidth(350);
    }
    
    /**
     * Method untuk mengambil foto dalam format blob dari database
     * @param url
     * @return Blob foto
     */
    public Blob getFoto(String url) {
        Blob foto = null;
        
        try {
            con = Koneksi.getKoneksi();
            Statement st = con.createStatement();
            
            // query mhs
            ResultSet rs = st.executeQuery("SELECT * FROM mhs WHERE foto = " + url);
            foto = rs.getBlob("foto");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return foto;
    }
    
    /**
     * Creates new form MahasiswaFrame
     */
    public MahasiswaFrame() {
        initComponents();
        FrameSetting.setFrame(this);
        lebarKolom();
        rbLaki.setSelected(true);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        fChooser = new javax.swing.JFileChooser();
        bKembali = new javax.swing.JLabel();
        bLogout = new javax.swing.JLabel();
        bExit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rbLaki = new javax.swing.JRadioButton();
        rbPerempuan = new javax.swing.JRadioButton();
        cbProgCetak1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbProgStudi = new javax.swing.JComboBox<>();
        dpTglLahir = new org.jdesktop.swingx.JXDatePicker();
        lFoto = new javax.swing.JLabel();
        bPilih = new javax.swing.JButton();
        eNama = new javax.swing.JTextField();
        eNpm = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        eTmptLahir = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cbCari = new javax.swing.JComboBox<>();
        eCari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tMhs = new javax.swing.JTable();
        bNpm = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        bCetak = new javax.swing.JButton();
        lbl_jam = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

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

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel2.setText("NPM");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, -1, -1));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel3.setText("Nama");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, -1, -1));

        jLabel6.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel6.setText("Tanggal Lahir");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 280, -1, -1));

        buttonGroup1.add(rbLaki);
        rbLaki.setFont(new java.awt.Font("Berlin Sans FB", 0, 16)); // NOI18N
        rbLaki.setText("Laki-laki");
        getContentPane().add(rbLaki, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 340, -1, -1));

        buttonGroup1.add(rbPerempuan);
        rbPerempuan.setFont(new java.awt.Font("Berlin Sans FB", 0, 16)); // NOI18N
        rbPerempuan.setText("Perempuan");
        getContentPane().add(rbPerempuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 340, -1, -1));

        cbProgCetak1.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbProgCetak1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S1 Teknik Informatika", "S1 Sistem Informasi" }));
        cbProgCetak1.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbProgCetak1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 650, 170, 30));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel7.setText("Prog. Studi");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 220, -1, -1));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel8.setText("Jenis Kelamin");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, -1, -1));

        cbProgStudi.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbProgStudi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S1 Teknik Informatika", "S1 Sistem Informasi" }));
        cbProgStudi.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbProgStudi, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 220, 300, 30));
        getContentPane().add(dpTglLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 280, 300, -1));

        lFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/img/800px-No-Image-Placeholder.svg.png"))); // NOI18N
        lFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lFoto.setPreferredSize(new java.awt.Dimension(183, 224));
        getContentPane().add(lFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 180, 190, 210));

        bPilih.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bPilih.setText("Pilih Foto");
        bPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPilihActionPerformed(evt);
            }
        });
        getContentPane().add(bPilih, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 400, 190, -1));

        eNama.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        getContentPane().add(eNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 280, 300, -1));

        eNpm.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eNpm.setEnabled(false);
        getContentPane().add(eNpm, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 190, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel5.setText("Tempat Lahir");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 340, -1, -1));

        eTmptLahir.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        getContentPane().add(eTmptLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 340, 300, -1));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
        jLabel15.setText("Cari Mahasiswa");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 650, -1, -1));

        cbCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        cbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NPM", "Nama" }));
        cbCari.setPreferredSize(new java.awt.Dimension(64, 18));
        getContentPane().add(cbCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 650, 220, 30));

        eCari.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        eCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eCariKeyPressed(evt);
            }
        });
        getContentPane().add(eCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 650, 300, -1));

        tMhs.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        tMhs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "ID", "NPM", "Nama", "Jenis Kelamin", "Program Studi", "Tanggal Lahir", "Tempat Lahir", "Foto"
            }
        ));
        tMhs.setGridColor(new java.awt.Color(0, 0, 0));
        tMhs.setMinimumSize(new java.awt.Dimension(100, 0));
        tMhs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tMhsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tMhs);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 700, 1430, 280));

        bNpm.setBackground(new java.awt.Color(198, 210, 211));
        bNpm.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bNpm.setForeground(new java.awt.Color(59, 59, 59));
        bNpm.setText("Buat NPM");
        bNpm.setToolTipText("");
        bNpm.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(171, 176, 174), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(198, 210, 211), 2)));
        bNpm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNpmActionPerformed(evt);
            }
        });
        getContentPane().add(bNpm, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, 110, 30));

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
        getContentPane().add(bBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 450, 180, 40));

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
        getContentPane().add(bHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 450, 180, 40));

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
        getContentPane().add(bUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 180, 40));

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
        getContentPane().add(bSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 450, 180, 40));

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
        getContentPane().add(bCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 650, 100, 30));

        lbl_jam.setFont(new java.awt.Font("Microsoft YaHei", 1, 20)); // NOI18N
        lbl_jam.setForeground(new java.awt.Color(255, 255, 255));
        lbl_jam.setText("Jam");
        getContentPane().add(lbl_jam, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1540, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DATA MAHASISWA");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 30, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1550, 110));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1560, 1040));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bExitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_bExitMouseClicked

    private void bPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPilihActionPerformed
        // TODO add your handling code here:
        FileFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fChooser.setFileFilter(filter);
        BufferedImage img = null;
        try {
            int result = fChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fChooser.getSelectedFile();
                img = ImageIO.read(file);
                int type = img.getType() == 0? BufferedImage.TYPE_INT_ARGB : img.getType();
                bImage = resizeImage(img, type);
                lFoto.setIcon(new ImageIcon(bImage));
            }
        } catch (IOException e) {
            System.err.println("Error bPilih : " + e);
        }
    }//GEN-LAST:event_bPilihActionPerformed

    private void eCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eCariKeyPressed
        // TODO add your handling code here:
        String pilihCari = cbCari.getSelectedItem().toString();
        String pilihan = "";
        switch(pilihCari){
            case "NPM" :
                pilihan = "npm";
                break;
            case "Nama" :
                pilihan = "nama";
                break;
        }
        resetTable(" WHERE " + pilihan + " like '%" + eCari.getText() + "%'");
    }//GEN-LAST:event_eCariKeyPressed

    private void tMhsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tMhsMouseClicked
        // TODO add your handling code here:
        eNpm.setText(tMhs.getValueAt(tMhs.getSelectedRow(), 2).toString());
        eNama.setText(tMhs.getValueAt(tMhs.getSelectedRow(), 3).toString());
        rbJenisKelaminSetSelected(tMhs.getValueAt(tMhs.getSelectedRow(), 4).toString());
        cbProgStudi.setSelectedIndex(0);
        dpTglLahir.setDate(getFormattedDate(tMhs.getValueAt(tMhs.getSelectedRow(), 6).toString()));
        eTmptLahir.setText(tMhs.getValueAt(tMhs.getSelectedRow(), 7).toString());

        Blob blob = (Blob) tMhs.getValueAt(tMhs.getSelectedRow(), 8);
        bImage = getBufferedImage(blob);
        lFoto.setIcon(new ImageIcon(bImage));
  
        eNama.requestFocus();
        bNpm.setEnabled(false);
        bSimpan.setEnabled(false);
    }//GEN-LAST:event_tMhsMouseClicked

    private void bNpmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNpmActionPerformed
        // TODO add your handling code here:
        eNpm.setText(makeNpm());
    }//GEN-LAST:event_bNpmActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        clearField();
    }//GEN-LAST:event_bBatalActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int pilihan = JOptionPane.showConfirmDialog(
                        null, 
                        "Yakin mau hapus", 
                        "Konfirmasi hapus", 
                        JOptionPane.YES_NO_OPTION);
        if (pilihan == 0) {
            if (!(eNpm.getText().equals(""))) {
                try {
                    con = Koneksi.getKoneksi();
                    qry = "DELETE FROM mhs WHERE npm = ?";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, eNpm.getText());
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

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        if (eNama.getText().equals("") ||
            eTmptLahir.getText().equals("")) 
        { 
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diubah");
        }else {
            qry = "UPDATE mhs SET nama=?, j_kelamin=?, prog_studi=?, "
                    + "tgl_lahir=?, tempat_lahir=?, foto=? WHERE npm=?";
            
            try {
                ps = con.prepareStatement(qry);
                ps.setString(1, eNama.getText());
                ps.setString(2, rbJenisKelaminGetSelected());
                ps.setString(3, cbProgStudi.getSelectedItem().toString());
                ps.setString(4, dateFormat.format(dpTglLahir.getDate()));
                ps.setString(5, eTmptLahir.getText());
                ps.setBlob(6, getBlobImage(bImage));
                ps.setString(7, eNpm.getText());
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

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if (eNpm.getText().equals("") ||
            eNama.getText().equals("") ||
            eTmptLahir.getText().equals("")) 
        {
            JOptionPane.showMessageDialog(null, "Lengkapi data");
        } else {
            mhs = new Mhs();
            mhs.setIdMhs(0);
            mhs.setNpm(eNpm.getText());
            mhs.setNama(eNama.getText());
            mhs.setTptLahir(eTmptLahir.getText());
            mhs.setProgStudi(cbProgStudi.getSelectedItem().toString());
            mhs.setTglLahir(dateFormat.format(dpTglLahir.getDate()));
            mhs.setJk(rbJenisKelaminGetSelected());
            
            try {
                mhs.setFoto(getBlobImage(bImage));
                mhs.setPass("mahasiswa");
                
                try {   
                    con = Koneksi.getKoneksi();
                    qry = "INSERT INTO mhs VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = con.prepareStatement(qry);
                    ps.setInt(1, mhs.getIdMhs());
                    ps.setString(2, mhs.getNpm());
                    ps.setString(3, mhs.getNama());
                    ps.setString(4, mhs.getJk());
                    ps.setString(5, mhs.getProgStudi());
                    ps.setString(6, mhs.getTglLahir());
                    ps.setString(7, mhs.getTptLahir());
                    ps.setString(8, mhs.getPass());
                    ps.setBlob(9, mhs.getFoto());
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Berhasil menyimpan data", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
                    resetTable("");
                    clearField();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage(), "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
                    System.err.println("gagal simpan data: " + e.getMessage());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Pilih foto!", "Pemberitahuan gagal simpan", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("error foto: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bKembaliMouseClicked
        // TODO add your handling code here:
        new MenuUtama().setVisible(true);
        dispose();
    }//GEN-LAST:event_bKembaliMouseClicked

    private void bLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bLogoutMouseClicked
        // TODO add your handling code here:
        new Login().setVisible(true);
        dispose();
    }//GEN-LAST:event_bLogoutMouseClicked

    private void bCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCetakActionPerformed
        // TODO add your handling code here:
        try{
            HashMap param = new HashMap();
            //Mengambil parameter
            param.put("prog_studi", cbProgCetak1.getSelectedItem().toString());

            JasperPrint JPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("../../report/mhsReport.jasper"), param, con);
            JasperViewer.viewReport(JPrint, false);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error print: " + ex.getMessage());
        }
    }//GEN-LAST:event_bCetakActionPerformed

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
            java.util.logging.Logger.getLogger(MahasiswaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MahasiswaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MahasiswaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MahasiswaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MahasiswaFrame().setVisible(true);
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
    private javax.swing.JButton bPilih;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbCari;
    private javax.swing.JComboBox<String> cbProgCetak1;
    private javax.swing.JComboBox<String> cbProgStudi;
    private org.jdesktop.swingx.JXDatePicker dpTglLahir;
    private javax.swing.JTextField eCari;
    private javax.swing.JTextField eNama;
    private javax.swing.JTextField eNpm;
    private javax.swing.JTextField eTmptLahir;
    private javax.swing.JFileChooser fChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lFoto;
    private javax.swing.JLabel lbl_jam;
    private javax.swing.JRadioButton rbLaki;
    private javax.swing.JRadioButton rbPerempuan;
    private javax.swing.JTable tMhs;
    // End of variables declaration//GEN-END:variables
}
