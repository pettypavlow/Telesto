package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.parser.ids.BolusTypeIDs;
import sugar.free.telesto.parser.utils.BOCUtil;
import sugar.free.telesto.parser.utils.ByteBuf;

public class BolusDeliveredEvent extends HistoryEvent {

    private BolusType bolusType;
    private int startHour;
    private int startMinute;
    private int startSecond;
    private double immediateAmount;
    private double extendedAmount;
    private int duration;
    private int bolusID;

    @Override
    public void parse(ByteBuf byteBuf) {
        bolusType = BolusTypeIDs.IDS.getA(byteBuf.readUInt16LE());
        byteBuf.shift(1);
        startHour = BOCUtil.parseBOC(byteBuf.readByte());
        startMinute = BOCUtil.parseBOC(byteBuf.readByte());
        startSecond = BOCUtil.parseBOC(byteBuf.readByte());
        immediateAmount = byteBuf.readUInt16Decimal();
        extendedAmount = byteBuf.readUInt16Decimal();
        duration = byteBuf.readUInt16LE();
        byteBuf.shift(2);
        bolusID = byteBuf.readUInt16LE();
    }

    public BolusType getBolusType() {
        return bolusType;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getStartSecond() {
        return startSecond;
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
