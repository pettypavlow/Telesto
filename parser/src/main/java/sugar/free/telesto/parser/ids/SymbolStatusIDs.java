package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.SymbolStatus;
import sugar.free.telesto.parser.utils.BiMap;

public class SymbolStatusIDs {

    public static final BiMap<SymbolStatus, Integer> IDS = new BiMap<>();

    static {
        IDS.put(SymbolStatus.FULL, 31);
        IDS.put(SymbolStatus.LOW, 227);
        IDS.put(SymbolStatus.EMPTY, 252);
    }

}
