package com.example.edifierwifidevice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

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
    //--buttons
    ImageButton power;
    ImageButton mute;
    ImageButton minusb;
    ImageButton plusb;
    ImageButton line1b;
    ImageButton line2b;
    ImageButton opticalb;
    ImageButton coaxialb;
    ImageButton bluetoothb;

    boolean state;


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
        //temp = findViewById(R.id.tempButton);
        threadConnect = new Thread(new ClientThread());
        threadStarted();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        power = (ImageButton) findViewById(R.id.power);
        mute = (ImageButton) findViewById(R.id.mute);
        minusb = (ImageButton) findViewById(R.id.minus);
        plusb = (ImageButton) findViewById(R.id.plus);
        line1b = (ImageButton) findViewById(R.id.line1);
        line2b = (ImageButton) findViewById(R.id.line2);
        opticalb = (ImageButton) findViewById(R.id.opt);
        coaxialb = (ImageButton) findViewById(R.id.coaxial);
        bluetoothb = (ImageButton) findViewById(R.id.bluet);
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final int vibratemilsec = 50;
        //final int delaysender = 150;

        SwitchCompat onOffSwitch = (SwitchCompat)  findViewById(R.id.on_off_switch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                state = isChecked;
            }

        });
        /*temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = "12345" + "\n" + "67890";
                sendMessage(temp);
            }
        });*/

        //---handler button

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "power";
                sendMessage(s);

            }
        });
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "mute";
                sendMessage(s);

            }
        });
        minusb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "minus";
                sendMessage(s);

            }
        });
        plusb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "plus";
                sendMessage(s);

            }
        });
        line1b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "line1";
                sendMessage(s);

            }
        });
        line2b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "line2";
                sendMessage(s);

            }
        });
        opticalb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "optical";
                sendMessage(s);

            }
        });
        coaxialb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "coaxial";
                sendMessage(s);

            }
        });
        bluetoothb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == true)
                {
                    v.vibrate(vibratemilsec);
                }
                String s = "2730db" + "\n" + "bluetooth";
                sendMessage(s);

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

