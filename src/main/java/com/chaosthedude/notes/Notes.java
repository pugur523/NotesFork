package com.chaosthedude.notes;

import com.chaosthedude.notes.note.Note;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Notes implements ClientModInitializer {

	public static final String MOD_NAME = "NotesFork";

	public static final String MOD_ID = "notes";

	public static String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();;

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static Note pinnedNote;

	// private static KeyBinding openNotes;

	@Override
	public void onInitializeClient() {
		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
		LOGGER.info("{} (v{}) has initialized!", MOD_NAME, MOD_VERSION);
	}

}
