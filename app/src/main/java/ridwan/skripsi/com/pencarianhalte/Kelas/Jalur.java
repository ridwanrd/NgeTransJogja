package ridwan.skripsi.com.pencarianhalte.Kelas;

/**
 * Created by gueone on 10/15/2017.
 */

public class Jalur {
    int id_jalur;
    int id_halte;
    int id_trayek;

    public int getUrutan_jalur() {
        return urutan_jalur;
    }

    public void setUrutan_jalur(int urutan_jalur) {
        this.urutan_jalur = urutan_jalur;
    }

    public double getLat_halte() {
        return lat_halte;
    }

    public void setLat_halte(double lat_halte) {
        this.lat_halte = lat_halte;
    }

    public double getLng_halte() {
        return lng_halte;
    }

    public void setLng_halte(double lng_halte) {
        this.lng_halte = lng_halte;
    }

    int urutan_jalur;
    String nama_halte, nama_trayek;
    double lat_halte, lng_halte;

    public int getId_jalur() {
        return id_jalur;
    }

    public void setId_jalur(int id_jalur) {
        this.id_jalur = id_jalur;
    }

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

    public String getNama_halte() {
        return nama_halte;
    }

    public void setNama_halte(String nama_halte) {
        this.nama_halte = nama_halte;
    }

    public String getNama_trayek() {
        return nama_trayek;
    }

    public void setNama_trayek(String nama_trayek) {
        this.nama_trayek = nama_trayek;
    }
}
