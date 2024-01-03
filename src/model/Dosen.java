
package model;

/**
 *
 * @author Randa
 */
public class Dosen {
    
    private String nip;
    private String nama;
    private String telepon;
    private String alamat;

    public Dosen() {
    }

    public Dosen(String nip, String nama) {
        this.nip = nip;
        this.nama = nama;
    }

    public Dosen(String nip, String nama, String telepon, String alamat) {
        this.nip = nip;
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
}
