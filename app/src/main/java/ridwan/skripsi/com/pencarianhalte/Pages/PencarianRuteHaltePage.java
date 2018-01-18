package ridwan.skripsi.com.pencarianhalte.Pages;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.Adapter.InfoWindowAdapter;
import ridwan.skripsi.com.pencarianhalte.Adapter.RuteHalteListAdapter;
import ridwan.skripsi.com.pencarianhalte.Interface.ListHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Interface.RuteHalteCallback;
import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;
import ridwan.skripsi.com.pencarianhalte.R;
import ridwan.skripsi.com.pencarianhalte.Service.HalteService;
import ridwan.skripsi.com.pencarianhalte.Service.ListHalteService;
import ridwan.skripsi.com.pencarianhalte.Service.RuteHalteService;

public class PencarianRuteHaltePage extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {
    @Bind(R.id.toolbar_default)
    protected Toolbar toolbar_default;
    @Bind(R.id.waiting_progress)
    protected RelativeLayout pb;
    @Bind(R.id.nofound_text)
    protected RelativeLayout nofound_text;
    @Bind(R.id.noconnection)
    protected RelativeLayout noconnection;

    @Bind(R.id.pencarian_rute_halte_btn)
    protected Button pencarian_rute_halte_btn;
    @Bind(R.id.pencarian_rute_halte_rv)
    protected RecyclerView pencarian_rute_halte_rv;

    @Bind(R.id.pencarian_rute_halte_asal)
    protected RelativeLayout pencarian_rute_halte_asal;
    @Bind(R.id.pencarian_rute_halte_tujuan)
    protected RelativeLayout pencarian_rute_halte_tujuan;

    @Bind(R.id.pencarian_rute_halte_asal_txt)
    protected TextView pencarian_rute_halte_asal_txt;
    @Bind(R.id.pencarian_rute_halte_tujuan_txt)
    protected TextView pencarian_rute_halte_tujuan_txt;

    @Bind(R.id.pencarian_rute_halte_asal_id)
    protected TextView pencarian_rute_halte_asal_id;
    @Bind(R.id.pencarian_rute_halte_tujuan_id)
    protected TextView pencarian_rute_halte_tujuan_id;

    ActionBar actionBar;
    RuteHalteCallback ruteHalteCallback;
    ListHalteCallback listHalteCallback;

    RuteHalteService ruteHalteService;
    HalteService halteService;

    ProgressDialog progressDialog;
    Dialog dialog_pilihan;
    ListView lv_infowindow;
    InfoWindowAdapter infoWindowAdapter;

    ArrayList<Halte> haltes;

    RuteHalteListAdapter ruteHalteListAdapter;

    int index = 1;
    private ArrayList<Jalur> jalurs;
    private HashMap<String, List<Trayek>> trayeks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tampilan = inflater.inflate(R.layout.activity_pencarian_rute_halte_page, container, false);
        ButterKnife.bind(this, tampilan);

        setToolbar();
        setAllCallback();
        setAllComponent();

        return tampilan;
    }

    private void setAllComponent() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);
        haltes = new ArrayList<>();

        dialog_pilihan = new Dialog(getActivity());
        dialog_pilihan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pilihan.setContentView(R.layout.layout_info_window_list);
        lv_infowindow = (ListView) dialog_pilihan.findViewById(R.id.infowindow_listview);
        lv_infowindow.setOnItemClickListener(this);

        pencarian_rute_halte_btn.setOnClickListener(this);
        pencarian_rute_halte_asal.setOnClickListener(this);
        pencarian_rute_halte_tujuan.setOnClickListener(this);
    }

    private void setWindowManager(ArrayList<Halte> haltes){
        this.haltes = haltes;
        infoWindowAdapter = new InfoWindowAdapter(getActivity(), R.layout.layout_info_window_item, haltes);
        lv_infowindow.setAdapter(infoWindowAdapter);
    }

    private void setAllCallback() {
        ruteHalteCallback = new RuteHalteCallback() {
            @Override
            public void setPencarianHalte(ArrayList<Jalur> jalurs, HashMap<String, List<Trayek>> trayeks, int status) {
                progressDialog.hide();
                if (status == 1) {
                    setJalurs(jalurs);
                    setTrayeks(trayeks);
                    nofound_text.setVisibility(View.GONE);
                    noconnection.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    pencarian_rute_halte_rv.setVisibility(View.VISIBLE);
                    initRecycler();
                } else if (status == 2) {
                    nofound_text.setVisibility(View.VISIBLE);
                    noconnection.setVisibility(View.GONE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Rute tidak ditemukan", Snackbar.LENGTH_LONG).show();
                    pencarian_rute_halte_rv.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);

                } else {
                    pencarian_rute_halte_rv.setVisibility(View.GONE);
                    nofound_text.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);

                }
            }
        };
        listHalteCallback = new ListHalteCallback() {
            @Override
            public void setListHalte(ArrayList<Trayek> parent, HashMap<String, List<Jalur>> child, int status) {

            }

            @Override
            public void setListHalte(ArrayList<Halte> haltes, int status) {
                progressDialog.hide();
                dialog_pilihan.show();
                if(status==1){
                    setWindowManager(haltes);
                }
            }
        };
    }

    private void initRecycler() {
        pencarian_rute_halte_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        pencarian_rute_halte_rv.setLayoutManager(lm);

        ruteHalteListAdapter = new RuteHalteListAdapter(jalurs, trayeks, getActivity());
        pencarian_rute_halte_rv.setAdapter(ruteHalteListAdapter);
    }

    private void setToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_default);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Pencarian Halte");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pencarian_rute_halte_asal:
                progressDialog.show();
                index = 1;
                halteService = new HalteService(getActivity(), listHalteCallback);
            break;
            case R.id.pencarian_rute_halte_tujuan:
                progressDialog.show();
                index = 2;
                halteService = new HalteService(getActivity(), listHalteCallback);
                break;
            case R.id.pencarian_rute_halte_btn:
                Log.d("asd", "onClick: "+pencarian_rute_halte_asal_id.getText());
                Log.d("asd", "onClick: "+pencarian_rute_halte_tujuan_id.getText());
                progressDialog.show();
                ruteHalteService = new RuteHalteService(getActivity(), ruteHalteCallback, Integer.parseInt(pencarian_rute_halte_asal_id.getText().toString()), Integer.parseInt(pencarian_rute_halte_tujuan_id.getText().toString()));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog_pilihan.hide();
        Log.d("asd", "onItemClick: "+index);
        switch (index){
            case 1:
                setKomponenByClick(pencarian_rute_halte_asal_txt, pencarian_rute_halte_asal_id, haltes.get(position));
                break;
            case 2:
                setKomponenByClick(pencarian_rute_halte_tujuan_txt, pencarian_rute_halte_tujuan_id, haltes.get(position));
                break;
        }
    }

    private void setKomponenByClick(TextView pencarian_rute_halte_asal_txt, TextView pencarian_rute_halte_tujuan_id, Halte halte) {
        pencarian_rute_halte_asal_txt.setText(halte.getNama_halte());
        pencarian_rute_halte_tujuan_id.setText(String.valueOf(halte.getId_halte()));
        Log.d("tess", "setKomponenByClick: "+halte.getId_halte());
    }

    public void setJalurs(ArrayList<Jalur> jalurs) {
        this.jalurs = jalurs;
    }

    public void setTrayeks(HashMap<String,List<Trayek>> trayeks) {
        this.trayeks = trayeks;
    }
    @Override
    public void onStop() {
        super.onStop();
        if(ruteHalteService!= null && ruteHalteService.isVolley())
            ruteHalteService.stopProses();
    }
}
