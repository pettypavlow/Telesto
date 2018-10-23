package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum BatteryType implements Parcelable {

    ALKALI,
    LITHIUM,
    NI_MH;

    public static final Creator<BatteryType> CREATOR = new Creator<BatteryType>() {
        @Override
        public BatteryType createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public BatteryType[] newArray(int size) {
            return new BatteryType[size];
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
