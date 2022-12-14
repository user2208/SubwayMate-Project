package com.example.subwaymateui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.subwaymateui.ui.statistics.StatisticsFragment;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Route_Algorithm extends AppCompatActivity {
    private ImageButton search_back_button;
    private Button least_time_button;
    private Button least_distance_button;
    private Button least_money_button;
    private Button detail_button;
    private Button add_statistic_button;

    private TextView route;

    String route_ = "";

    List<List<String>> list = null;

    private String departure = "";
    private String arrival = "";

    private int last_weight;


    Graph g;

    //////////////////////////////////
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    //////////////////////////////////
    int send_time = 0;
    int send_distance = 0;
    int send_money = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_route);

        departure = getIntent().getStringExtra("dep");
        arrival = getIntent().getStringExtra("arr");

        TextView departure_view = (TextView) findViewById(R.id.departure_view);
        departure_view.setText(departure);
        TextView arrival_view = (TextView) findViewById(R.id.arrival_view);
        arrival_view.setText(arrival);

        backColorChange(departure_view, departure);
        backColorChange(arrival_view, arrival);

        ///
        TextView color_zone = (TextView) findViewById(R.id.color_zone);
        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ///////////////
        pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        editor2 = pref2.edit();
        //////////////


        g = new Graph(Integer.parseInt(departure), Integer.parseInt(arrival));
        try {
            list = readDataFromCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }

        route = (TextView) findViewById(R.id.route);


        least_time_button = (Button) findViewById(R.id.least_time_button);
        least_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                for (List<String> item : list) {
                    if (i == 0) {
                        i += 1;
                    }else {
                        g.input(Integer.valueOf(item.get(0)),Integer.valueOf(item.get(1)),Integer.valueOf(item.get(2)));
                        g.inputExtra(Integer.valueOf(item.get(0)),Integer.valueOf(item.get(1)), Integer.valueOf(item.get(2)), Integer.valueOf(item.get(3)), Integer.valueOf(item.get(4)));
                    }
                }

                String select = "????????????";
                route_ = makeRoute(g);
                g.e_algorithm();
                setRouteText(route, select, g.statistic_time, g.statistic_distance, g.statistic_money);
                setSendData(g.statistic_time, g.statistic_distance, g.statistic_money);
                g.statistic_time = 0;
                g.statistic_distance = 0;
                g.statistic_money = 0;
            }
        });
        least_distance_button = (Button) findViewById(R.id.least_distance_button);
        least_distance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                for (List<String> item : list) {
                    if (i == 0) {
                        i += 1;
                    }else {
                        g.input(Integer.valueOf(item.get(0)),Integer.valueOf(item.get(1)),Integer.valueOf(item.get(3)));
                        g.inputExtra(Integer.valueOf(item.get(0)),Integer.valueOf(item.get(1)), Integer.valueOf(item.get(2)), Integer.valueOf(item.get(3)), Integer.valueOf(item.get(4)));
                    }
                }
                //makeRoute(g);

                String select = "????????????";
                route_ = makeRoute(g);
                g.e_algorithm();
                setRouteText(route, select, g.statistic_time, g.statistic_distance, g.statistic_money);
                setSendData(g.statistic_time, g.statistic_distance, g.statistic_money);
                g.statistic_time = 0;
                g.statistic_distance = 0;
                g.statistic_money = 0;
            }
        });
        least_money_button = (Button) findViewById(R.id.least_money_button);
        least_money_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                for (List<String> item : list) {
                    if (i == 0) {
                        i += 1;
                    }else {
                        g.input(Integer.valueOf(item.get(0)),Integer.valueOf(item.get(1)),Integer.valueOf(item.get(4)));
                        g.inputExtra(Integer.valueOf(item.get(0)),Integer.valueOf(item.get(1)), Integer.valueOf(item.get(2)), Integer.valueOf(item.get(3)), Integer.valueOf(item.get(4)));
                    }
                }

                String select = "????????????";
                route_ = makeRoute(g);
                g.e_algorithm();
                setRouteText(route, select, g.statistic_time, g.statistic_distance, g.statistic_money);
                setSendData(g.statistic_time, g.statistic_distance, g.statistic_money);
                g.statistic_time = 0;
                g.statistic_distance = 0;
                g.statistic_money = 0;
            }
        });


        search_back_button = (ImageButton) findViewById(R.id.search_back_button);
        search_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        detail_button = (Button) findViewById(R.id.detail_button);
        detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route_ != "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Route_Algorithm.this);
                    builder.setTitle("?????? ??????");
                    builder.setMessage(route_);
                    builder.setPositiveButton("??????", null);
                    builder.create().show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Route_Algorithm.this);
                    builder.setTitle("????????? ?????????");
                    builder.setMessage("?????? ????????? ????????? ?????????!");
                    builder.setPositiveButton("??????", null);
                    builder.create().show();
                }
            }
        });

        add_statistic_button = (Button) findViewById(R.id.add_statistic_button);
        add_statistic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route_ != "") {
                    String temp_time = pref2.getString("TIME", "");
                    String temp_distance = pref2.getString("DISTANCE", "");
                    String temp_money = pref2.getString("MONEY", "");

                    send_time = send_time +  Integer.parseInt(temp_time);
                    send_distance = send_distance +  Integer.parseInt(temp_distance);
                    send_money = send_money +  Integer.parseInt(temp_money);

                    editor2.putString("TIME", Integer.toString(send_time));
                    editor2.putString("DISTANCE", Integer.toString(send_distance));
                    editor2.putString("MONEY", Integer.toString(send_money));
                    editor2.apply();


                    AlertDialog.Builder builder = new AlertDialog.Builder(Route_Algorithm.this);
                    builder.setTitle("??????");
                    builder.setMessage("????????? ???????????????!");
                    builder.setPositiveButton("??????", null);
                    builder.create().show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Route_Algorithm.this);
                    builder.setTitle("????????? ?????????");
                    builder.setMessage("?????? ????????? ????????? ?????????!");
                    builder.setPositiveButton("??????", null);
                    builder.create().show();
                }

            }
        });
    }
    public void setSendData(int t, int d, int m){
        this.send_time = t;
        this.send_distance = d;
        this.send_money = m;
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

    public void setRouteText(TextView routeT, String select, int t, int d, int m){
        switch (select){
            case "????????????":
                routeT.setText("[????????????: ????????????]" +
                        "\n????????? ??? ??????: " + g.m_route.size() +
                        "\n?????? ??????: " + invertTime(last_weight) +
                        "\n?????? ??????: " + d + "m" +
                        "\n??????: " + m + "???");
                break;
            case "????????????":
                routeT.setText("[????????????: ????????????]" +
                        "\n????????? ??? ??????: " + g.m_route.size() +
                        "\n?????? ??????: " + invertTime(t) +
                        "\n?????? ??????: " + last_weight + "m" +
                        "\n??????: " + m + "???");
                break;
            case "????????????":
                routeT.setText("[????????????: ????????????]" +
                        "\n????????? ??? ??????: " + g.m_route.size() +
                        "\n?????? ??????: " + invertTime(t) +
                        "\n?????? ??????: " + d + "m" +
                        "\n??????: " + last_weight + "???");
                break;
        }


    }
    public String invertTime(int time){
        int min = time / 60;
        int hour = min / 60;
        int sec = time % 60;
        if (min >= 60){
            min = min % 60;
        }

        if (hour != 0 && min != 0 && sec != 0){
            return hour + "?????? " + min + "??? " + sec + "???";
        }else if (hour != 0 && min != 0 && sec == 0) {
            return hour + "?????? " + min + "???";
        }else if (hour != 0 && min == 0 && sec == 0){
            return hour + "??????";
        }else if (hour != 0 && min == 0 && sec != 0){
            return hour + "?????? " + sec + "???";
        }else if (hour == 0 && min != 0 && sec != 0) {
            return min + "??? " + sec + "???";
        }else if (hour == 0 && min != 0 && sec == 0){
            return min + "???";
        }else if (hour == 0 && min == 0 && sec == 0){
            return null;
        }else if (hour == 0 && min == 0 && sec != 0){
            return sec + "???";
        }

        return null;
    }

    static class Graph{
        int num_station;
        int m_station; //?????? ??????
        int departures; //?????????
        int arrivals; //?????????

        int min_route = Integer.MAX_VALUE;  //??????????????? ?????????

        Stack<Integer> route = new Stack<>();
        Stack<Integer> saved_weight = new Stack<>(); //?????? ????????? ?????? ??????

        boolean visited[] = new boolean[1000];
        int[][] weight = new int[1000][1000];

        public Graph(int departures, int arrivals) {
            this.departures = departures;
            this.arrivals = arrivals;
        }

        public void input(int current, int next, int weight) {
            this.weight[current][next] = weight;
            this.weight[next][current] = weight;
        }



        private Stack<Integer> m_route = new Stack<>();
        private int m_weight = Integer.MAX_VALUE;
        public int getNumS(){
            return m_route.size();
        }

        public void algorithm(int current, int arrivals, Stack<Integer> route, int[][] weight, boolean[] visited, Stack<Integer> saved_weight) {
            visited[current] = true;
            route.push(current);

            if (current == arrivals) {
                if (route.size() <= min_route) {
                    min_route = route.size();
                    for (Integer i : route) {
                        System.out.print(i + "  ");
                    }
                    int result_weight = 0;
                    for (Integer w : saved_weight) {
                        result_weight += w;
                    }
                    if (result_weight <= m_weight){ //?????????
                        m_route.clear();
                        m_weight = result_weight;
                        for (Integer i : route) {
                            m_route.add(i);
                        }

                    }
                    System.out.println();
                    System.out.println("==??? ?????????: " + result_weight + "============="  + "\n==???????????? ??????=============");

                }
            }
            for (int i = 101; i < 904; i++) {
                if ((weight[current][i] != 0) && (visited[i] == false)) {
                    if (route.size() < min_route) {
                        addWeight(current, i);
                        algorithm(i, arrivals, route, weight, visited, saved_weight);
                        visited[i] = false;
                    }
                }
            }
            if (!saved_weight.isEmpty()) {
                saved_weight.pop();//
            }
            route.pop();

        }
        public void addWeight(int current, int i) {
            saved_weight.push(weight[current][i]);
        }

        int[][] e_time_weight = new int[1000][1000];
        int[][] e_distance_weight = new int[1000][1000];
        int[][] e_money_weight = new int[1000][1000];
        int statistic_time = 0;
        int statistic_distance = 0;
        int statistic_money = 0;

        public void inputExtra(int current, int next, int e_time, int e_distance, int e_money){
            e_time_weight[current][next] = e_time;
            e_time_weight[next][current] = e_time;
            e_distance_weight[current][next] = e_distance;
            e_distance_weight[next][current] = e_distance;
            e_money_weight[current][next] = e_money;
            e_money_weight[next][current] = e_money;
        }
        public void e_algorithm(){
           for (int i = 0; i < m_route.size()-1; i++){
                statistic_time = statistic_time + e_time_weight[m_route.elementAt(i)][m_route.elementAt(i+1)];
                statistic_distance = statistic_distance + e_distance_weight[m_route.elementAt(i)][m_route.elementAt(i+1)];
                statistic_money = statistic_money + e_money_weight[m_route.elementAt(i)][m_route.elementAt(i+1)];
            }
        }

    }


    public String makeRoute(Graph g){
        System.out.println("===========================================");
        g.algorithm(g.departures, g.arrivals, g.route, g.weight, g.visited, g.saved_weight);
        System.out.println(g.m_weight);
        last_weight = g.m_weight;
        g.m_weight = Integer.MAX_VALUE;

        String route_ = "";
        int first = 1;
        for (Integer I : g.m_route){
            if (first == 1){
                String temp = Integer.toString(I);
                route_ += temp;
                first += 1;
            }else {
                String temp = Integer.toString(I);
                temp = " > " + temp;
                route_ += temp;
            }
        }

        System.out.println("DONE");
        return route_;
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
}
