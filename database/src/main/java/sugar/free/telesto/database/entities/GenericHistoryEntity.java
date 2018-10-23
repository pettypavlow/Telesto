package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class GenericHistoryEntity implements Parcelable, Comparable<GenericHistoryEntity> {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long eventPosition;
    private String pumpSerialNumber;
    @ColumnInfo(index = true)
    private Date pumpCreationDate;
    @ColumnInfo(index = true)
    private Date dateReceived;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(eventPosition);
        dest.writeString(pumpSerialNumber);
        dest.writeLong(pumpCreationDate.getTime());
        dest.writeLong(dateReceived.getTime());
    }

    public void readFromParcel(Parcel in) {
        id = in.readLong();
        eventPosition = in.readLong();
        pumpSerialNumber = in.readString();
        pumpCreationDate = new Date(in.readLong());
        dateReceived = new Date(in.readLong());
    }

    public static final Creator<GenericHistoryEntity> CREATOR = new Creator<GenericHistoryEntity>() {
        @Override
        public GenericHistoryEntity createFromParcel(Parcel in) {
            GenericHistoryEntity genericHistoryEntity = null;
            switch (in.readString()) {
                case "PowerUp":
                    genericHistoryEntity = new PowerUp();
                    break;
                case "BolusDelivered":
                    genericHistoryEntity = new BolusDelivered();
                    break;
                case "BolusProgrammed":
                    genericHistoryEntity = new BolusProgrammed();
                    break;
                case "CannulaFilled":
                    genericHistoryEntity = new CannulaFilled();
                    break;
                case "SniffingDone":
                    genericHistoryEntity = new SniffingDone();
                    break;
                case "OccurrenceOfAlert":
                    genericHistoryEntity = new OccurrenceOfAlert();
                    break;
                case "OperatingModeChanged":
                    genericHistoryEntity = new OperatingModeChanged();
                    break;
                case "EndOfTBR":
                    genericHistoryEntity = new EndOfTBR();
                    break;
                case "TotalDailyDose":
                    genericHistoryEntity = new TotalDailyDose();
                    break;
                case "TubeFilled":
                    genericHistoryEntity = new TubeFilled();
                    break;
            }
            genericHistoryEntity.readFromParcel(in);
            return genericHistoryEntity;
        }

        @Override
        public GenericHistoryEntity[] newArray(int size) {
            return new GenericHistoryEntity[size];
        }
    };

    @Override
    public int compareTo(@NonNull GenericHistoryEntity o) {
        return (int) (eventPosition - o.eventPosition);
    }

    public long getId() {
        return this.id;
    }

    public long getEventPosition() {
        return this.eventPosition;
    }

    public String getPumpSerialNumber() {
        return this.pumpSerialNumber;
    }

    public Date getPumpCreationDate() {
        return this.pumpCreationDate;
    }

    public Date getDateReceived() {
        return this.dateReceived;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEventPosition(long eventPosition) {
        this.eventPosition = eventPosition;
    }

    public void setPumpSerialNumber(String pumpSerialNumber) {
        this.pumpSerialNumber = pumpSerialNumber;
    }

    public void setPumpCreationDate(Date pumpCreationDate) {
        this.pumpCreationDate = pumpCreationDate;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }
}
