package sugar.free.telesto.parser.ids;

import sugar.free.telesto.parser.satl.ConnectionRequest;
import sugar.free.telesto.parser.satl.ConnectionResponse;
import sugar.free.telesto.parser.satl.DataMessage;
import sugar.free.telesto.parser.satl.DisconnectMessage;
import sugar.free.telesto.parser.satl.ErrorMessage;
import sugar.free.telesto.parser.satl.KeyRequest;
import sugar.free.telesto.parser.satl.KeyResponse;
import sugar.free.telesto.parser.satl.SatlMessage;
import sugar.free.telesto.parser.satl.SynAckResponse;
import sugar.free.telesto.parser.satl.SynRequest;
import sugar.free.telesto.parser.satl.VerifyConfirmRequest;
import sugar.free.telesto.parser.satl.VerifyConfirmResponse;
import sugar.free.telesto.parser.satl.VerifyDisplayRequest;
import sugar.free.telesto.parser.satl.VerifyDisplayResponse;
import sugar.free.telesto.parser.utils.BiMap;

public class SatlCommandIDs {

    public static final BiMap<Class<? extends SatlMessage>, Byte> IDS = new BiMap<>();

    static {
        IDS.put(DataMessage.class, (byte) 3);
        IDS.put(ErrorMessage.class, (byte) 6);
        IDS.put(ConnectionRequest.class, (byte) 9);
        IDS.put(ConnectionResponse.class, (byte) 10);
        IDS.put(KeyRequest.class, (byte) 12);
        IDS.put(VerifyConfirmRequest.class, (byte) 14);
        IDS.put(KeyResponse.class, (byte) 17);
        IDS.put(VerifyDisplayRequest.class, (byte) 18);
        IDS.put(VerifyDisplayResponse.class, (byte) 20);
        IDS.put(SynRequest.class, (byte) 23);
        IDS.put(SynAckResponse.class, (byte) 24);
        IDS.put(DisconnectMessage.class, (byte) 27);
        IDS.put(VerifyConfirmResponse.class, (byte) 30);
    }

}
