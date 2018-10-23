package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum SymbolStatus implements Parcelable {

    FULL,
    LOW,
    EMPTY;

    public static final Creator<SymbolStatus> CREATOR = new Creator<SymbolStatus>() {
        @Override
        public SymbolStatus createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public SymbolStatus[] newArray(int size) {
            return new SymbolStatus[size];
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
