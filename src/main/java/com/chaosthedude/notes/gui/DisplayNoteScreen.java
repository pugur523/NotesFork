package com.chaosthedude.notes.gui;

import java.util.ArrayList;
import java.util.List;

import com.chaosthedude.notes.Notes;
import com.chaosthedude.notes.config.Configs;
import com.chaosthedude.notes.note.Note;
import com.chaosthedude.notes.util.RenderUtils;
import com.chaosthedude.notes.util.StringUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

//#if MC <= 11802
//$$ import net.minecraft.text.LiteralText;
//$$ import net.minecraft.text.TranslatableText;
//#endif

@Environment(EnvType.CLIENT)
public class DisplayNoteScreen extends Screen {

	private final Screen parentScreen;
	private NotesButton doneButton;
	private NotesButton pinButton;
	private NotesButton editButton;
	private NotesButton deleteButton;
	private NotesButton prevButton;
	private NotesButton nextButton;
	private Note note;
	private int page;
	private List<String> pages;

	public DisplayNoteScreen(Screen parentScreen, Note note) {
		//#if MC >= 11900
		super(Text.literal(note.getTitle()));
		//#else
		//$$ super(new LiteralText(note.getTitle()));
		//#endif
		this.parentScreen = parentScreen;
		this.note = note;

		page = 0;
		pages = new ArrayList<String>();
		pages.add("");
	}

	@Override
	public void init() {
		setupButtons();
		setupPages();
	}

	@Override
	public void tick() {
		prevButton.active = page > 0;
		nextButton.active = page < pages.size() - 1;
	}

	//#if MC >= 12001
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
		//#if MC >= 12002
		//$$  renderBackground(context, mouseX, mouseY, partialTicks);
		//#else
		renderBackground(context);
		//#endif
		context.drawCenteredTextWithShadow(textRenderer, title.getString(), width / 2 + 60, 15, -1);
		displayNote(context);

		super.render(context, mouseX, mouseY, partialTicks);
	}
	//#endif

	public void displayNote(DrawContext context) {
		List<String> lines = RenderUtils.splitStringToWidth(pages.get(page), width - 200);
		RenderUtils.renderSplitString(context, lines, 160, 40, 0xFFFFFF);
	}

	private void setupButtons() {

		//#if MC >= 11900
		editButton = addDrawableChild(new NotesButton(10, 40, 110, 20, Text.translatable("notes.edit"), (onPress) -> {
		//#else
		//$$ editButton = addDrawableChild(new NotesButton(10, 40, 110, 20, new TranslatableText("notes.edit"), (onPress) -> {
		//#endif
			client.setScreen(new EditNoteScreen(DisplayNoteScreen.this.parentScreen, note));
		}));

		//#if MC >= 11900
		deleteButton = addDrawableChild(new NotesButton(10, 65, 110, 20, Text.translatable("notes.delete"), (onPress) -> {
		//#else
		//$$ deleteButton = addDrawableChild(new NotesButton(10, 65, 110, 20, new TranslatableText("notes.delete"), (onPress) -> {
		//#endif
			deleteNote();
		}));

		//#if MC >= 11900
		pinButton = addDrawableChild(new NotesButton(10, 90, 110, 20, isPinned() ? Text.translatable("notes.unpin") : Text.translatable("notes.pin"), (onPress) -> {
		//#else
		//$$ pinButton = addDrawableChild(new NotesButton(10, 90, 110, 20, isPinned() ? new TranslatableText("notes.unpin") : new TranslatableText("notes.pin"),(onPress) -> {
		//#endif
			togglePin();
			if (isPinned()) {
				client.setScreen(null);
			}
		}));

		//#if MC >= 11900
		doneButton = addDrawableChild(new NotesButton(10, height - 30, 110, 20, Text.translatable("gui.done"), (onPress) -> {
		//#else
		//$$ doneButton = addDrawableChild(new NotesButton(10, height - 30, 110, 20, new TranslatableText("gui.done"), (onPress) -> {
		//#endif
			client.setScreen(parentScreen);
		}));

		//#if MC >= 11900
		prevButton = addDrawableChild(new NotesButton(130, height - 30, 20, 20, Text.translatable("<"), (onPress) -> {
		//#else
		//$$ prevButton = addDrawableChild(new NotesButton(130, height - 30, 20, 20, new TranslatableText("<"), (onPress) -> {
		//#endif
			if (page > 0) {
				page--;
			}
		}));

		//#if MC >= 11900
		nextButton = addDrawableChild(new NotesButton(width - 30, height - 30, 20, 20, Text.translatable(">"), (onPress) -> {
		//#else
		//$$ nextButton = addDrawableChild(new NotesButton(width - 30, height - 30, 20, 20, new TranslatableText(">"), (onPress) -> {
		//#endif
			if (page < pages.size() - 1) {
				page++;
			}
		}));
	}

	private void setupPages() {
		if (note != null) {
			final List<String> lines = Configs.Generic.WRAP_NOTE.getBooleanValue() ? RenderUtils.splitStringToWidth(note.getFilteredText(), width - 200) : StringUtils.wrapToWidth(note.getFilteredText(), width - 200);
			pages = new ArrayList<String>();
			int lineCount = 0;
			String page = "";
			for (String line : lines) {
				if (lineCount > 15) {
					pages.add(page);
					page = "";
					lineCount = 0;
				}

				page = page + line + "\n";
				lineCount++;
			}

			if (!page.isEmpty()) {
				pages.add(page);
			}
		}

		if (pages.isEmpty()) {
			pages.add("");
		}
	}

	private boolean isPinned() {
		return note.equals(Notes.pinnedNote);
	}

	private void togglePin() {
		if (isPinned()) {
			Notes.pinnedNote = null;
			//#if MC >= 11900
			pinButton.setMessage(Text.translatable("notes.pin"));
			//#else
			//$$ pinButton.setMessage(new TranslatableText("notes.pin"));
			//#endif
		} else {
			Notes.pinnedNote = note;
			//#if MC >= 11900
			pinButton.setMessage(Text.translatable("notes.unpin"));
			//#else
			//$$ pinButton.setMessage(new TranslatableText("notes.unpin"));
			//#endif
		}
	}

	private void deleteNote() {
		client.setScreen(new NotesConfirmScreen((result) -> {
			if (result) {
				note.delete();
			}

			DisplayNoteScreen.this.client.setScreen(parentScreen);
		//#if MC >= 11900
		}, Text.translatable("notes.confirmDelete"), Text.literal(note.getTitle())));
		//#else
		//$$ }, new TranslatableText("notes.confirmDelete"), new LiteralText(note.getTitle())));
		//#endif
	}

}