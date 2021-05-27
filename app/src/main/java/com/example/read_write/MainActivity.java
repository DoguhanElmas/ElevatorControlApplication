
package com.example.read_write;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    Tag myTag;
    Context context;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean isNfcOpen;

    private NfcAdapter mNfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);   if (nfcAdapter == null) {
            // NFC is not available for device
        } else if (!nfcAdapter.isEnabled()) {
            isNfcOpen = true;
            checkNfc(null);
        } else {
            isNfcOpen = false;
        }
        TextView video = (TextView)findViewById(R.id.video);

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               watchTutorial();

            }
        });

        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] {tagDetected};
    }
    //This function shows a modal when phone's NFC is disabled
    public void checkNfc(View view) {
        NfcDialog dialog = new NfcDialog();
        dialog.show(getSupportFragmentManager(),"NFC");

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    //NFC Reading process starts here
    private void readFromIntent(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
        ){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length;i++){
                    msgs[i] = (NdefMessage)rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    // this function opens a video
    void watchTutorial(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=NvCcSgVAzvk")));
    }

    //NFC Reading process continue here
    private void buildTagViews(NdefMessage[] msgs){
        if(msgs == null | msgs.length == 0) return;

        String floorCount = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLenght = payload[0] & 0063;

        try {
            floorCount = new String(payload,languageCodeLenght+1,payload.length-languageCodeLenght-1,textEncoding);
        }catch (UnsupportedEncodingException e){

        }
        readElevatorPanel(floorCount);


    }
    //this function send 'floor_count' data to FloorList class.
    void readElevatorPanel(String floorCount){
        Intent i = new Intent(this,FloorList.class);
        i.putExtra("floor_count",floorCount);
        startActivityForResult(i,1000);
    }



    //This function called when new NFC intent
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){

            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

//    private void write(String text,Tag tag)throws IOException, FormatException {
//        NdefRecord[] records = {createRecord(text)};
//        NdefMessage message = new NdefMessage(records);
//        Ndef ndef = Ndef.get(tag);
//        ndef.connect();
//        ndef.writeNdefMessage(message);
//        ndef.close();
//
//    }
//
//    private NdefRecord createRecord(String text) throws  UnsupportedEncodingException{
//        String lang = "en";
//        byte[] textBytes = text.getBytes();
//        byte[] langBytes = lang.getBytes("US-ASCII");
//        int langLength = langBytes.length;
//        int textLength = textBytes.length;
//        byte[] payload = new byte[1+langLength+textLength];
//
//        payload[0] = (byte) langLength;
//
//        System.arraycopy(langBytes,0,payload,1,langLength);
//        System.arraycopy(textBytes,0,payload,1+langLength,textLength);
//
//        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],payload);
//        return  recordNFC;
//    }

    @Override
    public void onPause(){
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableReaderMode(this);
    }

    @Override
    public void onResume(){
        super.onResume();

//        if(mNfcAdapter!= null) {
//            Bundle options = new Bundle();
//
//            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);
//
//            mNfcAdapter.enableReaderMode(this,
//                    this,
//                    NfcAdapter.FLAG_READER_NFC_A |
//                            NfcAdapter.FLAG_READER_NFC_B |
//                            NfcAdapter.FLAG_READER_NFC_F |
//                            NfcAdapter.FLAG_READER_NFC_V |
//                            NfcAdapter.FLAG_READER_NFC_BARCODE |
//                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
//                    options);
//        }

    }



    @Override
    public void onTagDiscovered(Tag tag) {
        Ndef mNdef = Ndef.get(tag);
//        if (mNdef!= null) {
//
//            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
//
//
//
//      }
    }
}