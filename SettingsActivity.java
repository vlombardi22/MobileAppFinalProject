package com.example.vhl2.bandapp3;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Settings Activity focuses on the UI around the settings tab in the
 * app which allows the user to change their profile such as change there
 * name and instrument
 */
public class SettingsActivity extends AppCompatActivity {
    private BandMember editMember;

    /**
     * Method called on the creation of the activity setting it up before
     * the user can interact with it
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        editMember = (BandMember) intent.getSerializableExtra("currentMember");

        final EditText editName = (EditText) findViewById(R.id.EditNameText);
        final EditText newPassword = (EditText) findViewById(R.id.EditPasswordText);
        final EditText repeatPassword = (EditText) findViewById(R.id.EditRepeatPasswordText);
        final Spinner sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);
        final Spinner classSpinner = (Spinner) findViewById(R.id.classSpinner);
        Button finishButton = findViewById(R.id.updateSettingsButton);
        //sets the userName textBox to what they currently have thier name as
        editName.setText(editMember.getName());
        //sets the instrument spinner to what the user's current instrument is
        switch(editMember.getInstrument()) {
            case "saxophone":
                sectionSpinner.setSelection(0);
                break;
            case "trumpet":
                sectionSpinner.setSelection(1);
                break;
            case "percussion":
                sectionSpinner.setSelection(2);
                break;
            case "flute":
                sectionSpinner.setSelection(3);
                break;
            case "clarinet":
                sectionSpinner.setSelection(4);
                break;
            case "trombone":
                sectionSpinner.setSelection(5);
                break;
            case "bass":
                sectionSpinner.setSelection(6);
                break;
            case "sousaphone":
                sectionSpinner.setSelection(7);
        }
        //Sets the year spinner to what the user's current year is
        switch (editMember.getYear()) {
            case "freshman":
                classSpinner.setSelection(0);
                break;
            case "sophomore":
                classSpinner.setSelection(1);
                break;
            case "junior":
                classSpinner.setSelection(2);
                break;
            case "senior":
                classSpinner.setSelection(3);
                break;
            case "director":
                classSpinner.setSelection(4);
        }

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this if statement checks if the user edited the password at all and if
                // they did edit the password textbox make sure the password and repeatpassword
                // boxes are the same. if tha tis the case then change the password
                if(!newPassword.getText().toString().equals("")) {
                    if(!newPassword.getText().toString().equals(repeatPassword.getText().toString())) {
                        Toast.makeText(SettingsActivity.this, "Passwords do not match",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        editMember.setPassword(newPassword.getText().toString());
                    }
                }
                // the rest of this program just updates the profile with the new information
                editMember.setName(editName.getText().toString());
                editMember.setInstrument(sectionSpinner.getSelectedItem().toString());
                editMember.setYear(classSpinner.getSelectedItem().toString());

                Intent settingsIntent = new Intent();
                settingsIntent.putExtra("changedInfoMember", editMember);
                setResult(Activity.RESULT_OK, settingsIntent);
                finish();

            }
        });


    }
}


