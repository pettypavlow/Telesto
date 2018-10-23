package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class TotalDailyDose extends GenericHistoryEntity {

    private Date date;
    private double bolus;
    private double basal;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeLong(date.getTime());
        dest.writeDouble(bolus);
        dest.writeDouble(basal);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        date = new Date(in.readLong());
        bolus = in.readDouble();
        basal = in.readDouble();
        super.readFromParcel(in);
    }

    public static final Parcelable.Creator<TotalDailyDose> CREATOR = new Parcelable.Creator<TotalDailyDose>() {
        @Override
        public TotalDailyDose createFromParcel(Parcel source) {
            source.readString();
            TotalDailyDose totalDailyDose = new TotalDailyDose();
            totalDailyDose.readFromParcel(source);
            return totalDailyDose;
        }

        @Override
        public TotalDailyDose[] newArray(int size) {
            return new TotalDailyDose[size];
        }
    };

    public Date getDate() {
        return this.date;
    }

    public double getBolus() {
        return this.bolus;
    }

    public double getBasal() {
        return this.basal;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBolus(double bolus) {
        this.bolus = bolus;
    }

    public void setBasal(double basal) {
        this.basal = basal;
    }
}
