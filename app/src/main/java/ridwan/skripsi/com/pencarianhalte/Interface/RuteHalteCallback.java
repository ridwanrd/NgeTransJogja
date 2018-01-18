package ridwan.skripsi.com.pencarianhalte.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;

/**
 * Created by root on 11/15/17.
 */

public interface RuteHalteCallback {
    public void setPencarianHalte(ArrayList<Jalur> jalurs, HashMap<String, List<Trayek>> trayeks, int status);
}
