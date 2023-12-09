package com.HanifNurIlhamSanjayaJBusBR;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import com.HanifNurIlhamSanjayaJBusBR.request.BaseApiService;
import com.HanifNurIlhamSanjayaJBusBR.request.UtilsApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busListView = findViewById(R.id.busListView);
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
        getAllBuses();
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
            Intent intent = new Intent(this, AboutMeActivity.class);
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
                viewPaginatedList(listBus, currentPage);
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

    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusViewAdapter busArrayAdapter = new BusViewAdapter(this, convertBusesToBusViews(paginatedList));
        busListView.setAdapter(busArrayAdapter);
    }

    private void getAllBuses() {
        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful()) {
                    listBus = response.body();
                    listSize = listBus.size();
                    // Update the number of pages and recreate pagination
                    recreatePagination();
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                // Handle failure, if needed
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "Network failure. Please inform the user and possibly retry.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Conversion issue. Big problems.", Toast.LENGTH_SHORT).show();
                }
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

    private ArrayList<BusView> convertBusesToBusViews(List<Bus> buses) {
        // Convert the list of buses to a list of BusViews
        ArrayList<BusView> busViews = new ArrayList<BusView>();
        for (Bus bus : buses) {
            // Assuming BusView has a constructor that matches the fields of Bus
            BusView busView = new BusView(bus.getId(), bus.getName(), bus.getDepartureStation().toString(), bus.getDestination().toString());
            busViews.add(busView);
        }
        return busViews;
    }
}
