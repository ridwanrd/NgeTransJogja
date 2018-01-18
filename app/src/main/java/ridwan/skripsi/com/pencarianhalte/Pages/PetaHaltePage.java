package ridwan.skripsi.com.pencarianhalte.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.Adapter.MarkerInfoWindowAdapter;
import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.R;

/**
 * Created by gueone on 10/18/2017.
 */

public class PetaHaltePage extends AppCompatActivity implements OnMapReadyCallback {


    Halte halte;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_peta);
        ButterKnife.bind(this);
        setLocation();
        setActionBar();

        setMap();
    }

    private void setMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRoute);
        mapFragment.getMapAsync(this);
    }

    private void setLocation() {
        this.halte = new Halte();
        this.halte.setLat_halte(getIntent().getDoubleExtra("lat_halte", 0));
        this.halte.setLng_halte(getIntent().getDoubleExtra("lng_halte", 0));
        this.halte.setNama_halte(getIntent().getStringExtra("nama_halte"));
    }

    private void setActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(halte.getNama_halte());
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
        LatLng lokasi_tujuan = new LatLng(halte.getLat_halte(), halte.getLng_halte());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi_tujuan));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus));
        markerOptions.position(lokasi_tujuan).title(halte.getNama_halte());
        mMap.addMarker(markerOptions);
        mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(this, getLayoutInflater(), halte));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

    }
}