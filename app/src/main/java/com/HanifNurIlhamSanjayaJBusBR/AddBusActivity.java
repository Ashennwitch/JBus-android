package com.HanifNurIlhamSanjayaJBusBR;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.HanifNurIlhamSanjayaJBusBR.model.BusType;

public class AddBusActivity extends AppCompatActivity {

    // Buat field berikut, menjadikan enum class BusType ke array.
    private BusType[] busType = BusType.values();

    // Buat field selectedBusType, field ini akan menyimpan bus type yang dipilih
    private BusType selectedBusType;

    // Buat juga field Spinner yang nanti akan dihubungkan ke id nya
    private Spinner busTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        // Inisialisasi spinner
        busTypeSpinner = findViewById(R.id.spinner1);

        // Buat adapter untuk spinner
        ArrayAdapter adBus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        // Set adapter untuk spinner
        busTypeSpinner.setAdapter(adBus);

        // menambahkan OISL (OnItemSelectedListener) untuk spinner
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);
    }

    // Definisikan sebuah field bernama busTypeOISL berupa instance dari static interface
    // AdapterView.OnItemSelectedListener.
    // Instance ini akan menjadi argumen untuk method setOnItemSelectedListener yang dimiliki oleh spinner.
    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            // mengisi field selectedBusType sesuai dengan item yang dipilih
            selectedBusType = busType[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
