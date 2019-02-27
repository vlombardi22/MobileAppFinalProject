package com.example.vhl2.bandapp3;
/**
 * This activity allows users to look at both their own points and the points of everyone else in the
 * band. It displays the every single band member's username section and total travel points in a ListView
 * admins can use the plus button to go into addPointsActivity. They can use the trash button to
 * clear out the entire database. finally admins can long click to delete specific users
 * Programming Assignment #8
 *
 *@author Vincent Lombardi & Dominic Gianatassio
 * @version v1.0
 */
// sources
// collections https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html
// firebase https://firebase.google.com/docs/database/?utm_source=studio
// firebase https://firebase.google.com/docs/reference/android/com/google/firebase/database/ChildEventListener
// saxophone <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// trombone <div>Icons made by <a href="https://www.flaticon.com/authors/nikita-golubev" title="Nikita Golubev">Nikita Golubev</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// trumpet <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// sousaphone <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// flute <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// clarinet <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// bass <div>Icons made by <a href="https://www.flaticon.com/authors/roundicons" title="Roundicons">Roundicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
// drum <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private DatabaseReference myRoster;
    final int ADD_POINTS_CODE = 2;
    final int SETTINGS_CODE = 3;
    private BandMember currentUser;

    GridLayout gridLayout;
    List<BandMember> roster;
    ArrayAdapter<BandMember> arrayAdapter;
    ListView listView;


    /**
     * Creates a gridlayout and sets up a FireBase database reference that can read and write
     * data to our real time database. This function also creates a long click listener which
     * allows admins to delete users. This function also creates child
     * event listeners which update the ArrayList and ListView.
     * @param savedInstanceState a saved instance state for our app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(1);
        gridLayout.setBackgroundColor(Color.WHITE);

        roster = new ArrayList<BandMember>();
        listView = new ListView(this);
        GridLayout.Spec listViewRowSpec = GridLayout.spec(0);
        GridLayout.Spec listViewColSpec = GridLayout.spec(0, GridLayout.FILL);
        GridLayout.LayoutParams listViewLayoutParams = new GridLayout.LayoutParams(listViewRowSpec, listViewColSpec);
        listView.setLayoutParams(listViewLayoutParams);

        // In the Database all band members are children of Users
        myRoster = FirebaseDatabase.getInstance().getReference("Users");

        Intent intent = getIntent();
        currentUser = (BandMember) intent.getSerializableExtra("currentUser");
        Toast.makeText(this, "welcome " + currentUser.getUserName(), Toast.LENGTH_SHORT).show();

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            /**
             * checks if the items state has changed or has been selected.
             * @param actionMode current action mode
             * @param i position of selected item
             * @param l Adapter id of selected item
             * @param b boolean that says whether the item is checked or unchecked
             */
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                int numSelected = listView.getCheckedItemCount();
                actionMode.setTitle(numSelected + " selected");
            }

            /**
             * inflates the menu for mainActivity's action mode
             * @param actionMode current action mode
             * @param menu cam_menu.xml
             * @return true: menu creation is successful
             */
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.cam_menu, menu);
                return true;
            }

            /**
             * prepares the action mode. this function is currently an auto generated method stub
             * @param actionMode current action mode
             * @param menu menu file
             * @return false
             */
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            /**
             * Code for the Cam menu for removing users. Removes all items in the arrayList that have been selected
             * by the admin. if you are not an admin you are given a toast that says you cannot
             * preform this action.
             * @param actionMode current action mode
             * @param menuItem item clicked in the menu
             * @return true as the menu action was successfully completed.
             */
            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
                int menuId = menuItem.getItemId();

                switch (menuId) {
                    case R.id.deleteMenuAction:
                        if (currentUser.getAdmin()) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                            alertBuilder.setNegativeButton(R.string.no_button, null);
                            alertBuilder.setTitle(R.string.alert_title).setMessage(R.string.delete_warning);
                            alertBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                                /**
                                 * deletes selected items
                                 * @param dialogInterface the alert dialogue
                                 * @param i item id
                                 */
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for(int index = roster.size() - 1; index >=0; index--){
                                        if (listView.isItemChecked(index)) {
                                            String instrument = roster.get(index).getInstrument();
                                            String name = roster.get(index).getUserName();
                                            myRoster.child(instrument).child(name).removeValue();
                                            roster.remove(index);
                                        }
                                    }

                                    Collections.sort(roster);
                                    arrayAdapter.notifyDataSetChanged();
                                    actionMode.finish();

                                }
                            });
                            alertBuilder.show();
                        } else {
                            Toast.makeText(MainActivity.this, "you cannot delete users", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }


                return true;
            }

            /**
             * destroys the actionMode. Currently is an auto generated sub method
             * @param actionMode current action mode
             */
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {}
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            /**
             * This method takes in BandMember information and puts them in an arraylist
             * Which then is hooked up to a listView which displays all of our current band members
             * @param dataSnapshot and instance of a section of the database.
             * @param s previous child key
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    BandMember member = child.getValue(BandMember.class);
                    roster.add(member);
                    arrayAdapter.notifyDataSetChanged();
                }
            }



            /**
             * this code removes a section of data from the database and then adds it back
             * in a new sorted order along with a new element.
             * @param dataSnapshot reference to a section of the data
             * @param s previous child key
             */
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: changed!");
                boolean exists = true;

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    BandMember member = child.getValue(BandMember.class);

                    if (exists){
                        exists = false;
                        String instrument = member.getInstrument();
                        for(int i = roster.size() - 1; i >= 0; i--){
                            if(roster.get(i).getInstrument().equals(instrument)){
                                roster.remove(i);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    roster.add(member);
                }
                Collections.sort(roster);
                arrayAdapter.notifyDataSetChanged();
            }

            /**
             * A stub method for what to do if a child is removed. onChildChanged already
             * covers this.
             * @param dataSnapshot a reference to a section of the data
             */
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            /**
             * What to do if a child is moved in the database. currently this method is not in use
             * @param dataSnapshot a reference to a section of hte database
             * @param s previous child key
             */
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            /**
             * What to do if there is an error in the database. Currently this method is a stub.
             * @param databaseError an error in te database
             */
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        myRoster.addChildEventListener(childEventListener);
        arrayAdapter = new ArrayAdapter<BandMember>(this, android.R.layout.simple_list_item_activated_1, roster){
            /**
             * This function sets the icons for the user sections.
             * @param position index in the listView
             * @param convertView the old view
             * @param parent parent view group
             * @return an altered view
             */
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                String type = roster.get(position).getInstrument();
                TextView textView1 = (TextView) view.findViewById(android.R.id.text1);

                switch(type){
                    case "saxophone" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.saxophone,0,0 , 0);
                        break;
                    case "percussion" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drum,0,0 , 0);
                        break;
                    case "clarinet" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clarinet,0,0 , 0);
                        break;
                    case "flute" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flute,0,0 , 0);
                        break;
                    case "bass" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bassguitar,0,0 , 0);
                        break;
                    case "trumpet" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trumpet,0,0 , 0);
                        break;
                    case "trombone" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trombone,0,0 , 0);
                        break;
                    case "sousaphone" :
                        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.musicalsign,0,0 , 0);
                        break;
                    default:
                        Log.e(TAG, "addMember: invalid entry");
                        break;
                }
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        gridLayout.addView(listView);
        setContentView(gridLayout);
    }

    /**
     * creates the main_menu
     * @param menu menu.xml file.
     * @return true if action is successful.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Adds menu that allows admin users to clear the database and start the AddPoints activity.
     * @param item the menu item that was clicked
     * @return true if menu action is successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch(menuId) {
            case R.id.addMenuItem:
                if(currentUser.getAdmin()) {
                    Intent intent = new Intent(MainActivity.this, AddPointsActivity.class);
                    startActivityForResult(intent, ADD_POINTS_CODE);
                }else{
                    Toast.makeText(this, getString(R.string.deny_add_points_name), Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.clearMenu:
                if(currentUser.getAdmin()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            roster.clear();
                            myRoster.removeValue();
                            arrayAdapter.notifyDataSetChanged();
                            finish();
                        }
                    });
                    alertBuilder.setNegativeButton(R.string.no_button, null);
                    alertBuilder.setTitle(R.string.alert_title).setMessage(R.string.clear_warning);
                    alertBuilder.show();
                    return true;
                } else {
                    Toast.makeText(this, getString(R.string.deny_clear_name), Toast.LENGTH_SHORT).show();
                    return true;
                }

            case R.id.settingsItem:
                Intent settingsIntent = new Intent(MainActivity.this,
                        SettingsActivity.class);
                settingsIntent.putExtra("currentMember", currentUser);
                startActivityForResult(settingsIntent, SETTINGS_CODE);
                return true;
            case R.id.logoutButton:
                finish();
                return true;
            default:
                Log.e(TAG, "onOptionsItemSelected: Invalid item selected");
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates the listView after setting points from addPoints activity.
     * @param requestCode code used to request previous activity.
     * @param resultCode Code that is passed back from previous activity.
     * @param data intent passed back from previous activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_POINTS_CODE) {
                arrayAdapter.notifyDataSetChanged();
            } else if(requestCode == SETTINGS_CODE) {
                BandMember updatedUser = (BandMember) data.getSerializableExtra("changedInfoMember");
                for(int i = roster.size() - 1; i >= 0; i--){
                    if(roster.get(i).getUserName().equals(updatedUser.getUserName())){
                        if(roster.get(i).getName().equals(updatedUser.getName())) {
                            roster.remove(i);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(updatedUser.getInstrument().equals(currentUser.getInstrument())) {
                    myRoster.child(currentUser.getInstrument()).child(currentUser.getUserName())
                            .setValue(updatedUser);
                } else {
                    myRoster.child(currentUser.getInstrument()).child(currentUser.getUserName())
                            .removeValue();
                    myRoster.child(updatedUser.getInstrument()).child(updatedUser.getUserName())
                            .setValue(updatedUser);
                }
                currentUser = updatedUser;
            }
        }
    }
}
