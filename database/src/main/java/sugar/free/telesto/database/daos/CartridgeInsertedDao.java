package sugar.free.telesto.database.daos;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import sugar.free.telesto.database.entities.CartridgeInserted;

@Dao
public abstract class CartridgeInsertedDao extends GenericHistoryDao<CartridgeInserted> {

    @Query("SELECT * FROM CartridgeInserted")
    public abstract List<CartridgeInserted> getAll();

    @Query("SELECT * FROM CartridgeInserted WHERE pumpCreationDate > :creationDate")
    public abstract List<CartridgeInserted> getAllSincePumpCreationDate(Date creationDate);

    @Query("SELECT * FROM CartridgeInserted WHERE dateReceived > :dateReceived")
    public abstract List<CartridgeInserted> getAllSinceDateReceived(Date dateReceived);

}
