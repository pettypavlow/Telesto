package sugar.free.telesto.parser.app_layer.configuration;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;

public class OpenConfigurationWriteSessionMessage extends AppLayerMessage {

    public OpenConfigurationWriteSessionMessage() {
        super(MessagePriority.NORMAL, false, false, Service.CONFIGURATION);
    }
}
