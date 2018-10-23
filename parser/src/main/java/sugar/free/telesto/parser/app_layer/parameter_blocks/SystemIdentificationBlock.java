package sugar.free.telesto.parser.app_layer.parameter_blocks;

import sugar.free.telesto.parser.utils.ByteBuf;

public class SystemIdentificationBlock extends ParameterBlock {

    private String serialNumber;
    private long systemIdAppendix;
    private String manufacturingDate;


    @Override
    public void parse(ByteBuf byteBuf) {
        serialNumber = byteBuf.readUTF16(18);
        systemIdAppendix = byteBuf.readUInt32LE();
        manufacturingDate = byteBuf.readUTF16(22);
    }

    @Override
    public ByteBuf getData() {
        return null;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public long getSystemIdAppendix() {
        return systemIdAppendix;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }
}
