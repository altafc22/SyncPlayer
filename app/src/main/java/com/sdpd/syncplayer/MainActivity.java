package com.sdpd.syncplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnHost;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First Time Launch
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefsKey), MODE_PRIVATE);
        boolean isFirstRun = sharedPrefs.getBoolean(getString(R.string.isFirstRunSharedPrefsKey),true);
        if(isFirstRun) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

        // Setup the buttons and their on-click listeners
        btnHost = findViewById(R.id.btn_host);
        btnJoin = findViewById(R.id.btn_join);

        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HostActivity.class);
                startActivity(intent);
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
    }
}
