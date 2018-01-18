package ridwan.skripsi.com.pencarianhalte.Pages;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.Interface.DirectionRuteCallback;
import ridwan.skripsi.com.pencarianhalte.R;
import ridwan.skripsi.com.pencarianhalte.Service.DirectionRuteService;

/**
 * Created by gueone on 10/18/2017.
 */

public class PetaRuteTujuanPage extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    private Location myLocation;
    private Location directionLocation;

    DirectionRuteService directionRuteService;
    DirectionRuteCallback directionRuteCallback;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_peta);
        ButterKnife.bind(this);
        setLocation();
        setActionBar();
        setCallback();

        setMap();
        getDataRute();
    }

    private void setCallback() {
        directionRuteCallback = new DirectionRuteCallback() {
            @Override
            public void setMarker(String jarak, List<List<HashMap<String, String>>> routes, int status) {
//                progressDialog.hide();
                if(status==1){
                    initRute(routes);
                }else{
                    Toast.makeText(PetaRuteTujuanPage.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
    }

    private void initRute(List<List<HashMap<String, String>>> routes) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
//                    MarkerOptions markerOptions = new MarkerOptions();

        for(int i=0;i<routes.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = routes.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(8);
            lineOptions.color(Color.GREEN);
        }
        mMap.addPolyline(lineOptions);
    }

    private void getDataRute() {
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/
        directionRuteService = new DirectionRuteService(this, directionRuteCallback, myLocation.getLatitude(), myLocation.getLongitude(), directionLocation.getLatitude(), directionLocation.getLongitude());
    }

    private void setLocation() {
        myLocation = new Location("");
        directionLocation = new Location("");
        myLocation.setLatitude(getIntent().getDoubleExtra("lat_halte", 0));
        myLocation.setLongitude(getIntent().getDoubleExtra("lng_halte", 0));
        directionLocation.setLatitude(getIntent().getDoubleExtra("lat_tujuan", 0));
        directionLocation.setLongitude(getIntent().getDoubleExtra("lng_tujuan", 0));
    }

    private void setMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRoute);
        mapFragment.getMapAsync(this);
    }

    private void setActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rute Lokasi Tujuan");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMarker(myLocation, 0);
        setMarker(directionLocation, 1);
    }

    private void setMarker(Location location, int a){
        LatLng lokasi_tujuan = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        if(a==0){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi_tujuan));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus));
            markerOptions.position(lokasi_tujuan).title("Halte Tujuan");
        }else{
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_tujuan));
            markerOptions.position(lokasi_tujuan).title("Tempat Tujuan");
        }
        mMap.addMarker(markerOptions);
    }


}
