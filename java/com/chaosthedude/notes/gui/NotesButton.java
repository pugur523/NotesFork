package com.chaosthedude.notes.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class NotesButton extends Button {

	public NotesButton(int x, int y, int width, int height, Component label, OnPress onPress) {
		super(x, y, width, height, label, onPress);
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			Minecraft mc = Minecraft.getInstance();
			float state = 2;
			if (!active) {
				state = 5;
			} else if (isHovered) {
				state = 4;
			}
			final float f = state / 2 * 0.9F + 0.1F;
			final int color = (int) (255.0F * f);

			GuiComponent.fill(poseStack, x, y, x + width, y + height, color / 2 << 24);
			drawCenteredString(poseStack, mc.font, getMessage(), x + width / 2, y + (height - 8) / 2, 0xffffff);
		}
	}

}