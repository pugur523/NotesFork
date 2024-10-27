package com.chaosthedude.notes.mixins;

import java.util.List;

import com.chaosthedude.notes.config.Configs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.chaosthedude.notes.Notes;
import com.chaosthedude.notes.util.RenderUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.MathHelper;

//#if MC < 12000
//$$ import net.minecraft.client.gui.screen.Screen;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class HUDMixin {
		
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "render", at = @At(value = "TAIL"))
	private void renderPinnedNote(DrawContext context, float tickDelta, CallbackInfo info) {
		if (!client.options.hudHidden && (client.currentScreen == null || client.currentScreen instanceof ChatScreen)) {
			if (Notes.pinnedNote != null && Notes.pinnedNote.isValidScope()) {
				Notes.pinnedNote.update();
				
				final int maxWidth = MathHelper.floor(client.getWindow().getScaledWidth() * Configs.Generic.PINNED_WIDTH_SCALE.getDoubleValue());
				final int maxHeight = MathHelper.floor(client.getWindow().getScaledHeight() * Configs.Generic.PINNED_HEIGHT_SCALE.getDoubleValue());
	
				final String text = Notes.pinnedNote.getFilteredText();
				final List<String> widthSplitLines = RenderUtils.splitStringToWidth(text, maxWidth);
				final List<String> lines = RenderUtils.splitStringToHeight(widthSplitLines, maxHeight);

				// If any lines were removed, add ellipses
				if (widthSplitLines.size() > lines.size()) {
					String lastLine = lines.get(lines.size() - 1);
					lines.set(lines.size() - 1, RenderUtils.addEllipses(lastLine, maxWidth));
				}

				final int renderWidth = RenderUtils.getSplitStringWidth(lines, maxWidth);
				final int renderHeight = RenderUtils.getSplitStringHeight(lines, maxHeight);

				final int renderX = RenderUtils.getPinnedNoteX(Configs.Generic.PINNED_NOTE_POSITION.getStringValue(), renderWidth);
				final int renderY = RenderUtils.getPinnedNoteY(Configs.Generic.PINNED_NOTE_POSITION.getStringValue(), renderHeight);

				//#if MC >= 11900
				final int opacity = (int) (255.0F * client.options.getTextBackgroundOpacity().getValue());
				//#else
				//$$ final int opacity = (int) (255.0F * client.options.getTextBackgroundOpacity(0.6f));
				//#endif
	
				// Render opaque background with padding of 5 on each side
				//#if MC >= 12000
				context.fill(renderX - 5, renderY - 5, renderX + renderWidth + 5, renderY + renderHeight + 5, opacity << 24);
				//#else
				//$$ Screen.fill(context, renderX - 5, renderY - 5, renderX + renderWidth + 5, renderY + renderHeight + 5, opacity << 24);
				//#endif

				// Render note
				RenderUtils.renderSplitString(context, lines, renderX, renderY, 0xffffff);
			}
		}
	}

}
