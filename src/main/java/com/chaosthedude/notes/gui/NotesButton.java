package com.chaosthedude.notes.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

//#if MC <= 11904
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

@Environment(EnvType.CLIENT)
public class NotesButton extends ButtonWidget {

	public NotesButton(int x, int y, int width, int height, Text label, PressAction onPress) {
		super(x, y, width, height, label, onPress, DEFAULT_NARRATION_SUPPLIER);
	}

	@Override
	//#if MC >= 12000
	public void renderButton(DrawContext context, int mouseX, int mouseY, float partialTicks) {
	//#else
	//$$ public void renderButton(MatrixStack context, int mouseX, int mouseY, float delta) {
	//#endif
		if (visible) {
			MinecraftClient mc = MinecraftClient.getInstance();
			float state = 2;
			if (!active) {
				state = 5;
			} else if (isHovered()) {
				state = 4;
			}
			final float f = state / 2 * 0.9F + 0.1F;
			final int color = (int) (255.0F * f);	

			context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color / 2 << 24);
			context.drawCenteredTextWithShadow(mc.textRenderer, getMessage(), getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2, 0xffffff);
		}
	}

	protected int getHoverState(boolean mouseOver) {
		int state = 2;
		if (!active) {
			state = 5;
		} else if (mouseOver) {
			state = 4;
		}

		return state;
	}

}