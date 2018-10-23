package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class TotalDailyDose implements Parcelable {

    private double bolus;
    private double basal;
    private double bolusAndBasal;

    protected TotalDailyDose(Parcel in) {
        bolus = in.readDouble();
        basal = in.readDouble();
        bolusAndBasal = in.readDouble();
    }

    public static final Creator<TotalDailyDose> CREATOR = new Creator<TotalDailyDose>() {
        @Override
        public TotalDailyDose createFromParcel(Parcel in) {
            return new TotalDailyDose(in);
        }

        @Override
        public TotalDailyDose[] newArray(int size) {
            return new TotalDailyDose[size];
        }
    };

    public TotalDailyDose() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(bolus);
        parcel.writeDouble(basal);
        parcel.writeDouble(bolusAndBasal);
    }

    public double getBolus() {
        return this.bolus;
    }

    public double getBasal() {
        return this.basal;
    }

    public double getBolusAndBasal() {
        return this.bolusAndBasal;
    }

    public void setBolus(double bolus) {
        this.bolus = bolus;
    }

    public void setBasal(double basal) {
        this.basal = basal;
    }

    public void setBolusAndBasal(double bolusAndBasal) {
        this.bolusAndBasal = bolusAndBasal;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof TotalDailyDose)) return false;
        final TotalDailyDose other = (TotalDailyDose) o;
        if (!other.canEqual((Object) this)) return false;
        if (Double.compare(this.getBolus(), other.getBolus()) != 0) return false;
        if (Double.compare(this.getBasal(), other.getBasal()) != 0) return false;
        if (Double.compare(this.getBolusAndBasal(), other.getBolusAndBasal()) != 0) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $bolus = Double.doubleToLongBits(this.getBolus());
        result = result * PRIME + (int) ($bolus >>> 32 ^ $bolus);
        final long $basal = Double.doubleToLongBits(this.getBasal());
        result = result * PRIME + (int) ($basal >>> 32 ^ $basal);
        final long $bolusAndBasal = Double.doubleToLongBits(this.getBolusAndBasal());
        result = result * PRIME + (int) ($bolusAndBasal >>> 32 ^ $bolusAndBasal);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TotalDailyDose;
    }
}
