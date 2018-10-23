package sugar.free.telesto.database.entities;

import android.os.Parcel;

import androidx.room.Entity;
import sugar.free.telesto.descriptors.BolusType;

@Entity(inheritSuperIndices = true)
public class BolusProgrammed extends GenericHistoryEntity {

    private BolusType bolusType;
    private double immediateAmount;
    private double extendedAmount;
    private int duration;
    private int bolusId;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeParcelable(bolusType, flags);
        dest.writeDouble(immediateAmount);
        dest.writeDouble(extendedAmount);
        dest.writeInt(bolusId);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        bolusType = in.readParcelable(BolusType.class.getClassLoader());
        immediateAmount = in.readDouble();
        extendedAmount = in.readDouble();
        duration = in.readInt();
        bolusId = in.readInt();
        super.readFromParcel(in);
    }

    public static final Creator<BolusProgrammed> CREATOR = new Creator<BolusProgrammed>() {
        @Override
        public BolusProgrammed createFromParcel(Parcel source) {
            source.readString();
            BolusProgrammed bolusProgrammed = new BolusProgrammed();
            bolusProgrammed.readFromParcel(source);
            return bolusProgrammed;
        }

        @Override
        public BolusProgrammed[] newArray(int size) {
            return new BolusProgrammed[size];
        }
    };

    public BolusType getBolusType() {
        return this.bolusType;
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
