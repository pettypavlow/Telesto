package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.ActiveTBR;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetActiveTBRMessage extends AppLayerMessage {

    private ActiveTBR activeTBR;

    public GetActiveTBRMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        ActiveTBR activeTBR = new ActiveTBR();
        activeTBR.setPercentage(byteBuf.readUInt16LE());
        activeTBR.setRemainingDuration(byteBuf.readUInt16LE());
        activeTBR.setInitialDuration(byteBuf.readUInt16LE());
        if (activeTBR.getPercentage() != 100) this.activeTBR = activeTBR;
    }

    public ActiveTBR getActiveTBR() {
        return this.activeTBR;
    }
}
