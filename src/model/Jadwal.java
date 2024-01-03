
package model;

/**
 *
 * @author Randa
 */
public class Jadwal {
    
    private String kodeJadwal;
    private Makul makul;
    private Dosen dosen;
    private Kelas kelas;
    private String semester;
    private String jam;
    private String hari;

    public Jadwal() {
    }

    public Jadwal(String kodeJadwal, String kode_makul, String makul,
                  String nip_dosen, String dosen, String kode_kelas, 
                  String kelas, String semester, String jam, String hari) 
    {
        this.kodeJadwal = kodeJadwal;
        this.makul = new Makul(kode_makul, makul);
        this.dosen = new Dosen(nip_dosen, dosen);
        this.kelas = new Kelas(kode_kelas, kelas);
        this.semester = semester;
        this.jam = jam;
        this.hari = hari;
    }

    public String getKodeJadwal() {
        return kodeJadwal;
    }

    public void setKodeJadwal(String kodeJadwal) {
        this.kodeJadwal = kodeJadwal;
    }

    public Makul getMakul() {
        return makul;
    }

    public void setMakul(Makul makul) {
        this.makul = makul;
    }

    public Dosen getDosen() {
        return dosen;
    }

    public void setDosen(Dosen dosen) {
        this.dosen = dosen;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }
    
}
