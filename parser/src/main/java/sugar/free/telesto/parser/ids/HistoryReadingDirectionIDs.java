package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.BasalProfile;
import sugar.free.telesto.parser.app_layer.history.HistoryReadingDirection;
import sugar.free.telesto.parser.utils.BiMap;

public class HistoryReadingDirectionIDs {

    public static final BiMap<HistoryReadingDirection, Integer> IDS = new BiMap<>();

    static {
        IDS.put(HistoryReadingDirection.FORWARD, 31);
        IDS.put(HistoryReadingDirection.BACKWARD, 227);
    }

}
