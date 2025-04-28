package at.s2g.ai.actions.toolwindow;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import at.s2g.ai.actions.editor.EditorActionsUtil;

public class CreateNewConversationAction extends AnAction {

  private final Runnable onCreate;

  public CreateNewConversationAction(Runnable onCreate) {
    super("Create New Chat", "Create new chat", AllIcons.General.Add);
    this.onCreate = onCreate;
    EditorActionsUtil.registerAction(this);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
      var project = event.getProject();
      if (project != null) {
        onCreate.run();
      }

  }
}
