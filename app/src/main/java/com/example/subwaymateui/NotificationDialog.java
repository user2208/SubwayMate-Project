package com.example.subwaymateui;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class NotificationDialog extends AppCompatActivity {

    private Button eStart;
    private Button eEnd;
    //private EditText eTime;
    //private EditText eStation;
    private Button stationBtn;
    private NotificationDialogHelper mNotificationhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        eStart = findViewById(R.id.set_as_departure); // 출발지
        eEnd = findViewById(R.id.set_as_arrival); // 도착지
        //eTime = findViewById(R.id.edit_time); // 시간
        //eStation = findViewById(R.id.edit_station); // 정거장 수
        stationBtn = findViewById(R.id.find_route_button);

        mNotificationhelper = new NotificationDialogHelper(this);

        stationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = eStart.getText().toString();
                String end = eEnd.getText().toString();
                //String time = eTime.getText().toString();
                //String station = eStation.getText().toString();

                sendOnChannel(start + " 승차", "목적지 : " + end);
                //sendOnChannel1(start + " 승차", "목적지 : " + end, "오후(오전) " + time + " 도착 예정", "정류장 " + station +"개 남음");
            }
        });
    }

    public void sendOnChannel(String start, String end) {
        NotificationCompat.Builder nb = mNotificationhelper.getChannelNotification(start, end);
        mNotificationhelper.getManager().notify(1, nb.build());
    }
};