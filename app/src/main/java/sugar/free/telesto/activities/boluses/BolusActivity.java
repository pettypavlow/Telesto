package sugar.free.telesto.activities.boluses;

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
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import sugar.free.telesto.R;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.activities.BackButtonActivity;
import sugar.free.telesto.descriptors.AvailableBolusTypes;
import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.exceptions.DisconnectedException;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.ExceptionTranslator;
import sugar.free.telesto.utils.MessageRequestUtil;
import sugar.free.telesto.views.DurationTextInput;

public abstract class BolusActivity extends BackButtonActivity implements TextWatcher, ConnectionService.StateCallback {

    private AlertDialog dialog;
    private AvailableBolusTypes availableBolusTypes;
    private TransitionDrawable windowBackground;
    private boolean windowError;
    private double minimumAmount;
    private double maximumAmount;

    private TextView text;
    private LinearLayout viewContainer;
    private TextInputLayout immediateAmountParent;
    private TextInputLayout extendedAmountParent;
    private TextInputLayout durationParent;
    private TextInputEditText immediateAmount;
    private TextInputEditText extendedAmount;
    private DurationTextInput duration;
    private Button deliver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immediateAmount = findViewById(R.id.immediate_amount);
        extendedAmount = findViewById(R.id.extended_amount);
        duration = findViewById(R.id.duration);
        deliver = findViewById(R.id.deliver);
        text = findViewById(R.id.text);
        viewContainer = findViewById(R.id.view_container);
        immediateAmountParent = findViewById(R.id.immediate_amount_parent);
        extendedAmountParent = findViewById(R.id.extended_amount_parent);
        durationParent = findViewById(R.id.duration_parent);

        windowBackground = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.error_transition);
        getWindow().setBackgroundDrawable(windowBackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= 28) getWindow().setNavigationBarDividerColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        immediateAmount.addTextChangedListener(this);
        immediateAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!text.matches(".*\\d+.*")) {
                    immediateAmount.setError(getString(R.string.the_field_must_not_be_empty));
                    return;
                }
                double amount = Double.parseDouble(text.replace(",", "."));
                if (amount < minimumAmount)
                    immediateAmount.setError(getString(R.string.the_minimum_amount_is, minimumAmount));
                else if (amount > maximumAmount)
                    immediateAmount.setError(getString(R.string.the_maximum_amount_is, maximumAmount));
                else immediateAmount.setError(null);
            }
        });
        extendedAmount.addTextChangedListener(this);
        extendedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!text.matches(".*\\d+.*")) {
                    extendedAmount.setError(getString(R.string.the_field_must_not_be_empty));
                    return;
                }
                double amount = Double.parseDouble(text.replace(",", "."));
                if (amount < minimumAmount)
                    extendedAmount.setError(getString(R.string.the_minimum_amount_is, minimumAmount));
                else if (amount > maximumAmount)
                    extendedAmount.setError(getString(R.string.the_maximum_amount_is, maximumAmount));
                else extendedAmount.setError(null);
            }
        });
        duration.addTextChangedListener(this);

        switch (getBolusType()) {
            case STANDARD:
                extendedAmountParent.setVisibility(View.GONE);
                durationParent.setVisibility(View.GONE);
                extendedAmount.setVisibility(View.GONE);
                duration.setVisibility(View.GONE);
                break;
            case EXTENDED:
                immediateAmountParent.setVisibility(View.GONE);
                immediateAmount.setVisibility(View.GONE);
                break;
        }

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
            if (state == TelestoState.CONNECTED) new ReadBolusConstraintsTask().execute();
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
        if (immediateAmount.getVisibility() != View.GONE) {
            String text = immediateAmount.getText().toString().replaceAll(",", ".");
            if (text.matches(".*\\d+.*")) {
                double amount = Double.parseDouble(text);
                if (amount < minimumAmount || amount > maximumAmount) success = false;
            } else success = false;
        }
        if (extendedAmount.getVisibility() != View.GONE) {
            String text = extendedAmount.getText().toString().replaceAll(",", ".");
            if (text.matches(".*\\d+.*")) {
                double amount = Double.parseDouble(text);
                if (amount < minimumAmount || amount > maximumAmount) success = false;
            } else success = false;
        }
        if (duration.getVisibility() != View.GONE && !duration.isValid()) success = false;
        deliver.setEnabled(success);
    }

    protected abstract BolusType getBolusType();

    @Override
    protected void setup() {
        setContentView(R.layout.activity_bolus);
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

    public void deliverClicked(View view) {
        String message = null;
        String title = null;
        switch (getBolusType()) {
            case MULTIWAVE:
                title = getString(R.string.deliver_multiwave_bolus);
                message = getString(R.string.do_you_really_want_to_deliver_immediately_and_extended,
                        Double.parseDouble(immediateAmount.getText().toString()),
                        Double.parseDouble(extendedAmount.getText().toString()),
                        duration.getText());
                break;
            case STANDARD:
                title = getString(R.string.deliver_standard_bolus);
                message = getString(R.string.do_you_really_want_to_deliver_immediately,
                        Double.parseDouble(immediateAmount.getText().toString()));
                break;
            case EXTENDED:
                title = getString(R.string.deliver_extended_bolus);
                message = getString(R.string.do_you_really_want_to_deliver_extended,
                        Double.parseDouble(extendedAmount.getText().toString()),
                        duration.getText());
                break;
        }
        dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> new DeliverBolusTask().execute()).show();
    }

    private class DeliverBolusTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                MessageRequestUtil.deliverBolus(getBolusType(), immediateAmount.getVisibility() == View.GONE ? 0 : Double.parseDouble(immediateAmount.getText().toString()),
                        extendedAmount.getVisibility() == View.GONE ? 0 : Double.parseDouble(extendedAmount.getText().toString()),
                        duration.getDurationInMinutes());
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

    private class ReadBolusConstraintsTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                availableBolusTypes = MessageRequestUtil.getAvailableBolusTypes();
                minimumAmount = MessageRequestUtil.getFactoryMinBolusAmount();
                maximumAmount = MessageRequestUtil.getMaxBolusAmount();
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
            } else if (!availableBolusTypes.isBolusTypeAvailable(getBolusType())) {
                text.setVisibility(View.VISIBLE);
                viewContainer.setVisibility(View.GONE);
                if (!windowError) {
                    windowError = true;
                    windowBackground.startTransition(500);
                }
                text.setText(getString(R.string.bolus_type_not_available));
            } else {
                text.setVisibility(View.GONE);
                viewContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}
