package com.glyceryl6.detachable.block;

import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class EasternDetachableTntBlock extends AbstractDetachableTntBlock {

    public EasternDetachableTntBlock(Properties properties) {
        super(properties);
    }

    protected String getIdName() {
        return "eastern_detachable_tnt";
    }

}