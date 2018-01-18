package ridwan.skripsi.com.pencarianhalte.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.R;

public class PencarianListAdapter extends RecyclerView.Adapter<PencarianListAdapter.Wadah> {
    ArrayList<Jalur> jalurs;
    Context context;

    TextView dialog_lat, dialog_lng, dialog_nama_halte;
    FloatingActionButton btn_floating;

    Dialog dialog_alert;

    public PencarianListAdapter() {
    }

    public PencarianListAdapter(Context context, ArrayList<Jalur> jalurs) {
        this.jalurs = jalurs;
        this.context = context;

        setDialog();
    }
    private void setDialog() {
        dialog_alert = new Dialog(context);
        dialog_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_alert.setContentView(R.layout.layout_info_halte);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog_alert.getWindow();
        lp.copyFrom(window.getAttributes());

        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog_lat = (TextView) dialog_alert.findViewById(R.id.info_halte_lattitude);
        dialog_lng = (TextView) dialog_alert.findViewById(R.id.info_halte_longitude);
        dialog_nama_halte = (TextView) dialog_alert.findViewById(R.id.info_halte_name);
        btn_floating = (FloatingActionButton) dialog_alert.findViewById(R.id.info_halte_direction);
    }

    private void initDialog(Jalur jalur){
        dialog_alert.show();
        dialog_lat.setText(String.valueOf(jalur.getLat_halte()));
        dialog_lng.setText(String.valueOf(jalur.getLng_halte()));
        dialog_nama_halte.setText(jalur.getNama_halte());
    }

    @Override
    public PencarianListAdapter.Wadah onCreateViewHolder(ViewGroup parent, int viewType) {
        View tampilan = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pencarian_list_adapter, parent, false);
        return new Wadah(tampilan);
    }

    @Override
    public void onBindViewHolder(PencarianListAdapter.Wadah holder, int position) {
        final Jalur jalur = jalurs.get(position);
        holder.tv_halte.setText(jalur.getNama_halte());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog(jalur);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jalurs.size();
    }

    public class Wadah extends RecyclerView.ViewHolder {
        TextView tv_halte;
        public Wadah(View itemView) {
            super(itemView);
           tv_halte= (TextView) itemView.findViewById(R.id.pencarian_list_adapter_halte);
        }
    }
}
