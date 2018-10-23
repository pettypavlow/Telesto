package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.BolusProgrammed;

@Dao
public abstract class BolusProgrammedDao extends GenericHistoryDao<BolusProgrammed> {

    @Query("SELECT * FROM BolusProgrammed")
    public abstract List<BolusProgrammed> getAll();

    @Query("SELECT * FROM BolusProgrammed WHERE pumpCreationDate > :creationDate")
    public abstract List<BolusProgrammed> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM BolusProgrammed WHERE dateReceived > :dateReceived")
    public abstract List<BolusProgrammed> getAllSinceDateReceived(Date dateReceived);

}
