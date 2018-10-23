package sugar.free.telesto.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.descriptors.ActiveBasalRate;
import sugar.free.telesto.descriptors.ActiveBolus;
import sugar.free.telesto.descriptors.ActiveTBR;
import sugar.free.telesto.descriptors.BasalProfile;
import sugar.free.telesto.descriptors.BatteryStatus;
import sugar.free.telesto.descriptors.BatteryType;
import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.descriptors.CartridgeStatus;
import sugar.free.telesto.descriptors.CartridgeType;
import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.descriptors.SymbolStatus;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.descriptors.TotalDailyDose;
import sugar.free.telesto.exceptions.ConnectionLostException;
import sugar.free.telesto.exceptions.DisconnectedException;
import sugar.free.telesto.parser.app_layer.status.GetPumpStatusRegisterMessage;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.MessageRequestUtil;

public class StatusService extends Service implements ConnectionService.StateCallback, TelestoApp.InitializationCompletedCallback {

    private Map<Object, Long> requestRates = new HashMap<>();
    private long currentRate;

    private SharedPreferences sharedPreferences;
    private LocalBinder localBinder = new LocalBinder();
    private Thread thread;

    private MutableLiveData<Date> lastUpdated = new MutableLiveData<>();
    private MutableLiveData<OperatingMode> operatingMode = new MutableLiveData<>();
    private MutableLiveData<BatteryStatus> batteryStatus = new MutableLiveData<>();
    private MutableLiveData<CartridgeStatus> cartridgeStatus = new MutableLiveData<>();
    private MutableLiveData<TotalDailyDose> totalDailyDose = new MutableLiveData<>();
    private MutableLiveData<ActiveBasalRate> activeBasalRate = new MutableLiveData<>();
    private MutableLiveData<ActiveTBR> activeTBR = new MutableLiveData<>();
    private MutableLiveData<ActiveBolus[]> activeBoluses = new MutableLiveData<>();

    public void requestRate(Object lock, long rate) {
        requestRates.put(lock, rate);
        determineFastestRate();
    }

    public void withdrawRateRequest(Object lock) {
        requestRates.remove(lock);
        determineFastestRate();
    }

