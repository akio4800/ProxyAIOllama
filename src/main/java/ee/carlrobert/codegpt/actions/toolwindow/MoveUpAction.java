package ee.carlrobert.codegpt.actions.toolwindow;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;

import ee.carlrobert.codegpt.actions.editor.EditorActionsUtil;
import ee.carlrobert.codegpt.conversations.Conversation;
import ee.carlrobert.codegpt.conversations.ConversationService;

public class MoveUpAction extends MoveAction {

  public MoveUpAction(Runnable onRefresh) {
    super("Move Up", "Move up", AllIcons.Actions.MoveUp, onRefresh);
    EditorActionsUtil.registerAction(this);
  }

  @Override
  protected Optional<Conversation> getConversation(@NotNull Project project) {
    return ConversationService.getInstance().getNextConversation();
  }
}
