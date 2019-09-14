package com.example.telephony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TelephonyManager telephonyManager;
    TextView tv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("fake","tlephony channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                String s="null";
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        s = "IDLE";
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        s = "RINGING";
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        s = "off hook";
                        break;
                }
                Toast.makeText(MainActivity.this,"call state "+s+"phone number:" + phoneNumber,Toast.LENGTH_LONG).show();
                //Snackbar.make(findViewById(android.R.id.content), "call state "+s+"phone number:" + phoneNumber, Snackbar.LENGTH_LONG).show();
            }
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                Snackbar.make(findViewById(android.R.id.content), "signal changed ! " + signalStrength, Snackbar.LENGTH_LONG).show();
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
        tv_log = findViewById(R.id.tv_log);
        getPhoneData();

    }

    void getPhoneData() {
        String s = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1234);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                s +="IMEI: android Q is ку-ку!";
            }else {
                s += "IMEI:" + telephonyManager.getImei();
            }
        } else {
            s += "IMEI:" + telephonyManager.getDeviceId();
        }
        s += "\n sim country:" + telephonyManager.getSimCountryIso();
        s += "\n phone type:" + telephonyManager.getPhoneType();
        s += "\n operator name" + telephonyManager.getNetworkOperatorName();
        s += "\n roaming:" + telephonyManager.isNetworkRoaming();
        s += "\n phone number:" + telephonyManager.getLine1Number();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);
                s += "\n\t number:" + info.getNumber();
                s += "\n\t net name:" + info.getCarrierName();
                s += "\n\t number:" + info.getNumber();
            }
        }
        tv_log.setText(s);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1234) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoneData();
            } else {
                finish();
            }
            return;
        }

    }

    public void onDial(View view) {

        startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:7777")));
    }

    public void onCall(View view) {
        //permission CALL_PHONE required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},5555);
                return;
            }
        }
        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:5555")));
    }

    public void sendDirectSms(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},5555);
                return;
            }
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("5556",
                null,
                "some text",
                null,
                null);
    }

    public void sendViaSender(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.fromParts("sms","5556",null))//  /#
                .putExtra("sms_body","hello "));
    }
}
