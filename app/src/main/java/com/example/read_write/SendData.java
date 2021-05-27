package com.example.read_write;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;

public class SendData extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    NfcAdapter mNfcAdapter;
    String targetFloor = "";
    boolean isNfcOpen;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_nfc);
        targetFloor = getIntent().getExtras().getString("destination");


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            // NFC is not available for device
        } else if (!mNfcAdapter.isEnabled()) {
            isNfcOpen = true;
            checkNfc(null);
        } else {
            isNfcOpen = false;
        }
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        mNfcAdapter.setOnNdefPushCompleteCallback( this,this);
        send();

        TextView video = (TextView)findViewById(R.id.video);

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchTutorial();

            }
        });

    }

    // this function opens a video
    void watchTutorial(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=PRkEuAyw3Po")));
    }

    // application closes when data sended
    public void onNdefPushComplete( NfcEvent arg0) {
        finishAffinity();
    }

    public void checkNfc(View view) {
        NfcDialog dialog = new NfcDialog();
        dialog.show(getSupportFragmentManager(),"NFC");

    }

    public void send() {
         Toast.makeText(this, "Place the back of the device against the NFC and touch the screen to send data.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    // this function creates the message to be sent
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage message = create_MIME_NdefMessage("application/nfcchat", targetFloor);
//        Log.v("asd", "76 ");
//        targetFloor = "";
        return message;
    }

    // this function helpes the createNdefMessage function
    public NdefMessage create_MIME_NdefMessage(String mimeType, String payload) {
        NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA ,
                mimeType.getBytes(), new byte[0],
                payload.getBytes(Charset.forName("US-ASCII")));
        NdefMessage message = new NdefMessage(new NdefRecord[] { mimeRecord});
        return message;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//            processIntent(getIntent());

        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


}
