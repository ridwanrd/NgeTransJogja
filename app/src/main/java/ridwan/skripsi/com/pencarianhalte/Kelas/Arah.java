package ridwan.skripsi.com.pencarianhalte.Kelas;

/**
 * Created by gueone on 10/17/2017.
 */

public class Arah {
    int id_halte_tujuan, id_trayek, id_halte_asal, urutan_asal;
    String nama_halte_asal, nama_trayek, nama_halte_tujuan;

    public int getId_halte_tujuan() {
        return id_halte_tujuan;
    }

    public void setId_halte_tujuan(int id_halte_tujuan) {
        this.id_halte_tujuan = id_halte_tujuan;
    }

    public int getId_trayek() {
        return id_trayek;
    }

    public void setId_trayek(int id_trayek) {
        this.id_trayek = id_trayek;
    }

    public int getId_halte_asal() {
        return id_halte_asal;
    }

    public void setId_halte_asal(int id_halte_asal) {
        this.id_halte_asal = id_halte_asal;
    }

    public int getUrutan_asal() {
        return urutan_asal;
    }

    public void setUrutan_asal(int urutan_asal) {
        this.urutan_asal = urutan_asal;
    }

    public String getNama_halte_asal() {
        return nama_halte_asal;
    }

    public void setNama_halte_asal(String nama_halte_asal) {
        this.nama_halte_asal = nama_halte_asal;
    }

    public String getNama_trayek() {
        return nama_trayek;
    }

    public void setNama_trayek(String nama_trayek) {
        this.nama_trayek = nama_trayek;
    }

    public String getNama_halte_tujuan() {
        return nama_halte_tujuan;
    }

    public void setNama_halte_tujuan(String nama_halte_tujuan) {
        this.nama_halte_tujuan = nama_halte_tujuan;
    }
}
