package ridwan.skripsi.com.pencarianhalte.Pages;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.Adapter.PencarianListAdapter;
import ridwan.skripsi.com.pencarianhalte.Interface.DirectionRuteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.PencarianHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Arah;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.R;
import ridwan.skripsi.com.pencarianhalte.Service.DirectionRuteService;
import ridwan.skripsi.com.pencarianhalte.Service.PencarianHalteService;
import ridwan.skripsi.com.pencarianhalte.Utility.DirectionSetter;

public class PencarianPage extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener {
    String TAG = "asd";
    @Bind(R.id.toolbar_default)
    protected Toolbar toolbar_default;

    @Bind(R.id.waiting_progress)
    protected RelativeLayout pb;
    @Bind(R.id.nofound_text)
    protected RelativeLayout nofound_text;
    @Bind(R.id.noconnection)
    protected RelativeLayout noconnection;
    @Bind(R.id.layout_no_reach)
    protected RelativeLayout layout_no_reach;
    @Bind(R.id.progress_page_info)
    protected LinearLayout progress_page_info;
    @Bind(R.id.pencarian_result_layout)
    protected LinearLayout result_layout;
//    @Bind(R.id.pencarian_list_result_layout) protected LinearLayout list_result_layout;

    //    @Bind(R.id.pencarian_list_result_lv) protected ListView list_result_lv;
    @Bind(R.id.pencarian_result_rv)
    protected RecyclerView result_rv;

    @Bind(R.id.pencarian_tujuan_info)
    protected RelativeLayout tujuan_info;
    @Bind(R.id.pencarian_tujuan_info_txt)
    protected TextView tujuan_info_txt;

    @Bind(R.id.pencarian_asal_info)
    protected RelativeLayout asal_info;
    @Bind(R.id.pencarian_asal_info_txt)
    protected TextView asal_info_txt;

    ActionBar actionBar;
    PlaceAutocompleteFragment autocompleteFragment;
    private GoogleApiClient mGoogleApiClient;
    PencarianListAdapter pencarianListAdapter;

    DirectionSetter directionSetter;

    private Location myLocation;
    private Location directionLocation;
    ArrayList<Jalur> jalurs;

    Intent intent;

    PencarianHalteService pencarianHalteService;
    PencarianHalteCallback pencarianHalteCallback;

    DirectionRuteService directionRuteService;
    DirectionRuteCallback directionAsalCallback;
    DirectionRuteCallback directionTujuanCallback;

    public Location getMyLocation() {
        return myLocation;
    }

    public void setJalurs(ArrayList<Jalur> jalurs) {
        this.jalurs = jalurs;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tampilan = inflater.inflate(R.layout.activity_pencarian_page, container, false);
        ButterKnife.bind(this, tampilan);
        directionSetter = new DirectionSetter();

        setToolbar();
        setAllCallback();

        if(Build.VERSION.SDK_INT > 17){
            autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        }else{
            autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        }
        autocompleteFragment.setOnPlaceSelectedListener(this);
        buildGoogleApiClient();

        asal_info.setOnClickListener(this);
        tujuan_info.setOnClickListener(this);

        return tampilan;
    }

