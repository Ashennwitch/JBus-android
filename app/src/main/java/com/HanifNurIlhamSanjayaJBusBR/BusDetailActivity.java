package com.HanifNurIlhamSanjayaJBusBR;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BusDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        // Mendapatkan data bus yang dikirim dari MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String busName = extras.getString("busName");
            String departure = extras.getString("departure");
            String arrival = extras.getString("arrival");
            int capacity = extras.getInt("capacity");
            String facilities = extras.getString("facilities");
            String busType = extras.getString("busType");
            double price = extras.getDouble("price");

            // Menampilkan data bus di layout activity_bus_detail.xml
            displayBusDetails(busName, departure, arrival, capacity, facilities, busType, price);
        }
    }

    private void displayBusDetails(String busName, String departure, String arrival,
                                   int capacity, String facilities, String busType, double price) {
        // Menyambungkan ke elemen-elemen XML di activity_bus_detail.xml
        TextView busNameTextView = findViewById(R.id.busNameTextView);
        TextView departureTextView = findViewById(R.id.departureTextView);
        TextView arrivalTextView = findViewById(R.id.destinationTextView);
        TextView capacityTextView = findViewById(R.id.capacityTextView);
        TextView facilitiesTextView = findViewById(R.id.facilitiesTextView);
        TextView busTypeTextView = findViewById(R.id.busTypeTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);

        // Menetapkan nilai data bus ke elemen-elemen XML
        busNameTextView.setText(busName);
        departureTextView.setText("Departure: " + departure);
        arrivalTextView.setText("Arrival: " + arrival);
        capacityTextView.setText("Capacity: " + capacity);
        facilitiesTextView.setText("Facilities: " + facilities);
        busTypeTextView.setText("Bus Type: " + busType);
        priceTextView.setText("Price: $" + price);
    }
}

