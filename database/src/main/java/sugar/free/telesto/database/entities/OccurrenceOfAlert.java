package sugar.free.telesto.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import sugar.free.telesto.descriptors.AlertType;

@Entity(inheritSuperIndices = true)
public class OccurrenceOfAlert extends GenericHistoryEntity {

    private AlertType alertType;
    private int alertId;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        dest.writeParcelable(alertType, flags);
        dest.writeInt(alertId);
        super.writeToParcel(dest, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        alertType = in.readParcelable(AlertType.class.getClassLoader());
        alertId = in.readInt();
        super.readFromParcel(in);
    }

    public static final Parcelable.Creator<OccurrenceOfAlert> CREATOR = new Parcelable.Creator<OccurrenceOfAlert>() {
        @Override
        public OccurrenceOfAlert createFromParcel(Parcel source) {
            source.readString();
            OccurrenceOfAlert occurrenceOfAlert = new OccurrenceOfAlert();
            occurrenceOfAlert.readFromParcel(source);
            return occurrenceOfAlert;
        }

        @Override
        public OccurrenceOfAlert[] newArray(int size) {
            return new OccurrenceOfAlert[size];
        }
    };

    public AlertType getAlertType() {
        return this.alertType;
    }

    public int getAlertId() {
        return this.alertId;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }
}
