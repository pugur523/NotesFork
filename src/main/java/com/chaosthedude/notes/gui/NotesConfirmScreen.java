package com.chaosthedude.notes.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;

//#if MC < 11900
//$$ import net.minecraft.text.TranslatableText;
//#endif

@Environment(EnvType.CLIENT)
public class NotesConfirmScreen extends ConfirmScreen {
	
	private BooleanConsumer callbackFunction;

	public NotesConfirmScreen(BooleanConsumer callbackFunction, Text confirmButtonLabel, Text cancelButtonLabel) {
		super(callbackFunction, confirmButtonLabel, cancelButtonLabel);
		this.callbackFunction = callbackFunction;
	}

	@Override
	protected void init() {
		super.init();
		clearChildren();
		//#if MC >= 11900
		addDrawableChild(new NotesButton(this.width / 2 - 155, height / 6 + 96, 150, 20, Text.translatable("gui.yes"), (button) -> {
		//#else
		//$$ addDrawableChild(new NotesButton(this.width / 2 - 155, height / 6 + 96, 150, 20, new TranslatableText("gui.yes"), (button) -> {
		//#endif
			callbackFunction.accept(true);
		}));
		//#if MC >= 11900
		addDrawableChild(new NotesButton(this.width / 2 - 155 + 160, height / 6 + 96, 150, 20, Text.translatable("gui.no"), (button) -> {
		//#else
		//$$ addDrawableChild(new NotesButton(this.width / 2 - 155 + 160, height / 6 + 96, 150, 20, new TranslatableText("gui.no"), (button) -> {
		//#endif
			callbackFunction.accept(false);
		}));
	}

}
