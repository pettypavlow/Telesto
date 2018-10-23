package sugar.free.telesto.activities;

import androidx.appcompat.app.AppCompatActivity;
import sugar.free.telesto.TelestoApp;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        TelestoApp.getConnectionService().requestConnection(this);
    }

    @Override
    protected void onPause() {
        TelestoApp.getConnectionService().withdrawConnectionRequest(this);
        super.onPause();
    }

}
