package sugar.free.telesto.parser.ids;

import sugar.free.telesto.parser.app_layer.parameter_blocks.ActiveBRProfileBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile1Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile1NameBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile2Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile2NameBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile3Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile3NameBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile4Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile4NameBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile5Block;
import sugar.free.telesto.parser.app_layer.parameter_blocks.BRProfile5NameBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.ParameterBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.FactoryMaxBolusAmountBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.FactoryMinBolusAmountBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.MaxBolusAmountBlock;
import sugar.free.telesto.parser.app_layer.parameter_blocks.SystemIdentificationBlock;
import sugar.free.telesto.parser.utils.BiMap;

public class ConfigurationBlockIDs {

    public static final BiMap<Class<? extends ParameterBlock>, Integer> IDS = new BiMap<>();

    static {
        IDS.put(FactoryMaxBolusAmountBlock.class, 41222);
        IDS.put(MaxBolusAmountBlock.class, 31);
        IDS.put(FactoryMinBolusAmountBlock.class, 60183);
        IDS.put(SystemIdentificationBlock.class, 35476);
        IDS.put(BRProfile1Block.class, 7136);
        IDS.put(BRProfile2Block.class, 7167);
        IDS.put(BRProfile3Block.class, 7532);
        IDS.put(BRProfile4Block.class, 7539);
        IDS.put(BRProfile5Block.class, 7567);
        IDS.put(BRProfile1NameBlock.class, 48265);
        IDS.put(BRProfile2NameBlock.class, 48278);
        IDS.put(BRProfile3NameBlock.class, 48975);
        IDS.put(BRProfile4NameBlock.class, 48976);
        IDS.put(BRProfile5NameBlock.class, 49068);
        IDS.put(ActiveBRProfileBlock.class, 7568);
    }

}
