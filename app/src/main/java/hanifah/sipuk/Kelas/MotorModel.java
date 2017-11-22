package hanifah.sipuk.Kelas;

/**
 * Created by Glory on 11/8/2017.
 */

public class MotorModel {

    String nama;
    String ket;
    String harga;
    String tahun;
    String silinder;
    String jenis;
    String dp;
    String gambar;


    public MotorModel(String nama, String ket, String harga, String tahun, String silinder, String jenis, String dp,String gambar) {
        this.nama = nama;
        this.ket = ket;
        this.harga = harga;
        this.tahun = tahun;
        this.silinder = silinder;
        this.jenis = jenis;
        this.dp = dp;
        this.gambar = gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }


    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getSilinder() {
        return silinder;
    }

    public void setSilinder(String silinder) {
        this.silinder = silinder;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
