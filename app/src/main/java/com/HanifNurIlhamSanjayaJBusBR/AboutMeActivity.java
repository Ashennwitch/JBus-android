package com.HanifNurIlhamSanjayaJBusBR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.HanifNurIlhamSanjayaJBusBR.model.Account;

import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private TextView nameTextView, emailTextView, balanceTextView;
    private Button TopUp;
    private EditText topUpInput;
    private BaseApiService mApiService;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mApiService = UtilsApi.getApiService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        topUpInput = findViewById(R.id.topUpAmountEditText);
        TopUp = findViewById(R.id.topUpButton);

        // Mendapatkan referensi ke TextView inisial
        TextView initialTextView = findViewById(R.id.initial);

        // Mendapatkan referensi ke TextView untuk username, email, dan balance
        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);
        TextView balanceTextView = findViewById(R.id.balance);

        // Set inisial nama pengguna (ganti dengan inisial Anda)
        initialTextView.setText("H");

        displayAccountData(LoginActivity.loggedAccount);

        TopUp.setOnClickListener(v -> performTopUp());
    }
/*
        // Tambahkan listener untuk tombol top-up
        TopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementasikan proses top-up di sini
                performTopUp();
            }
        });*/


    private void displayAccountData(Account account) {
        if (account != null) {
            nameTextView.setText(account.name);
            emailTextView.setText(account.email);
            // Sesuaikan dengan field balance pada kelas Account
            balanceTextView.setText(String.valueOf(account.balance));
        }
    }

    private void performTopUp() {
        String topUpAmountStr = topUpInput.getText().toString();
        if (topUpAmountStr.isEmpty()) {
            Toast.makeText(mContext, "Please enter top-up amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double topUpAmount = Double.parseDouble(topUpAmountStr);

        mApiService.topUp(LoginActivity.loggedAccount.id, topUpAmount).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Double> responseBody = response.body();
                    if (responseBody.success) {
                        // Update balance di layout
                        balanceTextView.setText(String.valueOf(responseBody.payload));
                        Toast.makeText(mContext, "Top-up successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Top-up failed: " + responseBody.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Top-up failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(mContext, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }
}