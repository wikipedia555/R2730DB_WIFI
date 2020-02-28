package com.example.edifierwifidevice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Thread threadConnect;
    String SERVER_IP;
    private Socket socket;
    private static final int SERVERPORT = 8888;
    Button temp;
    Intent wifiSettings;
    SharedPreferences bufSettings;
    final String SAVED_IP = "saved_ip";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiSettings = new Intent(this, WifiSettingsActivity.class);
        if(getIntent().getStringExtra("ip") != null) {
            SERVER_IP = getIntent().getStringExtra("ip");
            saveSettings(SERVER_IP);
        }
        SERVER_IP = loadSettings();
        Log.e("Server IP", SERVER_IP);
        temp = findViewById(R.id.tempButton);
        threadConnect = new Thread(new ClientThread());
        threadStarted();
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = "12345" + "\n" + "67890";
                sendMessage(temp);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.wifi_settings:
                startActivity(wifiSettings);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void threadStarted()
    {
        if(threadConnect.isAlive())
        {
            threadConnect.interrupt();
        }
        threadConnect = new Thread(new ClientThread());
        threadConnect.start();
    }




    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

//        public void onClick(View view)
//        {
//            ClientThread myThread = new ClientThread();
//            myThread.run();
//        }

    }

    public void sendMessage (String str)
    {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            Log.i("Message", "Send");
            int SDK_INT = android.os.Build.VERSION.SDK_INT;

            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                // Where you get exception write that code inside this.
                Log.i("StrictMode", "Yes");
                out.println(str);
            }
            else
            {
                Log.i("StrictMode", "No");
                out.println(str);
            }
            // out.println(str);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveSettings(String ip)
    {
        bufSettings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = bufSettings.edit();
        ed.putString(SAVED_IP,ip);
        ed.commit();
    }

    private String loadSettings()
    {
        bufSettings = getPreferences(MODE_PRIVATE);
        String ip = bufSettings.getString(SAVED_IP, "192.168.0.1");
        return ip;


    }



}

