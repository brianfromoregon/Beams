package com.lovingishard.beams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * http://commons.wikimedia.org/wiki/File:Candle.jpg
 */
public class MainActivity extends LocationRequestingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(getClass().getName(), "onCreate");
        setContentView(R.layout.activity_main);
    }

    public void onBtnClicked(View ignored) {
        Log.d(getClass().getName(), "onBtnClicked");

        if (gpsEnabled()) {
            final EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Love Report")
                    .setMessage("We're noting your GPS location, feel free to include a message too.")
                    .setView(input)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            Log.i(getClass().getName(), "Entered: " + value);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("GPS Not Enabled")
                    .setMessage("This app needs GPS to work. Please enable and try again!")
                    .show();
        }

    }
}
