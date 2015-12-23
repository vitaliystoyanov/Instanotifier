package com.stoyanov.developer.instanotifier.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeTextView extends TextView {

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTime(String unixTimestamp) {
        Long timestamp = Long.parseLong(unixTimestamp);
        super.setText(getDate(timestamp));
    }

    private String getDate(long timestamp){
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
