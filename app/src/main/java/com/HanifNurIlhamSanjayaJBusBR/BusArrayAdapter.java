package com.HanifNurIlhamSanjayaJBusBR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import java.util.List;

public class BusArrayAdapter extends ArrayAdapter<Bus> {

    public BusArrayAdapter(Context context, List<Bus> busList) {
        super(context, 0, busList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bus bus = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
        }

        // Lookup view for data population
        TextView busName = convertView.findViewById(R.id.busNameTextView);

        // Populate the data into the template view using the data object
        if (bus != null) {
            busName.setText(bus.name);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
