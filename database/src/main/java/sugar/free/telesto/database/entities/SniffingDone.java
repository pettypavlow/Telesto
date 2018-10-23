package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class SniffingDone extends GenericHistoryEntity {

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

    public static final Parcelable.Creator<SniffingDone> CREATOR = new Parcelable.Creator<SniffingDone>() {
        @Override
        public SniffingDone createFromParcel(Parcel source) {
            source.readString();
            SniffingDone sniffingDone = new SniffingDone();
            sniffingDone.readFromParcel(source);
            return sniffingDone;
        }

        @Override
        public SniffingDone[] newArray(int size) {
            return new SniffingDone[size];
        }
    };

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
