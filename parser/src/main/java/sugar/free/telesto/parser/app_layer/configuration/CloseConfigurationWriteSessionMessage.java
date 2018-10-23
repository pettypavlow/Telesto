package sugar.free.telesto.parser.app_layer.configuration;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;

public class CloseConfigurationWriteSessionMessage extends AppLayerMessage {

    public CloseConfigurationWriteSessionMessage() {
        super(MessagePriority.NORMAL, false, false, Service.CONFIGURATION);
    }
}
