package com.wdullaer.materialdatetimepicker.time;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.wdullaer.materialdatetimepicker.time.Timepoint.TYPE.HOUR;
import static com.wdullaer.materialdatetimepicker.time.Timepoint.TYPE.MINUTE;

/**
 * Simple utility class that represents a time in the day up to second precision
 * The time input is expected to use 24 hour mode.
 * Fields are modulo'd into their correct ranges.
 * It does not handle timezones.
 * <p>
 * Created by wdullaer on 13/10/15.
 */
@SuppressWarnings("WeakerAccess")
public class Timepoint implements Parcelable, Comparable<Timepoint> {
    public static final Parcelable.Creator<Timepoint> CREATOR
            = new Parcelable.Creator<Timepoint>() {
        public Timepoint createFromParcel(Parcel in) {
            return new Timepoint(in);
        }

        public Timepoint[] newArray(int size) {
            return new Timepoint[size];
        }
    };
    private int hour;
    private int minute;
    private int second;

    public Timepoint(Timepoint time) {
        this(time.hour, time.minute, time.second);
    }

    public Timepoint(@IntRange(from = 0, to = 23) int hour,
                     @IntRange(from = 0, to = 59) int minute,
                     @IntRange(from = 0, to = 59) int second) {
        this.hour = hour % 24;
        this.minute = minute % 60;
        this.second = second % 60;
    }

    public Timepoint(@IntRange(from = 0, to = 23) int hour,
                     @IntRange(from = 0, to = 59) int minute) {
        this(hour, minute, 0);
    }

    public Timepoint(@IntRange(from = 0, to = 23) int hour) {
        this(hour, 0);
    }

    public Timepoint(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }

    @IntRange(from = 0, to = 23)
    public int getHour() {
        return hour;
    }

    @IntRange(from = 0, to = 59)
    public int getMinute() {
        return minute;
    }

    @IntRange(from = 0, to = 59)
    public int getSecond() {
        return second;
    }

    public boolean isAM() {
        return hour < 12;
    }

    public boolean isPM() {
        return !isAM();
    }

    public void setAM() {
        if (hour >= 12) hour = hour % 12;
    }

    public void setPM() {
        if (hour < 12) hour = (hour + 12) % 24;
    }

    public void add(TYPE type, int value) {
        if (type == MINUTE) value *= 60;
        if (type == HOUR) value *= 3600;
        value += toSeconds();

        switch (type) {
            case SECOND:
                second = (value % 3600) % 60;
            case MINUTE:
                minute = (value % 3600) / 60;
            case HOUR:
                hour = (value / 3600) % 24;
        }
    }

    public int get(@NonNull TYPE type) {
        switch (type) {
            case SECOND:
                return getSecond();
            case MINUTE:
                return getMinute();
            case HOUR:
            default: // Makes the compiler happy
                return getHour();
        }
    }

    public int toSeconds() {
        return 3600 * hour + 60 * minute + second;
    }

    @Override
    public int hashCode() {
        return toSeconds();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timepoint timepoint = (Timepoint) o;

        return hashCode() == timepoint.hashCode();
    }

    public boolean equals(@Nullable Timepoint time, @NonNull TYPE resolution) {
        if (time == null) return false;
        boolean output = true;
        switch (resolution) {
            case SECOND:
                output = output && time.getSecond() == getSecond();
            case MINUTE:
                output = output && time.getMinute() == getMinute();
            case HOUR:
                output = output && time.getHour() == getHour();
        }
        return output;
    }

    @Override
    public int compareTo(@NonNull Timepoint t) {
        return hashCode() - t.hashCode();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(hour);
        out.writeInt(minute);
        out.writeInt(second);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "" + hour + "h " + minute + "m " + second + "s";
    }

    public enum TYPE {
        HOUR,
        MINUTE,
        SECOND
    }
}
