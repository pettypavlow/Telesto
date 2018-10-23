package sugar.free.telesto.parser.ids;

import sugar.free.telesto.parser.satl.PairingStatus;
import sugar.free.telesto.parser.utils.BiMap;

public class PairingStatusIDs {

    public static final BiMap<PairingStatus, Integer> IDS = new BiMap<>();

    static {
        IDS.put(PairingStatus.PENDING, 1683);
        IDS.put(PairingStatus.REJECTED, 7850);
        IDS.put(PairingStatus.CONFIRMED, 11835);
    }

}
