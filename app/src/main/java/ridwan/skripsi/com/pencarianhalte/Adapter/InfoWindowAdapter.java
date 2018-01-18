package ridwan.skripsi.com.pencarianhalte.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.R;

public class InfoWindowAdapter extends ArrayAdapter<Halte> {
    int size = 0;

    public InfoWindowAdapter(Context context, int resource, List<Halte> haltes) {
        super(context, resource, haltes);
    }



    public InfoWindowAdapter(@NonNull Context context, int resource, int size) {
        super(context, resource);
        this.size = size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.layout_info_window_item, parent, false);
        }
        Halte halte = getItem(position);
        if (halte !=null){
            TextView title = (TextView) view.findViewById(R.id.info_window_halte);
            if(size!=0){
                title.setTextSize(size);
            }
            title.setText(halte.getNama_halte());
        }
        return view;
    }
}
