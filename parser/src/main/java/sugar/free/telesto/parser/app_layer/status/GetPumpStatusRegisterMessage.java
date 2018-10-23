package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetPumpStatusRegisterMessage extends AppLayerMessage {

    private boolean operatingModeChanged;
    private boolean batteryStatusChanged;
    private boolean cartridgeStatusChanged;
    private boolean totalDailyDoseChanged;
    private boolean activeBasalRateChanged;
    private boolean activeTBRChanged;
    private boolean activeBolusesChanged;

    public GetPumpStatusRegisterMessage() {
        super(MessagePriority.NORMAL, false, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        operatingModeChanged = byteBuf.readBoolean();
        batteryStatusChanged = byteBuf.readBoolean();
        cartridgeStatusChanged = byteBuf.readBoolean();
        totalDailyDoseChanged = byteBuf.readBoolean();
        activeBasalRateChanged = byteBuf.readBoolean();
        activeTBRChanged = byteBuf.readBoolean();
        activeBolusesChanged = byteBuf.readBoolean();
    }

    public boolean isOperatingModeChanged() {
        return this.operatingModeChanged;
    }

    public boolean isBatteryStatusChanged() {
        return this.batteryStatusChanged;
    }

    public boolean isCartridgeStatusChanged() {
        return this.cartridgeStatusChanged;
    }

    public boolean isTotalDailyDoseChanged() {
        return this.totalDailyDoseChanged;
    }

    public boolean isActiveBasalRateChanged() {
        return this.activeBasalRateChanged;
    }

    public boolean isActiveTBRChanged() {
        return this.activeTBRChanged;
    }

    public boolean isActiveBolusesChanged() {
        return this.activeBolusesChanged;
    }
}
