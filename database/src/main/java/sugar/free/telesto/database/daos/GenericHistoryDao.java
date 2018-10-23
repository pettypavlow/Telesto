package sugar.free.telesto.database.daos;

import androidx.room.Insert;
import sugar.free.telesto.database.entities.GenericHistoryEntity;

public abstract class GenericHistoryDao<T extends GenericHistoryEntity> {

    @SuppressWarnings("unchecked")
    @Insert
    public abstract void insert(T... entities);

}
