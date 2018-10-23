package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.CartridgeRemoved;

@Dao
public abstract class CartridgeRemovedDao extends GenericHistoryDao<CartridgeRemoved> {

    @Query("SELECT * FROM CartridgeRemoved")
    public abstract List<CartridgeRemoved> getAll();

    @Query("SELECT * FROM CartridgeRemoved WHERE pumpCreationDate > :creationDate")
    public abstract List<CartridgeRemoved> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM CartridgeRemoved WHERE dateReceived > :dateReceived")
    public abstract List<CartridgeRemoved> getAllSinceDateReceived(Date dateReceived);

}
