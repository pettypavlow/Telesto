package sugar.free.telesto.parser.app_layer.remote_control;

import sugar.free.telesto.descriptors.AvailableBolusTypes;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetAvailableBolusTypesMessage extends AppLayerMessage {

    private AvailableBolusTypes availableBolusTypes;

    public GetAvailableBolusTypesMessage() {
        super(MessagePriority.NORMAL, false, false, Service.REMOTE_CONTROL);
    }

    @Override
    protected void parse(ByteBuf byteBuf) throws Exception {
        availableBolusTypes = new AvailableBolusTypes();
        availableBolusTypes.setStandardAvailable(byteBuf.readBoolean());
        availableBolusTypes.setExtendedAvailable(byteBuf.readBoolean());
        availableBolusTypes.setMultiwaveAvailable(byteBuf.readBoolean());
    }

    public AvailableBolusTypes getAvailableBolusTypes() {
        return this.availableBolusTypes;
    }
}
