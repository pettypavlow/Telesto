package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.room.Entity;
import sugar.free.telesto.descriptors.BolusType;

@Entity(inheritSuperIndices = true)
public class BolusDelivered extends GenericHistoryEntity {

    private BolusType bolusType;
    private Date startTime;
    private double immediateAmount;
    private double extendedAmount;
    private int duration;
    private int bolusId;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeParcelable(bolusType, flags);
        dest.writeLong(startTime.getTime());
        dest.writeDouble(immediateAmount);
        dest.writeDouble(extendedAmount);
        dest.writeInt(duration);
        dest.writeInt(bolusId);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        bolusType = in.readParcelable(BolusType.class.getClassLoader());
        startTime = new Date(in.readLong());
        immediateAmount = in.readDouble();
        extendedAmount = in.readDouble();
        duration = in.readInt();
        bolusId = in.readInt();
        super.readFromParcel(in);
    }

    public static final Parcelable.Creator<BolusDelivered> CREATOR = new Parcelable.Creator<BolusDelivered>() {
        @Override
        public BolusDelivered createFromParcel(Parcel source) {
            source.readString();
            BolusDelivered bolusDelivered = new BolusDelivered();
            bolusDelivered.readFromParcel(source);
            return bolusDelivered;
        }

        @Override
        public BolusDelivered[] newArray(int size) {
            return new BolusDelivered[size];
        }
    };

    public BolusType getBolusType() {
        return this.bolusType;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public double getImmediateAmount() {
        return this.immediateAmount;
    }

    public double getExtendedAmount() {
        return this.extendedAmount;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getBolusId() {
        return this.bolusId;
    }

    public void setBolusType(BolusType bolusType) {
        this.bolusType = bolusType;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setImmediateAmount(double immediateAmount) {
        this.immediateAmount = immediateAmount;
    }

    public void setExtendedAmount(double extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setBolusId(int bolusId) {
        this.bolusId = bolusId;
    }
}
