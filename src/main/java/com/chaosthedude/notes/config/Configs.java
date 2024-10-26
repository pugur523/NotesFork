package com.chaosthedude.notes.config;

import com.chaosthedude.notes.Notes;
import com.chaosthedude.notes.util.PinnedNotePosition;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;

public class Configs implements IConfigHandler
{
    private static final String CONFIG_FILE_NAME = Notes.MOD_ID + ".json";

    public static class Generic
    {
        public static final ConfigString DATE_FORMAT = new ConfigString("dateFormat", "M/d/yy h:mm a", "The date format used in timestamps. Uses Java SimpleDateFormat conventions.");
        public static final ConfigBoolean USE_IN_GAME_EDITOR = new ConfigBoolean("useInGameEditor", true, "Determines whether the in-game editor or the system's default text editor will be used to edit notes. If the system editor is not available, the in-game editor will be used.");
        public static final ConfigBoolean USE_IN_GAME_VIEWER = new ConfigBoolean("useInGameViewer", true, "Determines whether the in-game viewer or the system's default text viewer will be used to view notes. If the system viewer is not available, the in-game viewer will be used.");
        public static final ConfigOptionList PINNED_NOTE_POSITION = new ConfigOptionList("pinnedNotePosition", PinnedNotePosition.CENTER_RIGHT, "The HUD position of a pinned note.");
        public static final ConfigDouble PINNED_WIDTH_SCALE = new ConfigDouble("pinnedWidthScale", 0.2, 0, 8.0, "The maximum width of a pinned note relative to the screen's width.");
        public static final ConfigDouble PINNED_HEIGHT_SCALE = new ConfigDouble("pinnedHeightScale", 1.0, 0, 8.0, "The maximum percentage of the screen's display height that a pinned note can take up.");
        public static final ConfigBoolean WRAP_NOTE = new ConfigBoolean("wrapNote", true, "Determines whether displayed notes will be word wrapped.");
        public static final ConfigInteger TAB_INDENT_SCALE = new ConfigInteger("tabIndentScale", 2, 0, 16, "Determines tab's indent size in editor mode. (read as space's count)");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                DATE_FORMAT,
                USE_IN_GAME_EDITOR,
                USE_IN_GAME_VIEWER,
                PINNED_NOTE_POSITION,
                PINNED_WIDTH_SCALE,
                PINNED_HEIGHT_SCALE,
                WRAP_NOTE,
                TAB_INDENT_SCALE
        );
    }

    public static void onConfigLoaded() {
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
            }
        }

        onConfigLoaded();
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
