package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class ActiveTBR implements Parcelable {

    private int percentage;
    private int remainingDuration;
    private int initialDuration;

    protected ActiveTBR(Parcel in) {
        percentage = in.readInt();
        remainingDuration = in.readInt();
        initialDuration = in.readInt();
    }

    public static final Creator<ActiveTBR> CREATOR = new Creator<ActiveTBR>() {
        @Override
        public ActiveTBR createFromParcel(Parcel in) {
            return new ActiveTBR(in);
        }

        @Override
        public ActiveTBR[] newArray(int size) {
            return new ActiveTBR[size];
        }
    };

    public ActiveTBR() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(percentage);
        parcel.writeInt(remainingDuration);
        parcel.writeInt(initialDuration);
    }

    public int getPercentage() {
        return this.percentage;
    }

    public int getRemainingDuration() {
        return this.remainingDuration;
    }

    public int getInitialDuration() {
        return this.initialDuration;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setRemainingDuration(int remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    public void setInitialDuration(int initialDuration) {
        this.initialDuration = initialDuration;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ActiveTBR)) return false;
        final ActiveTBR other = (ActiveTBR) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getPercentage() != other.getPercentage()) return false;
        if (this.getRemainingDuration() != other.getRemainingDuration()) return false;
        if (this.getInitialDuration() != other.getInitialDuration()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getPercentage();
        result = result * PRIME + this.getRemainingDuration();
        result = result * PRIME + this.getInitialDuration();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActiveTBR;
    }
}
