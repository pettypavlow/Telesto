package sugar.free.telesto.parser.app_layer;

import android.util.Log;

import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;

import sugar.free.telesto.descriptors.BolusType;
import sugar.free.telesto.exceptions.IncompatibleAppVersionException;
import sugar.free.telesto.exceptions.InvalidAppCRCException;
import sugar.free.telesto.exceptions.SecondChannelFailedException;
import sugar.free.telesto.exceptions.UnknownAppCommandException;
import sugar.free.telesto.exceptions.UnknownServiceException;
import sugar.free.telesto.exceptions.app_layer_errors.AppLayerErrorException;
import sugar.free.telesto.exceptions.app_layer_errors.UnknownAppLayerErrorCodeException;
import sugar.free.telesto.parser.app_layer.remote_control.DeliverBolusMessage;
import sugar.free.telesto.parser.ids.AppCommandIDs;
import sugar.free.telesto.parser.ids.AppErrorIDs;
import sugar.free.telesto.parser.ids.ServiceIDs;
import sugar.free.telesto.parser.satl.DataMessage;
import sugar.free.telesto.parser.utils.ByteBuf;
import sugar.free.telesto.parser.utils.crypto.Cryptograph;

public class AppLayerMessage implements Comparable<AppLayerMessage> {

    private static final byte VERSION = 0x20;

    private final MessagePriority messagePriority;
    private final boolean inCRC;
    private final boolean outCRC;
    private final Service service;

    public AppLayerMessage(MessagePriority messagePriority, boolean inCRC, boolean outCRC, Service service) {
        this.messagePriority = messagePriority;
        this.inCRC = inCRC;
        this.outCRC = outCRC;
        this.service = service;
    }

    protected ByteBuf getData() {
        return new ByteBuf(0);
    }

    protected void parse(ByteBuf byteBuf) throws Exception {

    }

    public ByteBuf serialize(Class<? extends AppLayerMessage> clazz) {
        byte[] data = getData().getBytes();
        ByteBuf byteBuf = new ByteBuf(4 + data.length + (outCRC ? 2 : 0));
        byteBuf.putByte(VERSION);
        byteBuf.putByte(ServiceIDs.IDS.getB(getService()));
        byteBuf.putUInt16LE(AppCommandIDs.IDS.getB(clazz));
        byteBuf.putBytes(data);
        if (outCRC) byteBuf.putUInt16LE(Cryptograph.calculateCRC(data));
        return byteBuf;
    }

    public static AppLayerMessage deserialize(ByteBuf byteBuf) throws Exception {
        byte version = byteBuf.readByte();
        byte service = byteBuf.readByte();
        int command = byteBuf.readUInt16LE();
        int error = byteBuf.readUInt16LE();
        Class<? extends AppLayerMessage> clazz = AppCommandIDs.IDS.getA(command);
        if (clazz == null) throw new UnknownAppCommandException();
        if (version != VERSION) throw new IncompatibleAppVersionException();
        AppLayerMessage message = clazz.newInstance();
        if (ServiceIDs.IDS.getA(service) == null) throw new UnknownServiceException();
        if (error != 0) {
            Class<? extends AppLayerErrorException> exceptionClass = AppErrorIDs.IDS.getA(error);
            if (exceptionClass == null) throw new UnknownAppLayerErrorCodeException(error);
            else throw exceptionClass.getConstructor(int.class).newInstance(error);
        }
        byte[] data = byteBuf.readBytes(byteBuf.getSize() - (message.inCRC ? 2 : 0));
        if (message.inCRC && Cryptograph.calculateCRC(data) != byteBuf.readUInt16LE()) throw new InvalidAppCRCException();
        message.parse(ByteBuf.from(data));
        return message;
    }

    public static DataMessage wrap(AppLayerMessage message) {
        DataMessage dataMessage = new DataMessage();
        dataMessage.setData(message.serialize(message.getClass()));
        return dataMessage;
    }

    public static AppLayerMessage unwrap(DataMessage dataMessage) throws Exception {
        return deserialize(dataMessage.getData());
    }

    @Override
    public int compareTo(AppLayerMessage o) {
        return messagePriority.compareTo(o.messagePriority);
    }

    public Service getService() {
        return this.service;
    }
}
