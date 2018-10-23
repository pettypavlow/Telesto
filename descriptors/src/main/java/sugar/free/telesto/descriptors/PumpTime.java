package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class PumpTime implements Parcelable {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    protected PumpTime(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }

    public static final Creator<PumpTime> CREATOR = new Creator<PumpTime>() {
        @Override
        public PumpTime createFromParcel(Parcel in) {
            return new PumpTime(in);
        }

        @Override
        public PumpTime[] newArray(int size) {
            return new PumpTime[size];
        }
    };

    public PumpTime() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeInt(second);
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
