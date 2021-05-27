package com.example.read_write;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Open extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open);

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
