package com.HanifNurIlhamSanjayaJBusBR;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        // Get bus ID and account ID from intent
        int busId = getIntent().getIntExtra("BUS_ID", 0);
        int accountId = getIntent().getIntExtra("ACCOUNT_ID", 0);

        // Perform payment
        performPayment(busId, accountId);
    }

    private void performPayment(int busId, int accountId) {
        mApiService = UtilsApi.getApiService();
        // You need to implement the bookBus method in your API service
        Call<BaseResponse> call = mApiService.bookBus(busId, accountId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Bus booked successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Failed to book bus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
