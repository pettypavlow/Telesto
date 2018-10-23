package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.PowerDown;

@Dao
public abstract class PowerDownDao extends GenericHistoryDao<PowerDown> {

    @Query("SELECT * FROM PowerDown")
    public abstract List<PowerDown> getAll();

    @Query("SELECT * FROM PowerDown WHERE pumpCreationDate > :creationDate")
    public abstract List<PowerDown> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM PowerDown WHERE dateReceived > :dateReceived")
    public abstract List<PowerDown> getAllSinceDateReceived(Date dateReceived);

}
