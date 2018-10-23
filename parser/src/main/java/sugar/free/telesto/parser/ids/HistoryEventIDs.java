package sugar.free.telesto.parser.ids;

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
import sugar.free.telesto.parser.app_layer.history.history_events.OccurrenceOfErrorEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.OccurrenceOfMaintenanceEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.OccurrenceOfWarningEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.OperatingModeChangedEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.PowerDownEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.PowerUpEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.SniffingDoneEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.StartOfTBREvent;
import sugar.free.telesto.parser.app_layer.history.history_events.TotalDailyDoseEvent;
import sugar.free.telesto.parser.app_layer.history.history_events.TubeFilledEvent;
import sugar.free.telesto.parser.utils.BiMap;

public class HistoryEventIDs {

    public static final BiMap<Class<? extends HistoryEvent>, Integer> IDS = new BiMap<>();

    static {
        IDS.put(BolusDeliveredEvent.class, 917);
        IDS.put(BolusProgrammedEvent.class, 874);
        IDS.put(CannulaFilledEvent.class, 3264);
        IDS.put(DateTimeChangedEvent.class, 165);
        IDS.put(DefaultDateTimeSetEvent.class, 170);
        IDS.put(EndOfTBREvent.class, 771);
        IDS.put(OccurrenceOfErrorEvent.class, 1011);
        IDS.put(OccurrenceOfMaintenanceEvent.class, 1290);
        IDS.put(OccurrenceOfWarningEvent.class, 1360);
        IDS.put(OperatingModeChangedEvent.class, 195);
        IDS.put(PowerUpEvent.class, 15);
        IDS.put(PowerDownEvent.class, 51);
        IDS.put(SniffingDoneEvent.class, 102);
        IDS.put(StartOfTBREvent.class, 240);
        IDS.put(TotalDailyDoseEvent.class, 960);
        IDS.put(TubeFilledEvent.class, 105);
        IDS.put(CartridgeInsertedEvent.class, 60);
        IDS.put(CartridgeRemovedEvent.class, 85);
        IDS.put(BasalDeliveryChangedEvent.class, 204);
    }

}
