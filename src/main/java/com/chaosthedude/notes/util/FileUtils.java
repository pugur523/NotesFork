package com.chaosthedude.notes.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import net.minecraft.client.MinecraftClient;

public class FileUtils {

	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	public static File getRootSaveDirectory() {
		final File saveDir = new File(CLIENT.runDirectory, "notes");
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		return saveDir;
	}

	public static boolean isNote(File file) {
		return getFileExtension(file).equals("txt");
	}

	public static String getFileName(File file) {
		return FilenameUtils.getBaseName(file.getAbsolutePath());
	}

	public static String getFileExtension(File file) {
		return FilenameUtils.getExtension(file.getAbsolutePath());
	}

}
