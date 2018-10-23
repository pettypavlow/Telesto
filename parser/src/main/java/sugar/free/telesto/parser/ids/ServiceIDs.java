package sugar.free.telesto.parser.ids;

import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.BiMap;

public class ServiceIDs {

    public static final BiMap<Service, Byte> IDS = new BiMap<>();

    static {
        IDS.put(Service.CONNECTION, (byte) 0);
        IDS.put(Service.STATUS, (byte) 15);
        IDS.put(Service.HISTORY, (byte) 60);
        IDS.put(Service.CONFIGURATION, (byte) 85);
        IDS.put(Service.REMOTE_CONTROL, (byte) 102);
        IDS.put(Service.PARAMETER, (byte) 51);
    }
}
