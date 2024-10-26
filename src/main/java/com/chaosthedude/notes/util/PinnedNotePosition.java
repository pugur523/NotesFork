package com.chaosthedude.notes.util;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum PinnedNotePosition implements IConfigOptionListEntry {
    CENTER_RIGHT("center_right", "notes.label.pinned_note_position.center_right"),
    CENTER_LEFT("center_left", "notes.label.pinned_note_position.center_left"),
    TOP_RIGHT("top_right", "notes.label.pinned_note_position.top_right"),
    TOP_LEFT("top_left", "notes.label.pinned_note_position.top_left"),
    BOTTOM_RIGHT("bottom_right", "notes.label.pinned_note_position.bottom_right"),
    BOTTOM_LEFT("bottom_left", "notes.label.pinned_note_position.bottom_left");


    private final String configString;
    private final String unlocName;


    PinnedNotePosition(String configString, String unlocName) {
        this.configString = configString;
        this.unlocName = unlocName;
    }

    @Override
    public String getStringValue() {
        return this.configString;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(this.unlocName);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();

        if (forward) {
            if (++id >= values().length) {
                id = 0;
            }
        } else {
            if (--id < 0) {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public PinnedNotePosition fromString(String name) {
        return fromStringStatic(name);
    }

    public static PinnedNotePosition fromStringStatic(String name) {
        for (PinnedNotePosition position : PinnedNotePosition.values()) {
            if (position.configString.equalsIgnoreCase(name)) {
                return position;
            }
        }

        return PinnedNotePosition.CENTER_RIGHT;
    }
}
