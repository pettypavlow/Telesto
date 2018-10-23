package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.BolusDelivered;

@Dao
public abstract class BolusDeliveredDao extends GenericHistoryDao<BolusDelivered> {

    @Query("SELECT * FROM BolusDelivered")
    public abstract List<BolusDelivered> getAll();

    @Query("SELECT * FROM BolusDelivered WHERE pumpCreationDate > :creationDate")
    public abstract List<BolusDelivered> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM BolusDelivered WHERE dateReceived > :dateReceived")
    public abstract List<BolusDelivered> getAllSinceDateReceived(Date dateReceived);

}
