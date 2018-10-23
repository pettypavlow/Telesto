package sugar.free.telesto.parser.app_layer.parameter_blocks;

import sugar.free.telesto.parser.utils.ByteBuf;

public abstract class NameBlock extends ParameterBlock {

    private String name;

    @Override
    public void parse(ByteBuf byteBuf) {
        name = byteBuf.readUTF16(40);
    }

    @Override
    public ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(42);
        byteBuf.putUTF16(name, 40);
        return byteBuf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
