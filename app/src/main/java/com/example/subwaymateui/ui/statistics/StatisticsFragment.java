package com.example.subwaymateui.ui.statistics;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.subwaymateui.R;
import com.example.subwaymateui.databinding.FragmentStatisticsBinding;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;

    String time ="";
    String distance ="";
    String money ="";


    SharedPreferences pref2;
    SharedPreferences.Editor editor2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        //final TextView textView = binding.textNotifications;
        //statisticsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        final TextView statistics = binding.statistics;

        pref2 = getContext().getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        editor2 = pref2.edit();
        time = pref2.getString("TIME", "");
        distance = pref2.getString("DISTANCE", "");
        money = pref2.getString("MONEY", "");

        statistics.setText("누적 이동 시간: " + invertTime(Integer.parseInt(time)) +
                           "\n\n누적 이동 거리: " + distance + "m" +
                           "\n\n누적 소모 비용: " + money + "원");




        return root;
    }
    public String invertTime(int time){
        int min = time / 60;
        int hour = min / 60;
        int sec = time % 60;
        if (min >= 60){
            min = min % 60;
        }

        if (hour != 0 && min != 0 && sec != 0){
            return hour + "시간 " + min + "분 " + sec + "초";
        }else if (hour != 0 && min != 0 && sec == 0) {
            return hour + "시간 " + min + "분";
        }else if (hour != 0 && min == 0 && sec == 0){
            return hour + "시간";
        }else if (hour != 0 && min == 0 && sec != 0){
            return hour + "시간 " + sec + "초";
        }else if (hour == 0 && min != 0 && sec != 0) {
            return min + "분 " + sec + "초";
        }else if (hour == 0 && min != 0 && sec == 0){
            return min + "분";
        }else if (hour == 0 && min == 0 && sec == 0){
            return null;
        }else if (hour == 0 && min == 0 && sec != 0){
            return sec + "초";
        }

        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}