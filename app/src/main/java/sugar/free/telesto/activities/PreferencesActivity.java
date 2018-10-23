package sugar.free.telesto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceFragmentCompat;
import sugar.free.telesto.R;
import sugar.free.telesto.TelestoApp;

public class PreferencesActivity extends BackButtonActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_preferences);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new PreferencesFragment())
                .commit();
    }

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

    @Override
    protected void onDestroy() {
        if (alertDialog != null) alertDialog.dismiss();
        super.onDestroy();
    }

    public static class PreferencesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            getPreferenceScreen().findPreference("deletePairing").setOnPreferenceClickListener((arg) -> {
                ((PreferencesActivity) getActivity()).alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete_pairing_dialog_title)
                        .setMessage(Html.fromHtml(getString(R.string.do_you_really_want_to_delete_the_pairing)))
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, (d, i) -> {
                            TelestoApp.getConnectionService().reset();
                            Intent intent = new Intent(getActivity(), LauncherActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .show();
                return true;
            });
        }
    }
}
