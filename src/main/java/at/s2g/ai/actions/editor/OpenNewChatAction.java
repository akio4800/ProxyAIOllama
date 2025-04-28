package at.s2g.ai.actions.editor;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import at.s2g.ai.Icons;
import at.s2g.ai.conversations.ConversationsState;
import at.s2g.ai.toolwindow.chat.ChatToolWindowContentManager;

public class OpenNewChatAction extends AnAction {

  public OpenNewChatAction() {
    super(Icons.OpenNewTab);
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    event.getPresentation().setEnabled(event.getProject() != null);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    var project = event.getProject();
    if (project != null) {
      ConversationsState.getInstance().setCurrentConversation(null);
      var tabPanel =
          project.getService(ChatToolWindowContentManager.class).createNewTabPanel();
      if (tabPanel != null) {
        tabPanel.displayLandingView();
      }
    }
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }
}
