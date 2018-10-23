package sugar.free.telesto.activities;

import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import sugar.free.telesto.R;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.descriptors.AvailableBolusTypes;
import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.exceptions.DisconnectedException;
import sugar.free.telesto.exceptions.app_layer_errors.PumpStoppedException;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.ExceptionTranslator;
import sugar.free.telesto.utils.MessageRequestUtil;
import sugar.free.telesto.views.DurationTextInput;

public class TBRActivity extends BackButtonActivity implements TextWatcher, ConnectionService.StateCallback {

    private AlertDialog dialog;
    private AvailableBolusTypes availableBolusTypes;
    private TransitionDrawable windowBackground;
    private boolean windowError;

    private TextView text;
    private LinearLayout viewContainer;
    private TextInputEditText percentage;
    private DurationTextInput duration;
    private Button setTBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        percentage = findViewById(R.id.percentage);
        duration = findViewById(R.id.duration);
        setTBR = findViewById(R.id.set_tbr);
        text = findViewById(R.id.text);
        viewContainer = findViewById(R.id.view_container);

        windowBackground = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.error_transition);
        getWindow().setBackgroundDrawable(windowBackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= 28) getWindow().setNavigationBarDividerColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        percentage.addTextChangedListener(this);
        percentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() == 0) {
                    percentage.setError(getString(R.string.the_field_must_not_be_empty));
                    return;
                }
                int value = Integer.parseInt(text);
                if (value == 100)
                    percentage.setError(getString(R.string.the_value_must_not_be_100));
                else if (value > 250)
                    percentage.setError(getString(R.string.the_maximum_value_is_250));
                else if (value % 10 != 0)
                    percentage.setError(getString(R.string.the_step_size_is_10));
                else percentage.setError(null);
            }
        });
        duration.addTextChangedListener(this);

        TelestoApp.getConnectionService().registerStateCallback(this);
        stateChanged(TelestoApp.getConnectionService().getState());
    }

    @Override
    protected void onDestroy() {
        TelestoApp.getConnectionService().unregisterStateCallback(this);
        if (dialog != null) dialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void stateChanged(TelestoState state) {
        runOnUiThread(() -> {
            if (state == TelestoState.CONNECTED) new CheckOperatingModeTask().execute();
            else {
                text.setVisibility(View.VISIBLE);
                viewContainer.setVisibility(View.GONE);
                if (!windowError) {
                    windowError = true;
                    windowBackground.startTransition(500);
                }
                text.setText(ExceptionTranslator.getString(new DisconnectedException()));
            }
        });
    }

    private void doSanityChecks() {
        boolean success = true;
        String text = percentage.getText().toString();
        if (text.length() == 0) success = false;
        else {
            int value = Integer.parseInt(text);
            if (value == 100) success = false;
            else if (value > 250) success = false;
            else if (value % 10 != 0) success = false;
        }
        if (!duration.isValid()) success = false;
        setTBR.setEnabled(success);
    }

    @Override
    protected void setup() {
        setContentView(R.layout.activity_tbr);
        super.setup();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        doSanityChecks();
    }

    public void setTBRClicked(View view) {
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.set_tbr_dialog)
                .setMessage(Html.fromHtml(getString(R.string.do_you_really_want_to_run_your_basal_rate_at_for, Integer.parseInt(percentage.getText().toString()), duration.getText().toString())))
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> new TBRActivity.SetTBRTask().execute()).show();
    }

    private class SetTBRTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (MessageRequestUtil.getActiveTBR() == null)
                    MessageRequestUtil.setTBR(Integer.parseInt(percentage.getText().toString()), duration.getDurationInMinutes());
                else MessageRequestUtil.changeTBR(Integer.parseInt(percentage.getText().toString()), duration.getDurationInMinutes());
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            text.setVisibility(View.VISIBLE);
            viewContainer.setVisibility(View.GONE);
            if (windowError) {
                windowError = false;
                windowBackground.reverseTransition(500);
            }
            text.setText(R.string.please_wait);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (exception != null) {
                text.setVisibility(View.VISIBLE);
                viewContainer.setVisibility(View.GONE);
                if (!windowError) {
                    windowError = true;
                    windowBackground.startTransition(500);
                }
                text.setText(ExceptionTranslator.getString(exception));
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
            } else finish();
        }
    }

    private class CheckOperatingModeTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (MessageRequestUtil.getOperatingMode() != OperatingMode.STARTED) exception = new PumpStoppedException(0);
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            text.setVisibility(View.VISIBLE);
            viewContainer.setVisibility(View.GONE);
            if (windowError) {
                windowError = false;
                windowBackground.reverseTransition(500);
            }
            text.setText(R.string.loading);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (exception != null) {
                text.setVisibility(View.VISIBLE);
                viewContainer.setVisibility(View.GONE);
                if (!windowError) {
                    windowError = true;
                    windowBackground.startTransition(500);
                }
                text.setText(ExceptionTranslator.getString(exception));
            } else {
                text.setVisibility(View.GONE);
                viewContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}
