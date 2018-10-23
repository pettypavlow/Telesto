package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.CannulaFilled;

@Dao
public abstract class CannulaFilledDao extends GenericHistoryDao<CannulaFilled> {

    @Query("SELECT * FROM CannulaFilled")
    public abstract List<CannulaFilled> getAll();

    @Query("SELECT * FROM CannulaFilled WHERE pumpCreationDate > :creationDate")
    public abstract List<CannulaFilled> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM CannulaFilled WHERE dateReceived > :dateReceived")
    public abstract List<CannulaFilled> getAllSinceDateReceived(Date dateReceived);

}
