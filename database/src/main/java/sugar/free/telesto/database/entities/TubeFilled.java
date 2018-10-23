package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class TubeFilled extends GenericHistoryEntity {

    private double amount;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeDouble(amount);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        amount = in.readDouble();
        super.readFromParcel(in);
    }

    public static final Parcelable.Creator<TubeFilled> CREATOR = new Parcelable.Creator<TubeFilled>() {
        @Override
        public TubeFilled createFromParcel(Parcel source) {
            source.readString();
            TubeFilled tubeFilled = new TubeFilled();
            tubeFilled.readFromParcel(source);
            return tubeFilled;
        }

        @Override
        public TubeFilled[] newArray(int size) {
            return new TubeFilled[size];
        }
    };

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
