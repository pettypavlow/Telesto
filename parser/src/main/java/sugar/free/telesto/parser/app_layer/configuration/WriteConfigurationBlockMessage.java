package sugar.free.telesto.parser.app_layer.configuration;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.app_layer.parameter_blocks.ParameterBlock;
import sugar.free.telesto.parser.ids.ConfigurationBlockIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class WriteConfigurationBlockMessage extends AppLayerMessage {

    private ParameterBlock parameterBlock;
    private Class<? extends ParameterBlock> configurationBlockId;

    public WriteConfigurationBlockMessage() {
        super(MessagePriority.NORMAL, false, true, Service.CONFIGURATION);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf configBlockData = parameterBlock.getData();
        ByteBuf data = new ByteBuf(4 + configBlockData.getSize());
        data.putUInt16LE(ConfigurationBlockIDs.IDS.getB(parameterBlock.getClass()));
        data.putUInt16LE(31);
        data.putByteBuf(configBlockData);
        return data;
    }

    @Override
    protected void parse(ByteBuf byteBuf) throws Exception {
        configurationBlockId = ConfigurationBlockIDs.IDS.getA(byteBuf.readUInt16LE());
    }

    public Class<? extends ParameterBlock> getConfigurationBlockId() {
        return this.configurationBlockId;
    }

    public void setParameterBlock(ParameterBlock parameterBlock) {
        this.parameterBlock = parameterBlock;
    }
}
