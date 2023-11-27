package com.HanifNurIlhamSanjayaJBusBR;// ManageBusActivity.java

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {

    private ListView busListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        busListView = findViewById(R.id.manageBusListView);

        // Ganti dengan id akun yang sedang aktif
        int accountId = 123; // Ganti dengan id akun yang sesuai

        // Panggil fungsi API untuk mendapatkan list bus milik akun
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Bus>> call = apiService.getMyBus(accountId);
        //mApiService.register(nameS, emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>()
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bus> myBusList = response.body();

                    // Gunakan myBusList untuk mengisi ListView menggunakan ArrayAdapter
                    BusArrayAdapter busArrayAdapter = new BusArrayAdapter(ManageBusActivity.this, myBusList);
                    busListView.setAdapter(busArrayAdapter);
                } else {
                    // Handle response errors
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                // Handle network failures
            }
        });
    }
}
