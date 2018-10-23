package sugar.free.telesto.database.entities;

import android.os.Parcel;

import java.util.Date;

import androidx.room.Entity;
import sugar.free.telesto.descriptors.BolusType;

@Entity(inheritSuperIndices = true)
public class BasalDeliveryChanged extends GenericHistoryEntity {

    private double oldBasalRate;
    private double newBasalRate;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeDouble(oldBasalRate);
        dest.writeDouble(newBasalRate);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        oldBasalRate = in.readDouble();
        newBasalRate = in.readDouble();
        super.readFromParcel(in);
    }

    public static final Creator<BasalDeliveryChanged> CREATOR = new Creator<BasalDeliveryChanged>() {
        @Override
        public BasalDeliveryChanged createFromParcel(Parcel source) {
            source.readString();
            BasalDeliveryChanged basalDeliveryChanged = new BasalDeliveryChanged();
            basalDeliveryChanged.readFromParcel(source);
            return basalDeliveryChanged;
        }

        @Override
        public BasalDeliveryChanged[] newArray(int size) {
            return new BasalDeliveryChanged[size];
        }
    };

    public double getOldBasalRate() {
        return oldBasalRate;
    }

    public void setOldBasalRate(double oldBasalRate) {
        this.oldBasalRate = oldBasalRate;
    }

    public double getNewBasalRate() {
        return newBasalRate;
    }

    public void setNewBasalRate(double newBasalRate) {
        this.newBasalRate = newBasalRate;
    }
}
