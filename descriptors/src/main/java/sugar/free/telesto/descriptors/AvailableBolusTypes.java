package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class AvailableBolusTypes implements Parcelable {

    public static final Creator<AvailableBolusTypes> CREATOR = new Creator<AvailableBolusTypes>() {
        @Override
        public AvailableBolusTypes createFromParcel(Parcel in) {
            return new AvailableBolusTypes(in);
        }

        @Override
        public AvailableBolusTypes[] newArray(int size) {
            return new AvailableBolusTypes[size];
        }
    };
    private boolean standardAvailable;
    private boolean extendedAvailable;
    private boolean multiwaveAvailable;

    protected AvailableBolusTypes(Parcel in) {
        standardAvailable = in.readByte() != 0;
        extendedAvailable = in.readByte() != 0;
        multiwaveAvailable = in.readByte() != 0;
    }

    public AvailableBolusTypes() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (standardAvailable ? 1 : 0));
        parcel.writeByte((byte) (extendedAvailable ? 1 : 0));
        parcel.writeByte((byte) (multiwaveAvailable ? 1 : 0));
    }

    public boolean isStandardAvailable() {
        return this.standardAvailable;
    }

    public void setStandardAvailable(boolean standardAvailable) {
        this.standardAvailable = standardAvailable;
    }

    public boolean isExtendedAvailable() {
        return this.extendedAvailable;
    }

    public void setExtendedAvailable(boolean extendedAvailable) {
        this.extendedAvailable = extendedAvailable;
    }

    public boolean isMultiwaveAvailable() {
        return this.multiwaveAvailable;
    }

    public void setMultiwaveAvailable(boolean multiwaveAvailable) {
        this.multiwaveAvailable = multiwaveAvailable;
    }

    public boolean isBolusTypeAvailable(BolusType bolusType) {
        switch (bolusType) {
            case STANDARD:
                return standardAvailable;
            case EXTENDED:
                return extendedAvailable;
            case MULTIWAVE:
                return multiwaveAvailable;
            default:
                return false;
        }
    }
}
