package ridwan.skripsi.com.pencarianhalte.Interface;

import java.util.ArrayList;

import ridwan.skripsi.com.pencarianhalte.Kelas.Arah;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;

/**
 * Created by gueone on 10/15/2017.
 */

public interface PencarianHalteCallback {
    public void setPencarianHalte(ArrayList<Jalur> jalurs, Arah arah, int status);
}
