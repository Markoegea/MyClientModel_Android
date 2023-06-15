package com.kingmarco.myclientmodel.POJOs;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimestampDeserializer {
    private long seconds;
    private int nanoseconds;

    public static TimestampDeserializer generateNewTimestamp(){
        Timestamp timestamp = Timestamp.now();
        return new TimestampDeserializer(timestamp.getSeconds(), timestamp.getNanoseconds());
    }

    public TimestampDeserializer() {
    }

    public TimestampDeserializer(long seconds, int nanoseconds) {
        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public int getNanoseconds() {
        return nanoseconds;
    }

    @NonNull
    @Override
    public String toString() {
        if (seconds == 0F || nanoseconds == 0 ){
            return "No Exist";
        }
        Timestamp timestamp = new Timestamp(seconds,nanoseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        return sdf.format(timestamp.toDate());
    }
}
