package sugar.free.telesto.parser.app_layer.connection;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ActivateServiceMessage extends AppLayerMessage {

    private byte serviceID;
    private short version;
    private byte[] servicePassword;

    public ActivateServiceMessage() {
        super(MessagePriority.NORMAL, false, false, Service.CONNECTION);
    }

    protected void parse(ByteBuf byteBuf) {
        serviceID = byteBuf.readByte();
        version = byteBuf.readShort();
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(19);
        byteBuf.putByte(serviceID);
        byteBuf.putShort(version);
        byteBuf.putBytes(servicePassword);
        return byteBuf;
    }

    public byte getServiceID() {
        return this.serviceID;
    }

    public short getVersion() {
        return this.version;
    }

    public void setServiceID(byte serviceID) {
        this.serviceID = serviceID;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void setServicePassword(byte[] servicePassword) {
        this.servicePassword = servicePassword;
    }
}
