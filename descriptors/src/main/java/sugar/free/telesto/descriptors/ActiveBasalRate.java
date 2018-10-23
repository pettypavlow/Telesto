package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class ActiveBasalRate implements Parcelable {

    private BasalProfile activeBasalProfile;
    private String activeBasalProfileName;
    private double activeBasalRate;

    protected ActiveBasalRate(Parcel in) {
        activeBasalProfile = in.readParcelable(BasalProfile.class.getClassLoader());
        activeBasalProfileName = in.readString();
        activeBasalRate = in.readDouble();
    }

    public static final Creator<ActiveBasalRate> CREATOR = new Creator<ActiveBasalRate>() {
        @Override
        public ActiveBasalRate createFromParcel(Parcel in) {
            return new ActiveBasalRate(in);
        }

        @Override
        public ActiveBasalRate[] newArray(int size) {
            return new ActiveBasalRate[size];
        }
    };

    public ActiveBasalRate() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(activeBasalProfile, i);
        parcel.writeString(activeBasalProfileName);
        parcel.writeDouble(activeBasalRate);
    }

    public BasalProfile getActiveBasalProfile() {
        return this.activeBasalProfile;
    }

    public String getActiveBasalProfileName() {
        return this.activeBasalProfileName;
    }

    public double getActiveBasalRate() {
        return this.activeBasalRate;
    }

    public void setActiveBasalProfile(BasalProfile activeBasalProfile) {
        this.activeBasalProfile = activeBasalProfile;
    }

    public void setActiveBasalProfileName(String activeBasalProfileName) {
        this.activeBasalProfileName = activeBasalProfileName;
    }

    public void setActiveBasalRate(double activeBasalRate) {
        this.activeBasalRate = activeBasalRate;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ActiveBasalRate)) return false;
        final ActiveBasalRate other = (ActiveBasalRate) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$activeBasalProfile = this.getActiveBasalProfile();
        final Object other$activeBasalProfile = other.getActiveBasalProfile();
        if (this$activeBasalProfile == null ? other$activeBasalProfile != null : !this$activeBasalProfile.equals(other$activeBasalProfile))
            return false;
        final Object this$activeBasalProfileName = this.getActiveBasalProfileName();
        final Object other$activeBasalProfileName = other.getActiveBasalProfileName();
        if (this$activeBasalProfileName == null ? other$activeBasalProfileName != null : !this$activeBasalProfileName.equals(other$activeBasalProfileName))
            return false;
        if (Double.compare(this.getActiveBasalRate(), other.getActiveBasalRate()) != 0)
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $activeBasalProfile = this.getActiveBasalProfile();
        result = result * PRIME + ($activeBasalProfile == null ? 43 : $activeBasalProfile.hashCode());
        final Object $activeBasalProfileName = this.getActiveBasalProfileName();
        result = result * PRIME + ($activeBasalProfileName == null ? 43 : $activeBasalProfileName.hashCode());
        final long $activeBasalRate = Double.doubleToLongBits(this.getActiveBasalRate());
        result = result * PRIME + (int) ($activeBasalRate >>> 32 ^ $activeBasalRate);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActiveBasalRate;
    }
}
