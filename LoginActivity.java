package com.example.vhl2.bandapp3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


//LoginActivity is the first thing the user will see as it forces then to login before
// they have access to anything else. It also gives the user the option to sign up if they don't have an account
public class LoginActivity extends AppCompatActivity {
    private DataSnapshot dataSnapshot;
    private DatabaseReference myRoster;
    private List<BandMember> userList;
    private static final String TAG = "LoginActivity";
    final int ADD_MEMBER_CODE = 1;
    BandMember currentUser;
    //Called when the activity first starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myRoster = FirebaseDatabase.getInstance().getReference("Users");
        userList = new ArrayList<>();

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        Button loginButton = (Button) findViewById(R.id.LoginButton);
        final EditText loginText = (EditText) findViewById(R.id.inputUsername);
        final EditText passwordText = (EditText) findViewById(R.id.inputPassword);
        //Grabs all the users from the database in order to help authenticate the usre
        myRoster.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                        userList.add(snapshot.getValue(BandMember.class));
                        Log.d(TAG, "onDataChange: " + snapshot.getValue(BandMember.class).toString());
                        Log.d(TAG, "onDataChange: " + snapshot.getValue(BandMember.class).getPassword());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //This handles whne the user clicks the signup button and takes the user
        // to the sign up activity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NewUser.class);
                intent.putExtra("redo", false);
                startActivityForResult(intent, ADD_MEMBER_CODE);
            }
        });
        //This deals when the user clicks the login button. First it checks if the username exists in the database
        // if it does exist check if the password matches the password for the given username. If both of these are true
        // it will allow the user to login and otherwise show a toast saying that they have the wrong credentials
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isCorrect = false;
                for(int k = 0; k < userList.size(); k++) {
                    if(userList.get(k).getUserName().equals(loginText.getText().toString())) {
                        if(userList.get(k).getPassword().equals(passwordText.getText().toString())) {
                            isCorrect = true;
                            currentUser = userList.get(k);
                        }
                    }
                }
                if(isCorrect) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("currentUser", currentUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Username or Password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    /**
     * on Results method for main Activity checks if userenames exist in the database before
     * adding them and calls NewUser again if it finds a match.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == ADD_MEMBER_CODE)&&(resultCode == Activity.RESULT_OK)){
            Log.d(TAG, "onActivityResult: pre");
            BandMember temp = (BandMember) data.getSerializableExtra("newMember");
            boolean exists = false;
            for(int i = 0; i < userList.size(); i++){
                if(userList.get(i).getUserName().equals(temp.getUserName())){
                    exists = true;
                }
            }
            if(!exists) {
                addMember(temp);
            } else {
                Toast.makeText(this, "that user name is already taken", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, NewUser.class);
                intent.putExtra("newMember", temp);
                boolean redo = true;
                intent.putExtra("redo", true);
                startActivityForResult(intent, ADD_MEMBER_CODE);
            }
        }

    }


    /**
     * adds a member to one of the sub databases based on its membership
     * @param member incoming band member
     */
    public void addMember(BandMember member){
        String userId = member.getUserName();
        String userSection = member.getInstrument();
        switch(userSection){
            case "saxophone" :
                myRoster.child("saxophone").child(userId).setValue(member);
                break;
            case "percussion" :
                myRoster.child("percussion").child(userId).setValue(member);
                break;
            case "clarinet" :
                myRoster.child("clarinet").child(userId).setValue(member);
                break;
            case "flute" :
                myRoster.child("flute").child(userId).setValue(member);
                break;
            case "bass" :
                myRoster.child("bass").child(userId).setValue(member);
                break;
            case "trumpet" :
                myRoster.child("trumpet").child(userId).setValue(member);
                break;
            case "trombone" :
                myRoster.child("trombone").child(userId).setValue(member);
                break;
            case "sousaphone" :
                myRoster.child("sousaphone").child(userId).setValue(member);
                break;
            default:
                Log.e(TAG, "addMember: invalid entry");
                break;
        }
    }


}