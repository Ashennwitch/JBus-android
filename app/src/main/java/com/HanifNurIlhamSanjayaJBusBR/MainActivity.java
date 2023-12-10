package com.HanifNurIlhamSanjayaJBusBR;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 12; // You can experiment with this field
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus;
    private Button prevButton;
    private Button nextButton;
    private ListView busListView;
    private HorizontalScrollView pageScroll;
    private BaseApiService mApiService;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("JBus");
        busListView = findViewById(R.id.busListView);
        listView = findViewById(R.id.busListView);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        // Construct the footer
        paginationFooter();

        // Initial page load
        goToPage(currentPage);

        // Set listeners for the previous and next buttons
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0 ? currentPage - 1 : 0;
            goToPage(currentPage);
        });

        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages - 1 ? currentPage + 1 : currentPage;
            goToPage(currentPage);
        });

        // Fetch and display all buses
        fetchData();
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
            // Start intent to open AboutMeActivity
            Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void paginationFooter() {
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];

        for (int i = 0; i < noOfPages; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setTextColor(getResources().getColor(R.color.black));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
    }

    private void goToPage(int index) {
        for (int i = 0; i < noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private void viewPaginatedList(int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, busViews.size());
        List<BusView> paginatedList = busViews.subList(startIndex, endIndex);
        ArrayList<BusView> paginatedArrayList = new ArrayList<>(paginatedList);
        BusViewAdapter busViewAdapter = new BusViewAdapter(this, paginatedArrayList);
        listView.setAdapter(busViewAdapter);
    }


    // Declare busViews as a class-level variable
    private ArrayList<BusView> busViews;

    private void fetchData() {
        Call<List<Bus>> call = mApiService.getAllBus();
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful()) {
                    List<Bus> busList = response.body();

                    // Initialize ArrayList to store BusView objects
                    busViews = new ArrayList<>();

                    // Fill ArrayList with BusView objects
                    for (Bus bus : busList) {
                        busViews.add(new BusView(
                                R.drawable.baseline_directions_bus_24, // Replace with bus image ID
                                bus.getName(),
                                bus.getDepartureStation().stationName, // Use stationName field of Station
                                bus.getDestination().stationName // Use stationName field of Station
                        ));
                    }

                    // Update the number of pages and recreate pagination
                    listSize = busViews.size();
                    recreatePagination();

                    // Use BusViewAdapter to display data in ListView
                    BusViewAdapter adapter = new BusViewAdapter(MainActivity.this, busViews);
                    listView.setAdapter(adapter);
                } else {
                    // Handle error response
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                // Handle failure
                Log.e("MainActivity", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void recreatePagination() {
        // Recreate pagination based on the updated list size
        int val = listSize % pageSize;
        val = val == 0 ? 0 : 1;
        noOfPages = listSize / pageSize + val;

        // Remove existing buttons
        LinearLayout ll = findViewById(R.id.btn_layout);
        ll.removeAllViews();

        // Construct the new footer
        paginationFooter();

        // Load the first page after recreation
        goToPage(currentPage);
    }
}
