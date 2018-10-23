package sugar.free.telesto.parser.app_layer.parameter_blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sugar.free.telesto.descriptors.BasalProfileBlock;
import sugar.free.telesto.parser.utils.ByteBuf;

public abstract class BRProfileBlock extends ParameterBlock {

    private BasalProfileBlock[] profileBlocks;

    @Override
    public void parse(ByteBuf byteBuf) {
        List<BasalProfileBlock> profileBlocks = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            BasalProfileBlock basalProfileBlock = new BasalProfileBlock();
            basalProfileBlock.setDuration(byteBuf.readUInt16LE());
            profileBlocks.add(basalProfileBlock);
        }
        for (int i = 0; i < 24; i++) profileBlocks.get(i).setBasalAmount(byteBuf.readUInt16Decimal());
        Iterator<BasalProfileBlock> iterator = profileBlocks.iterator();
        while (iterator.hasNext())
            if (iterator.next().getDuration() == 0)
                iterator.remove();
        this.profileBlocks = profileBlocks.toArray(new BasalProfileBlock[profileBlocks.size()]);
    }

    @Override
    public ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(96);
        for (int i = 0; i < 24; i++) {
            if (profileBlocks.length > i) byteBuf.putUInt16LE(profileBlocks[i].getDuration());
            else byteBuf.putUInt16LE(0);
        }
        for (int i = 0; i < 24; i++) {
            if (profileBlocks.length > i) byteBuf.putUInt16Decimal(profileBlocks[i].getBasalAmount());
            else byteBuf.putUInt16Decimal(0);
        }
        return byteBuf;
    }

    public BasalProfileBlock[] getProfileBlocks() {
        return profileBlocks;
    }

    public void setProfileBlocks(BasalProfileBlock[] profileBlocks) {
        this.profileBlocks = profileBlocks;
    }
}
