package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum CartridgeType implements Parcelable {

    PREFILLED,
    SELF_FILLED;

    public static final Creator<CartridgeType> CREATOR = new Creator<CartridgeType>() {
        @Override
        public CartridgeType createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public CartridgeType[] newArray(int size) {
            return new CartridgeType[size];
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
