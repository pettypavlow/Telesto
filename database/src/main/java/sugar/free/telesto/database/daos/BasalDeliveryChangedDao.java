package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.BasalDeliveryChanged;

@Dao
public abstract class BasalDeliveryChangedDao extends GenericHistoryDao<BasalDeliveryChanged> {

    @Query("SELECT * FROM BasalDeliveryChanged")
    public abstract List<BasalDeliveryChanged> getAll();

    @Query("SELECT * FROM BasalDeliveryChanged WHERE pumpCreationDate > :creationDate")
    public abstract List<BasalDeliveryChanged> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM BasalDeliveryChanged WHERE dateReceived > :dateReceived")
    public abstract List<BasalDeliveryChanged> getAllSinceDateReceived(Date dateReceived);

}
