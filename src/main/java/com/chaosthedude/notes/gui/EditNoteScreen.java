package com.chaosthedude.notes.gui;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.chaosthedude.notes.Notes;
import com.chaosthedude.notes.note.Note;
import com.chaosthedude.notes.note.Scope;
import com.chaosthedude.notes.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EditNoteScreen extends Screen {

	private final Screen parentScreen;
	private NotesButton saveButton;
	private NotesButton globalButton;
	private NotesButton insertBiomeButton;
	private NotesButton insertChunkButton;
	private NotesButton insertCoordsButton;
	private NotesButton cancelButton;
	private NotesTitleField noteTitleField;
	private NotesTextField noteTextField;
	private String saveDirName;
	private Note note;
	private Scope scope;
	private boolean pinned;

	public EditNoteScreen(Screen parentScreen, @Nullable Note note) {
		super(new TextComponent(note != null ? I18n.get("notes.editNote") : I18n.get("notes.newNote")));
		this.parentScreen = parentScreen;
		if (note != null) {
			this.note = note;
		} else {
			this.note = new Note("New Note", "", Scope.getCurrentScope());
		}

		scope = Scope.getCurrentScope();
		pinned = this.note.isPinned();
	}

	@Override
	public void init() {
		minecraft.keyboardHandler.setSendRepeatsToGui(true);

		setupTextFields();
		setupButtons();
	}

	@Override
	public void tick() {
		noteTitleField.tick();
		noteTextField.tick();

		insertBiomeButton.active = insertChunkButton.active = insertCoordsButton.active = noteTextField.isFocused();
	}
	
	@Override
	public void onClose() {
		minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int par2, int par3) {
		super.keyPressed(keyCode, par2, par3);
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.setScreen(parentScreen);
			return true;
		} else if (keyCode == GLFW.GLFW_KEY_TAB && noteTitleField.isFocused()) {
			noteTitleField.setFocused(false);
			noteTextField.setFocused(true);
			return true;
		}
		
		updateNote();
		return false;
	}
	
	@Override
	public boolean keyReleased(int keyCode, int par2, int par3) {
		updateNote();
		return super.keyReleased(keyCode, par2, par3);
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(stack);
		drawCenteredString(stack, font, title.getString(), width / 2 + 60, 15, 0xffffff);
		drawCenteredString(stack, font, I18n.get("notes.saveAs", note.getUncollidingSaveName(note.getTitle())), width / 2 + 55, 65, 0x808080);

		super.render(stack, mouseX, mouseY, partialTicks);
	}

	private void setupButtons() {
		saveButton = addRenderableWidget(new NotesButton(10, 40, 110, 20, new TranslatableComponent("notes.save"), (onPress) -> {
			updateNote();
			note.save();
			minecraft.setScreen(new DisplayNoteScreen(parentScreen, note));
			if (pinned) {
				Notes.pinnedNote = note;
			}
		}));
		globalButton = addRenderableWidget(new NotesButton(10, 65, 110, 20, new TextComponent(I18n.get("notes.global") + ": " + (note.getScope() == Scope.GLOBAL ? I18n.get("notes.on") : I18n.get("notes.off"))), (onPress) -> {
			if (scope == Scope.GLOBAL) {
				scope = Scope.getCurrentScope();
			} else {
				scope = Scope.GLOBAL;
			}

			globalButton.setMessage(new TextComponent(I18n.get("notes.global") + (scope == Scope.GLOBAL ? ": " + I18n.get("notes.on") : ": " + I18n.get("notes.off"))));
			updateNote();
		}));
		insertBiomeButton = addRenderableWidget(new NotesButton(10, 90, 110, 20, new TranslatableComponent("notes.biome"), (onPress) -> {
			insertBiome();
		}));
		insertChunkButton = addRenderableWidget(new NotesButton(10, 115, 110, 20, new TranslatableComponent("notes.chunk"), (onPress) -> {
			insertChunk();
		}));
		insertCoordsButton = addRenderableWidget(new NotesButton(10, 140, 110, 20, new TranslatableComponent("notes.coordinates"), (onPress) -> {
			insertCoords();
		}));
		cancelButton = addRenderableWidget(new NotesButton(10, height - 30, 110, 20, new TranslatableComponent("gui.cancel"), (onPress) -> {
			minecraft.setScreen(parentScreen);
		}));

		insertBiomeButton.active = false;
		insertChunkButton.active = false;
		insertCoordsButton.active = false;
	}

	private void setupTextFields() {
		noteTitleField = addRenderableWidget(new NotesTitleField(font, 130, 40, width - 140, 20, new TextComponent("")));
		noteTitleField.setValue(note.getTitle());
		addRenderableWidget(noteTitleField);
		noteTitleField.changeFocus(true);
		noteTitleField.setFocused(true);
		setFocused(noteTitleField);

		noteTextField = addRenderableWidget(new NotesTextField(font, 130, 85, width - 140, height - 95, 5));
		noteTextField.setText(note.getFilteredText());
		addRenderableOnly(noteTextField);
	}

	private void updateNote() {
		note.setTitle(noteTitleField.getValue());
		note.setText(noteTextField.getText());
		note.setScope(scope);
	}

	private void insertBiome() {
		noteTextField.insert(StringUtils.fixBiomeName(minecraft.level, minecraft.level.getBiome(minecraft.player.blockPosition())));
	}

	private void insertChunk() {
		noteTextField.insert((int) minecraft.player.chunkPosition().x + ", " + (int) minecraft.player.chunkPosition().z);
	}

	private void insertCoords() {
		noteTextField.insert((int) minecraft.player.getBlockX() + ", " + (int) minecraft.player.getBlockY() + ", " + (int) minecraft.player.getBlockZ());
	}

}
