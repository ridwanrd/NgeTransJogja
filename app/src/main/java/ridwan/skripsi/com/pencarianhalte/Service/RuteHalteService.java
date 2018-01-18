package ridwan.skripsi.com.pencarianhalte.Service;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Interface.RuteHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.VolleyCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;
import ridwan.skripsi.com.pencarianhalte.Preferences.Konstanta;
import ridwan.skripsi.com.pencarianhalte.Utility.VolleyService;

/**
 * Created by root on 11/15/17.
 */

public class RuteHalteService {
    Context context;
    Konstanta konstanta;

    RuteHalteCallback ruteHalteCallback;
    ArrayList<Jalur> jalurs;
    HashMap<String, List<Trayek>> trayeks;

    public ArrayList<Jalur> getJalurs() {
        return jalurs;
    }

    public void setJalurs(JSONObject response) throws JSONException {
        jalurs = new ArrayList<>();
        JSONArray array_jalur = response.getJSONArray("jalur");
        for(int i=0;i<array_jalur.length();i++){
            JSONObject object_jalur = array_jalur.getJSONObject(i);
            Jalur jalur = new Jalur();

            jalur.setId_halte(object_jalur.getInt("id_halte"));
            jalur.setNama_halte(object_jalur.getString("nama_halte"));
//            jalur.setUrutan_jalur(Integer.valueOf(object_jalur.getString("urutan_jalur")));
            jalur.setLat_halte(Double.valueOf(object_jalur.getString("lat_halte")));
            jalur.setLng_halte(Double.valueOf(object_jalur.getString("lng_halte")));
            jalurs.add(jalur);
        }
    }

    public HashMap<String, List<Trayek>> getTrayeks() {
        return trayeks;
    }

    public void setTrayeks(JSONObject response) throws JSONException {
        trayeks = new HashMap<>();
        JSONArray array_jalur = response.getJSONArray("jalur");
        for(int i=0;i<array_jalur.length();i++){
            Boolean detect = false;
            JSONObject object_jalur = array_jalur.getJSONObject(i);
            JSONArray array_trayek = object_jalur.getJSONArray("trayek");
            for(int x=0; x<getJalurs().size(); x++){
                Jalur jalur = getJalurs().get(x);
                if(jalur.getId_halte() == object_jalur.getInt("id_halte") && !detect){
                    detect = true;
                    this.trayeks.put(String.valueOf(jalur.getId_halte()), trayeks(array_trayek));
                }
            }
        }
    }

    private ArrayList<Trayek> trayeks(JSONArray array_trayek) throws JSONException {
        ArrayList<Trayek> trayeks = new ArrayList<>();
        for(int i=0;i<array_trayek.length();i++){
            JSONObject object_trayek = array_trayek.getJSONObject(i);
            Trayek trayek = new Trayek();
            trayek.setNama_trayek(object_trayek.getString("nama_trayek"));
            trayeks.add(trayek);
        }
        return trayeks;
    }

    VolleyCallback callback;
    VolleyService volley;

    int id_halte_asal, id_halte_tujuan;

    public RuteHalteService(Context context, RuteHalteCallback ruteHalteCallback, int id_halte_asal, int id_halte_tujuan) {
        this.context = context;
        this.ruteHalteCallback = ruteHalteCallback;
        this.id_halte_asal = id_halte_asal;
        this.id_halte_tujuan = id_halte_tujuan;

        setVolleyCallback(ruteHalteCallback);
        getDatas();
    }

    private void setVolleyCallback(final RuteHalteCallback ruteHalteCallback) {
        callback = new VolleyCallback() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    if(response.getBoolean("result")){
                        setJalurs(response);
                        setTrayeks(response);
                        ruteHalteCallback.setPencarianHalte(getJalurs(), getTrayeks(), 1);
                    }else{
                        ruteHalteCallback.setPencarianHalte(null, null, 2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                ruteHalteCallback.setPencarianHalte(null, null, 0);
            }
        };
    }

    private void getDatas() {
        volley = new VolleyService(callback, context);
        volley.getDataVolley("GETCALL", konstanta.URL_RUTE_HALTE+"id_halte_asal="+id_halte_asal+"&id_halte_tujuan="+id_halte_tujuan);
    }
    public boolean isVolley(){
        if(volley==null){
            return false;
        }else{
            return true;
        }
    }
    public void stopProses(){
        volley.stopVolley();
    }
}
