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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.firebase.client.Firebase;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;
import com.sofiane.envol.ownersquarehangout.Utilies.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener , ConnectivityReceiver.ConnectivityReceiverListener{
    private EditText name_menu, description_menu, price_menu;
    private Spinner type_menu;
    private FloatingActionButton fab;
    private Firebase menu_ref;
    private String item_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name_menu = (EditText) findViewById(R.id.add_menu_name);
        description_menu = (EditText) findViewById(R.id.add_menu_description);
        price_menu = (EditText) findViewById(R.id.add_menu_price);
        type_menu = (Spinner) findViewById(R.id.add_menu_type);
        // Spinner click listener
        type_menu.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Entree");
        categories.add("Desserts");
        categories.add("Main");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        type_menu.setAdapter(dataAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_conf);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                String id_rest = pref.getString("login_shared", "def");

                menu_ref = new Firebase("https://hangoutenvol.firebaseio.com/Menu").child(id_rest);
                Map<String, String> user = new HashMap<String, String>();
                if (!description_menu.getText().toString().equals("") && !name_menu.getText().toString().equals("") && !price_menu.getText().toString().equals("")) {
                    user.put("id_restaurant", id_rest);
                    user.put("menu_description", description_menu.getText().toString());
                    // user.put("menu_id", menu_ref.push().getKey());
                    user.put("menu_name", name_menu.getText().toString());
                    user.put("menu_price", price_menu.getText().toString());
                    user.put("menu_type", item_type);
                    menu_ref.push().setValue(user);
                } else {

                }

                Intent intent = new Intent(AddMenuActivity.this, DetailMenuActivity.class);
                startActivity(intent);
                // test.setText(menu_ref.toString()+"..."+id_rest+"..."+description_menu.getText().toString()+"..."+name_menu.getText().toString()+"..."+price_menu.getText().toString()+"..."+item);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item_type = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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