package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.OperatingModeChanged;

@Dao
public abstract class OperatingModeChangedDao extends GenericHistoryDao<OperatingModeChanged> {

    @Query("SELECT * FROM OperatingModeChanged")
    public abstract List<OperatingModeChanged> getAll();

    @Query("SELECT * FROM OperatingModeChanged WHERE pumpCreationDate > :creationDate")
    public abstract List<OperatingModeChanged> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM OperatingModeChanged WHERE dateReceived > :dateReceived")
    public abstract List<OperatingModeChanged> getAllSinceDateReceived(Date dateReceived);

}
