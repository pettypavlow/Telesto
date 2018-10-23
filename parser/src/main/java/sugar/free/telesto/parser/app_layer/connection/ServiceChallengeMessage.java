package sugar.free.telesto.parser.app_layer.connection;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ServiceChallengeMessage extends AppLayerMessage {

    private byte serviceID;
    private byte[] randomData;
    private short version;

    public ServiceChallengeMessage() {
        super(MessagePriority.NORMAL, false, false, Service.CONNECTION);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        randomData = byteBuf.getBytes(16);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(3);
        byteBuf.putByte(serviceID);
        byteBuf.putShort(version);
        return byteBuf;
    }

    public byte[] getRandomData() {
        return this.randomData;
    }

    public void setServiceID(byte serviceID) {
        this.serviceID = serviceID;
    }

    public void setVersion(short version) {
        this.version = version;
    }
}
