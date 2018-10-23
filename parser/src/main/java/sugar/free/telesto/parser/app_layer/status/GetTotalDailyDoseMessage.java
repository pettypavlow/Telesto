package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.TotalDailyDose;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetTotalDailyDoseMessage extends AppLayerMessage {

    private TotalDailyDose TDD;

    public GetTotalDailyDoseMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        TDD = new TotalDailyDose();
        TDD.setBolus(byteBuf.readUInt16Decimal());
        TDD.setBasal(byteBuf.readUInt16Decimal());
        TDD.setBolusAndBasal(byteBuf.readUInt16Decimal());
    }

    public TotalDailyDose getTDD() {
        return this.TDD;
    }
}
