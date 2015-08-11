package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 20.04.15.
 */
public class DateAndPlace extends Fragment {
    private static final String TAG = "logs";

    //for global variables
    CurrentPlaceDate appState;

    //Buttons for setting if time and date
    private Button btnSelectBDate,btnSelectBTime;
    private Button btnSelectEDate,btnSelectETime;
    private Button setDateTime;
    static private TextView bDate,bTime;
    static private TextView eDate,eTime;
    static String beginDate,beginTime,beginComplete;
    static String endDate,endTime,endComplete;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm");


    //begin end end param

           static int bHour,eHour;
           static int bMinute,eMinute;
           static int bYear,eYear;
           static int bMonth,eMonth;
           static int bDay,eDay;
    String begin, end;




    // variables to save user selected date and time
    public  int year,month,day,hour,minute;


    // declare  the variables to show the date and time whenTime and Date Picker Dialog first appears
    private int mYear, mMonth, mDay,mHour,mMinute;





    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        appState = ((CurrentPlaceDate)myContext.getApplicationContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        Log.d(TAG, "love is one");
        View v = inflater.inflate(R.layout.table_time_input, null);
        // get the references of buttons
        btnSelectBDate = (Button) v.findViewById(R.id.buttonSelectBeginDate);
        btnSelectBTime = (Button) v.findViewById(R.id.buttonSelectBeginTime);
        btnSelectEDate = (Button) v.findViewById(R.id.buttonSelectEndDate);
        btnSelectETime = (Button) v.findViewById(R.id.buttonSelectEndTime);

        bDate = (TextView)v.findViewById(R.id.bDate);
        bTime = (TextView)v.findViewById(R.id.bTime);
        eDate = (TextView)v.findViewById(R.id.eDate);
        eTime = (TextView)v.findViewById(R.id.eTime);

        setDateTime = (Button)v.findViewById(R.id.setTimeDate);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(appState.getMin_time()*1000L);
        Log.d(TAG, "love of begin is" + appState.getMin_time());
        Log.d(TAG,"love of end is" + appState.getMax_time());
        bYear = calendar.get(Calendar.YEAR);
        bMonth = calendar.get(Calendar.MONTH);
        bDay = calendar.get(Calendar.DAY_OF_MONTH);
        beginDate = String.valueOf(bYear)+"/"+String.valueOf(bMonth+1)+"/"+String.valueOf(bDay);
        bDate.setText(beginDate);
        bHour = calendar.get(Calendar.HOUR_OF_DAY);
        bMinute = calendar.get(Calendar.MINUTE);
        beginTime = String.valueOf(bHour)+":"+String.valueOf(bMinute);
        bTime.setText(beginTime);


        calendar.setTimeInMillis(appState.getMax_time()*1000L);

        eYear = calendar.get(Calendar.YEAR);
        eMonth = calendar.get(Calendar.MONTH);
        eDay = calendar.get(Calendar.DAY_OF_MONTH);
        endDate = String.valueOf(eYear)+"/"+String.valueOf(eMonth+1)+"/"+String.valueOf(eDay);
        eDate.setText(endDate);
        eHour = calendar.get(Calendar.HOUR_OF_DAY);
        eMinute = calendar.get(Calendar.MINUTE);
        endTime = String.valueOf(eHour)+":"+String.valueOf(eMinute);
        eTime.setText(endTime);



        // создаем обработчик нажатия
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Меняем текст в TextView (tvOut)
                setDateTime.setText("ОК");
                begin = beginDate + " " + beginTime;
                end = endDate + " " + endTime;
                try {
                    Date dateB = formatter.parse(begin);
                    Date dateE = formatter.parse(end);
                    appState.setMax_time(dateE.getTime()/1000L);
                    appState.setMin_time(dateB.getTime()/1000L);
                    Log.d(TAG,"new love is "+dateB.getTime()/1000L);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        // присвоим обработчик кнопке OK (btnOk)
        setDateTime.setOnClickListener(oclBtnOk);


return v;
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int bYear = c.get(Calendar.YEAR);
            int bMonth = c.get(Calendar.MONTH);
            int bDay = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, bYear, bMonth, bDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            bYear = year;
            bMonth = month;
            bDay = day;
            beginDate = String.valueOf(bYear)+"/"+String.valueOf(bMonth+1)+"/"+String.valueOf(bDay);
            bDate.setText(beginDate);

        }
    }
    public static class TimePickerFragment extends DialogFragment
            implements android.app.TimePickerDialog.OnTimeSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
Log.d(TAG,"hout of love is "+hour);
            // Create a new instance of TimePickerDialog and return it
            return new android.app.TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                bHour=hour;
                bMinute=minute;
            beginTime = String.valueOf(bHour)+":"+String.valueOf(bMinute);
            bTime.setText(beginTime);
        }
    }

    public static class DateEndPickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int bYear = c.get(Calendar.YEAR);
            int bMonth = c.get(Calendar.MONTH);
            int bDay = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, bYear, bMonth, bDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            eYear = year;
            eMonth = month;
            eDay = day;
            endDate = String.valueOf(eYear)+"/"+String.valueOf(eMonth+1)+"/"+String.valueOf(eDay);
            eDate.setText(endDate);

        }
    }
    public static class TimeEndPickerFragment extends DialogFragment
            implements android.app.TimePickerDialog.OnTimeSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            Log.d(TAG,"hout of love is "+hour);
            // Create a new instance of TimePickerDialog and return it
            return new android.app.TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            eHour=hour;
            eMinute=minute;
            endTime = String.valueOf(eHour)+":"+String.valueOf(eMinute);
            eTime.setText(endTime);
        }
    }

}
