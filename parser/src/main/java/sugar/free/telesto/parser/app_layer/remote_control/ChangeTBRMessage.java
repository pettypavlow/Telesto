package sugar.free.telesto.parser.app_layer.remote_control;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ChangeTBRMessage extends AppLayerMessage {

    private int percentage;
    private int duration;


    public ChangeTBRMessage() {
        super(MessagePriority.NORMAL, false, true, Service.REMOTE_CONTROL);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(6);
        byteBuf.putUInt16LE(percentage);
        byteBuf.putUInt16LE(duration);
        byteBuf.putUInt16LE(31);
        return byteBuf;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
