package com.glisco.hodgepodge.compat;

import java.util.List;

public interface HodgepodgePlugin {

    void registerManipulators();

    List<String> getRequiredMods();

}
