package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class CannulaFilled extends GenericHistoryEntity {

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

    public static final Parcelable.Creator<CannulaFilled> CREATOR = new Parcelable.Creator<CannulaFilled>() {
        @Override
        public CannulaFilled createFromParcel(Parcel source) {
            source.readString();
            CannulaFilled cannulaFilled = new CannulaFilled();
            cannulaFilled.readFromParcel(source);
            return cannulaFilled;
        }

        @Override
        public CannulaFilled[] newArray(int size) {
            return new CannulaFilled[size];
        }
    };

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
