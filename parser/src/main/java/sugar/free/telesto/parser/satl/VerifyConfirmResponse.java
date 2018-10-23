package sugar.free.telesto.parser.satl;

import sugar.free.telesto.parser.ids.PairingStatusIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class VerifyConfirmResponse extends SatlMessage {

    private PairingStatus pairingStatus;

    @Override
    protected void parse(ByteBuf byteBuf) {
        pairingStatus = PairingStatusIDs.IDS.getA(byteBuf.readUInt16LE());
    }

    public PairingStatus getPairingStatus() {
        return this.pairingStatus;
    }
}
