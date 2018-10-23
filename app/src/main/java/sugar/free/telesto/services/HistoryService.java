package sugar.free.telesto.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.database.entities.BasalDeliveryChanged;
import sugar.free.telesto.database.entities.BolusDelivered;
import sugar.free.telesto.database.entities.BolusProgrammed;
import sugar.free.telesto.database.entities.CannulaFilled;
import sugar.free.telesto.database.entities.CartridgeInserted;
import sugar.free.telesto.database.entities.CartridgeRemoved;
import sugar.free.telesto.database.entities.EndOfTBR;
import sugar.free.telesto.database.entities.GenericHistoryEntity;
import sugar.free.telesto.database.entities.HistoryReadingOffset;
import sugar.free.telesto.database.entities.OccurrenceOfAlert;
import sugar.free.telesto.database.entities.OperatingModeChanged;
import sugar.free.telesto.database.entities.PowerDown;
import sugar.free.telesto.database.entities.PowerUp;
import sugar.free.telesto.database.entities.SniffingDone;
import sugar.free.telesto.database.entities.StartOfTBR;
import sugar.free.telesto.database.entities.TotalDailyDose;
import sugar.free.telesto.database.entities.TubeFilled;
import sugar.free.telesto.descriptors.PumpTime;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.parser.app_layer.history.HistoryReadingDirection;
import sugar.free.telesto.parser.app_layer.history.history_events.BasalDeliveryChangedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.BolusDeliveredEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.BolusProgrammedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.CannulaFilledEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.CartridgeInsertedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.CartridgeRemovedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.DateTimeChangedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.DefaultDateTimeSetEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.EndOfTBREvent;
import sugar.free.telesto.parser.app_layer.history.history_events.HistoryEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.OccurrenceOfAlertEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.OperatingModeChangedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.PowerDownEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.PowerUpEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.SniffingDoneEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.StartOfTBREvent;
import sugar.free.telesto.parser.app_layer.history.history_events.TotalDailyDoseEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.TubeFilledEvent;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.MessageRequestUtil;

public class HistoryService extends Service implements TelestoApp.InitializationCompletedCallback, ConnectionService.StateCallback {

    private final Object[] $threadLock = new Object[0];
    private LocalBinder localBinder = new LocalBinder();
    private PowerManager.WakeLock wakeLock;
    private Thread thread = null;

