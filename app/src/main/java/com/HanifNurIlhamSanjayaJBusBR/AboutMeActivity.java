package com.HanifNurIlhamSanjayaJBusBR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

/**
 * Activity yang menampilkan informasi akun pengguna.
 *
 * @author Hanif Nur Ilham Sanjaya
 */

public class AboutMeActivity extends AppCompatActivity {
    private TextView initialTextView, nameTextView, emailTextView, balanceTextView;
    private Button TopUp, manageBus;
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
        manageBus = findViewById(R.id.manageBus);
        // Set inisial nama pengguna (ganti dengan inisial Anda)
        //initialTextView.setText("H");


        TopUp.setOnClickListener(v -> performTopUp());
        handleRefreshAccount();
    }

    private void showRenterSection() {
        // Show or hide views relevant to a renter
        findViewById(R.id.renterSection).setVisibility(View.GONE);
        findViewById(R.id.nonRenterSection).setVisibility(View.VISIBLE);
    }

    private void showNonRenterSection() {
        // Show or hide views relevant to a non-renter
        findViewById(R.id.renterSection).setVisibility(View.VISIBLE);
        findViewById(R.id.nonRenterSection).setVisibility(View.GONE);
    }

    private void loadData(Account a) {
        topUpInput = findViewById(R.id.topUpAmountEditText);
        TopUp = findViewById(R.id.topUpButton);

        // Mendapatkan referensi ke TextView inisial
        initialTextView = findViewById(R.id.initial);

        // Mendapatkan referensi ke TextView untuk username, email, dan balance
        nameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        balanceTextView = findViewById(R.id.balance);

        initialTextView.setText(""+a.name.toUpperCase().charAt(0));
        nameTextView.setText(a.name);
        emailTextView.setText(a.email);
        balanceTextView.setText("IDR "+a.balance);
        if (a.company == null) {
            // Case: Account is a renter
            showRenterSection();

            // Listener for the button to navigate to ManageBusActivity
            manageBus.setOnClickListener(v -> {
                Intent intent = new Intent(AboutMeActivity.this, ManageBusActivity.class);
                startActivity(intent);
            });
        } else {
            // Case: Account is not a renter
            showNonRenterSection();

            // Listener for the button to navigate to RegisterRenterActivity
            TextView registerRenterButton = findViewById(R.id.notRegisteryet);
            registerRenterButton.setOnClickListener(v -> {
                Intent intent = new Intent(AboutMeActivity.this, RegisterRenterActivity.class);
                startActivity(intent);
            });
        }
    }

    private void performTopUp() {
        String topUpAmountStr = topUpInput.getText().toString();
        if (topUpAmountStr.isEmpty()) {
            Toast.makeText(mContext, "Please enter top-up amount", Toast.LENGTH_SHORT).show();
            return;
        }
        String amountS = topUpInput.getText().toString();
        Double amountD = amountS.isEmpty() ? 0d : Double.parseDouble(amountS);
        mApiService.topUp(LoginActivity.loggedAccount.id, amountD).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                BaseResponse<Double> res = response.body();
                if(res.success) {
                    balanceTextView.setText("IDR "+res.payload);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        handleRefreshAccount();
    }

    protected void handleRefreshAccount() {
        BaseApiService mApiService = UtilsApi.getApiService();
        mApiService.getAccountbyId(LoginActivity.loggedAccount.id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App error", Toast.LENGTH_SHORT).show();
                    return;
                }

                // if success, get the response body
                Account responseAccount = response.body();
                loadData(responseAccount);
            }

            // method for handling error talking to the server
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                // do something
            }
        });
    }

}