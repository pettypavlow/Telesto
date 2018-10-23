package sugar.free.telesto.activities.boluses;

import sugar.free.telesto.descriptors.BolusType;

public class MultiwaveBolusActivity extends BolusActivity {

    @Override
    protected BolusType getBolusType() {
        return BolusType.MULTIWAVE;
    }
}
