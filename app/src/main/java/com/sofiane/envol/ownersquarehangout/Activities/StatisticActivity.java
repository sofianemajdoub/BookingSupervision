package com.sofiane.envol.ownersquarehangout.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sofiane.envol.ownersquarehangout.Entities.Stat;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatisticActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    private BarChart BookingbarChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BookingbarChart = (BarChart) findViewById(R.id.Chart);

        checkConnection();


        SharedPreferences pref = getSharedPreferences("user", 0);
        String id_rest = pref.getString("login_shared", "def");
        final Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).child(Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1));
        Query queryRef = ref.orderByKey();

        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<String>();
        final Calendar cal = Calendar.getInstance();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Stat post = postSnapshot.getValue(Stat.class);
                    list.add(postSnapshot.getKey());
                    String key = postSnapshot.getKey();
                    float sum = Float.parseFloat(post.getSum_booking());

                    entries.add(new BarEntry(sum, i));
                    i++;
                    labels.add(key);
                    BarDataSet dataSet = new BarDataSet(entries,getResources().getString (R.string.bookings));
                    BarData data = new BarData(labels, dataSet);
                    BookingbarChart.setData(data);
                    BookingbarChart.setDescription("" + new SimpleDateFormat("MMM").format(cal.getTime()));

                    //      Toast.makeText(StatisticActivity.this, "xc " + list.size() + "..." + sum + "..." + key, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, OwnerMainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setText(R.string.alert_cnx)
                    .setActionTextColor(Color.WHITE)
                    .show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}

