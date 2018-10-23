package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.parser.utils.BiMap;

public class BolusTypeIDs {

    public static final BiMap<BolusType, Integer> IDS = new BiMap<>();

    static {
        IDS.put(BolusType.STANDARD, 31);
        IDS.put(BolusType.EXTENDED, 227);
        IDS.put(BolusType.MULTIWAVE, 252);
    }

}
