package ridwan.skripsi.com.pencarianhalte.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Kelas.Jalur;
import ridwan.skripsi.com.pencarianhalte.Kelas.Trayek;
import ridwan.skripsi.com.pencarianhalte.Pages.PetaHaltePage;
import ridwan.skripsi.com.pencarianhalte.R;

public class DaftarHalteAdapter extends SectioningAdapter{
    static final String TAG = DaftarHalteAdapter.class.getSimpleName();

    ArrayList<Trayek> parent;
    HashMap<String, List<Jalur>> child;
    Context context;

    TextView dialog_lat, dialog_lng, dialog_nama_halte;
    FloatingActionButton btn_floating;

    Dialog dialog_alert;

    public DaftarHalteAdapter() {
    }

    public DaftarHalteAdapter(Context context, ArrayList<Trayek> parent, HashMap<String, List<Jalur>> child) {
        this.parent = parent;
        this.child = child;
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

    private void initDialog(final Jalur jalur){
        dialog_alert.show();
        dialog_lat.setText(String.valueOf(jalur.getLat_halte()));
        dialog_lng.setText(String.valueOf(jalur.getLng_halte()));
        dialog_nama_halte.setText(jalur.getNama_halte());
        btn_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PetaHaltePage.class);
                intent.putExtra("lat_halte", jalur.getLat_halte());
                intent.putExtra("lng_halte", jalur.getLng_halte());
                intent.putExtra("nama_halte", jalur.getNama_halte());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getNumberOfSections() {
        return parent.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        String nv = String.valueOf(parent.get(sectionIndex).getId_trayek());
        Log.d("asd", "getNumberOfItemsInSection: "+child.get(nv).size());
        return child.get(nv).size();
    }
    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.daftar_halte_child, parent, false);
        Log.d(TAG, "onCreateItemViewHolder: ");
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.daftar_halte_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        String nv = String.valueOf(parent.get(sectionIndex).getId_trayek());
        Jalur jalur = child.get(nv).get(itemIndex);

        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        ivh.txt_item.setText(jalur.getNama_halte());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Trayek trayek = parent.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
//        viewHolder.itemView.setBackgroundColor(0x55FF9999);
        hvh.textView.setText(trayek.getNama_trayek());
    }
    @Override
    public void onBindGhostHeaderViewHolder(GhostHeaderViewHolder viewHolder, int sectionIndex) {
        /*if (USE_DEBUG_APPEARANCE) {
            viewHolder.itemView.setBackgroundColor(0xFF9999FF);
        }*/
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder implements View.OnClickListener {
        TextView txt_item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txt_item = (TextView) itemView.findViewById(R.id.list_child_txt);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int sectionIndex = DaftarHalteAdapter.this.getSectionForAdapterPosition(adapterPosition);
            int itemIndex = DaftarHalteAdapter.this.getPositionOfItemInSection(sectionIndex, adapterPosition);
            int nv = parent.get(sectionIndex).getId_trayek();
            Jalur jalur = child.get(String.valueOf(nv)).get(itemIndex);
            initDialog(jalur);
            /*int id_perizinan = Integer.valueOf(perizinan.getJenisizin_id());
            spDetailPerizinan.setSESSION(id_perizinan);
            Intent intent = new Intent(context, PerizinanDetail.class);
            intent.putExtra("nama_izin", perizinan.getJenisizin_name());
            context.startActivity(intent);*/
        }
    }
    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder implements View.OnClickListener {
        TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_header_txt);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
