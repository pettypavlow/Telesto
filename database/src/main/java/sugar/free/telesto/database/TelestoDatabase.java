package sugar.free.telesto.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import sugar.free.telesto.database.daos.BasalDeliveryChangedDao;
import sugar.free.telesto.database.daos.CartridgeInsertedDao;
import sugar.free.telesto.database.daos.CartridgeRemovedDao;
import sugar.free.telesto.database.daos.PowerDownDao;
import sugar.free.telesto.database.daos.PowerUpDao;
import sugar.free.telesto.database.daos.BolusDeliveredDao;
import sugar.free.telesto.database.daos.BolusProgrammedDao;
import sugar.free.telesto.database.daos.CannulaFilledDao;
import sugar.free.telesto.database.daos.SniffingDoneDao;
import sugar.free.telesto.database.daos.EndOfTBRDao;
import sugar.free.telesto.database.daos.HistoryReadingOffsetDao;
import sugar.free.telesto.database.daos.OccurrenceOfAlertDao;
import sugar.free.telesto.database.daos.OperatingModeChangedDao;
import sugar.free.telesto.database.daos.StartOfTBRDao;
import sugar.free.telesto.database.daos.TotalDailyDoseDao;
import sugar.free.telesto.database.daos.TubeFilledDao;
import sugar.free.telesto.database.entities.BasalDeliveryChanged;
import sugar.free.telesto.database.entities.CartridgeInserted;
import sugar.free.telesto.database.entities.CartridgeRemoved;
import sugar.free.telesto.database.entities.PowerDown;
import sugar.free.telesto.database.entities.PowerUp;
import sugar.free.telesto.database.entities.BolusDelivered;
import sugar.free.telesto.database.entities.BolusProgrammed;
import sugar.free.telesto.database.entities.CannulaFilled;
import sugar.free.telesto.database.entities.SniffingDone;
import sugar.free.telesto.database.entities.EndOfTBR;
import sugar.free.telesto.database.entities.GenericHistoryEntity;
import sugar.free.telesto.database.entities.HistoryReadingOffset;
import sugar.free.telesto.database.entities.OccurrenceOfAlert;
import sugar.free.telesto.database.entities.OperatingModeChanged;
import sugar.free.telesto.database.entities.StartOfTBR;
import sugar.free.telesto.database.entities.TotalDailyDose;
import sugar.free.telesto.database.entities.TubeFilled;
import sugar.free.telesto.descriptors.AlertType;
import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.descriptors.OperatingMode;

@TypeConverters(value = {TelestoDatabase.DateConverter.class, TelestoDatabase.BolusTypeConverter.class, TelestoDatabase.OperatingModeConverter.class, TelestoDatabase.AlertTypeConverter.class})
@Database(entities = {PowerUp.class, BolusDelivered.class, BolusProgrammed.class, CannulaFilled.class, SniffingDone.class, HistoryReadingOffset.class,
        OccurrenceOfAlert.class, OperatingModeChanged.class, EndOfTBR.class, TotalDailyDose.class, TubeFilled.class, StartOfTBR.class, PowerDown.class,
        CartridgeInserted.class, CartridgeRemoved.class, BasalDeliveryChanged.class}, version = 1, exportSchema = false)
public abstract class TelestoDatabase extends RoomDatabase {

    public abstract PowerUpDao getPowerUpDao();
    public abstract BolusDeliveredDao getBolusDeliveredDao();
    public abstract BolusProgrammedDao getBolusProgrammedDao();
    public abstract CannulaFilledDao getCannulaFilledDao();
    public abstract SniffingDoneDao getSniffingDoneDao();
    public abstract HistoryReadingOffsetDao getHistoryReadingOffsetDao();
    public abstract OccurrenceOfAlertDao getOccurrenceOfAlertDao();
    public abstract OperatingModeChangedDao getOperatingModeChangedDao();
    public abstract EndOfTBRDao getEndOfTBRDao();
    public abstract StartOfTBRDao getStartOfTBRDao();
    public abstract TotalDailyDoseDao getTotalDailyDoseDao();
    public abstract TubeFilledDao getTubeFilledDao();
    public abstract CartridgeInsertedDao getCartridgeInsertedDao();
    public abstract CartridgeRemovedDao getCartridgeRemovedDao();
    public abstract PowerDownDao getPowerDownDao();
    public abstract BasalDeliveryChangedDao getBasalDeliveryChangedDao();

    public static TelestoDatabase instantiate(Context context) {
        return Room.databaseBuilder(context, TelestoDatabase.class, "telesto").build();
    }

    public List<GenericHistoryEntity> getAll() {
        List<GenericHistoryEntity> genericHistoryEntities = new ArrayList<>();
        genericHistoryEntities.addAll(getPowerUpDao().getAll());
        genericHistoryEntities.addAll(getBolusDeliveredDao().getAll());
        genericHistoryEntities.addAll(getBolusProgrammedDao().getAll());
        genericHistoryEntities.addAll(getCannulaFilledDao().getAll());
        genericHistoryEntities.addAll(getSniffingDoneDao().getAll());
        genericHistoryEntities.addAll(getOccurrenceOfAlertDao().getAll());
        genericHistoryEntities.addAll(getOperatingModeChangedDao().getAll());
        genericHistoryEntities.addAll(getEndOfTBRDao().getAll());
        genericHistoryEntities.addAll(getTotalDailyDoseDao().getAll());
        genericHistoryEntities.addAll(getTubeFilledDao().getAll());
        genericHistoryEntities.addAll(getStartOfTBRDao().getAll());
        genericHistoryEntities.addAll(getCartridgeInsertedDao().getAll());
        genericHistoryEntities.addAll(getCartridgeRemovedDao().getAll());
        genericHistoryEntities.addAll(getPowerDownDao().getAll());
        genericHistoryEntities.addAll(getPowerDownDao().getAll());
        genericHistoryEntities.addAll(getBasalDeliveryChangedDao().getAll());
        Collections.sort(genericHistoryEntities);
        return genericHistoryEntities;
    }

