package sugar.free.telesto.parser.ids;

import sugar.free.telesto.descriptors.BasalProfile;
import sugar.free.telesto.parser.utils.BiMap;

public class ActiveBasalProfileIDs {

    public static final BiMap<BasalProfile, Integer> IDS = new BiMap<>();

    static {
        IDS.put(BasalProfile.PROFILE_1, 31);
        IDS.put(BasalProfile.PROFILE_2, 227);
        IDS.put(BasalProfile.PROFILE_3, 252);
        IDS.put(BasalProfile.PROFILE_4, 805);
        IDS.put(BasalProfile.PROFILE_5, 826);
    }

}
