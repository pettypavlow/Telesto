package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class ActiveBolus implements Parcelable {

    private int bolusID;
    private BolusType bolusType;
    private double initialAmount;
    private double remainingAmount;
    private int remainingDuration;

    protected ActiveBolus(Parcel in) {
        bolusID = in.readInt();
        bolusType = in.readParcelable(BolusType.class.getClassLoader());
        initialAmount = in.readDouble();
        remainingAmount = in.readDouble();
        remainingDuration = in.readInt();
    }

    public static final Creator<ActiveBolus> CREATOR = new Creator<ActiveBolus>() {
        @Override
        public ActiveBolus createFromParcel(Parcel in) {
            return new ActiveBolus(in);
        }

        @Override
        public ActiveBolus[] newArray(int size) {
            return new ActiveBolus[size];
        }
    };

    public ActiveBolus() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(bolusID);
        parcel.writeParcelable(bolusType, i);
        parcel.writeDouble(initialAmount);
        parcel.writeDouble(remainingAmount);
        parcel.writeInt(remainingDuration);
    }

    public int getBolusID() {
        return this.bolusID;
    }

    public BolusType getBolusType() {
        return this.bolusType;
    }

    public double getInitialAmount() {
        return this.initialAmount;
    }

    public double getRemainingAmount() {
        return this.remainingAmount;
    }

    public int getRemainingDuration() {
        return this.remainingDuration;
    }

    public void setBolusID(int bolusID) {
        this.bolusID = bolusID;
    }

    public void setBolusType(BolusType bolusType) {
        this.bolusType = bolusType;
    }

    public void setInitialAmount(double initialAmount) {
        this.initialAmount = initialAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public void setRemainingDuration(int remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ActiveBolus)) return false;
        final ActiveBolus other = (ActiveBolus) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getBolusID() != other.getBolusID()) return false;
        final Object this$bolusType = this.getBolusType();
        final Object other$bolusType = other.getBolusType();
        if (this$bolusType == null ? other$bolusType != null : !this$bolusType.equals(other$bolusType))
            return false;
        if (Double.compare(this.getInitialAmount(), other.getInitialAmount()) != 0) return false;
        if (Double.compare(this.getRemainingAmount(), other.getRemainingAmount()) != 0)
            return false;
        if (this.getRemainingDuration() != other.getRemainingDuration()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getBolusID();
        final Object $bolusType = this.getBolusType();
        result = result * PRIME + ($bolusType == null ? 43 : $bolusType.hashCode());
        final long $initialAmount = Double.doubleToLongBits(this.getInitialAmount());
        result = result * PRIME + (int) ($initialAmount >>> 32 ^ $initialAmount);
        final long $remainingAmount = Double.doubleToLongBits(this.getRemainingAmount());
        result = result * PRIME + (int) ($remainingAmount >>> 32 ^ $remainingAmount);
        result = result * PRIME + this.getRemainingDuration();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActiveBolus;
    }
}
