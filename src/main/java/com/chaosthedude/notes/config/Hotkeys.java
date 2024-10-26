package com.chaosthedude.notes.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class Hotkeys {
    public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui","LEFT_ALT,N","Open Config GUI Screen");
    public static final ConfigHotkey OPEN_NOTES_GUI = new ConfigHotkey("openNotesGui", "N", "Open Notes GUI Screen");

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI,
            OPEN_NOTES_GUI
    );
}
