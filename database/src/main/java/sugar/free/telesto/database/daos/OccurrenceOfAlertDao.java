package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.OccurrenceOfAlert;

@Dao
public abstract class OccurrenceOfAlertDao extends GenericHistoryDao<OccurrenceOfAlert> {

    @Query("SELECT * FROM OccurrenceOfAlert")
    public abstract List<OccurrenceOfAlert> getAll();

    @Query("SELECT * FROM OccurrenceOfAlert WHERE pumpCreationDate > :creationDate")
    public abstract List<OccurrenceOfAlert> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM OccurrenceOfAlert WHERE dateReceived > :dateReceived")
    public abstract List<OccurrenceOfAlert> getAllSinceDateReceived(Date dateReceived);

}
