package com.HanifNurIlhamSanjayaJBusBR;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.HanifNurIlhamSanjayaJBusBR.model.Account;
import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.model.BusType;
import com.HanifNurIlhamSanjayaJBusBR.model.Facility;
import com.HanifNurIlhamSanjayaJBusBR.model.Station;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {

    // Buat field berikut, menjadikan enum class BusType ke array.
    private BusType[] busType = BusType.values();
    private List<Station> stationList = new ArrayList<>();

    // Buat field selectedBusType, field ini akan menyimpan bus type yang dipilih
    private BusType selectedBusType;

    // Buat juga field Spinner yang nanti akan dihubungkan ke id nya
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;

    private int selectedDeptStationID;
    private int selectedArrStationID;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private BaseApiService mApiService;
    private Context mContext;
    private Button addButton;
    private EditText editTextBusName, editTextCapacity, editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mApiService = UtilsApi.getApiService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        editTextBusName = findViewById(R.id.editTextBusName);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextPrice = findViewById(R.id.editTextPrice);

        addButton = findViewById(R.id.buttonAdd);
        // Inisialisasi spinner
        busTypeSpinner = findViewById(R.id.spinner1);
        departureSpinner = findViewById(R.id.spinner2);
        arrivalSpinner = findViewById(R.id.spinner3);
        // Inisialisasi CheckBox sesuai dengan ID masing-masing
        acCheckBox = findViewById(R.id.checkBox1);
        wifiCheckBox = findViewById(R.id.checkBox2);
        toiletCheckBox = findViewById(R.id.checkBox3);
        lcdCheckBox = findViewById(R.id.checkBox4);
        coolboxCheckBox = findViewById(R.id.checkBox5);
        lunchCheckBox = findViewById(R.id.checkBox6);
        baggageCheckBox = findViewById(R.id.checkBox7);
        electricCheckBox = findViewById(R.id.checkBox8);

        // Buat adapter untuk spinner
        ArrayAdapter adBus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        // Set adapter untuk spinner
        busTypeSpinner.setAdapter(adBus);

        // menambahkan OISL (OnItemSelectedListener) untuk spinner
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);

        getAllStations();

        // Membuat listener untuk setiap CheckBox
        CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Clear the list before updating
                selectedFacilities.clear();

                // Tambahkan fasilitas yang dipilih ke dalam list
                if (acCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.AC);
                }
                if (wifiCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.WIFI);
                }
                if (toiletCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.TOILET);
                }
                if (lcdCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.LCD_TV);
                }
                if (coolboxCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.COOL_BOX);
                }
                if (lunchCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.LUNCH);
                }
                if (baggageCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.LARGE_BAGGAGE);
                }
                if (electricCheckBox.isChecked()) {
                    selectedFacilities.add(Facility.ELECTRIC_SOCKET);
                }
            }
        };

        // Pasang listener ke masing-masing CheckBox
        acCheckBox.setOnCheckedChangeListener(checkBoxListener);
        wifiCheckBox.setOnCheckedChangeListener(checkBoxListener);
        toiletCheckBox.setOnCheckedChangeListener(checkBoxListener);
        lcdCheckBox.setOnCheckedChangeListener(checkBoxListener);
        coolboxCheckBox.setOnCheckedChangeListener(checkBoxListener);
        lunchCheckBox.setOnCheckedChangeListener(checkBoxListener);
        baggageCheckBox.setOnCheckedChangeListener(checkBoxListener);
        electricCheckBox.setOnCheckedChangeListener(checkBoxListener);


        addButton.setOnClickListener(v->handleAddButton());

    }

    private void handleAddButton() {
        // Ambil data dari form (EditText, Spinner, CheckBox, dsb.)
        String busName = editTextBusName.getText().toString();
        int capacity = Integer.parseInt(editTextCapacity.getText().toString());
        int price = Integer.parseInt(editTextPrice.getText().toString());

        // Buat permintaan HTTP menggunakan Retrofit
        mApiService.create(
                LoginActivity.loggedAccount.id,
                busName,
                capacity,
                selectedFacilities,
                selectedBusType,
                price,
                selectedDeptStationID,
                selectedArrStationID
        ).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Account> responseBody = response.body();
                    if (responseBody != null && responseBody.success) {
                        // Berhasil menambahkan bus
                        // Kembali ke ManageBusActivity
                        Intent intent = new Intent(AddBusActivity.this, ManageBusActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Tutup activity saat ini jika tidak ingin kembali ke sini dari ManageBusActivity
                    } else {
                        // Gagal menambahkan bus
                        // Tampilkan pesan atau lakukan sesuatu sesuai kebutuhan
                        Toast.makeText(AddBusActivity.this, "Failed to add bus", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // API request failed
                    Toast.makeText(AddBusActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                // Tangani kegagalan permintaan HTTP
                // Tampilkan pesan atau lakukan sesuatu sesuai kebutuhan
                Toast.makeText(AddBusActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
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

    // Departure Spinner listener
    AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            // Mengisi field selectedDeptStationID sesuai dengan id yang dipilih
            selectedDeptStationID = stationList.get(position).id;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // Arrival Spinner listener
    AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            // Mengisi field selectedArrStationID sesuai dengan id yang dipilih
            selectedArrStationID = stationList.get(position).id;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
//    @GET("station/getAll")
//    Call<List<Station>> getAllStation();
    // Method untuk mendapatkan list station dari API
    private void getAllStations() {
        Call<List<Station>> call = mApiService.getAllStation();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful()) {
                    // Simpan response body ke listStation
                    stationList = response.body();

                    // Buat ArrayAdapter untuk Departure Spinner
                    ArrayAdapter<Station> deptAdapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_list_item_1, stationList);
                    deptAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    departureSpinner.setAdapter(deptAdapter);

                    // Set listener untuk Departure Spinner
                    departureSpinner.setOnItemSelectedListener(deptOISL);

                    // Buat ArrayAdapter untuk Arrival Spinner
                    ArrayAdapter<Station> arrAdapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_list_item_1, stationList);
                    arrAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    arrivalSpinner.setAdapter(arrAdapter);

                    // Set listener untuk Arrival Spinner
                    arrivalSpinner.setOnItemSelectedListener(arrOISL);
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                // Handle failure, if needed
                Toast.makeText(mContext, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

