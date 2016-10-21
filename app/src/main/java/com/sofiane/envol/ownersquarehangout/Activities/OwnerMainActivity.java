package com.sofiane.envol.ownersquarehangout.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sofiane.envol.ownersquarehangout.Entities.Stat;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class OwnerMainActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener {
    private FABToolbarLayout layout;
    private ImageView stat_btn, search_name_btn, search_qr__btn, close_btn, update_btn;
    private View fab;
    Firebase mRef;
    private String mUserId;
    PieChart pieChart;
    Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkConnection();
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        // Check Authentication
        mRef = new Firebase("https://hangoutenvol.firebaseio.com/");

        if (mRef.getAuth() == null) {//if not connected ==> login
            loadLoginView();
        }

        try {
            mUserId = mRef.getAuth().getUid();
        } catch (Exception e) {
            loadLoginView();
        }

        FloatingActionButton fab_exit = (FloatingActionButton) findViewById(R.id.fab_exit);
        fab_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref3 = getSharedPreferences("user", 0);
                String id_rest = pref3.getString("login_shared", "def");
                Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).child(Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1)).child(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Stat post = snapshot.getValue(Stat.class);
                        if (snapshot.exists()) {
                            showSpinner(Integer.parseInt(post.getSum_seats()));
                        } else {
                           // showSpinner(Integer.parseInt(post.getSeats()));
                                AlertDialog.Builder builder = new AlertDialog.Builder(OwnerMainActivity.this);
                                builder.setMessage(R.string.alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                //         Toast.makeText(OwnerMainActivity.this, "noDATA", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                builder.show();


                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });


            }


        });



        layout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        stat_btn = (ImageView) findViewById(R.id.stat);
        search_name_btn = (ImageView) findViewById(R.id.search_name);
        search_qr__btn = (ImageView) findViewById(R.id.search_qr);
        update_btn = (ImageView) findViewById(R.id.update);
        close_btn = (ImageView) findViewById(R.id.close);

        fab = findViewById(R.id.fabtoolbar_fab);

        search_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OwnerMainActivity.this, SearchByNameActivity.class);
                startActivity(intent);
            }
        });

        search_qr__btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OwnerMainActivity.this, SearchByQRActivity.class);
                startActivity(intent);
            }
        });
        stat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OwnerMainActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OwnerMainActivity.this, UpdateRestaurantActivity.class);
                startActivity(intent);
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.hide();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.show();
            }
        });

        pieChart = (PieChart) findViewById(R.id.chart);


        switchButton = (Switch) findViewById(R.id.switchButton);
        switchButton.setChecked(true);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    chartSeats();

                } else {
                    chartBookings();

                }
            }
        });

        if (switchButton.isChecked()) {
            chartSeats();

        } else {
            chartBookings();

        }

    }

    public void showSpinner(int max) {

        final Dialog d = new Dialog(OwnerMainActivity.this);
        // d.setTitle("Number of personnes");
        d.setContentView(R.layout.personne_dialog);
        Button b1 = (Button) d.findViewById(R.id.buttonSet);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(max);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
      //  np.setOnValueChangedListener(OwnerMainActivity.this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences pref3 = getSharedPreferences("user", 0);
                String id_rest = pref3.getString("login_shared", "def");
                Firebase upSeatref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).child(Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1)).child(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
                upSeatref.child("seats").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() ==Integer.valueOf(0)) {
                            mutableData.setValue(np.getValue());
                        } else {
                            long size = (Long) mutableData.getValue() + np.getValue();
                            mutableData.setValue(size);
                        }
                        return Transaction.success(mutableData);
                    }

                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        if (firebaseError != null) {
                            //          Toast.makeText(CreateBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            //        Toast.makeText(CreateBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Firebase downSeatref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).child(Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1)).child(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
                downSeatref.child("sum_seats").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == Integer.valueOf(0)) {
                            mutableData.setValue(0);
                        } else {
                            long size = (Long) mutableData.getValue() - np.getValue();
                            mutableData.setValue(size);
                        }
                        return Transaction.success(mutableData);
                    }

                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        if (firebaseError != null) {
                            //          Toast.makeText(CreateBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            //        Toast.makeText(CreateBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                d.dismiss();
            }
        });
        d.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_rest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mRef.unauth();

            getSharedPreferences("user", 0).edit().clear().commit();
            loadLoginView();

            return true;
        }
        if (id == R.id.action_alert) {
            Intent intent = new Intent(this, RepportActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_comment) {
            Intent intent = new Intent(this, CommentActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void chartBookings() {
        SharedPreferences pref1 = getSharedPreferences("user", 0);
        String id_rest = pref1.getString("login_shared", "def");
        final Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).child(Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1));


        ref.child(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                    Stat post = snapshot.getValue(Stat.class);
                    ArrayList<Entry> entries = new ArrayList<>();
                    if ((Float.parseFloat(post.getSum_booking()) >= Float.parseFloat(post.getValidate()))) {
                        entries.add(new Entry((Float.parseFloat(post.getSum_booking()) - Float.parseFloat(post.getValidate())), 0));
                        entries.add(new Entry(Float.parseFloat(post.getValidate()), 1));
                        pieChart.setDescription("");
                    } else {

                        entries.add(new Entry(Float.parseFloat(post.getValidate()), 1));
                        pieChart.setDescription("Full");
                    }
                    PieDataSet dataset = new PieDataSet(entries, "");
                    ArrayList<String> labels = new ArrayList<String>();
                    labels.add(getResources().getString (R.string.bookings));
                    labels.add(getResources().getString (R.string.bookings_validate));
                    PieData data = new PieData(labels, dataset);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

                    pieChart.setData(data);
                    pieChart.animateY(1000);
                    pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(OwnerMainActivity.this);
                    builder.setMessage(R.string.alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //         Toast.makeText(OwnerMainActivity.this, "noDATA", Toast.LENGTH_SHORT).show();
                                }
                            });

                    builder.show();


                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void chartSeats() {
        SharedPreferences pref2 = getSharedPreferences("user", 0);
        String id_rest = pref2.getString("login_shared", "def");
        String type = pref2.getString("type", "def");

        final Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).child(Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1));
        final Firebase ref_resto = new Firebase("https://hangoutenvol.firebaseio.com/").child(type).child(id_rest).child("nbr_booking");
        System.out.println("ref " + ref + " refresto " + ref_resto);


        ref.child(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");

                    //     Toast.makeText(OwnerMainActivity.this, "^^ " + snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    Stat post = snapshot.getValue(Stat.class);
                    ArrayList<Entry> entries = new ArrayList<>();

                    if ((Float.parseFloat(post.getSeats()) >= Float.parseFloat(post.getSum_seats()))) {
                        entries.add(new Entry(Float.parseFloat(post.getSum_seats()), 0));
                        entries.add(new Entry((Float.parseFloat(post.getSeats())), 1));
                        pieChart.setDescription("");

                    } else {

                        entries.add(new Entry(Float.parseFloat(post.getSum_seats()), 0));
                        pieChart.setDescription("Full");
                    }


                    PieDataSet dataset = new PieDataSet(entries, "");
                    ArrayList<String> labels = new ArrayList<String>();
                    labels.add(getResources().getString (R.string.Seats_taken));
                    labels.add(getResources().getString (R.string.Seat));
                    PieData data = new PieData(labels, dataset);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                    pieChart.setData(data);
                    pieChart.animateY(1000);
                    pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
                    ref_resto.setValue((Integer.parseInt(post.getSeats()) - Integer.parseInt(post.getSum_seats())) + "");
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(OwnerMainActivity.this);
                    builder.setMessage(R.string.alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //                   Toast.makeText(OwnerMainActivity.this, "noDATA", Toast.LENGTH_SHORT).show();
                                }
                            });

                    builder.show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
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