    public void setMyLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        this.myLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        Log.d("asd", "setMyLocation: "+this.myLocation.getLatitude()+" "+this.myLocation.getLongitude());
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        1000).show();
            } else {
                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    public Location getDirectionLocation() {
        return this.directionLocation;
    }

    public void setDirectionLocation(double lat, double lng) {
        this.directionLocation = new Location("");
        this.directionLocation.setLatitude(lat);
        this.directionLocation.setLongitude(lng);
    }

    private void setToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_default);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Pencarian Lokasi");
    }

    private void setAllCallback() {
        pencarianHalteCallback = new PencarianHalteCallback() {
            @Override
            public void setPencarianHalte(ArrayList<Jalur> jalurs, Arah arah, int status) {
                if(status==1){
                    layout_no_reach.setVisibility(View.GONE);
                    noconnection.setVisibility(View.GONE);
                    setJalurs(jalurs);
                    result_layout.setVisibility(View.VISIBLE);
                    directionRuteService = new DirectionRuteService(getActivity(), directionAsalCallback, myLocation.getLatitude(), myLocation.getLongitude(), jalurs.get(0).getLat_halte(), jalurs.get(0).getLng_halte());
//                    initKomponen(arah);
                    initRecycler();
                }else if(status==2){
                    layout_no_reach.setVisibility(View.VISIBLE);
                    noconnection.setVisibility(View.GONE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak menjangkau lokasi halte terdekat", Snackbar.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    result_layout.setVisibility(View.GONE);
                }else{
                    layout_no_reach.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                    result_layout.setVisibility(View.GONE);
                }
            }
        };
        directionAsalCallback = new DirectionRuteCallback() {
            @Override
            public void setMarker(String jarak, List<List<HashMap<String, String>>> routes, int status) {
                asal_info_txt.setText(jarak+
                                        " ke "+directionSetter.bearingLocation(myLocation.getLatitude(), myLocation.getLongitude(), jalurs.get(0).getLat_halte(), jalurs.get(0).getLng_halte())+
                                        " dari lokasi Anda");
                directionRuteService = new DirectionRuteService(getActivity(), directionTujuanCallback, directionLocation.getLatitude(), directionLocation.getLongitude(), jalurs.get(jalurs.size()-1).getLat_halte(), jalurs.get(jalurs.size()-1).getLng_halte());
            }
        };
        directionTujuanCallback = new DirectionRuteCallback() {
            @Override
            public void setMarker(String jarak, List<List<HashMap<String, String>>> routes, int status) {
                pb.setVisibility(View.GONE);

                tujuan_info_txt.setText(jarak+
                                        " ke "+directionSetter.bearingLocation(jalurs.get(jalurs.size()-1).getLat_halte(), jalurs.get(jalurs.size()-1).getLng_halte(), directionLocation.getLatitude(), directionLocation.getLongitude())+
                                        " dari halte tujuan");
            }
        };

    }

    private void initRecycler() {
        result_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        result_rv.setLayoutManager(lm);

        pencarianListAdapter = new PencarianListAdapter(getActivity(), jalurs);
        result_rv.setAdapter(pencarianListAdapter);
    }

    private void initKomponen(Arah arah) {
        asal_info_txt.setText(new DecimalFormat("##.##").format(directionSetter.getDistance(myLocation.getLatitude(), myLocation.getLongitude(), jalurs.get(0).getLat_halte(), jalurs.get(0).getLng_halte()))+
                                " km ke "+directionSetter.bearingLocation(myLocation.getLatitude(), myLocation.getLongitude(), jalurs.get(0).getLat_halte(), jalurs.get(0).getLng_halte())+
                                " dari lokasi Anda");
        tujuan_info_txt.setText(new DecimalFormat("##.##").format(directionSetter.getDistance(jalurs.get(jalurs.size()-1).getLat_halte(), jalurs.get(jalurs.size()-1).getLng_halte(), directionLocation.getLatitude(), directionLocation.getLongitude()))+
                                    " km ke "+directionSetter.bearingLocation(jalurs.get(jalurs.size()-1).getLat_halte(), jalurs.get(jalurs.size()-1).getLng_halte(), directionLocation.getLatitude(), directionLocation.getLongitude())+
                                    " dari halte tujuan");
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PlaceAutocompleteFragment f = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }
/*public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.main_container));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }*/

//  onClick general
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.pencarian_asal_info :
                intent = new Intent(getActivity(), PetaRuteAsalPage.class);
                intent.putExtra("lat_pengguna", myLocation.getLatitude());
                intent.putExtra("lng_pengguna", myLocation.getLongitude());

                intent.putExtra("lat_asal", jalurs.get(0).getLat_halte());
                intent.putExtra("lng_asal", jalurs.get(0).getLng_halte());
                startActivity(intent);
                break;
            case R.id.pencarian_tujuan_info :
                intent = new Intent(getActivity(), PetaRuteTujuanPage.class);
                intent.putExtra("lat_halte", jalurs.get(jalurs.size()-1).getLat_halte());
                intent.putExtra("lng_halte", jalurs.get(jalurs.size()-1).getLng_halte());

                intent.putExtra("lat_tujuan", directionLocation.getLatitude());
                intent.putExtra("lng_tujuan", directionLocation.getLongitude());
                startActivity(intent);
                break;
        }
    }

//  direction location callback
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

//  places google automatic
    @Override
    public void onPlaceSelected(Place place) {
        setDirectionLocation(place.getLatLng().latitude, place.getLatLng().longitude);
        mGoogleApiClient.connect();
        setMyLocation();
        Log.i(TAG, "onPlaceSelected 1: "+getDirectionLocation().getLatitude()+" "+getDirectionLocation().getLongitude());
        Log.i(TAG, "onPlaceSelected 2: "+getMyLocation().getLatitude()+" "+getMyLocation().getLongitude());

        progress_page_info.setVisibility(View.GONE);
        noconnection.setVisibility(View.GONE);
        result_layout.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        pencarianHalteService = new PencarianHalteService(getActivity(), pencarianHalteCallback, getMyLocation(), getDirectionLocation());
    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: "+status.getStatus());
    }

    @Override
    public void onStop() {
        super.onStop();
        if(pencarianHalteService!= null && pencarianHalteService.isVolley())
            pencarianHalteService.stopProses();
    }
}