package com.glyceryl6.detachable.block;

import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class SouthernDetachableTntBlock extends AbstractDetachableTntBlock {

    public SouthernDetachableTntBlock(Properties properties) {
        super(properties);
    }

    protected String getIdName() {
        return "southern_detachable_tnt";
    }

}