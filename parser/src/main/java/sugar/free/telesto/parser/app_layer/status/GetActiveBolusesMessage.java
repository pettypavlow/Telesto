package sugar.free.telesto.parser.app_layer.status;

import java.util.ArrayList;
import java.util.List;

import sugar.free.telesto.descriptors.ActiveBolus;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.ActiveBolusTypeIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetActiveBolusesMessage extends AppLayerMessage {

    private ActiveBolus[] activeBoluses;

    public GetActiveBolusesMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        List<ActiveBolus> activeBoluses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ActiveBolus activeBolus = new ActiveBolus();
            activeBolus.setBolusID(byteBuf.readUInt16LE());
            activeBolus.setBolusType(ActiveBolusTypeIDs.IDS.getA(byteBuf.readUInt16LE()));
            byteBuf.shift(2);
            byteBuf.shift(2);
            activeBolus.setInitialAmount(byteBuf.readUInt16Decimal());
            activeBolus.setRemainingAmount(byteBuf.readUInt16Decimal());
            activeBolus.setRemainingDuration(byteBuf.readUInt16LE());
            if (activeBolus.getBolusType() != null) activeBoluses.add(activeBolus);
        }
        this.activeBoluses = activeBoluses.toArray(new ActiveBolus[activeBoluses.size()]);
    }

    public ActiveBolus[] getActiveBoluses() {
        return this.activeBoluses;
    }
}
