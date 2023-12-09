package com.HanifNurIlhamSanjayaJBusBR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {

    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 12; // Sesuaikan dengan kebutuhan
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus;
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    private BusArrayAdapter busArrayAdapter;
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        getSupportActionBar().setTitle("Manage Bus");
        mApiService = UtilsApi.getApiService();

        busListView = findViewById(R.id.manageBusListView);
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);

        // Membuat objek dari BusArrayAdapter
        busArrayAdapter = new BusArrayAdapter(this, listBus);

        // Menetapkan adapter untuk ListView
        busListView.setAdapter(busArrayAdapter);

        // construct the footer
        paginationFooter();
        loadData(currentPage);

        // listener untuk button prev dan button
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0 ? currentPage - 1 : 0;
            loadData(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages - 1 ? currentPage + 1 : currentPage;
            loadData(currentPage);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.person_button) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void paginationFooter() {
        // Mendapatkan jumlah data dari API atau sumber data lainnya
        // Jumlah data ini kemudian digunakan untuk mengatur jumlah halaman (noOfPages)
        listSize = getListSize(); // Implementasi tergantung dari sumber datanya
        int val = listSize % pageSize;
        val = val == 0 ? 0 : 1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity =
                    Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
                    100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                loadData(currentPage);
            });
        }
    }

    private void loadData(int page) {
        // Implementasi untuk mendapatkan data dari API dengan pagination
        // Panggil API dengan parameter page untuk mengambil data halaman tertentu
        Call<List<Bus>> call = mApiService.getMyBus(page);

        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful()) {
                    listBus = response.body();

                    // Menampilkan data sesuai dengan halaman
                    viewPaginatedList(listBus, page);
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                // Handle failure, if needed
            }
        });
    }

    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        busArrayAdapter.clear();
        busArrayAdapter.addAll(paginatedList);
        busArrayAdapter.notifyDataSetChanged();
    }

    private int getListSize() {
        // Implementasi sesuai dengan sumber data (API, database, dll.)
        // yang memberikan informasi tentang jumlah total data
        return 0;
    }
}
