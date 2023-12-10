package com.HanifNurIlhamSanjayaJBusBR;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Adapter yang menampilkan data ke listView.
 *
 * @author Hanif Nur Ilham Sanjaya
 */

public class BusViewAdapter extends ArrayAdapter<BusView> {

    public BusViewAdapter(@NonNull Context context, ArrayList<BusView> arrayList) {
        super(context, 0, arrayList);
    }
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_bus_list_view, parent, false);
        }

        BusView currentBusView = getItem(position);

        ImageView busImageView = currentItemView.findViewById(R.id.busImageView);
        assert currentBusView != null;
        busImageView.setImageResource(currentBusView.getBusImgId());

        TextView busNameTextView = currentItemView.findViewById(R.id.busNameTextView);
        busNameTextView.setText(currentBusView.getmBusName());

        TextView departureTextView = currentItemView.findViewById(R.id.departureTextView);
        departureTextView.setText("Departure: " + currentBusView.getmDepartureStation());

        TextView destinationTextView = currentItemView.findViewById(R.id.destinationTextView);
        destinationTextView.setText("Destination: " + currentBusView.getmDestination());

        return currentItemView;
    }
}
