package com.chaosthedude.notes.note;

import java.io.File;
import java.util.Objects;

import com.chaosthedude.notes.util.FileUtils;
import com.chaosthedude.notes.util.StringUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;

public class Scope {

	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	public static final Scope GLOBAL = new Scope("notes.scope.global", "global") {
		@Override
		public boolean isActive() {
			return true;
		}
	};

	public static final Scope LOCAL = new Scope("notes.scope.local", "local") {
		@Override
		public File getCurrentSaveDirectory() {
			final File saveDirFile = new File(getRootSaveDirectory(), getWorldName());
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}

			return saveDirFile;
		}

		@Override
		public boolean isActive() {
			return isLocal();
		}
	};

	public static final Scope REMOTE = new Scope("notes.scope.remote", "remote") {
		@Override
		public File getCurrentSaveDirectory() {
			final File saveDirFile = new File(getRootSaveDirectory(), getServerIP());
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}

			return saveDirFile;
		}

		@Override
		public boolean isActive() {
			return isLocal();
		}
	};

	private String unlocName;
	private String saveDir;

	public Scope(String unlocName, String saveDir) {
		this.unlocName = unlocName;
		this.saveDir = saveDir;
	}

	public String localize() {
		return I18n.translate(unlocName);
	}

	public String format() {
		return "(" + localize() + ")";
	}

	public File getCurrentSaveDirectory() {
		return getRootSaveDirectory();
	}

	public File getRootSaveDirectory() {
		final File saveDirFile = new File(FileUtils.getRootSaveDirectory(), saveDir);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}

		return saveDirFile;
	}

	public boolean isActive() {
		return false;
	}

	public static Scope getCurrentScope() {
		if (isLocal()) {
			return LOCAL;
		} else if (isRemote()) {
			return REMOTE;
		}

		return GLOBAL;
	}

	public static Scope getCurrentScopeOrGlobal(boolean global) {
		return global ? GLOBAL : getCurrentScope();
	}

	public static Scope getScopeFromParentFile(File parentFile) {
		if (LOCAL.getRootSaveDirectory().equals(parentFile.getParentFile())) {
			return LOCAL;
		} else if (REMOTE.getRootSaveDirectory().equals(parentFile.getParentFile())) {
			return REMOTE;
		}

		return GLOBAL;
	}

	public static boolean currentScopeIsValid() {
		return getCurrentScope() != GLOBAL;
	}

	public static boolean isLocal() {
		return CLIENT.isIntegratedServerRunning();
	}

	public static boolean isRemote() {
		return CLIENT.getCurrentServerEntry() != null;
	}

	public static String getServerIP() {
		if (isRemote()) {
			return StringUtils.filterFileName(CLIENT.getCurrentServerEntry().address);
		}

		return null;
	}

	public static String getWorldName() {
		if (isLocal()) {
			//#if MC < 12100
			String[] result = StringUtils.filterFileName(Objects.requireNonNull(CLIENT.getServer()).getRunDirectory().getName()).split("~");
			//#else
			//$$ String[] result = StringUtils.filterFileName(String.valueOf(Objects.requireNonNull(CLIENT.getServer()).getRunDirectory().getName(0))).split("~");
			//#endif
			return StringUtils.filterFileName(result[result.length - 1]);
		}

		return null;
	}

}
