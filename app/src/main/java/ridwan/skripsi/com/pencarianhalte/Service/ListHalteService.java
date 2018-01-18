package ridwan.skripsi.com.pencarianhalte.Service;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Interface.ListHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.VolleyCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;
import ridwan.skripsi.com.pencarianhalte.Preferences.Konstanta;
import ridwan.skripsi.com.pencarianhalte.Utility.VolleyService;

/**
 * Created by gueone on 10/15/2017.
 */

public class ListHalteService{
    Context context;
    Konstanta konstanta;

    ListHalteCallback listHalteCallback;

    HashMap<String, List<Jalur>> haltes;
    ArrayList<Trayek> trayeks;

    List<Jalur> temp_haltes;

    VolleyCallback callback;
    VolleyService volley;
    public ListHalteService(final Context context, final ListHalteCallback listHalteCallback) {
        this.context = context;
        this.listHalteCallback = listHalteCallback;
        setVolleyCallback(listHalteCallback);
        getDatas();
    }

    public void getDatas() {
        volley = new VolleyService(callback, context);
        volley.getDataVolley("GETCALL", konstanta.URL_LIST);
    }

    public void setVolleyCallback(final ListHalteCallback listHalteCallback) {
        callback = new VolleyCallback() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("asd", "notifySuccess: "+response.toString());
                try {
                    setTrayeks(response);
                    setHaltes(response);
                    listHalteCallback.setListHalte(getTrayek(), getHalte(), 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("aas", "notifyError: "+error.getMessage());
                listHalteCallback.setListHalte(null, null, 0);
            }
        };
    }

    public void setHaltes(JSONObject data) throws JSONException {
        haltes = new HashMap<>();
        for(int i=0;i<getTrayek().size();i++){
            Trayek trayek = getTrayek().get(i);
            temp_haltes = new ArrayList<>();

            JSONArray array_haltes = data.getJSONArray("halte");
            for(int x=0;x<array_haltes.length();x++){
                JSONObject object_halte = array_haltes.getJSONObject(x);
                if(trayek.getId_trayek() == Integer.valueOf(object_halte.getString("id_trayek"))){
                    Jalur jalur = new Jalur();
                    jalur.setId_halte(Integer.valueOf(object_halte.getString("id_halte")));
                    jalur.setNama_halte(object_halte.getString("nama_halte"));
                    jalur.setLat_halte(Double.valueOf(object_halte.getString("lat_halte")));
                    jalur.setLng_halte(Double.valueOf(object_halte.getString("lng_halte")));
                    jalur.setId_trayek(Integer.valueOf(object_halte.getString("id_trayek")));

                    Log.d("asds "+i, "setHaltes: "+object_halte.getString("nama_halte"));
                    temp_haltes.add(jalur);
                }
            }
            this.haltes.put(String.valueOf(trayek.getId_trayek()), temp_haltes);
            Log.d("aas", "setHaltes: "+haltes.get(String.valueOf(trayek.getId_trayek())).size());
        }
    }

    public void setTrayeks(JSONObject data) throws JSONException {
        trayeks = new ArrayList<>();
        JSONArray array_trayeks = data.getJSONArray("trayek");
        for(int i=0;i<array_trayeks.length();i++){
            JSONObject object_trayek = array_trayeks.getJSONObject(i);
            Trayek trayek = new Trayek();

            trayek.setId_trayek(Integer.valueOf(object_trayek.getString("id_trayek")));
            trayek.setNama_trayek(object_trayek.getString("nama_trayek"));
            trayeks.add(trayek);
        }
    }

    private HashMap<String, List<Jalur>> getHalte() {
        return haltes;
    }

    private ArrayList<Trayek> getTrayek() {
        return trayeks;
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