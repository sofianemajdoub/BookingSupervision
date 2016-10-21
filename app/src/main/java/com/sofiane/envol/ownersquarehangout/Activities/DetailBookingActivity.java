package com.sofiane.envol.ownersquarehangout.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.ownersquarehangout.Entities.Booking;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;
import com.sofiane.envol.ownersquarehangout.Utilies.MyApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailBookingActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener {
    TextView name, date, time, seats, info, alert;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.Validate_Booking));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        info = (TextView) findViewById(R.id.txt_place);
        name = (TextView) findViewById(R.id.txt_name);
        date = (TextView) findViewById(R.id.txt_date);
        time = (TextView) findViewById(R.id.txt_time);
        seats = (TextView) findViewById(R.id.txt_seat);
        alert = (TextView) findViewById(R.id.txt_alert);


        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String id_rest = pref.getString("login_shared", "def");
        final Firebase fire = new Firebase("https://hangoutenvol.firebaseio.com/Booking/");
        final Firebase validateRef = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_rest);
        final Firebase pointsRef = new Firebase("https://hangoutenvol.firebaseio.com/Points/");
        Intent intent = getIntent();
        final String pos = intent.getStringExtra("position");
        final String id = intent.getStringExtra("id");

        String[] parts = pos.split("~#");

        final String part1 = parts[0];
        String part2 = parts[1];
        Query queryRef = fire.orderByChild("idBooking").equalTo(part2);

        queryRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                    final Booking post = postSnapshot.getValue(Booking.class);
                    if((post.getInfo().equals("validate"))){
                        fab.setVisibility(View.GONE);
                    }


                    String[] day_parts = post.getDate_book().toString().split("/");
                    final String day = day_parts[0];
                    final String month = day_parts[1];
                    final String year = day_parts[2];

                    info.setText(post.getResto_book());
                    name.setText(post.getName_user());
                    date.setText(post.getDate_book());
                    time.setText(post.getTime_book());
                    seats.setText(post.getSeats());

                    if (!post.getIdPlace().equals(id_rest) && !post.getInfo().equals("")) {
                        //  alert.setText("ERROR !");
                    } else {
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DateFormat df = new SimpleDateFormat("d/M/yyyy");
                                String now = df.format(Calendar.getInstance().getTime());

                                if (post.getDate_book().equals(now)) {

                                    fire.child(postSnapshot.getKey()).child("deleted").setValue("yes");
                                    fire.child(postSnapshot.getKey()).child("info").setValue("validate");


                                    validateRef.child(year).child(month).child(day).child("validate")
                                            .runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                    if (mutableData.getValue() == null) {
                                                        mutableData.setValue(1);
                                                    } else {
                                                        long size = (Long) mutableData.getValue() + 1;
                                                        mutableData.setValue(size);
                                                    }
                                                    return Transaction.success(mutableData);
                                                }

                                                public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                                                    if (firebaseError != null) {
                                                        //              Toast.makeText(DetailBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //            Toast.makeText(DetailBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    pointsRef.child(post.getIdUser()).child(post.getIdPlace()).child("name").setValue(post.getResto_book());
                                    pointsRef.child(post.getIdUser()).child(post.getIdPlace()).child("point")
                                            .runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                    if (mutableData.getValue() == null) {
                                                        mutableData.setValue(10);
                                                    } else {
                                                        long size = (Long) mutableData.getValue() + 10;
                                                        mutableData.setValue(size);
                                                    }
                                                    return Transaction.success(mutableData);
                                                }

                                                public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                                                    if (firebaseError != null) {
                                                        //          Toast.makeText(DetailBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //         Toast.makeText(DetailBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                    Intent intent = new Intent(DetailBookingActivity.this, SearchByNameActivity.class);
                                    startActivity(intent);

                                } else {
                                    date.setTextColor(Color.RED);
                                    Toast.makeText(DetailBookingActivity.this, "This Booking is Not For TODAY !", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        checkConnection();
    }

    @Override
    protected void onStart () {
        super.onStart();

    }

    @Override
    protected void onStop () {
        super.onStop();
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
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
