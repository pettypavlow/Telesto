package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.FirmwareVersions;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetFirmwareVersionsMessage extends AppLayerMessage {

    private FirmwareVersions firmwareVersions;

    public GetFirmwareVersionsMessage() {
        super(MessagePriority.NORMAL, false, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        firmwareVersions = new FirmwareVersions();
        firmwareVersions.setReleaseSWVersion(byteBuf.readASCII(13));
        firmwareVersions.setUiProcSWVersion(byteBuf.readASCII(11));
        firmwareVersions.setPcProcSWVersion(byteBuf.readASCII(11));
        firmwareVersions.setMdTelProcSWVersion(byteBuf.readASCII(11));
        firmwareVersions.setBtInfoPageVersion(byteBuf.readASCII(11));
        firmwareVersions.setConfigIndex(byteBuf.readUInt16LE());
        firmwareVersions.setHistoryIndex(byteBuf.readUInt16LE());
        firmwareVersions.setStateIndex(byteBuf.readUInt16LE());
        firmwareVersions.setVocabularyIndex(byteBuf.readUInt16LE());
    }

    public FirmwareVersions getFirmwareVersions() {
        return this.firmwareVersions;
    }
}
