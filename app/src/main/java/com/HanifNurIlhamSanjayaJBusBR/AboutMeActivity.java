package com.HanifNurIlhamSanjayaJBusBR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        // Mendapatkan referensi ke TextView inisial
        TextView initialTextView = findViewById(R.id.initial);

        // Mendapatkan referensi ke TextView untuk username, email, dan balance
        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);
        TextView balanceTextView = findViewById(R.id.balance);

        // Set inisial nama pengguna (ganti dengan inisial Anda)
        initialTextView.setText("H");

        // Set data pengguna (ganti dengan data Anda)
        usernameTextView.setText("Hanif Nur Ilham Sanjaya");
        emailTextView.setText("hanif@example.com");
        balanceTextView.setText("Rp 1.000.000");
    }
}