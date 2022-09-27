package com.wexalian.mods.babblinmc.block.entity.endertank.old;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.wexalian.mods.babblinmc.block.entity.endertank.ColorCode;
import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTank;
import com.wexalian.mods.babblinmc.block.entity.endertank.TankOwner;

public final class TankHelper {
    private static final Table<TankOwner, ColorCode, EnderTank> CONTAINERS_DATABASE = HashBasedTable.create();
    private static final Table<TankOwner, ColorCode, EnderTank> CONTAINERS_CLIENT_CACHE = HashBasedTable.create();
    
    public static Table<TankOwner, ColorCode, EnderTank> getContainerTable(boolean isClient) {
        return isClient ? CONTAINERS_CLIENT_CACHE : CONTAINERS_DATABASE;
    }
    
    public static EnderTank getEnderTank(TankOwner owner, ColorCode code, boolean isClient) {
        return getContainerTable(isClient).get(owner, code);
    }
}
