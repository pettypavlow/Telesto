package sugar.free.telesto.activities;

import android.os.Bundle;
import android.view.MenuItem;

import sugar.free.telesto.R;

public abstract class BackButtonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    protected void setup() {
        setSupportActionBar(findViewById(R.id.toolbar));
    }
}
