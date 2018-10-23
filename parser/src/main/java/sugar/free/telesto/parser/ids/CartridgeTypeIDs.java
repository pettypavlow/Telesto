package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.CartridgeType;
import sugar.free.telesto.parser.utils.BiMap;

public class CartridgeTypeIDs {

    public static final BiMap<CartridgeType, Integer> IDS = new BiMap<>();

    static {
        IDS.put(CartridgeType.PREFILLED, 31);
        IDS.put(CartridgeType.SELF_FILLED, 227);
    }

}
