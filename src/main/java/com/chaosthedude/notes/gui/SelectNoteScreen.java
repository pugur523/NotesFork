package com.chaosthedude.notes.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

//#if MC < 11900
//$$ import net.minecraft.text.TranslatableText;
//#endif

@Environment(EnvType.CLIENT)
public class SelectNoteScreen extends Screen {

	private Screen prevScreen;
	private NotesButton newButton;
	private NotesButton selectButton;
	private NotesButton editButton;
	private NotesButton copyButton;
	private NotesButton deleteButton;
	private NotesButton pinButton;
	private NotesButton cancelButton;
	private NotesList selectionList;

	public SelectNoteScreen(Screen prevScreen) {
		//#if MC >= 11900
		super(Text.translatable("notes.selectNote"));
		//#else
		//$$ super(new TranslatableText("notes.selectNote"));
		//#endif
		this.prevScreen = prevScreen;
	}

	@Override
	public void init() {
		setupButtons();
		selectionList = new NotesList(this, client, width + 110, height, 40, height - 64, 36);
		addDrawableChild(selectionList);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
		//#if MC < 12002
		renderBackground(context);
		//#endif
		selectionList.render(context, mouseX, mouseY, partialTicks);
		//#if MC >= 12000
		context.drawCenteredTextWithShadow(textRenderer, I18n.translate("notes.selectNote"), width / 2 + 60, 15, 0xffffff);
		//#else
		//$$ drawCenteredTextWithShadow(context, textRenderer, I18n.translate("notes.selectNote"), width / 2 + 60, 15, 0xffffff);
		//#endif
		super.render(context, mouseX, mouseY, partialTicks);
	}

	@Override
	public void tick() {
		if (selectionList.getSelectedOrNull() != null) {
			//#if MC >= 11900
			pinButton.setMessage(selectionList.getSelectedOrNull().isPinned() ? Text.translatable("notes.unpin") : Text.translatable("notes.pin"));
			//#else
			//$$ pinButton.setMessage(selectionList.getSelectedOrNull().isPinned() ? new TranslatableText("notes.unpin") : new TranslatableText("notes.pin"));
			//#endif
		}
	}

	public void selectNote(NotesListEntry entry) {
		final boolean enable = entry != null;
		selectButton.active = enable;
		deleteButton.active = enable;
		editButton.active = enable;
		copyButton.active = enable;
		pinButton.active = enable;
	}

	private void setupButtons() {
		//#if MC >= 11900
		newButton = addDrawableChild(new NotesButton(10, 40, 110, 20, Text.translatable("notes.new"), (onPress) -> {
		//#else
		//$$ newButton = addDrawableChild(new NotesButton(10, 40, 110, 20, new TranslatableText("notes.new"), (onPress) -> {
		//#endif
			client.setScreen(new EditNoteScreen(SelectNoteScreen.this, null));
		}));
		//#if MC >= 11900
		selectButton = addDrawableChild(new NotesButton(10, 75, 110, 20, Text.translatable("notes.select"), (onPress) -> {
		//#else
		//$$ selectButton = addDrawableChild(new NotesButton(10, 75, 110, 20, new TranslatableText("notes.select"), (onPress) -> {
		//#endif
			NotesListEntry notesEntry = SelectNoteScreen.this.selectionList.getSelectedOrNull();
			if (notesEntry != null) {
				notesEntry.loadNote();
			}
		}));
		//#if MC >= 11900
		editButton = addDrawableChild(new NotesButton(10, 100, 110, 20, Text.translatable("notes.edit"), (onPress) -> {
		//#else
		//$$ editButton = addDrawableChild(new NotesButton(10, 100, 110, 20, new TranslatableText("notes.edit"), (onPress) -> {
		//#endif
			NotesListEntry notesEntry = SelectNoteScreen.this.selectionList.getSelectedOrNull();
			if (notesEntry != null) {
				notesEntry.editNote();
			}
		}));
		//#if MC >= 11900
		copyButton = addDrawableChild(new NotesButton(10, 125, 110, 20, Text.translatable("notes.copy"), (onPress) -> {
		//#else
		//$$ copyButton = addDrawableChild(new NotesButton(10, 125, 110, 20, new TranslatableText("notes.copy"), (onPress) -> {
		//#endif
			NotesListEntry notesEntry = SelectNoteScreen.this.selectionList.getSelectedOrNull();
			notesEntry.copyNote();
		}));
		//#if MC >= 11900
		deleteButton = addDrawableChild(new NotesButton(10, 150, 110, 20, Text.translatable("notes.delete"), (onPress) -> {
		//#else
		//$$ deleteButton = addDrawableChild(new NotesButton(10, 150, 110, 20, new TranslatableText("notes.delete"), (onPress) -> {
		//#endif
			NotesListEntry notesEntry = SelectNoteScreen.this.selectionList.getSelectedOrNull();
			if (notesEntry != null) {
				notesEntry.deleteNote();
			}
		}));
		//#if MC >= 11900
		pinButton = addDrawableChild(new NotesButton(10, 175, 110, 20, Text.translatable("notes.pin"), (onPress) -> {
		//#else
		//$$ pinButton = addDrawableChild(new NotesButton(10, 175, 110, 20, new TranslatableText("notes.pin"), (onPress) -> {
		//#endif
			NotesListEntry notesEntry = SelectNoteScreen.this.selectionList.getSelectedOrNull();
			notesEntry.togglePin();
		}));
		//#if MC >= 11900
		cancelButton = addDrawableChild(new NotesButton(10, height - 30, 110, 20, Text.translatable("gui.cancel"), (onPress) -> {
		//#else
		//$$ cancelButton = addDrawableChild(new NotesButton(10, height - 30, 110, 20, new TranslatableText("gui.cancel"), (onPress) -> {
		//#endif
			client.setScreen(prevScreen);
		}));

		selectButton.active = false;
		deleteButton.active = false;
		editButton.active = false;
		copyButton.active = false;
		pinButton.active = false;
	}

}
