package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.parser.utils.BiMap;

public class ActiveBolusTypeIDs {

    public static final BiMap<BolusType, Integer> IDS = new BiMap<>();

    static {
        IDS.put(BolusType.STANDARD, 227);
        IDS.put(BolusType.EXTENDED, 252);
        IDS.put(BolusType.MULTIWAVE, 805);
    }

}
