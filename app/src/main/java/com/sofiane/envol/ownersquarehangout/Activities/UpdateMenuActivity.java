package com.sofiane.envol.ownersquarehangout.Activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.sofiane.envol.ownersquarehangout.Entities.Menus;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;

import java.util.ArrayList;

public class UpdateMenuActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextView menu_name, menu_description, menu_price;
    LinearLayout linear_name, linear_description, linear_price;
    EditText input_menu_name, input_menu_description, input_menu_price;

    Firebase ref;
    FloatingActionButton fab;
    ArrayList<Menus> menus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menu_name = (TextView) findViewById(R.id.menu_name);
        menu_description = (TextView) findViewById(R.id.menu_description);
        menu_price = (TextView) findViewById(R.id.menu_price);

        checkConnection();
        linear_name = (LinearLayout) findViewById(R.id.linear_name);
        linear_description = (LinearLayout) findViewById(R.id.linear_description);
        linear_price = (LinearLayout) findViewById(R.id.linear_price);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            menu_name.setText(bundle.getString("name_menu"));
            menu_description.setText(bundle.getString("description_menu"));
            menu_price.setText(bundle.getString("price_menu"));


        }

        linear_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMenuActivity.this);
                builder.setTitle(menu_name.getText());
                input_menu_name = new EditText(UpdateMenuActivity.this);
                input_menu_name.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input_menu_name);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        menu_name.setText(input_menu_name.getText());
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

        linear_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMenuActivity.this);
                builder.setTitle(menu_description.getText());
                input_menu_description = new EditText(UpdateMenuActivity.this);
                input_menu_description.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input_menu_description);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        menu_description.setText(input_menu_description.getText());
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

        linear_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMenuActivity.this);
                builder.setTitle(menu_price.getText());
                input_menu_price = new EditText(UpdateMenuActivity.this);
                input_menu_price.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input_menu_price);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        menu_price.setText(input_menu_price.getText());
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


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("user", 0);
                String id_rest = pref.getString("login_shared", "def");
                Bundle bundlez = getIntent().getExtras();
                String position = bundlez.getString("position");
                ref = new Firebase("https://hangoutenvol.firebaseio.com/Menu").child(id_rest).child(position);
                ref.setValue(null);
                finish();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, DetailMenuActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void update() {
        SharedPreferences pref = getSharedPreferences("user", 0);
        String id_rest = pref.getString("login_shared", "def");

        Bundle bundlez = getIntent().getExtras();
        String position = bundlez.getString("position");

        ref = new Firebase("https://hangoutenvol.firebaseio.com/Menu").child(id_rest).child(position);
        ref.child("menu_description").setValue(menu_description.getText().toString());
        ref.child("menu_name").setValue(menu_name.getText().toString());
        ref.child("menu_price").setValue(menu_price.getText().toString());

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

