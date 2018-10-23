package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.EndOfTBR;

@Dao
public abstract class EndOfTBRDao extends GenericHistoryDao<EndOfTBR> {

    @Query("SELECT * FROM EndOfTBR")
    public abstract List<EndOfTBR> getAll();

    @Query("SELECT * FROM EndOfTBR WHERE pumpCreationDate > :creationDate")
    public abstract List<EndOfTBR> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM EndOfTBR WHERE dateReceived > :dateReceived")
    public abstract List<EndOfTBR> getAllSinceDateReceived(Date dateReceived);

}
