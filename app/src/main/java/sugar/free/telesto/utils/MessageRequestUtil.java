package sugar.free.telesto.utils;

import java.util.List;

import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.descriptors.ActiveBasalRate;
import sugar.free.telesto.descriptors.ActiveBolus;
import sugar.free.telesto.descriptors.ActiveTBR;
import sugar.free.telesto.descriptors.Alert;
import sugar.free.telesto.descriptors.AvailableBolusTypes;
import sugar.free.telesto.descriptors.BasalProfile;
import sugar.free.telesto.descriptors.BasalProfileBlock;
import sugar.free.telesto.descriptors.BatteryStatus;
import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.descriptors.CartridgeStatus;
import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.descriptors.PumpTime;
import sugar.free.telesto.descriptors.TotalDailyDose;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.ReadParameterBlockMessage;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.app_layer.configuration.WriteConfigurationBlockMessage;
import sugar.free.telesto.parser.app_layer.parameter_blocks.ActiveBRProfileBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile1Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile2Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile3Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile4Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile5Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfileBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.ParameterBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.FactoryMaxBolusAmountBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.FactoryMinBolusAmountBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.MaxBolusAmountBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.SystemIdentificationBlock;
import sugar.free.telesto.parser.app_layer.history.HistoryReadingDirection;
import sugar.free.telesto.parser.app_layer.history.ReadHistoryEventsMessage;
import sugar.free.telesto.parser.app_layer.history.StartReadingHistoryMessage;
import sugar.free.telesto.parser.app_layer.history.StopReadingHistoryMessage;
import sugar.free.telesto.parser.app_layer.history.history_events.HistoryEvent;
import sugar.free.telesto.parser.app_layer.remote_control.CancelBolusMessage;
import sugar.free.telesto.parser.app_layer.remote_control.CancelTBRMessage;
import sugar.free.telesto.parser.app_layer.remote_control.ChangeTBRMessage;
import sugar.free.telesto.parser.app_layer.remote_control.ConfirmAlertMessage;
import sugar.free.telesto.parser.app_layer.remote_control.DeliverBolusMessage;
import sugar.free.telesto.parser.app_layer.remote_control.GetAvailableBolusTypesMessage;
import sugar.free.telesto.parser.app_layer.remote_control.SetOperatingModeMessage;
import sugar.free.telesto.parser.app_layer.remote_control.SetTBRMessage;
import sugar.free.telesto.parser.app_layer.remote_control.SnoozeAlertMessage;
import sugar.free.telesto.parser.app_layer.status.GetActiveAlertMessage;
import sugar.free.telesto.parser.app_layer.status.GetActiveBasalRateMessage;
import sugar.free.telesto.parser.app_layer.status.GetActiveBolusesMessage;
import sugar.free.telesto.parser.app_layer.status.GetActiveTBRMessage;
import sugar.free.telesto.parser.app_layer.status.GetBatteryStatusMessage;
import sugar.free.telesto.parser.app_layer.status.GetCartridgeStatusMessage;
import sugar.free.telesto.parser.app_layer.status.GetDateTimeMessage;
import sugar.free.telesto.parser.app_layer.status.GetOperatingModeMessage;
import sugar.free.telesto.parser.app_layer.status.GetPumpStatusRegisterMessage;
import sugar.free.telesto.parser.app_layer.status.GetTotalDailyDoseMessage;
import sugar.free.telesto.parser.app_layer.status.ResetPumpStatusRegisterMessage;

public class MessageRequestUtil {

