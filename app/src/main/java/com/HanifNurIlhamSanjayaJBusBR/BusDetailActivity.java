package com.HanifNurIlhamSanjayaJBusBR;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity yang menampilkan detail informasi Bus.
 *
 * @author Hanif Nur Ilham Sanjaya
 */

public class BusDetailActivity extends AppCompatActivity {
    private TextView busNameTextView, departureTextView, destinationTextView, capacityTextView, facilitiesTextView, busTypeTextView, priceTextView;
    private BaseApiService mApiService;
    private Button bookNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        // Initialize views
        busNameTextView = findViewById(R.id.busNameTextView);
        departureTextView = findViewById(R.id.departureTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        capacityTextView = findViewById(R.id.capacityTextView);
        facilitiesTextView = findViewById(R.id.facilitiesTextView);
        busTypeTextView = findViewById(R.id.busTypeTextView);
        priceTextView = findViewById(R.id.priceTextView);
        bookNow = findViewById(R.id.bookNowButton);
        // Get bus ID from intent
        int busId = getIntent().getIntExtra("BUS_ID", 0);

        // Fetch and display bus details
        fetchBusDetails(busId);

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start PaymentActivity
                Intent intent = new Intent(BusDetailActivity.this, PaymentsActivityActivity.class);

                // Pass the ID of the selected bus and the account ID to PaymentActivity
                intent.putExtra("BUS_ID", bus.getId());
                intent.putExtra("ACCOUNT_ID", LoginActivity.loggedAccount.id);

                // Start PaymentActivity
                startActivity(intent);
            }
        });

    }

    private void fetchBusDetails(int busId) {
        mApiService = UtilsApi.getApiService();
        Call<Bus> call = mApiService.getBusbyId(busId);
        call.enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (response.isSuccessful()) {
                    Bus bus = response.body();

                    // Display bus details
                    busNameTextView.setText(bus.getName());
                    departureTextView.setText(bus.getDepartureStation().stationName);
                    destinationTextView.setText(bus.getDestination().stationName);
                    capacityTextView.setText(String.valueOf(bus.getCapacity()));
                    facilitiesTextView.setText(bus.getFacilities().toString());
                    busTypeTextView.setText(bus.getBusType().toString());
                    priceTextView.setText(String.valueOf(bus.getPrice()));
                } else {
                    // Handle error response
                    Toast.makeText(BusDetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                // Handle failure
                Log.e("BusDetailActivity", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(BusDetailActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