    public List<GenericHistoryEntity> getAllSincePumpCreationDate(Date creationDate) {
        List<GenericHistoryEntity> genericHistoryEntities = new ArrayList<>();
        genericHistoryEntities.addAll(getPowerUpDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getBolusDeliveredDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getBolusProgrammedDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getCannulaFilledDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getSniffingDoneDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getOccurrenceOfAlertDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getOperatingModeChangedDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getEndOfTBRDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getTotalDailyDoseDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getTubeFilledDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getStartOfTBRDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getPowerDownDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getCartridgeRemovedDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getCartridgeInsertedDao().getAllSincePumpCreationDate(creationDate));
        genericHistoryEntities.addAll(getBasalDeliveryChangedDao().getAllSincePumpCreationDate(creationDate));
        Collections.sort(genericHistoryEntities);
        return genericHistoryEntities;
    }

    public List<GenericHistoryEntity> getAllSinceDateReceived(Date dateReceived) {
        List<GenericHistoryEntity> genericHistoryEntities = new ArrayList<>();
        genericHistoryEntities.addAll(getPowerUpDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getBolusDeliveredDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getBolusProgrammedDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getCannulaFilledDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getSniffingDoneDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getOccurrenceOfAlertDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getOperatingModeChangedDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getEndOfTBRDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getTotalDailyDoseDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getTubeFilledDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getStartOfTBRDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getPowerDownDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getCartridgeInsertedDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getCartridgeRemovedDao().getAllSinceDateReceived(dateReceived));
        genericHistoryEntities.addAll(getBasalDeliveryChangedDao().getAllSinceDateReceived(dateReceived));
        Collections.sort(genericHistoryEntities);
        return genericHistoryEntities;
    }

    public void insert(GenericHistoryEntity genericHistoryEntity) {
        if (genericHistoryEntity instanceof PowerUp) getPowerUpDao().insert((PowerUp) genericHistoryEntity);
        else if (genericHistoryEntity instanceof BolusDelivered) getBolusDeliveredDao().insert((BolusDelivered) genericHistoryEntity);
        else if (genericHistoryEntity instanceof BolusProgrammed) getBolusProgrammedDao().insert((BolusProgrammed) genericHistoryEntity);
        else if (genericHistoryEntity instanceof CannulaFilled) getCannulaFilledDao().insert((CannulaFilled) genericHistoryEntity);
        else if (genericHistoryEntity instanceof SniffingDone) getSniffingDoneDao().insert((SniffingDone) genericHistoryEntity);
        else if (genericHistoryEntity instanceof OccurrenceOfAlert) getOccurrenceOfAlertDao().insert((OccurrenceOfAlert) genericHistoryEntity);
        else if (genericHistoryEntity instanceof OperatingModeChanged) getOperatingModeChangedDao().insert((OperatingModeChanged) genericHistoryEntity);
        else if (genericHistoryEntity instanceof EndOfTBR) getEndOfTBRDao().insert((EndOfTBR) genericHistoryEntity);
        else if (genericHistoryEntity instanceof TotalDailyDose) getTotalDailyDoseDao().insert((TotalDailyDose) genericHistoryEntity);
        else if (genericHistoryEntity instanceof TubeFilled) getTubeFilledDao().insert((TubeFilled) genericHistoryEntity);
        else if (genericHistoryEntity instanceof StartOfTBR) getStartOfTBRDao().insert((StartOfTBR) genericHistoryEntity);
        else if (genericHistoryEntity instanceof CartridgeInserted) getCartridgeInsertedDao().insert((CartridgeInserted) genericHistoryEntity);
        else if (genericHistoryEntity instanceof CartridgeRemoved) getCartridgeRemovedDao().insert((CartridgeRemoved) genericHistoryEntity);
        else if (genericHistoryEntity instanceof PowerDown) getPowerDownDao().insert((PowerDown) genericHistoryEntity);
        else if (genericHistoryEntity instanceof BasalDeliveryChanged) getBasalDeliveryChangedDao().insert((BasalDeliveryChanged) genericHistoryEntity);
    }

    public static class DateConverter {

        @TypeConverter
        public static Date longToDate(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToLong(Date value) {
            return value == null ? null : value.getTime();
        }
    }

    public static class BolusTypeConverter {

        @TypeConverter
        public static BolusType stringToBolusType(String value) {
            return value == null ? null : BolusType.valueOf(value);
        }

        @TypeConverter
        public static String bolusTypeToString(BolusType value) {
            return value == null ? null : value.name();
        }
    }

    public static class OperatingModeConverter {

        @TypeConverter
        public static OperatingMode stringToOperatingMode(String value) {
            return value == null ? null : OperatingMode.valueOf(value);
        }

        @TypeConverter
        public static String operatingModeToString(OperatingMode value) {
            return value == null ? null : value.name();
        }
    }

    public static class AlertTypeConverter {

        @TypeConverter
        public static AlertType stringToAlertType(String value) {
            return value == null ? null : AlertType.valueOf(value);
        }

        @TypeConverter
        public static String alertTypeToString(AlertType value) {
            return value == null ? null : value.name();
        }
    }
}
