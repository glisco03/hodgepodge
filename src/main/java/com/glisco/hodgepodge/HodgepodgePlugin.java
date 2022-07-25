package com.glisco.hodgepodge;

import java.util.List;

public interface HodgepodgePlugin {

    void registerManipulators();

    List<String> getRequiredMods();

}
