package com.example.read_write;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class NfcDialog extends DialogFragment implements View.OnClickListener {
    Button yes_button, no_button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nfc_dialog, container, false);

        yes_button = (Button) view.findViewById(R.id.yesbtn);
        no_button = (Button) view.findViewById(R.id.nobtn);

        // setting onclick listener for buttons
        yes_button.setOnClickListener(this);
        no_button.setOnClickListener(this);

        return view;
    }

    // if user click the yes: phone settings opens
    // if user click the no: application closes
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yesbtn :
                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(i);
                getDialog().dismiss();
                break;

            case R.id.nobtn :
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                getActivity().finish();
                System.exit(1);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                dismiss();
                break;
        }

    }

}
