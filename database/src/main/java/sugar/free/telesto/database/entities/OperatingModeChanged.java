package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import sugar.free.telesto.descriptors.OperatingMode;

@Entity(inheritSuperIndices = true)
public class OperatingModeChanged extends GenericHistoryEntity {

    private OperatingMode oldValue;
    private OperatingMode newValue;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeParcelable(oldValue, flags);
        dest.writeParcelable(newValue, flags);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        oldValue = in.readParcelable(OperatingMode.class.getClassLoader());
        newValue = in.readParcelable(OperatingMode.class.getClassLoader());
        super.readFromParcel(in);
    }

    public static final Parcelable.Creator<OperatingModeChanged> CREATOR = new Parcelable.Creator<OperatingModeChanged>() {
        @Override
        public OperatingModeChanged createFromParcel(Parcel source) {
            source.readString();
            OperatingModeChanged operatingModeChanged = new OperatingModeChanged();
            operatingModeChanged.readFromParcel(source);
            return operatingModeChanged;
        }

        @Override
        public OperatingModeChanged[] newArray(int size) {
            return new OperatingModeChanged[size];
        }
    };

    public OperatingMode getOldValue() {
        return this.oldValue;
    }

    public OperatingMode getNewValue() {
        return this.newValue;
    }

    public void setOldValue(OperatingMode oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(OperatingMode newValue) {
        this.newValue = newValue;
    }
}
