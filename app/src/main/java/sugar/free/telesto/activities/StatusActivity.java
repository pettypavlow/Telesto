package sugar.free.telesto.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import sugar.free.telesto.R;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.activities.boluses.ExtendedBolusActivity;
import sugar.free.telesto.activities.boluses.MultiwaveBolusActivity;
import sugar.free.telesto.activities.boluses.StandardBolusActivity;
import sugar.free.telesto.descriptors.ActiveBasalRate;
import sugar.free.telesto.descriptors.ActiveBolus;
import sugar.free.telesto.descriptors.ActiveTBR;
import sugar.free.telesto.descriptors.BatteryStatus;
import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.descriptors.CartridgeStatus;
import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.ExceptionTranslator;
import sugar.free.telesto.utils.MessageRequestUtil;

public class StatusActivity extends BaseActivity implements ConnectionService.StateCallback, NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog alertDialog = null;

    private boolean operatingModeError;
    private boolean cartridgeError;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView connectionStatus;
    private TextView statusAge;

    private LinearLayout operatingModeContainer;
    private TextView operatingModeValue;
    private Button operatingModeButton;

    private LinearLayout cartridgeStatusContainer;
    private TextView cartridgeStatusValue;

    private LinearLayout activeBolus1Container;
    private TextView activeBolus1Amount;
    private TextView activeBolus1Duration;
    private ProgressBar activeBolus1ProgressBar;
    private Button activeBolus1Cancel;

    private LinearLayout activeBolus2Container;
    private TextView activeBolus2Amount;
    private TextView activeBolus2Duration;
    private ProgressBar activeBolus2ProgressBar;
    private Button activeBolus2Cancel;

    private LinearLayout activeBolus3Container;
    private TextView activeBolus3Amount;
    private TextView activeBolus3Duration;
    private ProgressBar activeBolus3ProgressBar;
    private Button activeBolus3Cancel;

    private LinearLayout activeTBRContainer;
    private TextView activeTBRAmount;
    private TextView activeTBRDuration;
    private ProgressBar activeTBRProgressBar;
    private Button activeTBRCancel;

    private LinearLayout activeBasalRateContainer;
    private TextView activeBasalRateAmount;
    private TextView activeBasalRateOriginalAmount;

    private LinearLayout batteryStatusContainer;
    private TextView batteryAmount;

    private Timer updateStatusTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        setSupportActionBar(findViewById(R.id.toolbar));

        connectionStatus = findViewById(R.id.connection_status);
        statusAge = findViewById(R.id.status_age);
        operatingModeContainer = findViewById(R.id.operating_mode_container);
        operatingModeValue = findViewById(R.id.operating_mode_value);
        operatingModeButton = findViewById(R.id.operating_mode_button);
        cartridgeStatusContainer = findViewById(R.id.cartridge_status_container);
        cartridgeStatusValue = findViewById(R.id.cartridge_status_value);
        activeBolus1Container = findViewById(R.id.active_bolus_1_container);
        activeBolus1Amount = findViewById(R.id.active_bolus_1_amount);
        activeBolus1Duration = findViewById(R.id.active_bolus_1_duration);
        activeBolus1ProgressBar = findViewById(R.id.active_bolus_1_progress_bar);
        activeBolus1Cancel = findViewById(R.id.active_bolus_1_cancel);
        activeBolus2Container = findViewById(R.id.active_bolus_2_container);
        activeBolus2Amount = findViewById(R.id.active_bolus_2_amount);
        activeBolus2Duration = findViewById(R.id.active_bolus_2_duration);
        activeBolus2ProgressBar = findViewById(R.id.active_bolus_2_progress_bar);
        activeBolus2Cancel = findViewById(R.id.active_bolus_2_cancel);
        activeBolus3Container = findViewById(R.id.active_bolus_3_container);
        activeBolus3Amount = findViewById(R.id.active_bolus_3_amount);
        activeBolus3Duration = findViewById(R.id.active_bolus_3_duration);
        activeBolus3ProgressBar = findViewById(R.id.active_bolus_3_progress_bar);
        activeBolus3Cancel = findViewById(R.id.active_bolus_3_cancel);
        activeTBRContainer = findViewById(R.id.active_tbr_container);
        activeTBRAmount = findViewById(R.id.active_tbr_amount);
        activeTBRDuration = findViewById(R.id.active_tbr_duration);
        activeTBRProgressBar = findViewById(R.id.active_tbr_progress_bar);
        activeTBRCancel = findViewById(R.id.active_tbr_cancel);
        activeBasalRateContainer = findViewById(R.id.active_basal_rate_container);
        activeBasalRateAmount = findViewById(R.id.active_basal_rate_amount);
        activeBasalRateOriginalAmount = findViewById(R.id.active_basal_rate_original_amount);
        activeBasalRateOriginalAmount.setPaintFlags(activeBasalRateOriginalAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        batteryStatusContainer = findViewById(R.id.battery_status_container);
        batteryAmount = findViewById(R.id.battery_amount);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        updateConnectionStatus(TelestoApp.getConnectionService().getState());
        updateStatusAge(TelestoApp.getStatusService().getLastUpdated().getValue());
        updateActiveBoluses(TelestoApp.getStatusService().getActiveBoluses().getValue());
        updateActiveTBR(TelestoApp.getStatusService().getActiveTBR().getValue());
        updateOperatingMode(TelestoApp.getStatusService().getOperatingMode().getValue());
        updateBasalRate(TelestoApp.getStatusService().getActiveBasalRate().getValue(), TelestoApp.getStatusService().getActiveTBR().getValue());
        updateBatteryStatus(TelestoApp.getStatusService().getBatteryStatus().getValue());
        updateCartridgeStatus(TelestoApp.getStatusService().getCartridgeStatus().getValue());

        TelestoApp.getConnectionService().registerStateCallback(this);
        updateStatus();

        Date lastStatusTime = TelestoApp.getStatusService().getLastUpdated().getValue();
        long statusTimeDelay = lastStatusTime == null ? 60000 : (System.currentTimeMillis() - lastStatusTime.getTime()) % (1000 * 60);

        updateStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED)
                    runOnUiThread(StatusActivity.this::updateStatus);
            }
        }, statusTimeDelay, 60 * 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.status_menu, menu);
        return true;
    }

    private void updateStatus() {
        TelestoApp.getStatusService().getLastUpdated().observe(this, this::updateStatusAge);
        TelestoApp.getStatusService().getActiveBoluses().observe(this, this::updateActiveBoluses);
        TelestoApp.getStatusService().getActiveTBR().observe(this, activeTBR -> {
            updateActiveTBR(activeTBR);
            updateBasalRate(TelestoApp.getStatusService().getActiveBasalRate().getValue(), activeTBR);
        });
        TelestoApp.getStatusService().getOperatingMode().observe(this, this::updateOperatingMode);
        TelestoApp.getStatusService().getActiveBasalRate().observe(this, basalRate -> {
            updateBasalRate(basalRate, TelestoApp.getStatusService().getActiveTBR().getValue());
        });
        TelestoApp.getStatusService().getBatteryStatus().observe(this, this::updateBatteryStatus);
        TelestoApp.getStatusService().getCartridgeStatus().observe(this, this::updateCartridgeStatus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView);
            return true;
        } else if (item.getItemId() == R.id.nav_settings) {
            startActivity(new Intent(this, PreferencesActivity.class));
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        TelestoApp.getConnectionService().unregisterStateCallback(this);
        updateStatusTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TelestoApp.getStatusService().requestRate(this, 200);
    }

    @Override
    protected void onPause() {
        if (alertDialog != null) alertDialog.dismiss();
        TelestoApp.getStatusService().withdrawRateRequest(this);
        super.onPause();
    }

    public void operatingModeButtonClicked(View view) {
        switch (TelestoApp.getStatusService().getOperatingMode().getValue()) {
            case STOPPED:
            case PAUSED:
                showConfirmationDialog(new SpannedString(getString(R.string.start_pump)),
                        Html.fromHtml(getString(R.string.do_you_really_want_to_start_your_pump)),
                        () -> {
                            Thread thread = new Thread(() -> {
                                try {
                                    MessageRequestUtil.setOperatingMode(OperatingMode.STARTED);
                                } catch (Exception e) {
                                    ExceptionTranslator.makeToast(StatusActivity.this, e);
                                }
                            });
                            thread.start();
                        });
                break;
            case STARTED:
                showConfirmationDialog(new SpannedString(getString(R.string.stop_pump)),
                        Html.fromHtml(getString(R.string.do_you_really_want_to_stop_your_pump)),
                        () -> {
                            Thread thread = new Thread(() -> {
                                try {
                                    MessageRequestUtil.setOperatingMode(OperatingMode.STOPPED);
                                } catch (Exception e) {
                                    ExceptionTranslator.makeToast(StatusActivity.this, e);
                                }
                            });
                            thread.start();
                        });
                break;
        }
    }

    public void cancelBolus1Clicked(View view) {
        cancelBolus(TelestoApp.getStatusService().getActiveBoluses().getValue()[0].getBolusID());
    }

    public void cancelBolus2Clicked(View view) {
        cancelBolus(TelestoApp.getStatusService().getActiveBoluses().getValue()[1].getBolusID());
    }

    public void cancelBolus3Clicked(View view) {
        cancelBolus(TelestoApp.getStatusService().getActiveBoluses().getValue()[2].getBolusID());
    }

    public void cancelTBRClicked(View view) {
        showConfirmationDialog(new SpannedString(getString(R.string.cancel_tbr)),
                Html.fromHtml(getString(R.string.do_you_really_want_to_cancel_this_bolus)),
                () -> {
                    Thread thread = new Thread(() -> {
                        try {
                            MessageRequestUtil.cancelTBR();
                        } catch (Exception e) {
                            ExceptionTranslator.makeToast(StatusActivity.this, e);
                        }
                    });
                    thread.start();
                });
    }

    private void cancelBolus(int bolusId) {
        showConfirmationDialog(new SpannedString(getString(R.string.cancel_bolus)),
                Html.fromHtml(getString(R.string.do_you_really_want_to_cancel_this_bolus)),
                () -> {
                    Thread thread = new Thread(() -> {
                        try {
                            MessageRequestUtil.cancelBolus(bolusId);
                        } catch (Exception e) {
                            ExceptionTranslator.makeToast(StatusActivity.this, e);
                        }
                    });
                    thread.start();
                });
    }

    private void updateConnectionStatus(TelestoState state) {
        if (state == TelestoState.CONNECTED) {
            connectionStatus.setVisibility(View.GONE);
            statusAge.setVisibility(View.GONE);
            activeBolus1Cancel.setVisibility(View.VISIBLE);
            activeBolus2Cancel.setVisibility(View.VISIBLE);
            activeBolus3Cancel.setVisibility(View.VISIBLE);
            activeTBRCancel.setVisibility(View.VISIBLE);
            operatingModeButton.setVisibility(View.VISIBLE);
        } else {
            if (alertDialog != null) alertDialog.dismiss();
            connectionStatus.setVisibility(View.VISIBLE);
            statusAge.setVisibility(View.VISIBLE);
            activeBolus1Cancel.setVisibility(View.GONE);
            activeBolus2Cancel.setVisibility(View.GONE);
            activeBolus3Cancel.setVisibility(View.GONE);
            activeTBRCancel.setVisibility(View.GONE);
            operatingModeButton.setVisibility(View.GONE);
            switch (state) {
                case CONNECTING:
                case SATL_SYN_REQUEST:
                case APP_CONNECT_MESSAGE:
                    connectionStatus.setText(R.string.connecting);
                    break;
                case WAITING:
                    connectionStatus.setText(R.string.waiting);
                    break;
                default:
                    connectionStatus.setVisibility(View.GONE);
            }
            updateStatus();
        }
    }

    private void updateStatusAge(Date date) {
        if (date == null) statusAge.setVisibility(View.GONE);
        else {
            statusAge.setVisibility(connectionStatus.getVisibility());
            int minutes = (int) ((System.currentTimeMillis() - date.getTime()) / 1000 / 60);
            int hours = minutes / 60;
            int days = hours / 24;
            String agePart;
            if (days > 0)
                agePart = getResources().getQuantityString(R.plurals.days_ago, days, days);
            else if (hours > 0)
                agePart = getResources().getQuantityString(R.plurals.hours_ago, hours, hours);
            else
                agePart = getResources().getQuantityString(R.plurals.minutes_ago, minutes, minutes);
            statusAge.setText(getString(R.string.last_time_connected, agePart));
        }
    }

    private void updateOperatingMode(OperatingMode operatingMode) {
        if (operatingMode == null) operatingModeContainer.setVisibility(View.GONE);
        else {
            operatingModeContainer.setVisibility(View.VISIBLE);
            switch (operatingMode) {
                case STOPPED:
                    operatingModeValue.setText(getString(R.string.stopped));
                    operatingModeButton.setText(R.string.start);
                    if (!operatingModeError) {
                        ((TransitionDrawable) operatingModeContainer.getBackground()).startTransition(200);
                        operatingModeError = true;
                    }
                    break;
                case STARTED:
                    operatingModeValue.setText(getString(R.string.started));
                    operatingModeButton.setText(R.string.stop);
                    if (operatingModeError) {
                        ((TransitionDrawable) operatingModeContainer.getBackground()).reverseTransition(200);
                        operatingModeError = false;
                    }
                    break;
                case PAUSED:
                    operatingModeValue.setText(getString(R.string.paused));
                    operatingModeButton.setText(R.string.start);
                    if (!operatingModeError) {
                        ((TransitionDrawable) operatingModeContainer.getBackground()).startTransition(200);
                        operatingModeError = true;
                    }
                    break;
            }
        }
    }

    private void updateCartridgeStatus(CartridgeStatus cartridgeStatus) {
        if (cartridgeStatus == null) cartridgeStatusContainer.setVisibility(View.GONE);
        else {
            cartridgeStatusContainer.setVisibility(View.VISIBLE);
            if (cartridgeStatus.isInserted()) {
                if (cartridgeError) {
                    ((TransitionDrawable) cartridgeStatusContainer.getBackground()).reverseTransition(200);
                    cartridgeError = false;
                }
                cartridgeStatusValue.setText(new DecimalFormat("##0.00").format(cartridgeStatus.getRemainingAmount()) + " U");
            } else {
                if (!cartridgeError) {
                    ((TransitionDrawable) cartridgeStatusContainer.getBackground()).startTransition(200);
                    cartridgeError = true;
                }
                cartridgeStatusValue.setText(R.string.not_inserted);
            }
        }
    }

    private void updateActiveBoluses(ActiveBolus[] activeBoluses) {
        ActiveBolus activeBolus1 = activeBoluses != null && activeBoluses.length >= 1 ? activeBoluses[0] : null;
        ActiveBolus activeBolus2 = activeBoluses != null && activeBoluses.length >= 2 ? activeBoluses[1] : null;
        ActiveBolus activeBolus3 = activeBoluses != null && activeBoluses.length >= 3 ? activeBoluses[2] : null;
        updateActiveBolus1(activeBolus1);
        updateActiveBolus2(activeBolus2);
        updateActiveBolus3(activeBolus3);
    }

    private void updateActiveBolus1(ActiveBolus activeBolus) {
        if (activeBolus == null) activeBolus1Container.setVisibility(View.GONE);
        else {
            int adjustedRemainingDuration = activeBolus.getRemainingDuration() - ((int) (( System.currentTimeMillis() - TelestoApp.getStatusService().getLastUpdated().getValue().getTime()) / 1000 / 60));
            if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED && (adjustedRemainingDuration <= 0 || activeBolus.getBolusType() == BolusType.STANDARD)) {
                activeBolus1Container.setVisibility(View.GONE);
                return;
            }
            activeBolus1Container.setVisibility(View.VISIBLE);
            activeBolus1ProgressBar.setMax((int) (activeBolus.getInitialAmount() * 100));
            if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED) {
                switch (activeBolus.getBolusType()) {
                    case MULTIWAVE:
                        activeBolus1Amount.setText(new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                        activeBolus1ProgressBar.setVisibility(View.GONE);
                        break;
                    case EXTENDED:
                        double remainingAmount = activeBolus.getInitialAmount() / ((double) activeBolus.getRemainingDuration() / activeBolus.getRemainingAmount() * activeBolus.getInitialAmount()) * adjustedRemainingDuration;
                        activeBolus1Amount.setText(new DecimalFormat("##0.00").format(remainingAmount) + " U / " +
                                new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                        activeBolus1ProgressBar.setVisibility(View.VISIBLE);
                        activeBolus1ProgressBar.setProgress((int) (remainingAmount * 100));
                }
            } else {
                activeBolus1Amount.setText(new DecimalFormat("##0.00").format(activeBolus.getRemainingAmount()) + " U / " +
                        new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                activeBolus1ProgressBar.setVisibility(View.VISIBLE);
                activeBolus1ProgressBar.setProgress((int) (activeBolus.getRemainingAmount() * 100));
            }
            int hours = adjustedRemainingDuration / 60;
            int minutes = adjustedRemainingDuration - hours * 60;
            activeBolus1Duration.setText(new DecimalFormat("#0").format(hours) + ":" + new DecimalFormat("00").format(minutes));
            switch (activeBolus.getBolusType()) {
                case STANDARD:
                    activeBolus1Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_blue));
                    activeBolus1Duration.setVisibility(View.GONE);
                    break;
                case EXTENDED:
                    activeBolus1Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_orange));
                    activeBolus1Duration.setVisibility(View.VISIBLE);
                    break;
                case MULTIWAVE:
                    activeBolus1Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_gray));
                    activeBolus1Duration.setVisibility(View.VISIBLE);
                    break;
            }
            int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
            activeBolus1Container.setPadding(padding, padding, padding, padding);
        }
    }

    private void updateActiveBolus2(ActiveBolus activeBolus) {
        if (activeBolus == null) activeBolus2Container.setVisibility(View.GONE);
        else {
            int adjustedRemainingDuration = activeBolus.getRemainingDuration() - ((int) (( System.currentTimeMillis() - TelestoApp.getStatusService().getLastUpdated().getValue().getTime()) / 1000 / 60));
            if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED && (adjustedRemainingDuration <= 0 || activeBolus.getBolusType() == BolusType.STANDARD)) {
                activeBolus2Container.setVisibility(View.GONE);
                return;
            }
            activeBolus2Container.setVisibility(View.VISIBLE);
            activeBolus2ProgressBar.setMax((int) (activeBolus.getInitialAmount() * 100));
            if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED) {
                switch (activeBolus.getBolusType()) {
                    case MULTIWAVE:
                        activeBolus2Amount.setText(new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                        activeBolus2ProgressBar.setVisibility(View.GONE);
                        break;
                    case EXTENDED:
                        double remainingAmount = activeBolus.getInitialAmount() / ((double) activeBolus.getRemainingDuration() / activeBolus.getRemainingAmount() * activeBolus.getInitialAmount()) * adjustedRemainingDuration;
                        activeBolus2Amount.setText(new DecimalFormat("##0.00").format(remainingAmount) + " U / " +
                                new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                        activeBolus2ProgressBar.setVisibility(View.VISIBLE);
                        activeBolus2ProgressBar.setProgress((int) (remainingAmount * 100));
                }
            } else {
                activeBolus2Amount.setText(new DecimalFormat("##0.00").format(activeBolus.getRemainingAmount()) + " U / " +
                        new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                activeBolus2ProgressBar.setVisibility(View.VISIBLE);
                activeBolus2ProgressBar.setProgress((int) (activeBolus.getRemainingAmount() * 100));
            }
            int hours = adjustedRemainingDuration / 60;
            int minutes = adjustedRemainingDuration - hours * 60;
            activeBolus2Duration.setText(new DecimalFormat("#0").format(hours) + ":" + new DecimalFormat("00").format(minutes));
            switch (activeBolus.getBolusType()) {
                case STANDARD:
                    activeBolus2Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_blue));
                    activeBolus2Duration.setVisibility(View.GONE);
                    break;
                case EXTENDED:
                    activeBolus2Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_orange));
                    activeBolus2Duration.setVisibility(View.VISIBLE);
                    break;
                case MULTIWAVE:
                    activeBolus2Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_gray));
                    activeBolus2Duration.setVisibility(View.VISIBLE);
                    break;
            }
            int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
            activeBolus2Container.setPadding(padding, padding, padding, padding);
        }
    }

    private void updateActiveBolus3(ActiveBolus activeBolus) {
        if (activeBolus == null) activeBolus3Container.setVisibility(View.GONE);
        else {
            int adjustedRemainingDuration = activeBolus.getRemainingDuration() - ((int) (( System.currentTimeMillis() - TelestoApp.getStatusService().getLastUpdated().getValue().getTime()) / 1000 / 60));
            if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED && (adjustedRemainingDuration <= 0 || activeBolus.getBolusType() == BolusType.STANDARD)) {
                activeBolus3Container.setVisibility(View.GONE);
                return;
            }
            activeBolus3Container.setVisibility(View.VISIBLE);
            activeBolus3ProgressBar.setMax((int) (activeBolus.getInitialAmount() * 100));
            if (TelestoApp.getConnectionService().getState() != TelestoState.CONNECTED) {
                switch (activeBolus.getBolusType()) {
                    case MULTIWAVE:
                        activeBolus3Amount.setText(new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                        activeBolus3ProgressBar.setVisibility(View.GONE);
                        break;
                    case EXTENDED:
                        double remainingAmount = activeBolus.getInitialAmount() / ((double) activeBolus.getRemainingDuration() / activeBolus.getRemainingAmount() * activeBolus.getInitialAmount()) * adjustedRemainingDuration;
                        activeBolus3Amount.setText(new DecimalFormat("##0.00").format(remainingAmount) + " U / " +
                                new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                        activeBolus3ProgressBar.setVisibility(View.VISIBLE);
                        activeBolus3ProgressBar.setProgress((int) (remainingAmount * 100));
                }
            } else {
                activeBolus3Amount.setText(new DecimalFormat("##0.00").format(activeBolus.getRemainingAmount()) + " U / " +
                        new DecimalFormat("##0.00").format(activeBolus.getInitialAmount()) + " U");
                activeBolus3ProgressBar.setVisibility(View.VISIBLE);
                activeBolus3ProgressBar.setProgress((int) (activeBolus.getRemainingAmount() * 100));
            }
            int hours = adjustedRemainingDuration / 60;
            int minutes = adjustedRemainingDuration - hours * 60;
            activeBolus3Duration.setText(new DecimalFormat("#0").format(hours) + ":" + new DecimalFormat("00").format(minutes));
            switch (activeBolus.getBolusType()) {
                case STANDARD:
                    activeBolus3Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_blue));
                    activeBolus3Duration.setVisibility(View.GONE);
                    break;
                case EXTENDED:
                    activeBolus3Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_orange));
                    activeBolus3Duration.setVisibility(View.VISIBLE);
                    break;
                case MULTIWAVE:
                    activeBolus3Container.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background_gray));
                    activeBolus3Duration.setVisibility(View.VISIBLE);
                    break;
            }
            int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
            activeBolus3Container.setPadding(padding, padding, padding, padding);
        }
    }

    private void updateActiveTBR(ActiveTBR activeTBR) {
        if (activeTBR == null) activeTBRContainer.setVisibility(View.GONE);
        else {
            int adjustedRemainingDuration = activeTBR.getRemainingDuration() - ((int) (( System.currentTimeMillis() - TelestoApp.getStatusService().getLastUpdated().getValue().getTime()) / 1000 / 60));
            if (adjustedRemainingDuration <= 0) {
                activeTBRContainer.setVisibility(View.GONE);
                return;
            }
            activeTBRContainer.setVisibility(View.VISIBLE);
            activeTBRAmount.setText(activeTBR.getPercentage() + " %");
            int initialHours = activeTBR.getInitialDuration() / 60;
            int initialMinutes = activeTBR.getInitialDuration() - initialHours * 60;
            int remainingHours = adjustedRemainingDuration / 60;
            int remainingMinutes = adjustedRemainingDuration - remainingHours * 60;
            activeTBRDuration.setText(new DecimalFormat("#0").format(remainingHours) + ":" + new DecimalFormat("00").format(remainingMinutes)
                    + " / " + new DecimalFormat("#0").format(initialHours) + ":" + new DecimalFormat("00").format(initialMinutes));
            activeTBRProgressBar.setMax(activeTBR.getInitialDuration());
            activeTBRProgressBar.setProgress(activeTBR.getRemainingDuration());
        }
    }

    private void updateBasalRate(ActiveBasalRate basalRate, ActiveTBR activeTBR) {
        if (basalRate == null) activeBasalRateContainer.setVisibility(View.GONE);
        else {
            activeBasalRateContainer.setVisibility(View.VISIBLE);
            if (activeTBR != null) {
                activeBasalRateAmount.setText(new DecimalFormat("#0.00")
                        .format(basalRate.getActiveBasalRate() / 100D * ((double) activeTBR.getPercentage())) + " U/h");
                activeBasalRateOriginalAmount.setVisibility(View.VISIBLE);
                activeBasalRateOriginalAmount.setText(new DecimalFormat("#0.00")
                        .format(basalRate.getActiveBasalRate()) + " U/h");

            } else {
                activeBasalRateAmount.setText(new DecimalFormat("#0.00").format(basalRate.getActiveBasalRate()) + " U/h");
                activeBasalRateOriginalAmount.setVisibility(View.GONE);
            }
        }
    }

    private void updateBatteryStatus(BatteryStatus batteryStatus) {
        if (batteryStatus == null) batteryStatusContainer.setVisibility(View.GONE);
        else {
            batteryStatusContainer.setVisibility(View.VISIBLE);
            batteryAmount.setText(batteryStatus.getBatteryAmount() + " %");
        }
    }

    @Override
    public void stateChanged(TelestoState state) {
        runOnUiThread(() -> updateConnectionStatus(state));
    }

    private void showConfirmationDialog(Spanned title, Spanned message, Runnable runnable) {
        if (alertDialog != null) alertDialog.dismiss();
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (d, i) -> runnable.run())
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Class<? extends Activity> activity = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_standard_bolus:
                activity = StandardBolusActivity.class;
                break;
            case R.id.nav_extended_bolus:
                activity = ExtendedBolusActivity.class;
                break;
            case R.id.nav_multiwave_bolus:
                activity = MultiwaveBolusActivity.class;
                break;
            case R.id.nav_tbr:
                activity = TBRActivity.class;
                break;
        }
        if (activity != null) startActivity(new Intent(this, activity));
        drawerLayout.closeDrawer(navigationView);
        return true;
    }
}