    @SuppressWarnings("unchecked")
    private static <T extends AppLayerMessage> T requestMessage(T request) throws Exception {
        return (T) TelestoApp.getConnectionService().requestMessage(request).await();
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParameterBlock> T readConfigurationBlock(Class<T> configurationBlock) throws Exception {
        ReadParameterBlockMessage readMessage = new ReadParameterBlockMessage();
        readMessage.setService(Service.CONFIGURATION);
        readMessage.setConfigurationBlockId(configurationBlock);
        return (T) requestMessage(readMessage).getParameterBlock();
    }

    private static void writeConfigurationBlock(ParameterBlock parameterBlock) throws Exception {
        WriteConfigurationBlockMessage writeMessage = new WriteConfigurationBlockMessage();
        writeMessage.setParameterBlock(parameterBlock);
        requestMessage(writeMessage);
    }

    public static GetPumpStatusRegisterMessage getPumpStatusRegister() throws Exception {
        return requestMessage(new GetPumpStatusRegisterMessage());
    }

    public static void resetPumpStatusRegister(boolean operatingModeChanged, boolean batteryStatusChanged, boolean cartridgeStatusChanged,
                                               boolean totalDailyDoseChanged, boolean activeBasalRateChanged, boolean activeTBRChanged, boolean activeBolusesChanged) throws Exception {
        ResetPumpStatusRegisterMessage resetMessage = new ResetPumpStatusRegisterMessage();
        resetMessage.setOperatingModeChanged(operatingModeChanged);
        resetMessage.setBatteryStatusChanged(batteryStatusChanged);
        resetMessage.setCartridgeStatusChanged(cartridgeStatusChanged);
        resetMessage.setTotalDailyDoseChanged(totalDailyDoseChanged);
        resetMessage.setActiveBasalRateChanged(activeBasalRateChanged);
        resetMessage.setActiveTBRChanged(activeTBRChanged);
        resetMessage.setActiveBolusesChanged(activeBolusesChanged);
        requestMessage(resetMessage);
    }

    public static OperatingMode getOperatingMode() throws Exception {
        return requestMessage(new GetOperatingModeMessage()).getOperatingMode();
    }

    public static void setOperatingMode(OperatingMode operatingMode) throws Exception {
        SetOperatingModeMessage message = new SetOperatingModeMessage();
        message.setOperatingMode(operatingMode);
        requestMessage(message);
    }

    public static BatteryStatus getBatteryStatus() throws Exception {
        return requestMessage(new GetBatteryStatusMessage()).getBatteryStatus();
    }

    public static CartridgeStatus getCartridgeStatus() throws Exception {
        return requestMessage(new GetCartridgeStatusMessage()).getCartridgeStatus();
    }

    public static TotalDailyDose getTotalDailyDose() throws Exception {
        return requestMessage(new GetTotalDailyDoseMessage()).getTDD();
    }

    public static ActiveBasalRate getActiveBasalRate() throws Exception {
        return requestMessage(new GetActiveBasalRateMessage()).getActiveBasalRate();
    }

    public static ActiveTBR getActiveTBR() throws Exception {
        return requestMessage(new GetActiveTBRMessage()).getActiveTBR();
    }

    public static ActiveBolus[] getActiveBoluses() throws Exception {
        return requestMessage(new GetActiveBolusesMessage()).getActiveBoluses();
    }

    public static Alert getActiveAlert() throws Exception {
        return requestMessage(new GetActiveAlertMessage()).getAlert();
    }

    public static void cancelBolus(int bolusID) throws Exception {
        CancelBolusMessage cancelBolusMessage = new CancelBolusMessage();
        cancelBolusMessage.setBolusID(bolusID);
        requestMessage(cancelBolusMessage);
    }

    public static void cancelTBR() throws Exception {
        requestMessage(new CancelTBRMessage());
    }

    public static double getFactoryMinBolusAmount() throws Exception {
        return readConfigurationBlock(FactoryMinBolusAmountBlock.class).getAmountLimitation();
    }

    public static double getFactoryMaxBolusAmount() throws Exception {
        return readConfigurationBlock(FactoryMaxBolusAmountBlock.class).getAmountLimitation();
    }

    public static double getMaxBolusAmount() throws Exception {
        return readConfigurationBlock(MaxBolusAmountBlock.class).getAmountLimitation();
    }

    public static AvailableBolusTypes getAvailableBolusTypes() throws Exception {
        return requestMessage(new GetAvailableBolusTypesMessage()).getAvailableBolusTypes();
    }

    public static int deliverBolus(BolusType bolusType, double immediateAmount, double extendedAmount, int duration) throws Exception {
        DeliverBolusMessage message = new DeliverBolusMessage();
        message.setBolusType(bolusType);
        message.setImmediateAmount(immediateAmount);
        message.setExtendedAmount(extendedAmount);
        message.setDuration(duration);
        return requestMessage(message).getBolusId();
    }

    public static int deliverStandardBolus(double immediateAmount) throws Exception {
        return deliverBolus(BolusType.STANDARD, immediateAmount, 0, 0);
    }

    public static int deliverExtendedBolus(double extendedAmount, int duration) throws Exception {
        return deliverBolus(BolusType.EXTENDED, 0, extendedAmount, duration);
    }

    public static int deliverMultiwaveBolus(double immediateAmount, double extendedAmount, int duration) throws Exception {
        return deliverBolus(BolusType.MULTIWAVE, immediateAmount, extendedAmount, duration);
    }

    public static void setTBR(int percentage, int duration) throws Exception {
        SetTBRMessage setTBRMessage = new SetTBRMessage();
        setTBRMessage.setPercentage(percentage);
        setTBRMessage.setDuration(duration);
        requestMessage(setTBRMessage);
    }

    public static void changeTBR(int percentage, int duration) throws Exception {
        ChangeTBRMessage changeTBRMessage = new ChangeTBRMessage();
        changeTBRMessage.setPercentage(percentage);
        changeTBRMessage.setDuration(duration);
        requestMessage(changeTBRMessage);
    }

    public static void startReadingHistory(long offset, HistoryReadingDirection direction) throws Exception {
        StartReadingHistoryMessage message = new StartReadingHistoryMessage();
        message.setOffset(offset);
        message.setDirection(direction);
        requestMessage(message);
    }

    public static void stopReadingHistory() throws Exception {
        requestMessage(new StopReadingHistoryMessage());
    }

    public static List<HistoryEvent> readHistoryEvents() throws Exception {
        return requestMessage(new ReadHistoryEventsMessage()).getHistoryEvents();
    }

    public static SystemIdentificationBlock getSystemIdentification() throws Exception {
        ReadParameterBlockMessage readMessage = new ReadParameterBlockMessage();
        readMessage.setService(Service.PARAMETER);
        readMessage.setConfigurationBlockId(SystemIdentificationBlock.class);
        return (SystemIdentificationBlock) requestMessage(readMessage).getParameterBlock();
    }

    public static PumpTime getPumpTime() throws Exception {
        return requestMessage(new GetDateTimeMessage()).getPumpTime();
    }

    public static BasalProfile getActiveBasalProfile() throws Exception {
        return readConfigurationBlock(ActiveBRProfileBlock.class).getActiveBasalProfile();
    }

    public static void setActiveBasalProfile(BasalProfile activeBasalProfile) throws Exception {
        ActiveBRProfileBlock activeBRProfileBlock = new ActiveBRProfileBlock();
        activeBRProfileBlock.setActiveBasalProfile(activeBasalProfile);
        writeConfigurationBlock(activeBRProfileBlock);
    }

    public static BasalProfileBlock[] getBasalProfile(BasalProfile basalProfile) throws Exception {
        Class<? extends BRProfileBlock> configurationBlock = null;
        switch (basalProfile) {
            case PROFILE_1:
                configurationBlock = BRProfile1Block.class;
                break;
            case PROFILE_2:
                configurationBlock = BRProfile2Block.class;
                break;
            case PROFILE_3:
                configurationBlock = BRProfile3Block.class;
                break;
            case PROFILE_4:
                configurationBlock = BRProfile4Block.class;
                break;
            case PROFILE_5:
                configurationBlock = BRProfile5Block.class;
                break;
        }
        return readConfigurationBlock(configurationBlock).getProfileBlocks();
    }

    public static void writeBasalProfile(BasalProfile basalProfile, BasalProfileBlock[] basalProfileBlocks) throws Exception {
        BRProfileBlock configurationBlock = null;
        switch (basalProfile) {
            case PROFILE_1:
                configurationBlock = new BRProfile1Block();
                break;
            case PROFILE_2:
                configurationBlock = new BRProfile2Block();
                break;
            case PROFILE_3:
                configurationBlock = new BRProfile3Block();
                break;
            case PROFILE_4:
                configurationBlock = new BRProfile4Block();
                break;
            case PROFILE_5:
                configurationBlock = new BRProfile5Block();
                break;
        }
        writeConfigurationBlock(configurationBlock);
    }

    public static void snoozeAlert(int alertId) throws Exception {
        SnoozeAlertMessage snoozeAlertMessage = new SnoozeAlertMessage();
        snoozeAlertMessage.setAlertID(alertId);
        requestMessage(snoozeAlertMessage);
    }

    public static void confirmAlert(int alertId) throws Exception {
        ConfirmAlertMessage confirmAlertMessage = new ConfirmAlertMessage();
        confirmAlertMessage.setAlertID(alertId);
        requestMessage(confirmAlertMessage);
    }
}
