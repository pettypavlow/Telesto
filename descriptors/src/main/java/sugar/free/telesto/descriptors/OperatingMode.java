package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum OperatingMode implements Parcelable {

    STARTED,
    STOPPED,
    PAUSED;

    public static final Creator<OperatingMode> CREATOR = new Creator<OperatingMode>() {
        @Override
        public OperatingMode createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public OperatingMode[] newArray(int size) {
            return new OperatingMode[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
