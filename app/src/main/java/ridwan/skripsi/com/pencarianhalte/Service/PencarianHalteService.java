package ridwan.skripsi.com.pencarianhalte.Service;

import android.content.Context;
import android.location.Location;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ridwan.skripsi.com.pencarianhalte.Interface.PencarianHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.VolleyCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Arah;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Preferences.Konstanta;
import ridwan.skripsi.com.pencarianhalte.Utility.VolleyService;

/**
 * Created by gueone on 10/15/2017.
 */

public class PencarianHalteService{
    Context context;
    Konstanta konstanta;

    PencarianHalteCallback pencarianHalteCallback;

    Arah arah;
    ArrayList<Jalur> jalurs;

    VolleyCallback callback;
    VolleyService volley;

    Location mylocation;
    Location directionLocation;

    public PencarianHalteService(Context context, PencarianHalteCallback pencarianHalteCallback, Location mylocation, Location directionLocation) {
        this.context = context;
        this.pencarianHalteCallback = pencarianHalteCallback;
        this.mylocation = mylocation;
        this.directionLocation = directionLocation;
        setVolleyCallback(pencarianHalteCallback);
        getDatas();
    }

    private void setVolleyCallback(final PencarianHalteCallback pencarianHalteCallback) {
        callback = new VolleyCallback() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    if(response.getBoolean("result")){
                        setArah(response);
                        setJalur(response);
                        pencarianHalteCallback.setPencarianHalte(getJalurs(), getArah(), 1);
                    }else{
                        pencarianHalteCallback.setPencarianHalte(null, null, 2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                pencarianHalteCallback.setPencarianHalte(null, null, 0);
            }
        };
    }

    private void getDatas() {
        volley = new VolleyService(callback, context);
        volley.getDataVolley("GETCALL", konstanta.URL_PENCARIAN+"lat_pengguna="+mylocation.getLatitude()+"&lng_pengguna="+mylocation.getLongitude()+"&lat_tujuan="+directionLocation.getLatitude()+"&lng_tujuan="+directionLocation.getLongitude());
    }

    private void setJalur(JSONObject response) throws JSONException {
        jalurs = new ArrayList<>();
        JSONArray array_jalur = response.getJSONArray("jalur");
        for(int i=0;i<array_jalur.length();i++){
            JSONObject object_jalur = array_jalur.getJSONObject(i);
            Jalur jalur = new Jalur();

            jalur.setNama_halte(object_jalur.getString("nama_halte"));
//            jalur.setUrutan_jalur(Integer.valueOf(object_jalur.getString("urutan_jalur")));
            jalur.setLat_halte(Double.valueOf(object_jalur.getString("lat_halte")));
            jalur.setLng_halte(Double.valueOf(object_jalur.getString("lng_halte")));
            jalurs.add(jalur);
        }
    }

    private void setArah(JSONObject response) throws JSONException {
        arah = new Arah();
        JSONArray array_arah = response.getJSONArray("arah");
        JSONObject object_arah = array_arah.getJSONObject(0);
        arah.setId_halte_asal(Integer.valueOf(object_arah.getString("id_halte_asal")));
        arah.setId_halte_tujuan(Integer.valueOf(object_arah.getString("id_halte_tujuan")));
//        arah.setId_trayek(Integer.valueOf(object_arah.getString("id_trayek")));
//        arah.setUrutan_asal(Integer.valueOf(object_arah.getString("urutan_asal")));
        arah.setNama_halte_asal(object_arah.getString("nama_halte_asal"));
        arah.setNama_halte_tujuan(object_arah.getString("nama_halte_tujuan"));
//        arah.setNama_trayek(object_arah.getString("nama_trayek"));
    }
    public Arah getArah() {
        return arah;
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

    public ArrayList<Jalur> getJalurs() {
        return jalurs;
    }
}