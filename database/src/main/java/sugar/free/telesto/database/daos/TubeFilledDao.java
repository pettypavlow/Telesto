package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.TubeFilled;

@Dao
public abstract class TubeFilledDao extends GenericHistoryDao<TubeFilled> {

    @Query("SELECT * FROM TubeFilled")
    public abstract List<TubeFilled> getAll();

    @Query("SELECT * FROM TubeFilled WHERE pumpCreationDate > :creationDate")
    public abstract List<TubeFilled> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM TubeFilled WHERE dateReceived > :dateReceived")
    public abstract List<TubeFilled> getAllSinceDateReceived(Date dateReceived);

}
