
package model;

/**
 *
 * @author Randa
 */
public class Krs {
    
    private String idKrs;
    private int idMhs;
    private String kodeJadwal;
    private String tahunAjar;
    private Mhs mhs;
    private Jadwal jadwal;
    private Dosen dosen;
    private Makul makul;
    private Kelas kelas;

    public Krs() {
    }

    public Krs(String idKrs, int idMhs, String tahunAjar, String kodeJadwal) {
        this.idKrs = idKrs;
        this.idMhs = idMhs;
        this.tahunAjar = tahunAjar;
        this.kodeJadwal = kodeJadwal;
    }
    
    public Krs(String idKrs, int idMhs, String npm, String namaMhs, String progStudi, 
                String kodeJadwal, String kodeMakul, String makul, int sks, 
                String semester, String kodeKelas, String kelas, String nipDosen,
                String dosen, String tahunAjar) 
    {
        this.idKrs = idKrs;
        this.kodeJadwal = kodeJadwal;
        this.tahunAjar = tahunAjar;
        this.mhs = new Mhs(idMhs, npm, namaMhs, progStudi);
        this.makul = new Makul(kodeMakul, makul, sks, semester);
        this.dosen = new Dosen(nipDosen, dosen);
        this.kelas = new Kelas(kodeKelas, kelas);
    }

    public String getIdKrs() {
        return idKrs;
    }

    public void setIdKrs(String idKrs) {
        this.idKrs = idKrs;
    }

    public int getIdMhs() {
        return idMhs;
    }

    public void setIdMhs(int idMhs) {
        this.idMhs = idMhs;
    }

    public String getKodeJadwal() {
        return kodeJadwal;
    }

    public void setKodeJadwal(String kodeJadwal) {
        this.kodeJadwal = kodeJadwal;
    }

    public String getTahunAjar() {
        return tahunAjar;
    }

    public void setTahunAjar(String tahunAjar) {
        this.tahunAjar = tahunAjar;
    }

    public Mhs getMhs() {
        return mhs;
    }

    public void setMhs(Mhs mhs) {
        this.mhs = mhs;
    }

    public Jadwal getJadwal() {
        return jadwal;
    }

    public void setJadwal(Jadwal jadwal) {
        this.jadwal = jadwal;
    }

    public Dosen getDosen() {
        return dosen;
    }

    public void setDosen(Dosen dosen) {
        this.dosen = dosen;
    }

    public Makul getMakul() {
        return makul;
    }

    public void setMakul(Makul makul) {
        this.makul = makul;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }
    
}
