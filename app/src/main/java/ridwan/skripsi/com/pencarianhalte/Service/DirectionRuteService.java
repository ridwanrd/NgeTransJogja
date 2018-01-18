package ridwan.skripsi.com.pencarianhalte.Service;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Interface.DirectionRuteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.VolleyCallback;
import ridwan.skripsi.com.pencarianhalte.Preferences.Konstanta;
import ridwan.skripsi.com.pencarianhalte.Utility.DirectionsJSONParser;
import ridwan.skripsi.com.pencarianhalte.Utility.VolleyService;

/**
 * Created by gueone on 10/19/2017.
 */

public class DirectionRuteService {
    Context context;
    Konstanta konstanta;

    DirectionRuteCallback directionRuteCallback;

    String jarak;
    List<List<HashMap<String, String>>> routes;

    VolleyCallback callback;
    VolleyService volley;

    public String getJarak() {
        return jarak;
    }

    public void setJarak(JSONObject data) throws JSONException {
        JSONArray array_item = data.getJSONArray("routes");
        JSONObject object_routes = array_item.getJSONObject(0);
        JSONArray legs = object_routes.getJSONArray("legs");
        JSONObject object_legs = legs.getJSONObject(0);
        JSONObject distance = object_legs.getJSONObject("distance");
        this.jarak = distance.getString("text");
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(JSONObject data) throws JSONException {
        DirectionsJSONParser parser = new DirectionsJSONParser();
        this.routes = parser.parse(data);
    }

    public DirectionRuteService(Context context, DirectionRuteCallback directionRuteCallback, Double lat1, Double lng1, Double lat2, Double lng2) {
        this.context = context;
        this.directionRuteCallback = directionRuteCallback;
        setVolleyCallback(directionRuteCallback);
        getDatas(lat1, lat2, lng1, lng2);
    }

    private void getDatas(Double lat1, Double lat2, Double lng1, Double lng2) {
        volley = new VolleyService(callback, context);
        volley.getDataVolley("GETCALL", konstanta.URL_DIRECTION(lat1, lat2, lng1, lng2));
    }

    private void setVolleyCallback(final DirectionRuteCallback directionRuteCallback) {
        callback = new VolleyCallback() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    Log.d("asd", "notifySuccess: sukses ambil direction");
                    setJarak(response);
                    setRoutes(response);
                    directionRuteCallback.setMarker(getJarak(), getRoutes(), 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void notifyError(String requestType, VolleyError error) {
                directionRuteCallback.setMarker(null, null, 0);
            }
        };
    }
}
