package sugar.free.telesto.parser.satl;

import sugar.free.telesto.parser.ids.SatlErrorIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ErrorMessage extends SatlMessage {

    private SatlError error;

    @Override
    protected void parse(ByteBuf byteBuf) {
        error = SatlErrorIDs.IDS.getA(byteBuf.readByte());
    }

    public SatlError getError() {
        return this.error;
    }
}
