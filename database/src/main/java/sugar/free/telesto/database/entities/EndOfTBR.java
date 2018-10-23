package sugar.free.telesto.database.entities;

import android.os.Parcel;

import java.sql.Time;
import java.util.Date;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class EndOfTBR extends GenericHistoryEntity {

    private Date startTime;
    private int percentage;
    private int duration;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeLong(startTime.getTime());
        dest.writeInt(percentage);
        dest.writeInt(duration);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        startTime = new Time(in.readLong());
        percentage = in.readInt();
        duration = in.readInt();
    }

    public static final Creator<EndOfTBR> CREATOR = new Creator<EndOfTBR>() {
        @Override
        public EndOfTBR createFromParcel(Parcel source) {
            source.readString();
            EndOfTBR endOfTBR = new EndOfTBR();
            endOfTBR.readFromParcel(source);
            return endOfTBR;
        }

        @Override
        public EndOfTBR[] newArray(int size) {
            return new EndOfTBR[size];
        }
    };

    public Date getStartTime() {
        return this.startTime;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
