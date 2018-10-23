package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum AlertStatus implements Parcelable {

    ACTIVE,
    SNOOZED;

    public static final Creator<AlertStatus> CREATOR = new Creator<AlertStatus>() {
        @Override
        public AlertStatus createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public AlertStatus[] newArray(int size) {
            return new AlertStatus[size];
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
