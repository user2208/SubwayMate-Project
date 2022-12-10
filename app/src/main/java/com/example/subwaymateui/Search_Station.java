package com.example.subwaymateui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ThemeUtil;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search_Station extends AppCompatActivity {
    private ImageButton search_back_button;
    private Button set_as_departure;
    private Button set_as_arrival;
    private Button reset_button;
    private Button find_route;
    private SearchView fragment_home_searchView;
    List<List<String>> list = null;

    public String searchingstation = "";

    private String departure = "";
    private String arrival = "";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        //값을 불러온다.
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        departure = pref.getString("DEP", "");
        arrival = pref.getString("ARR", "");


        setTheme(R.style.Theme_SubwayMateUI);
        searchingstation = getIntent().getStringExtra("searchingstation");
        TextView textView_current = (TextView)findViewById(R.id.currentset);


        setCurrent(textView_current);

        try {
            list = readDataFromCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }


        search_back_button = (ImageButton) findViewById(R.id.search_back_button);
        search_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView textView = (TextView)findViewById(R.id.whatStation);
        if (!searching(this.searchingstation)){
            textView.setText("일치하는 역이 없어요");
            textView.setTextSize(26);
        }else{
            //textView = (TextView)findViewById(R.id.whatStation);
            textView.setText(this.searchingstation);
            backColorChange(textView, searchingstation);
        }

        fragment_home_searchView = (SearchView) findViewById(R.id.searchView);
        fragment_home_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override  //검색버튼 누르면 호출됨
            public boolean onQueryTextSubmit(String s) {
                searchingstation = s;
                if (!searching(searchingstation)){
                    textView.setText("일치하는 역이 없어요");
                    textView.setTextSize(30);
                }else{
                    textView.setText(searchingstation);
                    backColorChange(textView, searchingstation);
                    textView.setTextSize(70);
                }
                return true;
            }

            @Override  //입력값 바뀔때 마다 호출됨
            public boolean onQueryTextChange(String s) {
                //System.out.println(s);
                return true;
            }

        });


        //setCurrent(textView_current);
        set_as_departure = (Button) findViewById(R.id.set_as_departure);
        set_as_departure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (searching(searchingstation)){
                    editor.putString("DEP", searchingstation);
                    editor.apply();
                    departure = searchingstation;
                    setCurrent(textView_current);
                }
            }
        });
        set_as_arrival = (Button) findViewById(R.id.set_as_arrival);
        set_as_arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searching(searchingstation)){
                    editor.putString("ARR", searchingstation);
                    editor.apply();
                    arrival = searchingstation;
                    setCurrent(textView_current);
                }
            }
        });

        reset_button = (Button) findViewById(R.id.reset_button);
        reset_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                departure = "";
                arrival = "";
                setCurrent(textView_current);
            }
        });





        find_route = (Button) findViewById(R.id.find_route_button);
        find_route.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (departure == arrival){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search_Station.this);
                    builder.setTitle("알림");
                    builder.setMessage("출발역과 도착역이 같아요!");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }else if (departure == "" && arrival != ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search_Station.this);
                    builder.setTitle("알림");
                    builder.setMessage("출발역을 설정해주세요!");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }else if (departure != "" && arrival == ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search_Station.this);
                    builder.setTitle("알림");
                    builder.setMessage("도착역을 설정해 주세요!");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }else if (departure == "" && arrival == ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search_Station.this);
                    builder.setTitle("알림");
                    builder.setMessage("출발역과 도착역을 설정해 주세요!");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Route_Algorithm.class);
                    intent.putExtra("dep", departure);
                    intent.putExtra("arr", arrival);
                    startActivity(intent);
                }
            }
        });
    }

    public void backColorChange(TextView tv, String depOrarri){
        switch (Integer.parseInt(depOrarri)/100){
            case 1: tv.setBackgroundColor(Color.parseColor("#00B24A"));
                break;
            case 2: tv.setBackgroundColor(Color.parseColor("#001693"));
                break;
            case 3: tv.setBackgroundColor(Color.parseColor("#9B0024"));
                break;
            case 4: tv.setBackgroundColor(Color.parseColor("#FF0004"));
                break;
            case 5: tv.setBackgroundColor(Color.parseColor("#0078DB"));
                break;
            case 6: tv.setBackgroundColor(Color.parseColor("#F4DC00"));
                break;
            case 7: tv.setBackgroundColor(Color.parseColor("#A6F900"));
                break;
            case 8: tv.setBackgroundColor(Color.parseColor("#00DCF9"));
                break;
            case 9: tv.setBackgroundColor(Color.parseColor("#9E00E8"));
                break;
        }
    }

    public void setCurrent(TextView tv){
        tv.setText("출발역: " + departure + "\n도착역: " + arrival);
    }

    public boolean searching(String station){
        int i = 0;
        for (List<String> item : list) {
            if (i == 0) {
                i += 1;
            }else {
                if (station.equals(item.get(0))){
                    //System.out.println(item.get(0) + " 존재합니다.");
                    return true;
                }
            }
        }
        return false;
    }


    public List<List<String>> readDataFromCsv() throws IOException {
        InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.stations));
        BufferedReader reader = new BufferedReader(is);
        CSVReader read = new CSVReader(reader);
        String[] nextLine = null;
        //
        List<List<String>> list = new ArrayList<List<String>>();
        String line = "";
        while ((line = reader.readLine()) != null){
            List<String> stringList = new ArrayList<>();
            String stringArray[] = line.split(",");
            stringList = Arrays.asList(stringArray);
            list.add(stringList);
        }

        return list;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
    private void reset() {
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
