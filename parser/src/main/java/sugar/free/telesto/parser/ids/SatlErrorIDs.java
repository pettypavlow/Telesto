package sugar.free.telesto.parser.ids;

import sugar.free.telesto.parser.satl.SatlError;
import sugar.free.telesto.parser.utils.BiMap;

public class SatlErrorIDs {

    public static final BiMap<SatlError, Byte> IDS = new BiMap<>();

    static {
        IDS.put(SatlError.UNDEFINED, (byte) 0);
        IDS.put(SatlError.INCOMPATIBLE_VERSION, (byte) 1);
        IDS.put(SatlError.INVALID_COMM_ID, (byte) 2);
        IDS.put(SatlError.INVALID_MAC_TRAILER, (byte) 3);
        IDS.put(SatlError.INVALID_CRC, (byte) 4);
        IDS.put(SatlError.INVALID_PACKET, (byte) 5);
        IDS.put(SatlError.INVALID_NONCE, (byte) 6);
        IDS.put(SatlError.DECRYPT_VERIFY_FAILED, (byte) 7);
        IDS.put(SatlError.COMPATIBLE_STATE, (byte) 8);
        IDS.put(SatlError.WRONG_STATE, (byte) 0x0F);
        IDS.put(SatlError.INVALID_MESSAGE_TYPE, (byte) 51);
        IDS.put(SatlError.INVALID_PAYLOAD_LENGTH, (byte) 60);
        IDS.put(SatlError.NONE, (byte) 255);
    }

}
