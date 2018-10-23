package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.Alert;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.AlertCategoryIDs;
import sugar.free.telesto.parser.ids.AlertStatusIDs;
import sugar.free.telesto.parser.ids.AlertTypeIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetActiveAlertMessage extends AppLayerMessage {

    private Alert alert;

    public GetActiveAlertMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        Alert alert = new Alert();
        alert.setAlertId(byteBuf.readUInt16LE());
        alert.setAlertCategory(AlertCategoryIDs.IDS.getA(byteBuf.readUInt16LE()));
        alert.setAlertType(AlertTypeIDs.IDS.getA(byteBuf.readUInt16LE()));
        alert.setAlertStatus(AlertStatusIDs.IDS.getA(byteBuf.readUInt16LE()));
        if (alert.getAlertType() != null) {
            switch (alert.getAlertType()) {
                case WARNING_38:
                    byteBuf.shift(4);
                    alert.setProgrammedBolusAmount(byteBuf.readUInt16Decimal());
                    alert.setDeliveredBolusAmount(byteBuf.readUInt16Decimal());
                    break;
                case REMINDER_07:
                case WARNING_36:
                    byteBuf.shift(2);
                    alert.setTBRAmount(byteBuf.readUInt16LE());
                    alert.setTBRDuration(byteBuf.readUInt16LE());
                    break;
                case WARNING_31:
                    alert.setCartridgeAmount(byteBuf.readUInt16Decimal());
                    break;
            }
        }
        if (alert.getAlertCategory() != null
                && alert.getAlertType() != null
                && alert.getAlertStatus() != null)
            this.alert = alert;
    }

    public Alert getAlert() {
        return this.alert;
    }
}