    private void determineFastestRate() {
        currentRate = 0;
        for (long rate : requestRates.values()) {
            if (rate > 0) {
                if (currentRate > 0) {
                    if (rate < currentRate) currentRate = rate;
                } else currentRate = rate;
            }
        }
        if (TelestoApp.getConnectionService().getState() == TelestoState.CONNECTED) {
            if (thread != null) thread.interrupt();
            thread = new Thread(this::threadCall);
            thread.start();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(getPackageName() + ".STATUS", MODE_PRIVATE);
        if (sharedPreferences.contains("statusLastUpdated"))
            lastUpdated.setValue(new Date(sharedPreferences.getLong("statusLastUpdated", 0)));
        if (sharedPreferences.contains("statusOperatingMode"))
            operatingMode.setValue(OperatingMode.valueOf(sharedPreferences.getString("statusOperatingMode", null)));
        if (sharedPreferences.contains("statusCartridgeType")
                && sharedPreferences.contains("statusCartridgeSymbolStatus")
                && sharedPreferences.contains("statusCartridgeRemainingAmount")
                && sharedPreferences.contains("statusCartridgeInserted")) {
            CartridgeStatus cartridgeStatus = new CartridgeStatus();
            cartridgeStatus.setInserted(sharedPreferences.getBoolean("statusCartridgeInserted", false));
            cartridgeStatus.setCartridgeType(CartridgeType.valueOf(sharedPreferences.getString("statusCartridgeType", null)));
            cartridgeStatus.setSymbolStatus(SymbolStatus.valueOf(sharedPreferences.getString("statusCartridgeSymbolStatus", null)));
            cartridgeStatus.setRemainingAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusCartridgeRemainingAmount", 0)));
            this.cartridgeStatus.setValue(cartridgeStatus);
        }
        if (sharedPreferences.contains("statusBatteryType")
                && sharedPreferences.contains("statusBatteryAmount")
                && sharedPreferences.contains("statusBatterySymbolStatus")) {
            BatteryStatus batteryStatus = new BatteryStatus();
            batteryStatus.setBatteryType(BatteryType.valueOf(sharedPreferences.getString("statusBatteryType", null)));
            batteryStatus.setBatteryAmount(sharedPreferences.getInt("statusBatteryAmount", 0));
            batteryStatus.setSymbolStatus(SymbolStatus.valueOf(sharedPreferences.getString("statusBatterySymbolStatus", null)));
            this.batteryStatus.setValue(batteryStatus);
        }
        if (sharedPreferences.contains("statusTDDBolus")
                && sharedPreferences.contains("statusTDDBasal")
                && sharedPreferences.contains("statusTDDBolusAndBasal")) {
            TotalDailyDose totalDailyDose = new TotalDailyDose();
            totalDailyDose.setBolus(Double.longBitsToDouble(sharedPreferences.getLong("statusTDDBolus", 0)));
            totalDailyDose.setBasal(Double.longBitsToDouble(sharedPreferences.getLong("statusTDDBasal", 0)));
            totalDailyDose.setBolusAndBasal(Double.longBitsToDouble(sharedPreferences.getLong("statusTDDBolusAndBasal", 0)));
            this.totalDailyDose.setValue(totalDailyDose);
        }
        if (sharedPreferences.contains("statusActiveBasalRateProfile")
                && sharedPreferences.contains("statusActiveBasalProfileName")
                && sharedPreferences.contains("statusActiveBasalRate")) {
            ActiveBasalRate activeBasalRate = new ActiveBasalRate();
            activeBasalRate.setActiveBasalProfile(BasalProfile.valueOf(sharedPreferences.getString("statusActiveBasalRateProfile", null)));
            activeBasalRate.setActiveBasalProfileName(sharedPreferences.getString("statusActiveBasalProfileName", null));
            activeBasalRate.setActiveBasalRate(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBasalRate", 0)));
            this.activeBasalRate.setValue(activeBasalRate);
        }
        if (sharedPreferences.contains("statusActiveTBRPercentage")
                && sharedPreferences.contains("statusActiveTBRRemainingDuration")
                && sharedPreferences.contains("statusActiveTBRInitialDuration")) {
            ActiveTBR activeTBR = new ActiveTBR();
            activeTBR.setPercentage(sharedPreferences.getInt("statusActiveTBRPercentage", 0));
            activeTBR.setRemainingDuration(sharedPreferences.getInt("statusActiveTBRRemainingDuration", 0));
            activeTBR.setInitialDuration(sharedPreferences.getInt("statusActiveTBRInitialDuration", 0));
            this.activeTBR.setValue(activeTBR);
        }
        ActiveBolus activeBolus1 = null;
        if (sharedPreferences.contains("statusActiveBolus1BolusID")
                && sharedPreferences.contains("statusActiveBolus1BolusType")
                && sharedPreferences.contains("statusActiveBolus1InitialAmount")
                && sharedPreferences.contains("statusActiveBolus1RemainingAmount")
                && sharedPreferences.contains("statusActiveBolus1RemainingDuration")) {
            activeBolus1 = new ActiveBolus();
            activeBolus1.setBolusID(sharedPreferences.getInt("statusActiveBolus1BolusID", 0));
            activeBolus1.setBolusType(BolusType.valueOf(sharedPreferences.getString("statusActiveBolus1BolusType", null)));
            activeBolus1.setInitialAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBolus1InitialAmount", 0)));
            activeBolus1.setRemainingAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBolus1RemainingAmount", 0)));
            activeBolus1.setRemainingDuration(sharedPreferences.getInt("statusActiveBolus1RemainingDuration", 0));
        }
        ActiveBolus activeBolus2 = null;
        if (sharedPreferences.contains("statusActiveBolus2BolusID")
                && sharedPreferences.contains("statusActiveBolus2BolusType")
                && sharedPreferences.contains("statusActiveBolus2InitialAmount")
                && sharedPreferences.contains("statusActiveBolus2RemainingAmount")
                && sharedPreferences.contains("statusActiveBolus2RemainingDuration")) {
            activeBolus2 = new ActiveBolus();
            activeBolus2.setBolusID(sharedPreferences.getInt("statusActiveBolus2BolusID", 0));
            activeBolus2.setBolusType(BolusType.valueOf(sharedPreferences.getString("statusActiveBolus2BolusType", null)));
            activeBolus2.setInitialAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBolus2InitialAmount", 0)));
            activeBolus2.setRemainingAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBolus2RemainingAmount", 0)));
            activeBolus2.setRemainingDuration(sharedPreferences.getInt("statusActiveBolus2RemainingDuration", 0));
        }
        ActiveBolus activeBolus3 = null;
        if (sharedPreferences.contains("statusActiveBolus3BolusID")
                && sharedPreferences.contains("statusActiveBolus3BolusType")
                && sharedPreferences.contains("statusActiveBolus3InitialAmount")
                && sharedPreferences.contains("statusActiveBolus3RemainingAmount")
                && sharedPreferences.contains("statusActiveBolus3RemainingDuration")) {
            activeBolus3 = new ActiveBolus();
            activeBolus3.setBolusID(sharedPreferences.getInt("statusActiveBolus3BolusID", 0));
            activeBolus3.setBolusType(BolusType.valueOf(sharedPreferences.getString("statusActiveBolus3BolusType", null)));
            activeBolus3.setInitialAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBolus3InitialAmount", 0)));
            activeBolus3.setRemainingAmount(Double.longBitsToDouble(sharedPreferences.getLong("statusActiveBolus3RemainingAmount", 0)));
            activeBolus3.setRemainingDuration(sharedPreferences.getInt("statusActiveBolus3RemainingDuration", 0));
        }
        ActiveBolus[] activeBoluses = new ActiveBolus[(activeBolus1 != null ? 1 : 0) + (activeBolus2 != null ? 1 : 0) + (activeBolus2 != null ? 1 : 0)];
        if (activeBolus1 != null) activeBoluses[0] = activeBolus1;
        if (activeBolus2 != null) activeBoluses[1] = activeBolus2;
        if (activeBolus3 != null) activeBoluses[2] = activeBolus3;
        this.activeBoluses.setValue(activeBoluses);
        TelestoApp.awaitCompletedInitialization(this);
    }

    @Override
    public void onInitializationCompleted() {
        requestRate(this, 5000);
        TelestoApp.getConnectionService().registerStateCallback(this);
    }

    @Override
    public void onDestroy() {
        TelestoApp.getConnectionService().unregisterStateCallback(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void stateChanged(TelestoState telestoState) {
        if (telestoState == TelestoState.CONNECTED) {
            thread = new Thread(this::threadCall);
            thread.start();
        } else if (thread != null) thread.interrupt();
    }

    private void threadCall() {
        try {
            update(true, true, true, true, true, true, true);
            MessageRequestUtil.resetPumpStatusRegister(true, true, true, true, true, true, true);
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(currentRate);
                queryStatusRegister();
            }
        } catch (InterruptedException ignored) {
        } catch (Exception e) {
        }
    }

    private void queryStatusRegister() {
        try {
            GetPumpStatusRegisterMessage statusRegister = MessageRequestUtil.getPumpStatusRegister();
            saveLastUpdated(new Date());
            if (statusRegister.isOperatingModeChanged()
                    || statusRegister.isBatteryStatusChanged()
                    || statusRegister.isCartridgeStatusChanged()
                    || statusRegister.isTotalDailyDoseChanged()
                    || statusRegister.isActiveBasalRateChanged()
                    || statusRegister.isActiveTBRChanged()
                    || statusRegister.isActiveBolusesChanged()) {
                update(statusRegister.isOperatingModeChanged(), statusRegister.isBatteryStatusChanged(), statusRegister.isCartridgeStatusChanged(),
                        statusRegister.isTotalDailyDoseChanged(), statusRegister.isActiveBasalRateChanged(), statusRegister.isActiveTBRChanged(), statusRegister.isActiveBolusesChanged());
                MessageRequestUtil.resetPumpStatusRegister(statusRegister.isOperatingModeChanged(), statusRegister.isBatteryStatusChanged(), statusRegister.isCartridgeStatusChanged(),
                        statusRegister.isTotalDailyDoseChanged(), statusRegister.isActiveBasalRateChanged(), statusRegister.isActiveTBRChanged(), statusRegister.isActiveBolusesChanged());
            }
        } catch (ConnectionLostException e) {
            Thread.currentThread().interrupt();
        } catch (DisconnectedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
        }
    }

    private void update(boolean updateOperatingMode, boolean updateBatteryStatus, boolean updateCartridgeStatus,
                        boolean updateTotalDailyDose, boolean updateActiveBasalRate, boolean updateActiveTBR, boolean updateActiveBoluses) {
        try {
            OperatingMode operatingMode;
            if (updateOperatingMode) {
                operatingMode = MessageRequestUtil.getOperatingMode();
                saveOperatingMode(operatingMode);
            } else operatingMode = this.operatingMode.getValue();
            if (updateBatteryStatus) saveBatteryStatus(MessageRequestUtil.getBatteryStatus());
            if (updateCartridgeStatus) saveCartridgeStatus(MessageRequestUtil.getCartridgeStatus());
            if (updateTotalDailyDose) saveTotalDailyDose(MessageRequestUtil.getTotalDailyDose());
            if (updateActiveBasalRate) {
                if (operatingMode == OperatingMode.STOPPED) saveActiveBasalRate(null);
                else saveActiveBasalRate(MessageRequestUtil.getActiveBasalRate());
            }
            if (updateActiveTBR) {
                if (operatingMode == OperatingMode.STOPPED) saveActiveTBR(null);
                else saveActiveTBR(MessageRequestUtil.getActiveTBR());
            }
            if (updateActiveBoluses) {
                if (operatingMode == OperatingMode.STOPPED) saveActiveBoluses(null);
                else saveActiveBoluses(MessageRequestUtil.getActiveBoluses());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveLastUpdated(Date lastUpdated) {
        if (lastUpdated.equals(this.lastUpdated.getValue())) return;
        this.lastUpdated.postValue(lastUpdated);
        sharedPreferences.edit().putLong("statusLastUpdated", lastUpdated.getTime()).apply();
    }

    private void saveOperatingMode(OperatingMode operatingMode) {
        if (operatingMode.equals(this.operatingMode.getValue())) return;
        this.operatingMode.postValue(operatingMode);
        sharedPreferences.edit().putString("statusOperatingMode", operatingMode.name()).apply();
    }

    private void saveCartridgeStatus(CartridgeStatus cartridgeStatus) {
        if (cartridgeStatus.equals(this.cartridgeStatus.getValue())) return;
        this.cartridgeStatus.postValue(cartridgeStatus);
        sharedPreferences.edit()
                .putBoolean("statusCartridgeInserted", cartridgeStatus.isInserted())
                .putString("statusCartridgeType", cartridgeStatus.getCartridgeType().name())
                .putString("statusCartridgeSymbolStatus", cartridgeStatus.getSymbolStatus().name())
                .putLong("statusCartridgeRemainingAmount", Double.doubleToLongBits(cartridgeStatus.getRemainingAmount()))
                .apply();
    }

    private void saveBatteryStatus(BatteryStatus batteryStatus) {
        if (batteryStatus.equals(this.batteryStatus.getValue())) return;
        this.batteryStatus.postValue(batteryStatus);
        sharedPreferences.edit()
                .putString("statusBatteryType", batteryStatus.getBatteryType().name())
                .putInt("statusBatteryAmount", batteryStatus.getBatteryAmount())
                .putString("statusBatterySymbolStatus", batteryStatus.getSymbolStatus().name())
                .apply();
    }

    private void saveTotalDailyDose(TotalDailyDose totalDailyDose) {
        if (totalDailyDose.equals(this.totalDailyDose.getValue())) return;
        this.totalDailyDose.postValue(totalDailyDose);
        sharedPreferences.edit()
                .putLong("statusTDDBolus", Double.doubleToLongBits(totalDailyDose.getBolus()))
                .putLong("statusTDDBasal", Double.doubleToLongBits(totalDailyDose.getBasal()))
                .putLong("statusTDDBolusAndBasal", Double.doubleToLongBits(totalDailyDose.getBolusAndBasal()))
                .apply();
    }

    private void saveActiveBasalRate(ActiveBasalRate activeBasalRate) {
        if (activeBasalRate == null && this.activeBasalRate.getValue() == null) return;
        if (activeBasalRate != null && activeBasalRate.equals(this.activeBasalRate.getValue()))
            return;
        this.activeBasalRate.postValue(activeBasalRate);
        if (activeBasalRate != null) {
            sharedPreferences.edit()
                    .putString("statusActiveBasalRateProfile", activeBasalRate.getActiveBasalProfile().name())
                    .putString("statusActiveBasalProfileName", activeBasalRate.getActiveBasalProfileName())
                    .putLong("statusActiveBasalRate", Double.doubleToLongBits(activeBasalRate.getActiveBasalRate()))
                    .apply();
        } else {
            sharedPreferences.edit()
                    .remove("statusActiveBasalRateProfile")
                    .remove("statusActiveBasalProfileName")
                    .remove("statusActiveBasalRate")
                    .apply();
        }
    }

    private void saveActiveTBR(ActiveTBR activeTBR) {
        if (activeTBR == null && this.activeTBR.getValue() == null) return;
        if (activeTBR != null && activeTBR.equals(this.activeTBR.getValue())) return;
        this.activeTBR.postValue(activeTBR);
        if (activeTBR != null) {
            sharedPreferences.edit()
                    .putInt("statusActiveTBRPercentage", activeTBR.getPercentage())
                    .putInt("statusActiveTBRRemainingDuration", activeTBR.getRemainingDuration())
                    .putInt("statusActiveTBRInitialDuration", activeTBR.getInitialDuration())
                    .apply();
        } else {
            sharedPreferences.edit()
                    .remove("statusActiveTBRPercentage")
                    .remove("statusActiveTBRRemainingDuration")
                    .remove("statusActiveTBRInitialDuration")
                    .apply();
        }
    }

    private void saveActiveBoluses(ActiveBolus[] activeBoluses) {
        if (Arrays.equals(activeBoluses, this.activeBoluses.getValue())) return;
        this.activeBoluses.postValue(activeBoluses);
        ActiveBolus activeBolus1 = activeBoluses != null && activeBoluses.length >= 1 ? activeBoluses[0] : null;
        ActiveBolus activeBolus2 = activeBoluses != null && activeBoluses.length >= 2 ? activeBoluses[1] : null;
        ActiveBolus activeBolus3 = activeBoluses != null && activeBoluses.length >= 3 ? activeBoluses[2] : null;
        if (activeBolus1 != null) {
            sharedPreferences.edit()
                    .putInt("statusActiveBolus1BolusID", activeBolus1.getBolusID())
                    .putString("statusActiveBolus1BolusType", activeBolus1.getBolusType().name())
                    .putLong("statusActiveBolus1InitialAmount", Double.doubleToLongBits(activeBolus1.getInitialAmount()))
                    .putLong("statusActiveBolus1RemainingAmount", Double.doubleToLongBits(activeBolus1.getRemainingAmount()))
                    .putInt("statusActiveBolus1RemainingDuration", activeBolus1.getRemainingDuration())
                    .apply();
        } else {
            sharedPreferences.edit()
                    .remove("statusActiveBolus1BolusID")
                    .remove("statusActiveBolus1BolusType")
                    .remove("statusActiveBolus1InitialAmount")
                    .remove("statusActiveBolus1RemainingAmount")
                    .remove("statusActiveBolus1RemainingDuration")
                    .apply();
        }
        if (activeBolus2 != null) {
            sharedPreferences.edit()
                    .putInt("statusActiveBolus2BolusID", activeBolus2.getBolusID())
                    .putString("statusActiveBolus2BolusType", activeBolus2.getBolusType().name())
                    .putLong("statusActiveBolus2InitialAmount", Double.doubleToLongBits(activeBolus2.getInitialAmount()))
                    .putLong("statusActiveBolus2RemainingAmount", Double.doubleToLongBits(activeBolus2.getRemainingAmount()))
                    .putInt("statusActiveBolus2RemainingDuration", activeBolus2.getRemainingDuration())
                    .apply();
        } else {
            sharedPreferences.edit()
                    .remove("statusActiveBolus2BolusID")
                    .remove("statusActiveBolus2BolusType")
                    .remove("statusActiveBolus2InitialAmount")
                    .remove("statusActiveBolus2RemainingAmount")
                    .remove("statusActiveBolus2RemainingDuration")
                    .apply();
        }
        if (activeBolus3 != null) {
            sharedPreferences.edit()
                    .putInt("statusActiveBolus3BolusID", activeBolus3.getBolusID())
                    .putString("statusActiveBolus3BolusType", activeBolus3.getBolusType().name())
                    .putLong("statusActiveBolus3InitialAmount", Double.doubleToLongBits(activeBolus3.getInitialAmount()))
                    .putLong("statusActiveBolus3RemainingAmount", Double.doubleToLongBits(activeBolus3.getRemainingAmount()))
                    .putInt("statusActiveBolus3RemainingDuration", activeBolus3.getRemainingDuration())
                    .apply();
        } else {
            sharedPreferences.edit()
                    .remove("statusActiveBolus3BolusID")
                    .remove("statusActiveBolus3BolusType")
                    .remove("statusActiveBolus3InitialAmount")
                    .remove("statusActiveBolus3RemainingAmount")
                    .remove("statusActiveBolus3RemainingDuration")
                    .apply();
        }
    }

    public MutableLiveData<Date> getLastUpdated() {
        return this.lastUpdated;
    }

    public MutableLiveData<OperatingMode> getOperatingMode() {
        return this.operatingMode;
    }

    public MutableLiveData<BatteryStatus> getBatteryStatus() {
        return this.batteryStatus;
    }

    public MutableLiveData<CartridgeStatus> getCartridgeStatus() {
        return this.cartridgeStatus;
    }

    public MutableLiveData<TotalDailyDose> getTotalDailyDose() {
        return this.totalDailyDose;
    }

    public MutableLiveData<ActiveBasalRate> getActiveBasalRate() {
        return this.activeBasalRate;
    }

    public MutableLiveData<ActiveTBR> getActiveTBR() {
        return this.activeTBR;
    }

    public MutableLiveData<ActiveBolus[]> getActiveBoluses() {
        return this.activeBoluses;
    }

    public class LocalBinder extends Binder {
        public StatusService getService() {
            return StatusService.this;
        }
    }
}
