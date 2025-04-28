package at.s2g.ai.actions.toolwindow;

import java.util.Optional;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import at.s2g.ai.conversations.Conversation;
import at.s2g.ai.conversations.ConversationsState;

public abstract class MoveAction extends AnAction {

  private final Runnable onRefresh;

  protected abstract Optional<Conversation> getConversation(@NotNull Project project);

  protected MoveAction(String text, String description, Icon icon, Runnable onRefresh) {
    super(text, description, icon);
    this.onRefresh = onRefresh;
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    event.getPresentation().setEnabled(ConversationsState.getCurrentConversation() != null);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    var project = event.getProject();
    if (project != null) {
      getConversation(project)
          .ifPresent(conversation -> {
            ConversationsState.getInstance().setCurrentConversation(conversation);
            onRefresh.run();
          });
    }
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }
}
