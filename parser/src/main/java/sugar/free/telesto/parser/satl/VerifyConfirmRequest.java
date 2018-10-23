package sugar.free.telesto.parser.satl;

import sugar.free.telesto.parser.ids.PairingStatusIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class VerifyConfirmRequest extends SatlMessage {

    private PairingStatus pairingStatus;

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(2);
        byteBuf.putUInt16LE(PairingStatusIDs.IDS.getB(PairingStatus.CONFIRMED));
        return byteBuf;
    }

    public void setPairingStatus(PairingStatus pairingStatus) {
        this.pairingStatus = pairingStatus;
    }
}
