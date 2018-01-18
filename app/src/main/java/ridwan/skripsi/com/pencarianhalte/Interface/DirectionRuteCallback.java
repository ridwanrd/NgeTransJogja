package ridwan.skripsi.com.pencarianhalte.Interface;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gueone on 10/19/2017.
 */

public interface DirectionRuteCallback {
    public void setMarker(String jarak, List<List<HashMap<String, String>>> routes, int status);
}
