package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.BatteryType;
import sugar.free.telesto.parser.utils.BiMap;

public class BatteryTypeIDs {

    public static final BiMap<BatteryType, Integer> IDS = new BiMap<>();

    static {
        IDS.put(BatteryType.ALKALI, 31);
        IDS.put(BatteryType.LITHIUM, 227);
        IDS.put(BatteryType.NI_MH, 252);
    }

}
