package com.HanifNurIlhamSanjayaJBusBR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.HanifNurIlhamSanjayaJBusBR.model.Account;
import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name, email, password;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        // Sesuaikan dengan ID yang kalian buat di layout
        name = findViewById(R.id.username_editText);
        email = findViewById(R.id.email_editText);
        password = findViewById(R.id.password_editText);
        registerButton = findViewById(R.id.login_button);

        registerButton.setOnClickListener(v->handleRegister());
    }

    protected void handleRegister() {
        // Handling empty field
        String nameS = name.getText().toString();
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if (nameS.isEmpty() || emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.register(nameS, emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                // Handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Account> res = response.body();

                // If success, finish this activity (back to login activity)
                if (res.success) finish();

                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
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
