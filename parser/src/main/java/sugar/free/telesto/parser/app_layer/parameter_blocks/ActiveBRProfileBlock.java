package sugar.free.telesto.parser.app_layer.parameter_blocks;

import sugar.free.telesto.descriptors.BasalProfile;
import sugar.free.telesto.parser.ids.ActiveBasalProfileIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ActiveBRProfileBlock extends ParameterBlock {

    private BasalProfile activeBasalProfile;

    @Override
    public void parse(ByteBuf byteBuf) {
        activeBasalProfile = ActiveBasalProfileIDs.IDS.getA(byteBuf.readUInt16LE());
    }

    @Override
    public ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(2);
        byteBuf.putUInt16LE(ActiveBasalProfileIDs.IDS.getB(activeBasalProfile));
        return byteBuf;
    }

    public BasalProfile getActiveBasalProfile() {
        return activeBasalProfile;
    }

    public void setActiveBasalProfile(BasalProfile activeBasalProfile) {
        this.activeBasalProfile = activeBasalProfile;
    }
}
