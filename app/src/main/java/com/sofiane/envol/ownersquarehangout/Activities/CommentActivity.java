package com.sofiane.envol.ownersquarehangout.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.sofiane.envol.ownersquarehangout.Adapters.CommentListAdapter;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;
import com.sofiane.envol.ownersquarehangout.Utilies.MyApplication;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ListView listViewComment;
    private Firebase fire_comment;
    private Query queryRef_comment;
    private CommentListAdapter mListAdapterComment;
    ImageView send;
    EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewComment = (ListView) findViewById(R.id.list_all_comment);
        send = (ImageView) findViewById(R.id.button_send);
        msg = (EditText) findViewById(R.id.msg);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int Myear = c.get(Calendar.YEAR);
                final int Mmonth = c.get(Calendar.MONTH) + 1;
                final int Mday = c.get(Calendar.DAY_OF_MONTH);
                SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                String id_rest = pref.getString("login_shared", "def");
                String name_place = pref.getString("name_place", "def");
                String id_user = pref.getString("id_user", "def");

                Firebase commentRef = new Firebase("https://hangoutenvol.firebaseio.com/Comment");

                Map<String, String> comment = new HashMap<String, String>();
                comment.put("name_user", "Owner " + name_place);
                comment.put("rating", "");
                comment.put("date", Mday + "/" + Mmonth + "/" + Myear);
                comment.put("description", msg.getText().toString());

                comment.put("id_user", id_user);
                comment.put("name_place", name_place);
                comment.put("img_user", "https://image.freepik.com/free-icon/male-user-shadow_318-34042.jpg");
                comment.put("id_place", id_rest);
                String x = commentRef.push().getKey();
                comment.put("id", x);
                commentRef.child(x).setValue(comment);
                msg.setText("");

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        checkConnection();

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
    public void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_rest = pref.getString("login_shared", "def");

        fire_comment = new Firebase("https://hangoutenvol.firebaseio.com/Comment/");
        queryRef_comment = fire_comment.orderByChild("id_place").equalTo(id_rest);

        mListAdapterComment = new CommentListAdapter(queryRef_comment, CommentActivity.this, R.layout.list_comment, getBaseContext());
        listViewComment.setAdapter(mListAdapterComment);

        mListAdapterComment.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listViewComment.setSelection(mListAdapterComment.getCount() - 1);
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
