package at.s2gplus.ai.toolwindow.chat.editor.actions;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

import com.intellij.icons.AllIcons.Actions;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditorManager;

import at.s2gplus.ai.ui.OverlayUtil;
import at.s2gplus.ai.util.EditorDiffUtil;
import at.s2gplus.ai.util.EditorUtil;

import static java.util.Objects.requireNonNull;

public class DiffAction extends AbstractAction {

  private final EditorEx editor;
  private final Point locationOnScreen;

  public DiffAction(EditorEx editor, @Nullable Point locationOnScreen) {
    super("Diff Selection", Actions.DiffWithClipboard);
    this.editor = editor;
    this.locationOnScreen = locationOnScreen;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    var project = requireNonNull(editor.getProject());
    var mainEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    if (mainEditor == null || !EditorUtil.hasSelection(mainEditor)) {
      OverlayUtil.showSelectedEditorSelectionWarning(project, locationOnScreen);
      return;
    }

    EditorDiffUtil.showDiff(
        project,
        editor,
        requireNonNull(mainEditor.getSelectionModel().getSelectedText()));
  }
}
