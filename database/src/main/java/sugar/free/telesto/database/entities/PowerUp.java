package sugar.free.telesto.database.entities;

import android.os.Parcel;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class PowerUp extends GenericHistoryEntity {

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        super.writeToParcel(dest, flags);
    }

    public static final Creator<PowerUp> CREATOR = new Creator<PowerUp>() {
        @Override
        public PowerUp createFromParcel(Parcel source) {
            source.readString();
            PowerUp powerUp = new PowerUp();
            powerUp.readFromParcel(source);
            return powerUp;
        }

        @Override
        public PowerUp[] newArray(int size) {
            return new PowerUp[size];
        }
    };
}
