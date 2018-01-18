package ridwan.skripsi.com.pencarianhalte.Kelas;

/**
 * Created by gueone on 10/15/2017.
 */

public class Trayek {
    int id_trayek;
    String nama_trayek;
    int id_halte;

    public int getId_halte() {
        return id_halte;
    }

    public void setId_halte(int id_halte) {
        this.id_halte = id_halte;
    }

    public int getId_trayek() {
        return id_trayek;
    }

    public void setId_trayek(int id_trayek) {
        this.id_trayek = id_trayek;
    }

    public String getNama_trayek() {
        return nama_trayek;
    }

    public void setNama_trayek(String nama_trayek) {
        this.nama_trayek = nama_trayek;
    }
}
