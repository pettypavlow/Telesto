package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.AlertStatus;
import sugar.free.telesto.parser.utils.BiMap;

public class AlertStatusIDs {

    public static final BiMap<AlertStatus, Integer> IDS = new BiMap<>();

    static {
        IDS.put(AlertStatus.ACTIVE, 31);
        IDS.put(AlertStatus.SNOOZED, 227);
    }

}
