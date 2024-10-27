package com.chaosthedude.notes.gui;

import java.util.Objects;

import com.chaosthedude.notes.note.Note;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;

//#if MC < 12000
//$$ import net.minecraft.client.gui.screen.Screen;
//#endif

@Environment(EnvType.CLIENT)
public class NotesList extends EntryListWidget<NotesListEntry> {

	private final SelectNoteScreen parentScreen;
	private boolean pseudoRenderSelection = true;

	public NotesList(SelectNoteScreen notesScreen, MinecraftClient mc, int width, int height, int top, int bottom, int slotHeight) {
		//#if MC >= 12004
		//$$ super(mc, width, height, top, bottom);
		//#else
		super(mc, width, height, top, bottom, slotHeight);
		//#endif
		this.parentScreen = notesScreen;
		refreshList();
	}
	
	@Override
	protected int getScrollbarPositionX() {
		return super.getScrollbarPositionX() + 20;
	}

	@Override
	public int getRowWidth() {
		return super.getRowWidth() + 50;
	}

	@Override
	protected boolean isSelectedEntry(int index) {
		return index >= 0 && index < children().size() ? children().get(index).equals(getSelectedOrNull()) : false;
	}
	
	@Override
	//#if MC >= 12004
	//$$ public void renderWidget(DrawContext context, int mouseX, int mouseY, float partialTicks) {
	//#else
	public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
	//#endif
		//#if MC >= 11900
		renderList(context, mouseX, mouseY, partialTicks);
		//#else
		//$$ renderList(context, 0, 0, mouseX, mouseY, partialTicks);
		//#endif
	}

	@Override
	//#if MC >= 11900
	protected void renderList(DrawContext context, int mouseX, int mouseY, float partialTicks) {
	//#else
	//$$ protected void renderList(MatrixStack context, int x, int y, int mouseX, int mouseY, float partialTicks) {
	//#endif
		int i = getEntryCount();
		for (int j = 0; j < i; ++j) {
			int k = getRowTop(j);
			int l = getRowBottom(j);
			//#if MC < 12004
			if (l >= top && k <= bottom) {
			//#else
			//$$ if (l >= getY() && k <= getBottom()) {
			//#endif
				int j1 = this.itemHeight - 4;
				NotesListEntry e = this.getEntry(j);
				int k1 = getRowWidth();
				if (pseudoRenderSelection && isSelectedEntry(j)) {
					//#if MC < 12004
					final int insideLeft = left + width / 2 - getRowWidth() / 2 + 2;
					//#else
					//$$ final int insideLeft = getX() + width / 2 - getRowWidth() / 2 + 2;
					//#endif
					//#if MC >= 12000
					context.fill(insideLeft - 4, k - 4, insideLeft + getRowWidth() + 4, k + itemHeight, 255 / 2 << 24);
					//#else
					//$$ Screen.fill(context, insideLeft - 4, k - 4, insideLeft + getRowWidth() + 4, k + itemHeight, 255 / 2 << 24);
					//#endif
				}

				int j2 = this.getRowLeft();
				e.render(context, j, k, j2, k1, j1, mouseX, mouseY, isMouseOver((double) mouseX, (double) mouseY) && Objects .equals(getEntryAtPosition((double) mouseX, (double) mouseY), e), partialTicks);
			}
		}
	}

	//#if MC < 12002
	@Override
	public void setRenderSelection(boolean value) {
		super.setRenderSelection(value);
		pseudoRenderSelection = value;
	}

	@Override
	protected void renderBackground(DrawContext context) {
		parentScreen.renderBackground(context);
	}

	//#endif
	
	protected int getRowBottom(int index) {
		return getRowTop(index) + itemHeight;
	}

	public void refreshList() {
		clearEntries();
		for (Note note : Note.getCurrentNotes()) {
			addEntry(new NotesListEntry(this, note));
		}
	}

	public void selectNote(NotesListEntry entry) {
		setSelected(entry);
		parentScreen.selectNote(entry);
	}

	public SelectNoteScreen getParentScreen() {
		return parentScreen;
	}

	@Override
	//#if MC < 12004
	public void appendNarrations(NarrationMessageBuilder builder) {
	//#else
	//$$ protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	//#endif

		
	}

}
