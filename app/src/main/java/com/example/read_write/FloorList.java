package com.example.read_write;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class FloorList extends AppCompatActivity  implements MyRecyclerViewAdapter.ItemClickListener
{

    ArrayList<String> floors = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floor_list);
        int floorCount = Integer.parseInt(getIntent().getExtras().getString("floor_count"));

        for(int i = 0;i <= floorCount;i++){
            floors.add(""+i);
        }

        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, floors);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked floor number " + adapter.getItem(position), Toast.LENGTH_LONG).show();
        toFloor(String.valueOf(position));

    }
    //This function send the targetFloor variable to SendData class, when it reaches the SendData the sending process start
    void toFloor(String targetFloor){
        Intent i = new Intent(getApplicationContext(),SendData.class);
        i.putExtra("destination",targetFloor);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
