package sugar.free.telesto.parser.app_layer.parameter_blocks;

import sugar.free.telesto.parser.utils.ByteBuf;

public abstract class ParameterBlock {

    public abstract void parse(ByteBuf byteBuf);
    public abstract ByteBuf getData();

}
