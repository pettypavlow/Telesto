package sugar.free.telesto.database.entities;

import android.os.Parcel;

import java.sql.Time;
import java.util.Date;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class StartOfTBR extends GenericHistoryEntity {

    private int percentage;
    private int duration;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeInt(percentage);
        dest.writeInt(duration);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        percentage = in.readInt();
        duration = in.readInt();
    }

    public static final Creator<StartOfTBR> CREATOR = new Creator<StartOfTBR>() {
        @Override
        public StartOfTBR createFromParcel(Parcel source) {
            source.readString();
            StartOfTBR startOfTBR = new StartOfTBR();
            startOfTBR.readFromParcel(source);
            return startOfTBR;
        }

        @Override
        public StartOfTBR[] newArray(int size) {
            return new StartOfTBR[size];
        }
    };

    public int getPercentage() {
        return this.percentage;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
