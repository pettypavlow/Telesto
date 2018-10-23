package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.StartOfTBR;

@Dao
public abstract class StartOfTBRDao extends GenericHistoryDao<StartOfTBR> {

    @Query("SELECT * FROM StartOfTBR")
    public abstract List<StartOfTBR> getAll();

    @Query("SELECT * FROM StartOfTBR WHERE pumpCreationDate > :creationDate")
    public abstract List<StartOfTBR> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM StartOfTBR WHERE dateReceived > :dateReceived")
    public abstract List<StartOfTBR> getAllSinceDateReceived(Date dateReceived);

}
