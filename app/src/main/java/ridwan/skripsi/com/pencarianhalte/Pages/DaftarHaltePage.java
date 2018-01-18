package ridwan.skripsi.com.pencarianhalte.Pages;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.Adapter.DaftarHalteAdapter;
import ridwan.skripsi.com.pencarianhalte.Interface.ListHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;
import ridwan.skripsi.com.pencarianhalte.R;
import ridwan.skripsi.com.pencarianhalte.Service.ListHalteService;

public class DaftarHaltePage extends Fragment implements View.OnClickListener {
    @Bind(R.id.toolbar_default) protected Toolbar toolbar_default;

    @Bind(R.id.waiting_progress) protected RelativeLayout pb;
    @Bind(R.id.nofound_text) protected RelativeLayout nofound_text;
    @Bind(R.id.noconnection) protected RelativeLayout noconnection;

    @Bind(R.id.daftar_halte_rv) protected RecyclerView recyclerView;
    @Bind(R.id.daftar_halte_swiperefresh) protected SwipeRefreshLayout swiper;

    ActionBar actionBar;
    DaftarHalteAdapter daftarHalteAdapter;

    ListHalteService listHalteService;
    ListHalteCallback listHalteCallback;

    StickyHeaderLayoutManager stickyHeaderLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tampilan = inflater.inflate(R.layout.activity_daftar_halte_page, container, false);
        ButterKnife.bind(this, tampilan);
        setToolbar();

        stickyHeaderLayoutManager = new StickyHeaderLayoutManager(); // init new stickyHeader
        setRecyclerView();

        setCallback();
        initService();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listHalteService = new ListHalteService(getActivity(), listHalteCallback);
            }
        });

        return tampilan;
    }

    private void initService() {
//        pb.setVisibility(View.VISIBLE);
        swiper.setRefreshing(true);
        listHalteService = new ListHalteService(getActivity(), listHalteCallback);
    }

    private void setCallback() {
        listHalteCallback = new ListHalteCallback() {
            @Override
            public void setListHalte(ArrayList<Trayek> parent, HashMap<String, List<Jalur>> child, int status) {
                pb.setVisibility(View.GONE);
                swiper.setRefreshing(false);
                if(status==1){
                    noconnection.setVisibility(View.GONE);
                    nofound_text.setVisibility(View.GONE);
                    initRecyclerView(parent, child);
                }else if(status==0){
                    noconnection.setVisibility(View.VISIBLE);
                    nofound_text.setVisibility(View.GONE);
                }
            }

            @Override
            public void setListHalte(ArrayList<Halte> haltes, int status) {

            }
        };
    }

    private void initRecyclerView(ArrayList<Trayek> parent, HashMap<String, List<Jalur>> child) {
        daftarHalteAdapter = new DaftarHalteAdapter(getActivity(), parent, child);
        recyclerView.setAdapter(daftarHalteAdapter);
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                Log.i("asd", "onHeaderPositionChanged: section: " + sectionIndex + " -> old: " + oldPosition.name() + " new: " + newPosition.name());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });
    }

    private void setToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_default);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Daftar Halte");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if(listHalteService!= null && listHalteService.isVolley())
            listHalteService.stopProses();
    }
}
