package sugar.free.telesto.parser.app_layer;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.app_layer.parameter_blocks.ParameterBlock;
import sugar.free.telesto.parser.ids.ConfigurationBlockIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ReadParameterBlockMessage extends AppLayerMessage {

    private Class<? extends ParameterBlock> configurationBlockId;
    private ParameterBlock parameterBlock;
    private Service service;

    public ReadParameterBlockMessage() {
        super(MessagePriority.NORMAL, true, false, null);
    }

    @Override
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(2);
        byteBuf.putUInt16LE(ConfigurationBlockIDs.IDS.getB(configurationBlockId));
        return byteBuf;
    }

    @Override
    protected void parse(ByteBuf byteBuf) throws Exception {
        parameterBlock = ConfigurationBlockIDs.IDS.getA(byteBuf.readUInt16LE()).newInstance();
        byteBuf.shift(2); //Restriction level
        parameterBlock.parse(byteBuf);
    }

    public ParameterBlock getParameterBlock() {
        return this.parameterBlock;
    }

    public void setConfigurationBlockId(Class<? extends ParameterBlock> configurationBlockId) {
        this.configurationBlockId = configurationBlockId;
    }
}
