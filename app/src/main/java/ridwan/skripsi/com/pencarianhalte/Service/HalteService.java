package ridwan.skripsi.com.pencarianhalte.Service;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ridwan.skripsi.com.pencarianhalte.Interface.ListHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.VolleyCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.Preferences.Konstanta;
import ridwan.skripsi.com.pencarianhalte.Utility.VolleyService;

/**
 * Created by root on 11/15/17.
 */

public class HalteService {
    Context context;
    Konstanta konstanta;

    ListHalteCallback listHalteCallback;
    ArrayList<Halte> haltes;

    public ArrayList<Halte> getHaltes() {
        return haltes;
    }

    public void setHaltes(JSONObject data) throws JSONException {
        haltes = new ArrayList<>();
        JSONArray array_haltes = data.getJSONArray("result");
        for(int x=0;x<array_haltes.length();x++){
            JSONObject object_halte = array_haltes.getJSONObject(x);
            Halte halte = new Halte();
            halte.setId_halte(object_halte.getInt("id_halte"));
            halte.setNama_halte(object_halte.getString("nama_halte"));
            haltes.add(halte);
        }
    }

    VolleyCallback callback;
    VolleyService volley;

    public HalteService(Context context, ListHalteCallback listHalteCallback) {
        this.context = context;
        this.listHalteCallback = listHalteCallback;
        setVolleyCallback(listHalteCallback);
        getDatas();
    }

    public void getDatas() {
        volley = new VolleyService(callback, context);
        volley.getDataVolley("GETCALL", konstanta.URL_LIST_REF);
    }

    public void setVolleyCallback(final ListHalteCallback listHalteCallback) {
        callback = new VolleyCallback() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    setHaltes(response);
                    listHalteCallback.setListHalte(getHaltes(), 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                listHalteCallback.setListHalte(null, 0);
            }
        };
    }
}
