package sugar.free.telesto.parser.app_layer.connection;

import org.spongycastle.util.encoders.Hex;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class DisconnectMessage extends AppLayerMessage {

    public DisconnectMessage() {
        super(MessagePriority.NORMAL, false, false, Service.CONNECTION);
    }

    @Override
    protected ByteBuf getData() {
        return ByteBuf.from(Hex.decode("0360"));
    }
}
