
package model;

/**
 *
 * @author Randa
 */
public class Kelas {
    
    String kodeKelas;
    String kelas;
    String semester;

    public Kelas() {
    }

    public Kelas(String kodeKelas, String kelas) {
        this.kodeKelas = kodeKelas;
        this.kelas = kelas;
    }

    public Kelas(String kodeKelas, String kelas, String semester) {
        this.kodeKelas = kodeKelas;
        this.kelas = kelas;
        this.semester = semester;
    }

    public String getKodeKelas() {
        return kodeKelas;
    }

    public void setKodeKelas(String kodeKelas) {
        this.kodeKelas = kodeKelas;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    
}
