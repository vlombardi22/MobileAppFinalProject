package com.example.vhl2.bandapp3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private DatabaseReference myRoster;

    GridLayout gridLayout;
    List<BandMember> roster;
    ArrayAdapter<BandMember> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(1);
        gridLayout.setBackgroundColor(Color.WHITE);

        roster = new ArrayList<>();
        listView = new ListView(this);
        GridLayout.Spec listViewRowSpec = GridLayout.spec(0);
        GridLayout.Spec listViewColSpec = GridLayout.spec(0, GridLayout.FILL);
        GridLayout.LayoutParams listViewLayoutParams = new GridLayout.LayoutParams(listViewRowSpec, listViewColSpec);
        listView.setLayoutParams(listViewLayoutParams);

        // In the Database all band members are children of Users
        myRoster = FirebaseDatabase.getInstance().getReference("Users");
        BandMember Vince = new BandMember("Vinent Lombardi","1234", true, "sophmore",
                "vlombardi", 25, "saxophone");
        BandMember Max = new BandMember("Maxwell Sherman","1234", false, "sophmore",
                "mSherman", 7, "percussion");
        BandMember Brian = new BandMember("Brian", "1234", false, "Junior", "BrianRocks", 35,  "base");

        addMember(Vince);
        addMember(Max);
        addMember(Brian);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                int numSelected = listView.getCheckedItemCount();
                actionMode.setTitle(numSelected + " selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.cam_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                int menuId = menuItem.getItemId();

                switch (menuId) {
                    case R.id.deleteMenuAction:
                    for(int x = roster.size()-1; x >= 0; x--){
                        if(listView.isItemChecked(x)){
                            String name = roster.get(x).getUserName();
                            myRoster.child(name).removeValue();
                            roster.remove(x);

                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                actionMode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            /**
             * This method takes in BandMember information and puts them in an arraylist
             * Which then is hooked up to a listView which displays all of our current band members
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                //myRoster.orderByChild(s);
                BandMember member = dataSnapshot.getValue(BandMember.class);
                roster.add(member);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.d(TAG, "onChildRemoved: ");



            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d(TAG, "onCancelled: ");
            }
        };

        myRoster.addChildEventListener(childEventListener);
        arrayAdapter = new ArrayAdapter<BandMember>(this, android.R.layout.simple_list_item_activated_1, roster);
        listView.setAdapter(arrayAdapter);
        gridLayout.addView(listView);
        setContentView(gridLayout);
    }

    /**
     * Push gives each value a key and setValue stores the objects in a database
     * @param member
     */
    public void addMember(BandMember member){
        String UserId = member.getUserName();
        myRoster.child(UserId).setValue(member);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch(menuId) {
            case R.id.addMenuItem:
//                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
//                startActivityForResult(intent, ADD_NOTE_CODE);
                Toast.makeText(this, "we are not ready to add things yet", Toast.LENGTH_SHORT);
                return true;
            case R.id.clearMenu:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRoster.removeValue();
                    }
                });
                alertBuilder.setNegativeButton(R.string.no_button, null);
                alertBuilder.setTitle(R.string.alert_title).setMessage(R.string.clear_warning);
                alertBuilder.show();
                return true;
            default:
                Log.e(TAG, "onOptionsItemSelected: Invalid item selected");
                return super.onOptionsItemSelected(item);
        }
    }



}
