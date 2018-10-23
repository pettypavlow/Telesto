package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum BasalProfile implements Parcelable {

    PROFILE_1,
    PROFILE_2,
    PROFILE_3,
    PROFILE_4,
    PROFILE_5;

    public static final Creator<BasalProfile> CREATOR = new Creator<BasalProfile>() {
        @Override
        public BasalProfile createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public BasalProfile[] newArray(int size) {
            return new BasalProfile[size];
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
