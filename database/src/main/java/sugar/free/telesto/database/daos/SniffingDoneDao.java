package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.SniffingDone;

@Dao
public abstract class SniffingDoneDao extends GenericHistoryDao<SniffingDone> {

    @Query("SELECT * FROM SniffingDone")
    public abstract List<SniffingDone> getAll();

    @Query("SELECT * FROM SniffingDone WHERE pumpCreationDate > :creationDate")
    public abstract List<SniffingDone> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM SniffingDone WHERE dateReceived > :dateReceived")
    public abstract List<SniffingDone> getAllSinceDateReceived(Date dateReceived);

}
