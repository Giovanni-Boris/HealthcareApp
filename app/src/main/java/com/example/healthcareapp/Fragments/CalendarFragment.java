package com.example.healthcareapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.healthcareapp.Entity.Alarm;
import com.example.healthcareapp.R;
import com.example.healthcareapp.Room.Datasource;

import java.util.Calendar;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CalendarFragment extends Fragment {
    public final String TAG = "CalendarFragment";

    private CalendarView calendarView;
    private Button buttonCalendar;

    private int dayNumber;

    private Datasource datasource;
    public CalendarFragment() {
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        datasource = Datasource.newInstance(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendar);
        buttonCalendar = view.findViewById(R.id.buttonCalendar);

        calendarListener();
        buttonListener();

        return view;
    }

    private void calendarListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                dayNumber = selectedDate.get(Calendar.DAY_OF_MONTH);
                Log.d(TAG, "Día seleccionado: " + dayNumber);
            }
        });
    }

    private void buttonListener(){
        buttonCalendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                simpleInsert(dayNumber);
            }
        });
    }

    private void simpleInsert(int day) {
        Completable.fromAction(() -> {
                    Alarm a = new Alarm();
                    a.setDay(day);
                    datasource.alarmDAO().insertAlarm(a);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Insercion realizada");
                });
    }
}