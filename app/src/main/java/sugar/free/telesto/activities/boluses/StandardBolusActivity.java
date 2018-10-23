package sugar.free.telesto.activities.boluses;

import android.os.Bundle;

import sugar.free.telesto.R;
import sugar.free.telesto.descriptors.BolusType;

public class StandardBolusActivity extends BolusActivity {

    @Override
    protected BolusType getBolusType() {
        return BolusType.STANDARD;
    }
}
