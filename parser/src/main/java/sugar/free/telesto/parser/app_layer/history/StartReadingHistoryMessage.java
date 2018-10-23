package sugar.free.telesto.parser.app_layer.history;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.HistoryReadingDirectionIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class StartReadingHistoryMessage extends AppLayerMessage {

    private long offset;
    private HistoryReadingDirection direction;

    public StartReadingHistoryMessage() {
        super(MessagePriority.NORMAL, false, true, Service.HISTORY);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(8);
        byteBuf.putUInt16LE(31);
        byteBuf.putUInt16LE(HistoryReadingDirectionIDs.IDS.getB(direction));
        byteBuf.putUInt32LE(offset);
        return byteBuf;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setDirection(HistoryReadingDirection direction) {
        this.direction = direction;
    }
}
