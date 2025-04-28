package at.s2g.ai.toolwindow.chat.editor.actions;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons.Actions;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;

import at.s2g.ai.CodeGPTBundle;
import at.s2g.ai.actions.ActionType;
import at.s2g.ai.actions.TrackableAction;
import at.s2g.ai.ui.OverlayUtil;

public class CopyAction extends TrackableAction {

  private final @NotNull Editor toolwindowEditor;

  public CopyAction(@NotNull Editor toolwindowEditor) {
    super(
        CodeGPTBundle.get("shared.copyCode"),
        CodeGPTBundle.get("shared.copyToClipboard"),
        Actions.Copy,
        ActionType.COPY_CODE);
    this.toolwindowEditor = toolwindowEditor;
  }

  @Override
  public void handleAction(@NotNull AnActionEvent event) {
    copyToClipboard(toolwindowEditor.getDocument().getText());
    showCopyBalloon(event);
  }

  public static void copyToClipboard(String text) {
    Toolkit.getDefaultToolkit()
        .getSystemClipboard()
        .setContents(new StringSelection(text), null);
  }

  public static void showCopyBalloon(AnActionEvent event) {
    var mouseEvent = (MouseEvent) event.getInputEvent();
    if (mouseEvent != null) {
      var locationOnScreen = mouseEvent.getLocationOnScreen();
      locationOnScreen.y = locationOnScreen.y - 16;

      OverlayUtil.showInfoBalloon(
          CodeGPTBundle.get("shared.copiedToClipboard"),
          locationOnScreen);
    }
  }
}
