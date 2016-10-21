package com.sofiane.envol.ownersquarehangout.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.ownersquarehangout.Entities.Restaurants;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;

public class UpdateRestaurantActivity extends AppCompatActivity   {
    private TextView phone_resto, open_resto, close_resto, nbr_book_resto, cuisine_resto;
    private LinearLayout phone, open, close, cuisine, nbr_seats;
    private static final String FIREBASE_URL = "https://hangoutenvol.firebaseio.com/";
    private Firebase ref_detail;
    ProgressDialog dialog;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences pref = getSharedPreferences("user", 0);
        String type = pref.getString("type", "def");


        phone_resto = (TextView) findViewById(R.id.phone_resto);
        open_resto = (TextView) findViewById(R.id.open_resto);
        close_resto = (TextView) findViewById(R.id.close_resto);
        nbr_book_resto = (TextView) findViewById(R.id.nbr_booking_resto);
        cuisine_resto = (TextView) findViewById(R.id.cuisine_resto);

        Button btn_update_menu = (Button) findViewById(R.id.btn_update_detail_menu);

        if (type.equals("NightClubs")) {
            btn_update_menu.setVisibility(View.GONE);
        }
        btn_update_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UpdateRestaurantActivity.this, DetailMenuActivity.class);
                startActivity(intent);
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        phone = (LinearLayout) findViewById(R.id.linear_phone);
        open = (LinearLayout) findViewById(R.id.linear_open);
        close = (LinearLayout) findViewById(R.id.linear_close);
        cuisine = (LinearLayout) findViewById(R.id.linear_cuisine);
        nbr_seats = (LinearLayout) findViewById(R.id.linear_nbr);

        if (type.equals("NightClubs")) {
            cuisine.setVisibility(View.GONE);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRestaurantActivity.this);
                builder.setTitle(phone_resto.getText());
                final EditText input_phone = new EditText(UpdateRestaurantActivity.this);
                input_phone.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input_phone);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phone_resto.setText(input_phone.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRestaurantActivity.this);
                builder.setTitle(open_resto.getText());
                final EditText input_open = new EditText(UpdateRestaurantActivity.this);
                input_open.setInputType(InputType.TYPE_CLASS_DATETIME);
                builder.setView(input_open);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        open_resto.setText(input_open.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRestaurantActivity.this);
                builder.setTitle(close_resto.getText());
                final EditText input_close = new EditText(UpdateRestaurantActivity.this);
                input_close.setInputType(InputType.TYPE_CLASS_DATETIME);
                builder.setView(input_close);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        close_resto.setText(input_close.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        cuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRestaurantActivity.this);
                builder.setTitle(cuisine_resto.getText());
                final EditText input_cuisine = new EditText(UpdateRestaurantActivity.this);
                input_cuisine.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builder.setView(input_cuisine);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cuisine_resto.setText(input_cuisine.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        nbr_seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRestaurantActivity.this);
                builder.setTitle(nbr_book_resto.getText());
                final EditText input_seats = new EditText(UpdateRestaurantActivity.this);
                input_seats.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input_seats);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nbr_book_resto.setText(input_seats.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
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

    private void update() {

        SharedPreferences pref = getSharedPreferences("user", 0);
        String id_rest = pref.getString("login_shared", "def");
        String type = pref.getString("type", "def");

        ref_detail = new Firebase(FIREBASE_URL).child(type).child(id_rest);
        ref_detail.child("phone").setValue(phone_resto.getText().toString());
        ref_detail.child("open_time").setValue(open_resto.getText().toString());
        ref_detail.child("close_time").setValue(close_resto.getText().toString());
        ref_detail.child("nbr_booking").setValue(nbr_book_resto.getText().toString());
        ref_detail.child("cuisine").setValue(cuisine_resto.getText().toString());

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("user", 0);
        String id_rest = pref.getString("login_shared", "def");
        String type = pref.getString("type", "def");
        ref_detail = new Firebase(FIREBASE_URL).child(type);
        //   Toast.makeText(UpdateRestaurantActivity.this, "" + ref_detail, Toast.LENGTH_SHORT).show();
        Query queryRest = ref_detail.orderByChild("id").equalTo(id_rest);

        queryRest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Restaurants post = postSnapshot.getValue(Restaurants.class);
                    System.out.println("%%" + post);
                    //       Toast.makeText(UpdateRestaurantActivity.this, "++ " + post.getPhone(), Toast.LENGTH_SHORT).show();
                    phone_resto.setText(post.getPhone());
                    open_resto.setText(post.getOpen_time());
                    close_resto.setText(post.getClose_time());
                    nbr_book_resto.setText(post.getNbr_booking());
                    cuisine_resto.setText(post.getCuisine());

                    //          Toast.makeText(UpdateRestaurantActivity.this, "" + post.getName(), Toast.LENGTH_SHORT).show();
                    setTitle(post.getName());
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
    }
   /*
    private void updateData(String cuisine, String phone, String open_time, String close_time, String nbr_booking) {
        Firebase mFirebaseRef = new Firebase(FIREBASE_URL).child("Restaurants").child("0000");

        Map<String, String> restaurant = new HashMap<String, String>();
        restaurant.put("address", post.getAddress());
        restaurant.put("name", post.getName());
        restaurant.put("close_time", close_time);
        restaurant.put("cuisine", cuisine);
        restaurant.put("nbr_booking", nbr_booking);
        restaurant.put("open_time", open_time);
        restaurant.put("phone", phone);
        restaurant.put("lat",post.getLat());
        restaurant.put("lng", post.getLng());

        Map<String, Map<String, String>> restaurants = new HashMap<String, Map<String, String>>();
        restaurants.put("0000", restaurant);

        mFirebaseRef.setValue(restaurants);


    }
*/



}


