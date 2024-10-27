package com.chaosthedude.notes.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.registry.Registry;
//#if MC >= 11900
import net.minecraft.registry.RegistryKeys;
//#endif
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public final class StringUtils {

	public static final char[] FILTER_CHARS = new char[] { '\r', '\f' };
	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
	private static final TextRenderer TEXT_RENDERER = CLIENT.textRenderer;

	public static String insertStringAt(String insert, String insertTo, int pos) {
		return insertTo.substring(0, pos) + insert + insertTo.substring(pos, insertTo.length());
	}

	public static List<String> wrapToWidth(String str, int wrapWidth) {
		final List<String> strings = new ArrayList<String>();
		String temp = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\n' || TEXT_RENDERER. getWidth(temp + String.valueOf(c)) >= wrapWidth) {
				strings.add(temp);
				temp = "";
			}

			if (c != '\n') {
				temp = temp + String.valueOf(c);
			}
		}

		strings.add(temp);

		return strings;
	}

	public static List<WrappedString> wrapToWidthWithIndication(String str, int wrapWidth) {
		final List<WrappedString> strings = new ArrayList<WrappedString>();
		String temp = "";
		boolean wrapped = false;
		for (int i = 0; i < str.length(); i++) {
			final char c = str.charAt(i);
			if (c == '\n') {
				strings.add(new WrappedString(temp, wrapped));
				temp = "";
				wrapped = false;
			} else if (TEXT_RENDERER.getWidth(temp + String.valueOf(c)) >= wrapWidth) {
				strings.add(new WrappedString(temp, wrapped));
				temp = "";
				wrapped = true;
			}

			if (c != '\n') {
				temp = temp + String.valueOf(c);
			}
		}

		strings.add(new WrappedString(temp, wrapped));

		return strings;
	}

	public static String filter(String s) {
		String filtered = s.replace(String.valueOf('\t'), "    ");
		for (char c : FILTER_CHARS) {
			filtered = filtered.replace(String.valueOf(c), "");
		}

		return filtered;
	}

	public static String filterFileName(String s) {
		String filtered = s;
		for (char c : SharedConstants.INVALID_CHARS_LEVEL_NAME) {
			filtered = filtered.replace(String.valueOf(c), "~");
		}

		return filtered;
	}
	

	public static String trimStringNewline(String text) {
		while (text != null && text.endsWith("\n")) {
			text = text.substring(0, text.length() - 1);
		}

		return text;
	}

	public static String fixBiomeName(World world, Biome biome) {
		final Optional<Identifier> optionalID = getKeyForBiome(world, biome);
		if (optionalID.isPresent()) {
			final String original = I18n.translate(Util.createTranslationKey("biome", optionalID.get()));
			String fixed = "";
			char pre = ' ';
			for (int i = 0; i < original.length(); i++) {
				final char c = original.charAt(i);
				if (Character.isUpperCase(c) && Character.isLowerCase(pre) && Character.isAlphabetic(pre)) {
					fixed = fixed + " ";
				}
				fixed = fixed + String.valueOf(c);
				pre = c;
			}
			return fixed;
		}
		
		return "";
	}
	
	private static Optional<? extends Registry<Biome>> getBiomeRegistry(World world) {
		//#if MC >= 11900
		return world.getRegistryManager().getOptional(RegistryKeys.BIOME);
		//#else
		//$$ return world.getRegistryManager().getOptional(Registry.BIOME_KEY);
		//#endif
	}

	private static Optional<Identifier> getKeyForBiome(World world, Biome biome) {
		return getBiomeRegistry(world).isPresent() ? Optional.of(getBiomeRegistry(world).get().getId(biome)) : Optional.empty();
	}

}
