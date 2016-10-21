package com.sofiane.envol.ownersquarehangout.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;

import java.util.Calendar;

public class SearchByQRActivity extends AppCompatActivity implements OnQRCodeReadListener ,
        ConnectivityReceiver.ConnectivityReceiverListener {
    private TextView idtxt, nametxt, datetxt, timetxt, seatstxt, id_user_txt, validationTxt;
    private QRCodeReaderView mydecoderview;
    private ImageView line_image;
    private RelativeLayout qrLayout, validationLayout;
    private FloatingActionButton fab_qrcode, fab_validate;
    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qrLayout = (RelativeLayout) findViewById(R.id.qrSearch);
        validationLayout = (RelativeLayout) findViewById(R.id.validationLayout);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        checkConnection();
        idtxt = (TextView) findViewById(R.id.id);
        nametxt = (TextView) findViewById(R.id.name);
        datetxt = (TextView) findViewById(R.id.date);
        timetxt = (TextView) findViewById(R.id.time);
        seatstxt = (TextView) findViewById(R.id.seats);
        id_user_txt = (TextView) findViewById(R.id.id_user);

        validationTxt = (TextView) findViewById(R.id.validationTxt);

        line_image = (ImageView) findViewById(R.id.red_line_image);

        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        line_image.setAnimation(mAnimation);

        fab_qrcode = (FloatingActionButton) findViewById(R.id.fab_qr);
        fab_validate = (FloatingActionButton) findViewById(R.id.fab_validate);

        fab_validate.setVisibility(View.GONE);

        fab_qrcode.setImageResource(R.drawable.camera);
        fab_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (qrLayout.getVisibility() == View.GONE) {
                    qrLayout.setVisibility(View.VISIBLE);
                    fab_qrcode.setImageResource(R.drawable.camera_off);
                } else {
                    qrLayout.setVisibility(View.GONE);
                    fab_qrcode.setImageResource(R.drawable.camera);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
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

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(100);


        mydecoderview.getCameraManager().stopPreview();

        String string = text;
        String[] parts = string.split("~#");

        final String part1 = parts[0];
        String part2 = parts[1];
        final String part3 = parts[2];
        String part4 = parts[3];
        String part5 = parts[4];
        String part6 = parts[5];
        final String part7 = parts[6];

//   QRcode = id + "~#" + name + "~#" + date + "~#" + time + "~#" + seats + "~#" + id_usr + "~#" + id_rest;

        // idtxt.setText(part1);
        nametxt.setText(part2);
        datetxt.setText(part3);
        timetxt.setText(part4);
        seatstxt.setText(part5);


        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String id_rest = pref.getString("login_shared", "def");


        int Myear = c.get(Calendar.YEAR);
        int Mmonth = c.get(Calendar.MONTH) + 1;
        int Mday = c.get(Calendar.DAY_OF_MONTH);


        if (!part7.equals(id_rest) || (!part3.equals((Mday + "/" + Mmonth + "/" + Myear)))) {
            //       Toast.makeText(SearchByQRActivity.this, "" + Mday + "/" + Mmonth + "/" + Myear, Toast.LENGTH_SHORT).show();
            validationLayout.setBackgroundColor(getResources().getColor(R.color.red));
            validationTxt.setText("This booking is not for us or not for to day");
        } else {
            fab_validate.setVisibility(View.VISIBLE);
            fab_validate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Firebase validationRef = new Firebase("https://hangoutenvol.firebaseio.com/Booking/" + part1 + "/" + "info");
                    final Firebase deletedRef = new Firebase("https://hangoutenvol.firebaseio.com/Booking/" + part1 + "/" + "deleted");

                    validationRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");

                            if (snapshot.getValue().equals("validate")) {
                                // Toast.makeText(SearchByQRActivity.this, "EXIST", Toast.LENGTH_SHORT).show();
                                validationLayout.setBackgroundColor(getResources().getColor(R.color.orange));
                                validationTxt.setText(R.string.exist);
                            } else {
                                validationLayout.setBackgroundColor(getResources().getColor(R.color.green));
                                validationTxt.setText(R.string.validate);
                                validate(part7);
                                validationRef.setValue("validate");
                                deletedRef.setValue("yes");
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });


                }
            });

        }
    }

    private void validate(String part7) {
        String[] day_parts = datetxt.getText().toString().split("/");
        String day = day_parts[0];
        String month = day_parts[1];
        String year = day_parts[2];


        Firebase validateRef = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + part7 + "/" + year + "/" + month + "/" + day + "/validate");
        validateRef.runTransaction(new Transaction.Handler() {
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
                    //     Toast.makeText(SearchByQRActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //   Toast.makeText(SearchByQRActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Called when your device have no camera
    @Override
    public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
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

