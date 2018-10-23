package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.AlertCategory;
import sugar.free.telesto.parser.utils.BiMap;

public class AlertCategoryIDs {

    public static final BiMap<AlertCategory, Integer> IDS = new BiMap<>();

    static {
        IDS.put(AlertCategory.REMINDER, 227);
        IDS.put(AlertCategory.MAINTENANCE, 252);
        IDS.put(AlertCategory.WARNING, 805);
        IDS.put(AlertCategory.ERROR, 826);
    }

}
