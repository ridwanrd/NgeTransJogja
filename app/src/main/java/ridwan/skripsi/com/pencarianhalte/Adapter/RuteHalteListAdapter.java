package ridwan.skripsi.com.pencarianhalte.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;
import ridwan.skripsi.com.pencarianhalte.R;

public class RuteHalteListAdapter extends RecyclerView.Adapter<RuteHalteListAdapter.Wadah> {
    ArrayList<Jalur> jalurs;
    HashMap<String,List<Trayek>> trayeks;
    Context context;

    public RuteHalteListAdapter() {
    }

    public RuteHalteListAdapter(ArrayList<Jalur> jalurs, HashMap<String, List<Trayek>> trayeks, Context context) {
        this.jalurs = jalurs;
        this.trayeks = trayeks;
        this.context = context;
    }

    @Override
    public RuteHalteListAdapter.Wadah onCreateViewHolder(ViewGroup parent, int viewType) {
        View tampilan = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rute_halte_list_adapter, parent, false);
        return new RuteHalteListAdapter.Wadah(tampilan);
    }

    @Override
    public void onBindViewHolder(RuteHalteListAdapter.Wadah holder, int position) {
        Jalur jalur = jalurs.get(position);
        holder.nama_halte.setText(jalur.getNama_halte());
        String tes="";
        for(String key : trayeks.keySet()){
            Log.d("aaa", "onBindViewHolder: "+key);
            Log.d("aaa", "onBindViewHolder: "+jalur.getId_halte());
            if(key.equals(String.valueOf(jalur.getId_halte()))){
                for(int xyz=0;xyz<trayeks.get(key).size();xyz++){
                    tes = tes+trayeks.get(key).get(xyz).getNama_trayek();
                    if(xyz==trayeks.get(key).size()-1){
                        tes = tes+" ";
                    }else{
                        tes = tes+", ";
                    }
                }
            }
        }
        holder.nama_trayek.setText(tes);
    }

    @Override
    public int getItemCount() {
        return jalurs.size();
    }

    public class Wadah extends RecyclerView.ViewHolder {
        TextView nama_halte, nama_trayek;
        public Wadah(View itemView) {
            super(itemView);
            nama_halte = (TextView) itemView.findViewById(R.id.rute_halte_list_adapter_halte);
            nama_trayek = (TextView) itemView.findViewById(R.id.rute_halte_list_adapter_trayek);
        }
    }
}
