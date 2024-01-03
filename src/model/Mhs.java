
package model;

import java.sql.Blob;

/**
 *
 * @author Randa
 */
public class Mhs {

    private int idMhs;
    private String npm;
    private String nama;
    private String jk;
    private String progStudi;
    private String tglLahir;
    private String tptLahir;
    private String pass;
    private Blob foto;

    public Mhs() {
    }
    
    public Mhs(int idMhs, String npm, String nama) {
        this.idMhs = idMhs;
        this.npm = npm;
        this.nama = nama;
    }

    public Mhs(int idMhs, String npm, String nama, String progStudi) {
        this.idMhs = idMhs;
        this.npm = npm;
        this.nama = nama;
        this.progStudi = progStudi;
    }

    public Mhs(int idMhs, String npm, String nama, String jk, String progStudi, String tglLahir, String tptLahir, String pass, Blob foto) {
        this.idMhs = idMhs;
        this.npm = npm;
        this.nama = nama;
        this.jk = jk;
        this.progStudi = progStudi;
        this.tglLahir = tglLahir;
        this.tptLahir = tptLahir;
        this.pass = pass;
        this.foto = foto;
    }

    public int getIdMhs() {
        return idMhs;
    }

    public void setIdMhs(int idMhs) {
        this.idMhs = idMhs;
    }
    
    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getProgStudi() {
        return progStudi;
    }

    public void setProgStudi(String progStudi) {
        this.progStudi = progStudi;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getTptLahir() {
        return tptLahir;
    }

    public void setTptLahir(String tptLahir) {
        this.tptLahir = tptLahir;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }
    
}
