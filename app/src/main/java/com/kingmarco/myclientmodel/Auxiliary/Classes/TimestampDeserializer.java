package com.kingmarco.myclientmodel.Auxiliary.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimestampDeserializer implements Parcelable {
    private long seconds;
    private int nanoseconds;
    private String dateFormat;

    public static final Parcelable.Creator<TimestampDeserializer> CREATOR = new Parcelable.Creator<TimestampDeserializer>() {
        @Override
        public TimestampDeserializer createFromParcel(Parcel parcel) {
            return new TimestampDeserializer(parcel);
        }

        @Override
        public TimestampDeserializer[] newArray(int i) {
            return new TimestampDeserializer[i];
        }
    };

    public static TimestampDeserializer generateNewTimestamp(){
        Timestamp timestamp = Timestamp.now();
        return new TimestampDeserializer(timestamp.getSeconds(), timestamp.getNanoseconds());
    }


    public TimestampDeserializer() {
    }

    protected TimestampDeserializer(Parcel in){
        this.seconds = in.readLong();
        this.nanoseconds = in.readInt();
        this.dateFormat = in.readString();
    }
    public TimestampDeserializer(long seconds, int nanoseconds) {
        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
        Timestamp timestamp = new Timestamp(seconds,nanoseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        this.dateFormat = sdf.format(timestamp.toDate());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(this.seconds);
        dest.writeInt(this.nanoseconds);
        dest.writeString(this.dateFormat);
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
        if (dateFormat == null){
            if (seconds == 0F || nanoseconds == 0 ){
                return "No Exist";
            }
            Timestamp timestamp = new Timestamp(seconds,nanoseconds);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            this.dateFormat = sdf.format(timestamp.toDate());
        }
        return dateFormat;
    }
}
