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
        //TopUp.setOnClickListener(v->performTopUp());

        // Mendapatkan referensi ke TextView inisial
        TextView initialTextView = findViewById(R.id.initial);

        // Mendapatkan referensi ke TextView untuk username, email, dan balance
        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);
        TextView balanceTextView = findViewById(R.id.balance);

        // Set inisial nama pengguna (ganti dengan inisial Anda)
        initialTextView.setText("H");

        displayAccountData(LoginActivity.loggedAccount);

        // Tambahkan listener untuk tombol top-up
        TopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementasikan proses top-up di sini
                performTopUp();
            }
        });
    }

    private void displayAccountData(Account account) {
        if (account != null) {
            nameTextView.setText(account.name);
            emailTextView.setText(account.email);
            // Sesuaikan dengan field balance pada kelas Account
            balanceTextView.setText(String.valueOf(account.balance));
        }
    }

    private void performTopUp() {
        String topUpAmount = topUpInput.getText().toString();
        //mContext = this;
        //mApiService = UtilsApi.getApiService();
        mApiService.topUp(Double.valueOf(LoginActivity.loggedAccount.balance), Double.valueOf(topUpAmount)).enqueue(new Callback<BaseResponse<Account>>() {

            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {

            }
        }};
        // Mendapatkan nilai top-up dari pengguna (contoh: 10000)


        // Memastikan loggedAccount tidak null
        if (LoginActivity.loggedAccount != null) {
            // Mendapatkan id akun yang sedang login
            int accountId = LoginActivity.loggedAccount.id;

            // Menggunakan Retrofit atau ApiService untuk membuat permintaan top-up
            // Sesuaikan dengan endpoint dan parameter yang diperlukan oleh backend Anda
            Call<BaseResponse<Account>> call = mApiService.getApiService().topUp(accountId, topUpAmount);

            // Melakukan enqueue untuk asinkronous request
            call.enqueue(new Callback<BaseResponse<Account>>() {
                @Override
                public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                    if (response.isSuccessful()) {
                        // Jika request berhasil, update data akun dan tampilkan pesan respons
                        Account updatedAccount = response.body().payload;
                        updateAccountData(updatedAccount);
                        showToast("Top-up successful");
                    } else {
                        // Jika request gagal, tampilkan pesan respons error
                        showToast("Top-up failed: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                    // Jika terjadi kesalahan dalam melakukan request, tampilkan pesan error
                    showToast("Error: " + t.getMessage());
                }
            });
        }
    }

    private void updateAccountData(Account updatedAccount) {
        // Update data akun setelah berhasil top-up
        LoginActivity.loggedAccount = updatedAccount;
        displayAccountData(updatedAccount);
    }

    private void showToast(String message) {
        // Menampilkan pesan respons dengan Toast
        Toast.makeText(AboutMeActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}