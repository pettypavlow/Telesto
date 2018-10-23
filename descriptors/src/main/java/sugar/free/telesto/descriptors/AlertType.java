package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum AlertType implements Parcelable {

    REMINDER_01,
    REMINDER_02,
    REMINDER_03,
    REMINDER_04,
    REMINDER_07,
    WARNING_31,
    WARNING_32,
    WARNING_33,
    WARNING_34,
    WARNING_36,
    WARNING_38,
    WARNING_39,
    MAINTENANCE_20,
    MAINTENANCE_21,
    MAINTENANCE_22,
    MAINTENANCE_23,
    MAINTENANCE_24,
    MAINTENANCE_25,
    MAINTENANCE_26,
    MAINTENANCE_27,
    MAINTENANCE_28,
    MAINTENANCE_29,
    MAINTENANCE_30,
    ERROR_6,
    ERROR_10,
    ERROR_13;

    public static final Creator<AlertType> CREATOR = new Creator<AlertType>() {
        @Override
        public AlertType createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public AlertType[] newArray(int size) {
            return new AlertType[size];
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
