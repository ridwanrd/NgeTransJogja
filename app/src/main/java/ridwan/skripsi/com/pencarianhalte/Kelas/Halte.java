package ridwan.skripsi.com.pencarianhalte.Kelas;

/**
 * Created by gueone on 10/15/2017.
 */

public class Halte {
    int id_halte;
    String nama_halte;

    public int getId_halte() {
        return id_halte;
    }

    public void setId_halte(int id_halte) {
        this.id_halte = id_halte;
    }

    public String getNama_halte() {
        return nama_halte;
    }

    public void setNama_halte(String nama_halte) {
        this.nama_halte = nama_halte;
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

    double lat_halte, lng_halte;
}
