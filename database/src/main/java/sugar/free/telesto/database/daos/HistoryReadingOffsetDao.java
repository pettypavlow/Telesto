package sugar.free.telesto.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import sugar.free.telesto.database.entities.HistoryReadingOffset;

@Dao
public abstract class HistoryReadingOffsetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(HistoryReadingOffset historyReadingOffset);

    @Query("SELECT * FROM HistoryReadingOffset WHERE pumpSerialNumber = :serialNumber")
    public abstract HistoryReadingOffset getBySerialNumber(String serialNumber);

}
