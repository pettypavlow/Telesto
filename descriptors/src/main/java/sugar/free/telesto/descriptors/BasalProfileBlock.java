package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class BasalProfileBlock implements Parcelable {

    private int duration;
    private double basalAmount;

    public BasalProfileBlock() {
    }

    protected BasalProfileBlock(Parcel in) {
        duration = in.readInt();
        basalAmount = in.readDouble();
    }

    public static final Creator<BasalProfileBlock> CREATOR = new Creator<BasalProfileBlock>() {
        @Override
        public BasalProfileBlock createFromParcel(Parcel in) {
            return new BasalProfileBlock(in);
        }

        @Override
        public BasalProfileBlock[] newArray(int size) {
            return new BasalProfileBlock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(duration);
        dest.writeDouble(basalAmount);
    }

    public int getDuration() {
        return this.duration;
    }

    public double getBasalAmount() {
        return this.basalAmount;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setBasalAmount(double basalAmount) {
        this.basalAmount = basalAmount;
    }
}
