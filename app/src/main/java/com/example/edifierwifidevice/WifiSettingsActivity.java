package com.example.edifierwifidevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WifiSettingsActivity extends AppCompatActivity {

    Button editIpButton;
    EditText editIpText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_settings);
        editIpButton = findViewById(R.id.edit_ip_but);
        editIpText = findViewById(R.id.edit_ip_text);
        final Intent sendIp = new Intent(this, MainActivity.class);


        editIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = editIpText.getText().toString();
                if(s != null)
                {
                    sendIp.putExtra("ip", s);
                    startActivity(sendIp);

                }

            }
        });
    }


}
