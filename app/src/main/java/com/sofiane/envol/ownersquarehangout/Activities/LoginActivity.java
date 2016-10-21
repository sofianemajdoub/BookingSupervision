package com.sofiane.envol.ownersquarehangout.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.ConnectivityReceiver;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected Firebase ref_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkConnection();
        emailEditText = (EditText) findViewById(R.id.emailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);
        ref_auth = new Firebase("https://hangoutenvol.firebaseio.com/");


        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               String email = emailEditText.getText().toString();
                                               String password = passwordEditText.getText().toString();

                                               email = email.trim();
                                               password = password.trim();

                                               if (email.isEmpty() || password.isEmpty()) {
                                                   AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                   builder.setMessage(R.string.login_error_message)
                                                           .setTitle(R.string.login_error_title)
                                                           .setPositiveButton(android.R.string.ok, null);
                                                   AlertDialog dialog = builder.create();
                                                   dialog.show();
                                               } else {

                                                   final String emailAddress = email;
                                                   //Login with an email/password combination
                                                   ref_auth.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                                       @Override
                                                       public void onAuthenticated(AuthData authData) {
                                                           // Authenticated successfully with payload authData
                                                           Map<String, Object> map = new HashMap<String, Object>();
                                                           map.put("email", emailAddress);

                                                           ref_auth.child("Owners").child(authData.getUid()).updateChildren(map);
                                                           ref_auth.child("Count").child(authData.getUid()).
                                                                   addValueEventListener(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(DataSnapshot snapshot) {
                                                                           System.out.println("___" + snapshot.getValue());

                                                                           String string = snapshot.getValue().toString();
                                                                           String[] parts = string.split("," + " ");
                                                                           String typee = parts[0];
                                                                           String Name_place = parts[1];
                                                                           String Id_restaurant = parts[2];

                                                                           String[] val1 = typee.split("=");
                                                                           String type_val = val1[1];
                                                                           String[] val2 = Name_place.split("=");
                                                                           String name_val = val2[1];
                                                                           String[] val3 = Id_restaurant.split("=");
                                                                           String Id_restaurant_val = val3[1];

                                                                           System.out.println("_ " + type_val + "_ " + name_val + " _ " + Id_restaurant_val);

                                                                           String Id_restaurant_val_x = Id_restaurant_val.substring(0, Id_restaurant_val.length() - 1);

                                                                           SharedPreferences.Editor editor = getSharedPreferences("user", 0).edit();
                                                                           editor.putString("login_shared", Id_restaurant_val_x);
                                                                           editor.putString("type",
                                                                                   type_val);
                                                                           editor.putString("name_place",
                                                                                   name_val);
                                                                           editor.putString("id_user", emailAddress);
                                                                           editor.commit();
                                                                           System.out.println("" + Id_restaurant_val_x + " .. " + type_val);

                                                                       }

                                                                       @Override
                                                                       public void onCancelled(FirebaseError firebaseError) {
                                                                           System.out.println("The read failed: " + firebaseError.getMessage());
                                                                       }
                                                                   });

                                                           Intent intent = new Intent(LoginActivity.this, OwnerMainActivity.class);
                                                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                           startActivity(intent);
                                                       }

                                                       @Override
                                                       public void onAuthenticationError(FirebaseError firebaseError) {
                                                           // Authenticated failed with error firebaseError
                                                           AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                           builder.setMessage(firebaseError.getMessage())
                                                                   .setTitle(R.string.login_error_title)
                                                                   .setPositiveButton(android.R.string.ok, null);
                                                           AlertDialog dialog = builder.create();
                                                           dialog.show();
                                                       }
                                                   });


                                               }


                                           }
                                       }
        );
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


