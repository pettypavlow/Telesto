package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.TotalDailyDose;

@Dao
public abstract class TotalDailyDoseDao extends GenericHistoryDao<TotalDailyDose> {

    @Query("SELECT * FROM TotalDailyDose")
    public abstract List<TotalDailyDose> getAll();

    @Query("SELECT * FROM TotalDailyDose WHERE pumpCreationDate > :creationDate")
    public abstract List<TotalDailyDose> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM TotalDailyDose WHERE dateReceived > :dateReceived")
    public abstract List<TotalDailyDose> getAllSinceDateReceived(Date dateReceived);

}
