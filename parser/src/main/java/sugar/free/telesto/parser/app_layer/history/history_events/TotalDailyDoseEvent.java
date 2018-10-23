package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.parser.utils.BOCUtil;
import sugar.free.telesto.parser.utils.ByteBuf;

public class TotalDailyDoseEvent extends HistoryEvent {

    private double basalTotal;
    private double bolusTotal;
    private int totalYear;
    private int totalMonth;
    private int totalDay;

    @Override
    public void parse(ByteBuf byteBuf) {
        basalTotal = byteBuf.readUInt32Decimal100();
        bolusTotal = byteBuf.readUInt32Decimal100();
        totalYear = BOCUtil.parseBOC(byteBuf.readByte()) * 100 + BOCUtil.parseBOC(byteBuf.readByte());
        totalMonth = BOCUtil.parseBOC(byteBuf.readByte());
        totalDay = BOCUtil.parseBOC(byteBuf.readByte());
    }

    public double getBasalTotal() {
        return basalTotal;
    }

    public double getBolusTotal() {
        return bolusTotal;
    }

    public int getTotalYear() {
        return totalYear;
    }

    public int getTotalMonth() {
        return totalMonth;
    }

    public int getTotalDay() {
        return totalDay;
    }
}
