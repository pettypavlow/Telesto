package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.PowerUp;

@Dao
public abstract class PowerUpDao extends GenericHistoryDao<PowerUp> {

    @Query("SELECT * FROM PowerUp")
    public abstract List<PowerUp> getAll();

    @Query("SELECT * FROM PowerUp WHERE pumpCreationDate > :creationDate")
    public abstract List<PowerUp> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM PowerUp WHERE dateReceived > :dateReceived")
    public abstract List<PowerUp> getAllSinceDateReceived(Date dateReceived);

}
