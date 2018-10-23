package sugar.free.telesto.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.descriptors.TelestoState;

public class LauncherActivity extends AppCompatActivity implements TelestoApp.InitializationCompletedCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelestoApp.awaitCompletedInitialization(this);
    }

    @Override
    public void onInitializationCompleted() {
        if (TelestoApp.getConnectionService().getState() != TelestoState.NOT_PAIRED) startActivity(new Intent(this, StatusActivity.class));
        else startActivity(new Intent(this, SetupActivity.class));
        finish();
    }
}
