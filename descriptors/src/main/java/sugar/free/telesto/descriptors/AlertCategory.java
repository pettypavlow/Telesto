package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum AlertCategory implements Parcelable {

    REMINDER,
    MAINTENANCE,
    WARNING,
    ERROR;

    public static final Creator<AlertCategory> CREATOR = new Creator<AlertCategory>() {
        @Override
        public AlertCategory createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public AlertCategory[] newArray(int size) {
            return new AlertCategory[size];
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
