package com.HanifNurIlhamSanjayaJBusBR;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.HanifNurIlhamSanjayaJBusBR.model.Account;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerNow = null;
    private Button loginButton = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;
    public static Account loggedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mApiService = UtilsApi.getApiService();

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerNow = findViewById(R.id.donthaveanaccount_textview);
        loginButton = findViewById(R.id.login_button);

        registerNow.setOnClickListener(v -> {
            moveActivity(this, RegisterActivity.class);
        });

        loginButton.setOnClickListener(v -> {
            moveActivity(this, MainActivity.class);
        });

        loginButton.setOnClickListener(v->handleLogin());
    }

    private void moveActivity(Context ctx, Class <? > cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast. LENGTH_SHORT) . show ();
    }

    private void handleLogin() {
        // Handling empty field
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Melakukan request login
        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                // Handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Account> res = response.body();

                // Jika login berhasil
                if (res.success) {
                    // Simpan data akun ke variabel global
                    loggedAccount = res.payload;

                    // Intent ke MainActivity
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);

                    // Tampilkan pesan selamat datang
                    Toast.makeText(mContext, "Welcome, " + loggedAccount.name + "!", Toast.LENGTH_SHORT).show();
                } else {
                    // Jika login gagal, tampilkan pesan kesalahan
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
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