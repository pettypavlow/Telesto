package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.parser.utils.BiMap;

public class OperatingModeIDs {

    public static final BiMap<OperatingMode, Integer> IDS = new BiMap<>();

    static {
        IDS.put(OperatingMode.STOPPED, 31);
        IDS.put(OperatingMode.STARTED, 227);
        IDS.put(OperatingMode.PAUSED, 252);
    }

}
