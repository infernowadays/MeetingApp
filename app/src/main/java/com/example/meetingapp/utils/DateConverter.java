package com.example.meetingapp.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;

import com.example.meetingapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateConverter {
    private final static String pattern = "dd MMMM";
    private final static String serverPattern = "YYYY-MM-dd";

    public static String getDate(int year, int month, int day) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale("RU"));
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return sdf.format(calendar.getTime());
    }

    public static Date getDateFromString(String dateString) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat(pattern);
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void showDatePickerDialog(Date date, Context context, DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                R.style.DialogTheme,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    public static String getStringDateFromDate(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getStringDateFromDateForServer(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(serverPattern);
        return dateFormat.format(date);
    }


    public static String getTodayDateString() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    public static String getTomorrowDateString() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);

        return dateFormat.format(calendar.getTime());
    }

    public static String getCalendarDateFormat(int year, int month, int day) {
        String strMonth = String.valueOf(month + 1);
        if (month < 10) {
            strMonth = "0" + (month + 1);
        }

        String strDay = String.valueOf(day);
        if (day < 10) {
            strDay = "0" + day;
        }

        return year + "-" + strMonth + "-" + strDay;
    }
}
