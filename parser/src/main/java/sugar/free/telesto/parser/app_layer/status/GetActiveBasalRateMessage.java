package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.ActiveBasalRate;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.ActiveBasalProfileIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetActiveBasalRateMessage extends AppLayerMessage {

    private ActiveBasalRate activeBasalRate;

    public GetActiveBasalRateMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        ActiveBasalRate activeBasalRate = new ActiveBasalRate();
        activeBasalRate.setActiveBasalProfile(ActiveBasalProfileIDs.IDS.getA(byteBuf.readUInt16LE()));
        activeBasalRate.setActiveBasalProfileName(byteBuf.readUTF16(30));
        activeBasalRate.setActiveBasalRate(byteBuf.readUInt16Decimal());
        if (activeBasalRate.getActiveBasalProfile() != null) this.activeBasalRate = activeBasalRate;
    }

    public ActiveBasalRate getActiveBasalRate() {
        return this.activeBasalRate;
    }
}
