package com.sofiane.envol.ownersquarehangout.Utilies;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.ownersquarehangout.Adapters.ListMenuAdapter;
import com.sofiane.envol.ownersquarehangout.Entities.BookingForOwner;
import com.sofiane.envol.ownersquarehangout.Entities.Menus;


import java.util.ArrayList;

public class FireBaseClient {

    Context c;
    String DB_URL;
    RecyclerView rv;
    Firebase fire;
    ProgressDialog pd;

    ArrayList<Menus> menus = new ArrayList<>();
    ArrayList<BookingForOwner> bookings = new ArrayList<>();

    ListMenuAdapter adapter;

    public FireBaseClient(Context c, String DB_URL, RecyclerView rv) {
        this.c = c;
        this.DB_URL = DB_URL;
        this.rv = rv;

        //INITIALIZE

        pd = new ProgressDialog(c);
        pd.setMessage("Loading..");
        pd.setIndeterminate(false);
        pd.setCancelable(true);
        pd.show();
        //INSTANTIATE
        fire = new Firebase(DB_URL);
        fire.keepSynced(true);
    }

    //RETRIEVE
    public void findByCuisine(String val) {
        Query queryRef = fire.orderByChild("menu_type").equalTo(val);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Menus m = new Menus();
                    m.setMenu_description(postSnapshot.getValue(Menus.class).getMenu_description());
                    m.setMenu_price(postSnapshot.getValue(Menus.class).getMenu_price());
                    m.setMenu_name(postSnapshot.getValue(Menus.class).getMenu_name());
                    m.setMenu_id(postSnapshot.getKey());
                    menus.add(m);

                    if (menus.size() > 0) {
                        adapter = new ListMenuAdapter(c, menus);
                        rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    public void clear_menu() {
        menus.clear();
    }

    public void clear_restaurant() {
        bookings.clear();
    }
}
