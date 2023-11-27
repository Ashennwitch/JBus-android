package com.HanifNurIlhamSanjayaJBusBR;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.HanifNurIlhamSanjayaJBusBR.R;
import com.HanifNurIlhamSanjayaJBusBR.model.Account;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;
import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private EditText companyNameEditText, addressEditText, phoneEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        mApiService = UtilsApi.getApiService();

        companyNameEditText = findViewById(R.id.companyNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register button click
                registerRenter();
            }
        });
    }

    private void registerRenter() {
        String companyName = companyNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        // Validate input fields
        if (companyName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make API request to register as renter
        mApiService.registerRenter(LoginActivity.loggedAccount.id, companyName, address, phone).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Account> responseBody = response.body();
                    if (responseBody != null && responseBody.success) {
                        // Registration as renter successful, navigate to AboutMeActivity
                        Intent intent = new Intent(RegisterRenterActivity.this, AboutMeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterRenterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // API request failed
                    Toast.makeText(RegisterRenterActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                // Request failed
                Toast.makeText(RegisterRenterActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}