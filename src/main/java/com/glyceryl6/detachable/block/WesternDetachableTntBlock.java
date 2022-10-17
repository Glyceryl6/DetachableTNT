package com.glyceryl6.detachable.block;

import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class WesternDetachableTntBlock extends AbstractDetachableTntBlock {

    public WesternDetachableTntBlock(Properties properties) {
        super(properties);
    }

    protected String getIdName() {
        return "western_detachable_tnt";
    }

}