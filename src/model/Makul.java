
package model;

/**
 *
 * @author Randa
 */
public class Makul {
    
    private String kodeMakul;
    private String makul;
    private int sks;
    private String semester;

    public Makul() {
    }

    public Makul(String kodeMakul) {
        this.kodeMakul = kodeMakul;
    }

    public Makul(String kodeMakul, String makul) {
        this.kodeMakul = kodeMakul;
        this.makul = makul;
    }
    
    public Makul(String kodeMakul, String makul, int sks, String semester) {
        this.kodeMakul = kodeMakul;
        this.makul = makul;
        this.sks = sks;
        this.semester = semester;
    }

    public String getKodeMakul() {
        return kodeMakul;
    }

    public void setKodeMakul(String kodeMakul) {
        this.kodeMakul = kodeMakul;
    }

    public String getMakul() {
        return makul;
    }

    public void setMakul(String makul) {
        this.makul = makul;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    
}
