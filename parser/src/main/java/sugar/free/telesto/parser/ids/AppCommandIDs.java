package sugar.free.telesto.parser.ids;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.configuration.CloseConfigurationWriteSessionMessage;
import sugar.free.telesto.parser.app_layer.configuration.OpenConfigurationWriteSessionMessage;
import sugar.free.telesto.parser.app_layer.ReadParameterBlockMessage;
import sugar.free.telesto.parser.app_layer.configuration.WriteConfigurationBlockMessage;
import sugar.free.telesto.parser.app_layer.connection.ActivateServiceMessage;
import sugar.free.telesto.parser.app_layer.connection.BindMessage;
import sugar.free.telesto.parser.app_layer.connection.ConnectMessage;
import sugar.free.telesto.parser.app_layer.connection.DisconnectMessage;
import sugar.free.telesto.parser.app_layer.connection.ServiceChallengeMessage;
import sugar.free.telesto.parser.app_layer.history.ReadHistoryEventsMessage;
import sugar.free.telesto.parser.app_layer.history.StartReadingHistoryMessage;
import sugar.free.telesto.parser.app_layer.history.StopReadingHistoryMessage;
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
import sugar.free.telesto.parser.app_layer.status.GetFirmwareVersionsMessage;
import sugar.free.telesto.parser.app_layer.status.GetOperatingModeMessage;
import sugar.free.telesto.parser.app_layer.status.GetPumpStatusRegisterMessage;
import sugar.free.telesto.parser.app_layer.status.GetTotalDailyDoseMessage;
import sugar.free.telesto.parser.app_layer.status.ResetPumpStatusRegisterMessage;
import sugar.free.telesto.parser.utils.BiMap;

public class AppCommandIDs {

    public static final BiMap<Class<? extends AppLayerMessage>, Integer> IDS = new BiMap<>();

    static {
        IDS.put(ConnectMessage.class, 61451);
        IDS.put(BindMessage.class, 62413);
        IDS.put(DisconnectMessage.class, 61460);
        IDS.put(ActivateServiceMessage.class, 61687);
        IDS.put(ServiceChallengeMessage.class, 62418);
        IDS.put(GetActiveAlertMessage.class, 985);
        IDS.put(GetActiveBolusesMessage.class, 1647);
        IDS.put(GetActiveTBRMessage.class, 1462);
        IDS.put(GetAvailableBolusTypesMessage.class, 6362);
        IDS.put(GetBatteryStatusMessage.class, 805);
        IDS.put(GetCartridgeStatusMessage.class, 826);
        IDS.put(GetDateTimeMessage.class, 227);
        IDS.put(GetFirmwareVersionsMessage.class, 11992);
        IDS.put(GetOperatingModeMessage.class, 252);
        IDS.put(GetPumpStatusRegisterMessage.class, 31);
        IDS.put(ResetPumpStatusRegisterMessage.class, 35476);
        IDS.put(GetActiveBasalRateMessage.class, 1449);
        IDS.put(GetTotalDailyDoseMessage.class, 966);
        IDS.put(CancelTBRMessage.class, 6201);
        IDS.put(CancelBolusMessage.class, 7136);
        IDS.put(SetOperatingModeMessage.class, 6182);
        IDS.put(ReadParameterBlockMessage.class, 7766);
        IDS.put(WriteConfigurationBlockMessage.class, 7850);
        IDS.put(CloseConfigurationWriteSessionMessage.class, 7861);
        IDS.put(OpenConfigurationWriteSessionMessage.class, 7753);
        IDS.put(DeliverBolusMessage.class, 6915);
        IDS.put(SetTBRMessage.class, 6341);
        IDS.put(ChangeTBRMessage.class, 42067);
        IDS.put(ReadHistoryEventsMessage.class, 10408);
        IDS.put(StartReadingHistoryMessage.class, 10324);
        IDS.put(StopReadingHistoryMessage.class, 38887);
        IDS.put(ConfirmAlertMessage.class, 1683);
        IDS.put(SnoozeAlertMessage.class, 1676);
    }

}
