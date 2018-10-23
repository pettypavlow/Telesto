package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.PumpTime;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetDateTimeMessage extends AppLayerMessage {

    private PumpTime pumpTime;

    public GetDateTimeMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        pumpTime = new PumpTime();
        pumpTime.setYear(byteBuf.readUInt16LE());
        pumpTime.setMonth(byteBuf.readUInt8());
        pumpTime.setDay(byteBuf.readUInt8());
        pumpTime.setHour(byteBuf.readUInt8());
        pumpTime.setMinute(byteBuf.readUInt8());
        pumpTime.setSecond(byteBuf.readUInt8());
    }

    public PumpTime getPumpTime() {
        return this.pumpTime;
    }
}
