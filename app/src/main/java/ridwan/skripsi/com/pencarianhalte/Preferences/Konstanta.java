package ridwan.skripsi.com.pencarianhalte.Preferences;

import android.util.Log;

/**
 * Created by gueone on 10/15/2017.
 */

public class Konstanta {
//  public static final String BASE_URL = "http://haltetransjogja.tutorial-sourcecode.com/";
//    public static final String BASE_URL = "https://letsbusway.000webhostapp.com/";
    public static final String BASE_URL = "http://letsbusway.xyz/";
    public static final String URL_PENCARIAN = BASE_URL+"service/get-halte-result.php?";
    public static final String URL_RUTE_HALTE = BASE_URL+"service/list-halte-search.php?";
    public static final String URL_LIST = BASE_URL+"service/tampil-halte.php";
    public static final String URL_LIST_REF = BASE_URL+"service/list-halte.php";
    public static final String URL_DIRECTION(Double lat1, Double lat2, Double lng1, Double lng2){
        String str_origin = "origin="+lat1+","+lng1;
        String str_dest = "destination="+lat2+","+lng2;
        String sensor = "sensor=false";
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.d("url", "URL_DIRECTION: "+url);
        return url;
    }
}