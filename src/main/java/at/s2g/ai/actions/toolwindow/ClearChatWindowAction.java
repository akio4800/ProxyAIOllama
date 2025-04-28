package at.s2g.ai.actions.toolwindow;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

import at.s2g.ai.actions.editor.EditorActionsUtil;

public class ClearChatWindowAction extends DumbAwareAction {

  private final Runnable onActionPerformed;

  public ClearChatWindowAction(Runnable onActionPerformed) {
    super("Clear Window", "Clears a chat window", AllIcons.General.Reset);
    this.onActionPerformed = onActionPerformed;
    EditorActionsUtil.registerAction(this);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
      onActionPerformed.run();
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }
}