package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.parser.ids.BolusTypeIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class BolusProgrammedEvent extends HistoryEvent {

    private BolusType bolusType;
    private double immediateAmount;
    private double extendedAmount;
    private int duration;
    private int bolusID;

    @Override
    public void parse(ByteBuf byteBuf) {
        bolusType = BolusTypeIDs.IDS.getA(byteBuf.readUInt16LE());
        immediateAmount = byteBuf.readUInt16Decimal();
        extendedAmount = byteBuf.readUInt16Decimal();
        duration = byteBuf.readUInt16LE();
        byteBuf.shift(4);
        bolusID = byteBuf.readUInt16LE();
    }


    public BolusType getBolusType() {
        return bolusType;
    }

    public double getImmediateAmount() {
        return immediateAmount;
    }

    public double getExtendedAmount() {
        return extendedAmount;
    }

    public int getDuration() {
        return duration;
    }

    public int getBolusID() {
        return bolusID;
    }
}
