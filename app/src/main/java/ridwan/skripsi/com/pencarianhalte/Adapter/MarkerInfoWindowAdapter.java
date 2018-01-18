package ridwan.skripsi.com.pencarianhalte.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ridwan.skripsi.com.pencarianhalte.Kelas.Halte;
import ridwan.skripsi.com.pencarianhalte.R;

/**
 * Created by gueone on 10/18/2017.
 */

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Halte halte;

    public MarkerInfoWindowAdapter() {
    }

    public MarkerInfoWindowAdapter(Context context, LayoutInflater layoutInflater, Halte halte) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.halte = halte;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = layoutInflater.inflate(R.layout.layout_info_marker_halte, null);
        TextView tv_halte = ((TextView) infoWindow.findViewById(R.id.info_marker_halte));
        tv_halte.setText(halte.getNama_halte());
        return infoWindow;
    }
}
