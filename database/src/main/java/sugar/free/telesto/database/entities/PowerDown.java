package sugar.free.telesto.database.entities;

import android.os.Parcel;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class PowerDown extends GenericHistoryEntity {

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        super.writeToParcel(dest, flags);
    }

    public static final Creator<PowerDown> CREATOR = new Creator<PowerDown>() {
        @Override
        public PowerDown createFromParcel(Parcel source) {
            source.readString();
            PowerDown powerDown = new PowerDown();
            powerDown.readFromParcel(source);
            return powerDown;
        }

        @Override
        public PowerDown[] newArray(int size) {
            return new PowerDown[size];
        }
    };
}
