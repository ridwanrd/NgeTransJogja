package ridwan.skripsi.com.pencarianhalte.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;

/**
 * Created by gueone on 10/15/2017.
 */

public interface ListHalteCallback {
    public void setListHalte(ArrayList<Trayek> parent, HashMap<String, List<Jalur>> child, int status);
    public void setListHalte(ArrayList<Halte> haltes, int status);
}