    private String serialNumber;
    private long timeOffset;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Telesto:HistoryService");
        TelestoApp.awaitCompletedInitialization(this);
    }

    @Override
    public void onInitializationCompleted() {
        TelestoApp.getConnectionService().registerStateCallback(this);
        stateChanged(TelestoApp.getConnectionService().getState());
    }

    @Override
    public void stateChanged(TelestoState state) {
        if (state == TelestoState.CONNECTED) synchronizeHistory();
    }

    public void synchronizeHistory() {
        synchronized ($threadLock) {
            if (thread != null) return;
            thread = new Thread(() -> {
                wakeLock.acquire(20000);
                TelestoApp.getConnectionService().requestConnection(this);
                readHistory();
                TelestoApp.getConnectionService().withdrawConnectionRequest(this);
                wakeLock.release();
                synchronized ($threadLock) {
                    thread = null;
                }
            });
            thread.start();
        }
    }

    private void readHistory() {
        try {
            serialNumber = MessageRequestUtil.getSystemIdentification().getSerialNumber();
            PumpTime pumpTime = MessageRequestUtil.getPumpTime();
            timeOffset = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() - parseDate(pumpTime.getYear(),
                    pumpTime.getMonth(), pumpTime.getDay(), pumpTime.getHour(), pumpTime.getMinute(), pumpTime.getSecond());
            HistoryReadingOffset historyReadingOffset = TelestoApp.getDatabase().getHistoryReadingOffsetDao().getBySerialNumber(serialNumber);
            try {
                List<HistoryEvent> historyEvents = new ArrayList<>();
                if (historyReadingOffset == null) {
                    MessageRequestUtil.startReadingHistory(0xFFFFFFFF, HistoryReadingDirection.BACKWARD);
                    for (int i = 0; i < 50; i++) {
                        List<HistoryEvent> newEvents = MessageRequestUtil.readHistoryEvents();
                        if (newEvents.size() == 0) break;
                        historyEvents.addAll(newEvents);
                    }
                } else {
                    MessageRequestUtil.startReadingHistory(historyReadingOffset.getLastEventPosition() + 1, HistoryReadingDirection.FORWARD);
                    while (true) {
                        List<HistoryEvent> newEvents = MessageRequestUtil.readHistoryEvents();
                        if (newEvents.size() == 0) break;
                        historyEvents.addAll(newEvents);
                    }
                }
                processHistoryEvents(historyEvents);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    MessageRequestUtil.stopReadingHistory();
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processHistoryEvents(List<HistoryEvent> historyEvents) {
        Collections.sort(historyEvents);
        Collections.reverse(historyEvents);
        for (HistoryEvent historyEvent : historyEvents)
            if (!processHistoryEvent(historyEvent)) break;
        if (historyEvents.size() > 0) {
            HistoryReadingOffset historyReadingOffset = new HistoryReadingOffset();
            historyReadingOffset.setPumpSerialNumber(serialNumber);
            historyReadingOffset.setLastEventPosition(historyEvents.get(0).getEventPosition());
            TelestoApp.getDatabase().getHistoryReadingOffsetDao().insert(historyReadingOffset);
        }
    }

    private boolean processHistoryEvent(HistoryEvent event) {
        if (event instanceof DefaultDateTimeSetEvent) return false;
        GenericHistoryEntity entity = null;
        if (event instanceof DateTimeChangedEvent)
            processDateTimeChangedEvent((DateTimeChangedEvent) event);
        else if (event instanceof BolusDeliveredEvent)
            entity = processBolusDeliveredEvent((BolusDeliveredEvent) event);
        else if (event instanceof BolusProgrammedEvent)
            entity = processBolusProgrammedEvent((BolusProgrammedEvent) event);
        else if (event instanceof CannulaFilledEvent)
            entity = processCannulaFilledEvent((CannulaFilledEvent) event);
        else if (event instanceof EndOfTBREvent)
            entity = processEndOfTBREvent((EndOfTBREvent) event);
        else if (event instanceof OccurrenceOfAlertEvent)
            entity = processOccurrenceOfAlertEvent((OccurrenceOfAlertEvent) event);
        else if (event instanceof OperatingModeChangedEvent)
            entity = processOperatingModeChangedEvent((OperatingModeChangedEvent) event);
        else if (event instanceof SniffingDoneEvent)
            entity = processSniffingDoneEvent((SniffingDoneEvent) event);
        else if (event instanceof StartOfTBREvent)
            entity = processStartOfTBREvent((StartOfTBREvent) event);
        else if (event instanceof TotalDailyDoseEvent)
            entity = processTotalDailyDoseEvent((TotalDailyDoseEvent) event);
        else if (event instanceof TubeFilledEvent)
            entity = processTubeFilledEvent((TubeFilledEvent) event);
        else if (event instanceof BasalDeliveryChangedEvent)
            entity = processBasalDeliveryChangedEvent((BasalDeliveryChangedEvent) event);
        else if (event instanceof CartridgeInsertedEvent) entity = new CartridgeInserted();
        else if (event instanceof CartridgeRemovedEvent) entity = new CartridgeRemoved();
        else if (event instanceof PowerUpEvent) entity = new PowerUp();
        else if (event instanceof PowerDownEvent) entity = new PowerDown();
        if (entity != null) {
            processGenericHistoryEntity(entity, event);
            TelestoApp.getDatabase().insert(entity);
        }
        return true;
    }

    private void processGenericHistoryEntity(GenericHistoryEntity entity, HistoryEvent historyEvent) {
        entity.setEventPosition(historyEvent.getEventPosition());
        entity.setPumpSerialNumber(serialNumber);
        entity.setPumpCreationDate(new Date(parseDate(historyEvent.getEventYear(), historyEvent.getEventMonth(), historyEvent.getEventDay(),
                historyEvent.getEventHour(), historyEvent.getEventMinute(), historyEvent.getEventSecond()) + timeOffset));
        entity.setDateReceived(new Date());
    }

    private void processDateTimeChangedEvent(DateTimeChangedEvent event) {
        long timeAfter = parseDate(event.getEventYear(), event.getEventMonth(), event.getEventDay(), event.getEventHour(), event.getEventMinute(), event.getEventSecond());
        long timeBefore = parseDate(event.getBeforeYear(), event.getBeforeMonth(), event.getBeforeDay(), event.getBeforeHour(), event.getBeforeMinute(), event.getBeforeSecond());
        timeOffset = timeBefore - timeAfter;
    }

    private BolusDelivered processBolusDeliveredEvent(BolusDeliveredEvent event) {
        BolusDelivered bolusDelivered = new BolusDelivered();
        bolusDelivered.setBolusType(event.getBolusType());
        bolusDelivered.setStartTime(new Date(parseRelativeDate(event.getEventYear(), event.getEventMonth(), event.getEventDay(), event.getEventHour(),
                event.getEventMinute(), event.getEventSecond(), event.getStartHour(), event.getStartMinute(), event.getStartSecond()) + timeOffset));
        bolusDelivered.setImmediateAmount(event.getImmediateAmount());
        bolusDelivered.setExtendedAmount(event.getExtendedAmount());
        bolusDelivered.setDuration(event.getDuration());
        bolusDelivered.setBolusId(event.getBolusID());
        return bolusDelivered;
    }

    private BolusProgrammed processBolusProgrammedEvent(BolusProgrammedEvent event) {
        BolusProgrammed bolusProgrammed = new BolusProgrammed();
        bolusProgrammed.setBolusType(event.getBolusType());
        bolusProgrammed.setImmediateAmount(event.getImmediateAmount());
        bolusProgrammed.setExtendedAmount(event.getExtendedAmount());
        bolusProgrammed.setDuration(event.getDuration());
        bolusProgrammed.setBolusId(event.getBolusID());
        return bolusProgrammed;
    }

    private CannulaFilled processCannulaFilledEvent(CannulaFilledEvent event) {
        CannulaFilled cannulaFilled = new CannulaFilled();
        cannulaFilled.setAmount(event.getAmount());
        return cannulaFilled;
    }

    private EndOfTBR processEndOfTBREvent(EndOfTBREvent event) {
        EndOfTBR endOfTBR = new EndOfTBR();
        endOfTBR.setStartTime(new Date(parseRelativeDate(event.getEventYear(), event.getEventMonth(), event.getEventDay(), event.getEventHour(),
                event.getEventMinute(), event.getEventSecond(), event.getStartHour(), event.getStartMinute(), event.getStartSecond()) + timeOffset));
        endOfTBR.setPercentage(event.getAmount());
        endOfTBR.setDuration(event.getDuration());
        return endOfTBR;
    }

    private OccurrenceOfAlert processOccurrenceOfAlertEvent(OccurrenceOfAlertEvent event) {
        OccurrenceOfAlert occurrenceOfAlert = new OccurrenceOfAlert();
        occurrenceOfAlert.setAlertId(event.getAlertID());
        occurrenceOfAlert.setAlertType(event.getAlertType());
        return occurrenceOfAlert;
    }

    private OperatingModeChanged processOperatingModeChangedEvent(OperatingModeChangedEvent event) {
        OperatingModeChanged operatingModeChanged = new OperatingModeChanged();
        operatingModeChanged.setOldValue(event.getOldValue());
        operatingModeChanged.setNewValue(event.getNewValue());
        return operatingModeChanged;
    }

    private SniffingDone processSniffingDoneEvent(SniffingDoneEvent event) {
        SniffingDone sniffingDone = new SniffingDone();
        sniffingDone.setAmount(event.getAmount());
        return sniffingDone;
    }

    private StartOfTBR processStartOfTBREvent(StartOfTBREvent event) {
        StartOfTBR startOfTBR = new StartOfTBR();
        startOfTBR.setDuration(event.getDuration());
        startOfTBR.setPercentage(event.getAmount());
        return startOfTBR;
    }

    private TotalDailyDose processTotalDailyDoseEvent(TotalDailyDoseEvent event) {
        TotalDailyDose totalDailyDose = new TotalDailyDose();
        totalDailyDose.setBasal(event.getBasalTotal());
        totalDailyDose.setBolus(event.getBolusTotal());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        calendar.set(Calendar.YEAR, event.getTotalYear());
        calendar.set(Calendar.MONTH, event.getTotalMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, event.getTotalDay());
        totalDailyDose.setDate(calendar.getTime());
        return totalDailyDose;
    }

    private TubeFilled processTubeFilledEvent(TubeFilledEvent event) {
        TubeFilled tubeFilled = new TubeFilled();
        tubeFilled.setAmount(event.getAmount());
        return tubeFilled;
    }

    private BasalDeliveryChanged processBasalDeliveryChangedEvent(BasalDeliveryChangedEvent event) {
        BasalDeliveryChanged basalDeliveryChanged = new BasalDeliveryChanged();
        basalDeliveryChanged.setOldBasalRate(event.getOldBasalRate());
        basalDeliveryChanged.setNewBasalRate(event.getNewBasalRate());
        return basalDeliveryChanged;
    }

    private long parseDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis();
    }

    private long parseRelativeDate(int year, int month, int day, int hour, int minute, int second, int relativeHour, int relativeMinute, int relativeSecond) {
        if (relativeHour * 60 * 60 + relativeMinute * 60 + relativeSecond >= hour * 60 * 60 * minute * 60 + second)
            day--;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, relativeHour);
        calendar.set(Calendar.MINUTE, relativeMinute);
        calendar.set(Calendar.SECOND, relativeSecond);
        return calendar.getTimeInMillis();
    }

    public class LocalBinder extends Binder {
        public HistoryService getService() {
            return HistoryService.this;
        }
    }
}
