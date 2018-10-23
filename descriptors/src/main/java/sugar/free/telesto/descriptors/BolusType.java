package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum BolusType implements Parcelable {

    STANDARD,
    EXTENDED,
    MULTIWAVE;

    public static final Creator<BolusType> CREATOR = new Creator<BolusType>() {
        @Override
        public BolusType createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public BolusType[] newArray(int size) {
            return new BolusType[size];
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
