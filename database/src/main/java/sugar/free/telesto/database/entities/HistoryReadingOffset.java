package sugar.free.telesto.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryReadingOffset {

    @PrimaryKey
    @NonNull
    private String pumpSerialNumber;
    private long lastEventPosition;

    @NonNull
    public String getPumpSerialNumber() {
        return this.pumpSerialNumber;
    }

    public long getLastEventPosition() {
        return lastEventPosition;
    }

    public void setPumpSerialNumber(@NonNull String pumpSerialNumber) {
        this.pumpSerialNumber = pumpSerialNumber;
    }

    public void setLastEventPosition(long lastEvenPosition) {
        this.lastEventPosition = lastEvenPosition;
    }
}
