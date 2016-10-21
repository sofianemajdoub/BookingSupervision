package com.sofiane.envol.ownersquarehangout.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;
import com.sofiane.envol.ownersquarehangout.Utilies.FireBaseClient;
import com.sofiane.envol.ownersquarehangout.Utilies.MyApplication;

public class DetailMenuActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener {
    final static String DB_URL = "https://hangoutenvol.firebaseio.com/Menu";
    private RecyclerView rv_entree, rv_main, rv_dessert;
    private FireBaseClient client_entree, client_main, client_dessert;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv_entree = (RecyclerView) findViewById(R.id.mRecylcer_entree);
        rv_entree.setLayoutManager(new LinearLayoutManager(DetailMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));

        rv_main = (RecyclerView) findViewById(R.id.mRecylcer_main);
        rv_main.setLayoutManager(new LinearLayoutManager(DetailMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));

        rv_dessert = (RecyclerView) findViewById(R.id.mRecylcer_dessert);
        rv_dessert.setLayoutManager(new LinearLayoutManager(DetailMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));

        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_rest = pref.getString("login_shared", "def");
        client_entree = new FireBaseClient(this, DB_URL + "/" + id_rest, rv_entree);
        client_main = new FireBaseClient(this, DB_URL + "/" + id_rest, rv_main);
        client_dessert = new FireBaseClient(this, DB_URL + "/" + id_rest, rv_dessert);


        client_entree.findByCuisine("Entree");
        client_main.findByCuisine("Main");
        client_dessert.findByCuisine("Desserts");

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMenuActivity.this, AddMenuActivity.class);
                startActivity(intent);

                finish();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkConnection();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, UpdateRestaurantActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        client_entree.clear_menu();
        client_main.clear_menu();
        client_dessert.clear_menu();
        MyApplication.getInstance().setConnectivityListener(this);

        rv_entree = (RecyclerView) findViewById(R.id.mRecylcer_entree);
        rv_entree.setLayoutManager(new LinearLayoutManager(DetailMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));

        rv_main = (RecyclerView) findViewById(R.id.mRecylcer_main);
        rv_main.setLayoutManager(new LinearLayoutManager(DetailMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));

        rv_dessert = (RecyclerView) findViewById(R.id.mRecylcer_dessert);
        rv_dessert.setLayoutManager(new LinearLayoutManager(DetailMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));

        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_rest = pref.getString("login_shared", "def");
        client_entree = new FireBaseClient(this, DB_URL + "/" + id_rest, rv_entree);
        client_main = new FireBaseClient(this, DB_URL + "/" + id_rest, rv_main);
        client_dessert = new FireBaseClient(this, DB_URL + "/" + id_rest, rv_dessert);


        client_entree.findByCuisine("Entree");
        client_main.findByCuisine("Main");
        client_dessert.findByCuisine("Desserts");


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

