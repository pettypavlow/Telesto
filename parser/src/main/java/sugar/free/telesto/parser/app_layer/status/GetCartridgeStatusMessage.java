package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.CartridgeStatus;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.CartridgeTypeIDs;
import sugar.free.telesto.parser.ids.SymbolStatusIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetCartridgeStatusMessage extends AppLayerMessage {

    private CartridgeStatus cartridgeStatus;

    public GetCartridgeStatusMessage() {
        super(MessagePriority.NORMAL, false, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        cartridgeStatus = new CartridgeStatus();
        cartridgeStatus.setInserted(byteBuf.readBoolean());
        cartridgeStatus.setCartridgeType(CartridgeTypeIDs.IDS.getA(byteBuf.readUInt16LE()));
        cartridgeStatus.setSymbolStatus(SymbolStatusIDs.IDS.getA(byteBuf.readUInt16LE()));
        cartridgeStatus.setRemainingAmount(byteBuf.readUInt16Decimal());
    }

    public CartridgeStatus getCartridgeStatus() {
        return this.cartridgeStatus;
    }
}
