package com.glyceryl6.detachable.block;

import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class NorthernDetachableTntBlock extends AbstractDetachableTntBlock {

    public NorthernDetachableTntBlock(Properties properties) {
        super(properties);
    }

    protected String getIdName() {
        return "northern_detachable_tnt";
    }

}