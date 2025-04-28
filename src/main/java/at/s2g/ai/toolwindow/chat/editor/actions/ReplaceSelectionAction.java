package at.s2g.ai.toolwindow.chat.editor.actions;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.icons.AllIcons.Actions;
import com.intellij.openapi.editor.ex.EditorEx;

import at.s2g.ai.CodeGPTBundle;
import at.s2g.ai.ui.OverlayUtil;
import at.s2g.ai.util.EditorUtil;

import static java.util.Objects.requireNonNull;

public class ReplaceSelectionAction extends AbstractAction {

  private final @NotNull EditorEx toolwindowEditor;
  private final Point locationOnScreen;

  public ReplaceSelectionAction(
      @NotNull EditorEx toolwindowEditor,
      @Nullable Point locationOnScreen) {
    super(
        CodeGPTBundle.get("toolwindow.chat.editor.action.replaceSelection.title"),
        Actions.Replace);
    this.toolwindowEditor = toolwindowEditor;
    this.locationOnScreen = locationOnScreen;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    var project = requireNonNull(toolwindowEditor.getProject());
    if (EditorUtil.isMainEditorTextSelected(project)) {
      var mainEditor = EditorUtil.getSelectedEditor(project);
      if (mainEditor != null) {
        EditorUtil.replaceEditorSelection(mainEditor, toolwindowEditor.getDocument().getText());
      }
    } else {
      OverlayUtil.showSelectedEditorSelectionWarning(project, locationOnScreen);
    }
  }
}
