package com.chaosthedude.notes.config;

import com.chaosthedude.notes.gui.GuiConfigs;
import com.chaosthedude.notes.gui.SelectNoteScreen;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeyCallbackAdjustable;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Callbacks {

    public static void init() {
        MinecraftClient mc = MinecraftClient.getInstance();
        IHotkeyCallback callbackGeneric = new KeyCallbackHotkeysGeneric();
        Hotkeys.OPEN_CONFIG_GUI.getKeybind().setCallback(callbackGeneric);
        Hotkeys.OPEN_NOTES_GUI.getKeybind().setCallback(callbackGeneric);
    }

    private static class KeyCallbackHotkeysGeneric implements IHotkeyCallback
    {

        public KeyCallbackHotkeysGeneric()
        {
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            if (key == Hotkeys.OPEN_CONFIG_GUI.getKeybind())
            {
                GuiBase.openGui(new GuiConfigs());
                return true;
            }
            else if (key == Hotkeys.OPEN_NOTES_GUI.getKeybind()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc != null) mc.setScreen(new SelectNoteScreen(mc.currentScreen));
            }

            return false;
        }
    }

    public static class FeatureCallbackHold implements IValueChangeCallback<IConfigBoolean>
    {
        private final KeyBinding keyBind;

        public FeatureCallbackHold(KeyBinding keyBind)
        {
            this.keyBind = keyBind;
        }

        @Override
        public void onValueChanged(IConfigBoolean config)
        {
            if (config.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.keyBind.getBoundKeyTranslationKey()), true);
                KeyBinding.onKeyPressed(InputUtil.fromTranslationKey(this.keyBind.getBoundKeyTranslationKey()));
            }
            else
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.keyBind.getBoundKeyTranslationKey()), false);
            }
        }
    }

    private record KeyCallbackAdjustableFeature(IConfigBoolean config) implements IHotkeyCallback {
            private static IHotkeyCallback createCallback(IConfigBoolean config) {
                return new KeyCallbackAdjustable(config, new KeyCallbackAdjustableFeature(config));
            }

        @Override
            public boolean onKeyAction(KeyAction action, IKeybind key) {
                this.config.toggleBooleanValue();

                boolean enabled = this.config.getBooleanValue();
                String strStatus = enabled ? "ON" : "OFF";
                String preGreen = GuiBase.TXT_GREEN;
                String preRed = GuiBase.TXT_RED;
                String rst = GuiBase.TXT_RST;
                String prettyName = this.config.getPrettyName();
                strStatus = (enabled ? preGreen : preRed) + strStatus + rst;

                InfoUtils.printActionbarMessage("Toggled %s %s", prettyName, strStatus);
                return true;
            }
        }

}
